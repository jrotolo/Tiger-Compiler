package FindEscape;

public class FindEscape {
  Symbol.Table escEnv = new Symbol.Table(); // escEnv maps Symbol to Escape
  Absyn.FunctionDec parentFunction = null; // For checking for leaf functions

  public FindEscape(Absyn.Exp e) { traverseExp(0, e);  }

  void traverseVar(int depth, Absyn.Var v) {
    if (v instanceof Absyn.FieldVar)
      traverseVar(depth, (Absyn.FieldVar)v);
    else if (v instanceof Absyn.SimpleVar)
      traverseVar(depth, (Absyn.SimpleVar)v);
    else if (v instanceof Absyn.SubscriptVar)
      traverseVar(depth, (Absyn.SubscriptVar)v);
    else
      throw new Error("FindEscape.traverseVar: Var v is not valid subclass");
  }

  void traverseExp(int depth, Absyn.Exp e) {
    if (e instanceof Absyn.ArrayExp)
      traverseExp(depth, (Absyn.ArrayExp)e);
    else if (e instanceof Absyn.AssignExp)
      traverseExp(depth, (Absyn.AssignExp)e);
    else if (e instanceof Absyn.BreakExp)
      traverseExp(depth, (Absyn.BreakExp)e);
    else if (e instanceof Absyn.CallExp)
      traverseExp(depth, (Absyn.CallExp)e);
    else if (e instanceof Absyn.ForExp)
      traverseExp(depth, (Absyn.ForExp)e);
    else if (e instanceof Absyn.IfExp)
      traverseExp(depth, (Absyn.IfExp)e);
    else if (e instanceof Absyn.IntExp)
      traverseExp(depth, (Absyn.IntExp)e);
    else if (e instanceof Absyn.LetExp)
      traverseExp(depth, (Absyn.LetExp)e);
    else if (e instanceof Absyn.NilExp)
      traverseExp(depth, (Absyn.NilExp)e);
    else if (e instanceof Absyn.OpExp)
      traverseExp(depth, (Absyn.OpExp)e);
    else if (e instanceof Absyn.RecordExp)
      traverseExp(depth, (Absyn.RecordExp)e);
    else if (e instanceof Absyn.SeqExp)
      traverseExp(depth, (Absyn.SeqExp)e);
    else if (e instanceof Absyn.StringExp)
      traverseExp(depth, (Absyn.StringExp)e);
    else if (e instanceof Absyn.VarExp)
      traverseExp(depth, (Absyn.VarExp)e);
    else if (e instanceof Absyn.WhileExp)
      traverseExp(depth, (Absyn.WhileExp)e);
    else
      throw new Error("FindEscape.traverseExp: Exp e is not a valid subclass of Exp");
  }

  void traverseDec(int depth, Absyn.Dec d) {
    if (d instanceof Absyn.FunctionDec)
      traverseDec(depth, (Absyn.FunctionDec)d);
    else if (d instanceof Absyn.TypeDec)
      traverseDec(depth, (Absyn.TypeDec)d);
    else if (d instanceof Absyn.VarDec)
      traverseDec(depth, (Absyn.VarDec)d);
    else
      throw new Error("FindEscape.traverseDec: Dec d is not a valid subclass of Dec");
  }

  // TRAVERSEVAR METHODS
  void traveseVar(int depth, Absyn.FieldVar v) {
    traverseVar(depth, v.var);
  }

  void traverseVar(int depth, Absyn.SimpleVar v) {
    Escape varEsc = (Escape) escEnv.get(v.name);

    if (varEsc != null && varEsc.depth < depth)
      varEsc.setEscape();
  }

  void traverseVar(int depth, Absyn.SubscriptVar v) {
    traverseVar(depth, v.var);
    traverseExp(depth, v.index);
  }

  // TRAVERSEEXP METHODS
  void traverseExp(int depth, Absyn.ArrayExp e) {
    traverseExp(depth, e.size);
    traverseExp(depth, e.init);
  }

  void traverseExp(int depth, Absyn.AssignExp e) {
    traverseVar(depth, e.var);
    traverseExp(depth, e.exp);
  }

  void traverseExp(int depth, Absyn.BreakExp e) {}

  void traverseExp(int depth, Absyn.CallExp e) {
    if (parentFunction != null)
      parentFunction.leaf = false;

    for (Absyn.ExpList args = e.args; args != null; args = args.tail)
      traverseExp(depth, args.head);
  }

  void traverseExp(int depth, Absyn.ForExp e) {
    traverseExp(depth, e.var.init);

    escEnv.beginScope();
    escEnv.put(e.var.name, new VarEscape(depth, e.var));

    traverseExp(depth, e.hi);
    traverseExp(depth, e.body);

    escEnv.endScope();
  }

  void traverseExp(int depth, Absyn.IfExp e) {
    traverseExp(depth, e.test);
    traverseExp(depth, e.thenclause);
    if (e.elseclause != null)
      traverseExp(depth, e.elseclause);
  }

  void traverseExp(int depth, Absyn.IntExp e) { }

  void traverseExp(int depth, Absyn.LetExp e) {
    escEnv.beginScope();

    for (Absyn.DecList dec = e.decs; dec != null; dec = dec.tail) {
      traverseDec(depth, dec.head);
    }

    traverseExp(depth, e.body);
    escEnv.endScope();
  }

  void traverseExp(int depth, Absyn.NilExp e) {}

  void traverseExp(int depth, Absyn.OpExp e) {
    traverseExp(depth, e.left);
    traverseExp(depth, e.right);
  }

  void traverseExp(int depth, Absyn.RecordExp e) {
    for (Absyn.FieldExpList field = e.fields; field != null; field = field.tail)
      traverseExp(depth, field.init);
  }

  void traverseExp(int depth, Absyn.SeqExp e) {
    for (Absyn.ExpList expList = e.list; expList != null; expList = expList.tail)
      traverseExp(depth, expList.head);
  }

  void traverseExp(int depth, Absyn.StringExp e) {}

  void traverseExp(int depth, Absyn.VarExp e) {
    traverseVar(depth, e.var);
  }

  void traverseExp(int depth, Absyn.WhileExp e) {
    traverseExp(depth, e.test);
    traverseExp(depth, e.body);
  }



  // TRAVERSEDEC METHODS
  void traverseDec(int depth, Absyn.FunctionDec d) {
    Absyn.FunctionDec oldParentFunction = parentFunction;
    depth = depth + 1;

    // Loop through the functions
    for (Absyn.FunctionDec function = d; function != null; function = function.next) {
      parentFunction = function;
      escEnv.beginScope();

      for (Absyn.FieldList param = function.params; param != null; param = param.tail)
        escEnv.put(param.name, new FormalEscape(depth, param));

      traverseExp(depth, function.body);

      escEnv.endScope();
    }

    parentFunction = oldParentFunction;
  }

  void traverseDec(int depth, Absyn.TypeDec d) {}

  void traverseDec(int depth, Absyn.VarDec d) {
    traverseExp(depth, d.init);
    escEnv.put(d.name, new VarEscape(depth, d));
  }

}
