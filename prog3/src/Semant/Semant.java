package Semant;

import Absyn.*;
import ErrorMsg.ErrorMsg;
import Symbol.*;
import Types.*;
import java.util.Hashtable;

public class Semant {
	TransExp transExpObj;

  Env env;

  static final VOID     VOID = new VOID();
  static final INT      INT = new INT();
  static final STRING   STRING = new STRING();
  static final NIL      NIL = new NIL();

  public Semant(ErrorMsg err) {
    this(new Env(err));
  }
  Semant(Env e) {
    this.env = e;
  }

  // transDec overrides

  Translate.Exp transDec(Dec d) {
    if ((d instanceof VarDec))
      return transDec((VarDec)d);
    if ((d instanceof TypeDec))
      return transDec((TypeDec)d);
    if ((d instanceof FunctionDec))
      return transDec((FunctionDec)d);
    throw new Error("Semant.transDec");
  }

  // Loop through the functions to statisfy all the definitions
  Translate.Exp transDec(FunctionDec d) {
    Hashtable hash = new Hashtable();
    for (FunctionDec f = d; f != null; f = f.next) {
      if (hash.put(f.name, f.name) != null)
        error(f.pos, "function redeclared");
      RECORD fields = transTypeFields(new Hashtable(), f.params);
      Type type = transTy(f.result);
      // Add the function to the environment
      f.entry = new FunEntry(fields, type);
      this.env.venv.put(f.name, f.entry);
    }

    for (FunctionDec f = d; f != null; f = f.next) {
      this.env.venv.beginScope();
      setTypeFields(f.entry.formals);
      Semant fun = new Semant(this.env);
      ExpTy body = fun.transExp(f.body);
      if (!body.ty.coerceTo(f.entry.result))
        error(f.body.pos, "result type mismatch");
      this.env.venv.endScope();
    }
    return null;
  }

  // Go through the type decs and process them recursively
  Translate.Exp transDec(TypeDec d) {
    Hashtable hash = new Hashtable();
    for (TypeDec type = d; type != null; type = type.next) {
      if (hash.put(type.name, type.name) != null)
        error(type.pos, "type redeclared");
      type.entry = new NAME(type.name);
      this.env.tenv.put(type.name, type.entry);
    }

    for (TypeDec type = d; type != null; type = type.next) {
      NAME name = type.entry;
      name.bind(transTy(type.ty));
    }

    for (TypeDec type = d; type != null; type = type.next) {
      NAME name = type.entry;
      if (name.isLoop())
        error(type.pos, "illegal type cycle");
    }
    return null;
  }

  // Translate the value being assigned to the variable
  Translate.Exp transDec(VarDec d) {
    ExpTy init = transExp(d.init);
    Type type;
    if (d.typ == null) {
      if (init.ty.coerceTo(NIL))
        error(d.pos, "record type expected");
        // If the type is not explicit, just use the initial value's type
      type = init.ty;
    }
    else {
        // If the type is explicit, translate the type and check that it is compatible
      type = transTy(d.typ);
      if (!init.ty.coerceTo(type))
        error(d.pos, "assignment type mismatch");
    }
    // Add the variable to the variable environment
    d.entry = new VarEntry(type);
    this.env.venv.put(d.name, d.entry);
    return null;
  }

  // transExp overrides

  ExpTy transExp(ArrayExp e) {
    NAME name = (NAME)this.env.tenv.get(e.typ);
    ExpTy size = transExp(e.size);
    ExpTy init = transExp(e.init);
    checkInt(size, e.size.pos);
    if (name != null) {
      Type actual = name.actual();
      if ((actual instanceof ARRAY)) {
        ARRAY array = (ARRAY)actual;
        if (!init.ty.coerceTo(array.element))
          error(e.init.pos, "element type mismatch");
        return new ExpTy(null, name);
      }
      error(e.pos, "array type expected");
    } else {
      error(e.pos, "undefined type: " + e.typ);
    } return new ExpTy(null, VOID);
  }

  ExpTy transExp(AssignExp e) {
    ExpTy var = transVar(e.var, true);
    ExpTy exp = transExp(e.exp);
    if (!exp.ty.coerceTo(var.ty))
      error(e.pos, "assignment type mismatch");
    return new ExpTy(null, VOID);
  }

  ExpTy transExp(BreakExp e) {
    error(e.pos, "break outside loop");
    return new ExpTy(null, VOID);
  }

  ExpTy transExp(CallExp e) {
    Entry entry = (Entry)this.env.venv.get(e.func);
    if ((entry instanceof FunEntry)) {
      FunEntry f = (FunEntry)entry;
      transArgs(e.pos, f.formals, e.args);
      return new ExpTy(null, f.result);
    }
    error(e.pos, "undefined function: " + e.func);
    return new ExpTy(null, VOID);
  }

  ExpTy transExp(Exp e) {
    ExpTy result;
    if (e == null)
        return new ExpTy(null, VOID);
    if ((e instanceof VarExp))
        result = transExp((VarExp)e);
    else if ((e instanceof NilExp))
        result = transExp((NilExp)e);
    else if ((e instanceof IntExp))
        result = transExp((IntExp)e);
    else if ((e instanceof StringExp))
        result = transExp((StringExp)e);
    else if ((e instanceof CallExp))
        result = transExp((CallExp)e);
    else if ((e instanceof OpExp))
        result = transExp((OpExp)e);
    else if ((e instanceof RecordExp))
        result = transExp((RecordExp)e);
    else if ((e instanceof SeqExp))
        result = transExp((SeqExp)e);
    else if ((e instanceof AssignExp))
        result = transExp((AssignExp)e);
    else if ((e instanceof IfExp))
        result = transExp((IfExp)e);
    else if ((e instanceof WhileExp))
        result = transExp((WhileExp)e);
    else if ((e instanceof ForExp))
        result = transExp((ForExp)e);
    else if ((e instanceof BreakExp))
        result = transExp((BreakExp)e);
    else if ((e instanceof LetExp))
        result = transExp((LetExp)e);
    else if ((e instanceof ArrayExp))
        result = transExp((ArrayExp)e);
    else
        throw new Error("Semant.transExp");

    e.type = result.ty;
    return result;
  }

  ExpTy transExp(ForExp e) {
    ExpTy lo = transExp(e.var.init);
    checkInt(lo, e.var.pos);
    ExpTy hi = transExp(e.hi);
    checkInt(hi, e.hi.pos);
    this.env.venv.beginScope();
    e.var.entry = new LoopVarEntry(INT);
    this.env.venv.put(e.var.name, e.var.entry);
    Semant loop = new LoopSemant(this.env);
    ExpTy body = loop.transExp(e.body);
    this.env.venv.endScope();
    if (!body.ty.coerceTo(VOID))
      error(e.body.pos, "result type mismatch");
    return new ExpTy(null, VOID);
  }

  ExpTy transExp(IfExp e) {
    ExpTy test = transExp(e.test);
    checkInt(test, e.test.pos);
    ExpTy thenclause = transExp(e.thenclause);
    ExpTy elseclause = transExp(e.elseclause);
    if ((!thenclause.ty.coerceTo(elseclause.ty)) &&
      (!elseclause.ty.coerceTo(thenclause.ty)))
      error(e.pos, "result type mismatch");
    return new ExpTy(null, elseclause.ty);
  }

  ExpTy transExp(IntExp e) {
    return new ExpTy(null, INT);
  }

  ExpTy transExp(LetExp e) {
    this.env.venv.beginScope();
    this.env.tenv.beginScope();
    for (DecList d = e.decs; d != null; d = d.tail) {
      transDec(d.head);
    }
    ExpTy body = transExp(e.body);
    this.env.venv.endScope();
    this.env.tenv.endScope();
    return new ExpTy(null, body.ty);
  }

  ExpTy transExp(NilExp e) {
    return new ExpTy(null, NIL);
  }

  ExpTy transExp(OpExp e) {
    ExpTy left = transExp(e.left);
    ExpTy right = transExp(e.right);

    switch (e.oper) {
    case OpExp.PLUS:
    case OpExp.MINUS:
    case OpExp.MUL:
    case OpExp.DIV:
      checkInt(left, e.left.pos);
      checkInt(right, e.right.pos);
      return new ExpTy(null, INT);
    case OpExp.EQ:
    case OpExp.NE:
      checkComparable(left, e.left.pos);
      checkComparable(right, e.right.pos);
      if ((!left.ty.coerceTo(right.ty)) && (!right.ty.coerceTo(left.ty)))
        error(e.pos, "incompatible operands for equality");
      return new ExpTy(null, INT);
    case OpExp.LT:
    case OpExp.LE:
    case OpExp.GT:
    case OpExp.GE:
      checkOrderable(left, e.left.pos);
      checkOrderable(right, e.right.pos);
      if ((!left.ty.coerceTo(right.ty)) && (!right.ty.coerceTo(left.ty)))
        error(e.pos, "incompatible operands for inequality");
      return new ExpTy(null, INT);
    }
    throw new Error("unknown operator");
  }

  ExpTy transExp(RecordExp e) {
    NAME name = (NAME)this.env.tenv.get(e.typ);
    if (name != null) {
      Type actual = name.actual();
      if ((actual instanceof RECORD)) {
        RECORD r = (RECORD)actual;
        transFields(e.pos, r, e.fields);
        return new ExpTy(null, name);
      }
      error(e.pos, "record type expected");
    } else {
      error(e.pos, "undefined type: " + e.typ);
    }return new ExpTy(null, VOID);
  }

  ExpTy transExp(SeqExp e) {
    Type type = VOID;
    for (ExpList exp = e.list; exp != null; exp = exp.tail) {
      ExpTy et = transExp(exp.head);
      type = et.ty;
    }
    return new ExpTy(null, type);
  }

  ExpTy transExp(StringExp e) {
    return new ExpTy(null, STRING);
  }

  ExpTy transExp(VarExp e) {
    return transVar(e.var);
  }

  ExpTy transExp(WhileExp e) {
    ExpTy test = transExp(e.test);
    checkInt(test, e.test.pos);
    Semant loop = new LoopSemant(this.env);
    ExpTy body = loop.transExp(e.body);
    if (!body.ty.coerceTo(VOID))
      error(e.body.pos, "result type mismatch");
    return new ExpTy(null, VOID);
  }



  public void transProg(Exp exp) {
    transExp(exp);
  }

  // transTy overrides

  Type transTy(ArrayTy t) {
    NAME name = (NAME)this.env.tenv.get(t.typ);
    if (name != null)
      return new ARRAY(name);
    error(t.pos, "undefined type: " + t.typ);
    return VOID;
  }

  Type transTy(NameTy t) {
    if (t == null)
      return VOID;
    NAME name = (NAME)this.env.tenv.get(t.name);
    if (name != null)
      return name;
    error(t.pos, "undefined type: " + t.name);
    return VOID;
  }

  Type transTy(RecordTy t) {
    RECORD type = transTypeFields(new Hashtable(), t.fields);
    if (type != null)
      return type;
    return VOID;
  }

  Type transTy(Ty t) {
    if ((t instanceof NameTy))
      return transTy((NameTy)t);
    if ((t instanceof RecordTy))
      return transTy((RecordTy)t);
    if ((t instanceof ArrayTy))
      return transTy((ArrayTy)t);
    throw new Error("Semant.transTy");
  }

  // transVar overrides

  ExpTy transVar(FieldVar v) {
    ExpTy var = transVar(v.var);
    Type actual = var.ty.actual();
    if ((actual instanceof RECORD)) {
      for (RECORD field = (RECORD)actual;
        field != null;
        field = field.tail) {
        if (field.fieldName == v.field)
          return new ExpTy(null, field.fieldType);
      }
      error(v.pos, "undefined field: " + v.field);
    }
    else {
      error(v.var.pos, "record expected");
    }
    return new ExpTy(null, VOID);
  }

  ExpTy transVar(SimpleVar v, boolean lhs) {
    Entry entry = (Entry)this.env.venv.get(v.name);
    if ((entry instanceof VarEntry)) {
      VarEntry ent = (VarEntry)entry;
      if ((lhs) && ((ent instanceof LoopVarEntry)))
        error(v.pos, "assignment to loop index");
      return new ExpTy(null, ent.ty);
    }
    error(v.pos, "undefined variable: " + v.name);
    return new ExpTy(null, VOID);
  }

  ExpTy transVar(SubscriptVar v) {
    ExpTy var = transVar(v.var);
    ExpTy index = transExp(v.index);
    checkInt(index, v.index.pos);
    Type actual = var.ty.actual();
    if ((actual instanceof ARRAY)) {
      ARRAY array = (ARRAY)actual;
      return new ExpTy(null, array.element);
    }
    error(v.var.pos, "array expected");
    return new ExpTy(null, VOID);
  }

  ExpTy transVar(Var v) {
    return transVar(v, false);
  }

  ExpTy transVar(Var v, boolean lhs) {
    if ((v instanceof SimpleVar))
      return transVar((SimpleVar)v, lhs);
    if ((v instanceof FieldVar))
      return transVar((FieldVar)v);
    if ((v instanceof SubscriptVar))
      return transVar((SubscriptVar)v);
    throw new Error("Semant.transVar");
  }

  // Helper Methods

  private Translate.Exp checkComparable(ExpTy et, int pos) {
    Type actualType = et.ty.actual();
    if ((!(actualType instanceof INT)) &&
      (!(actualType instanceof STRING)) &&
      (!(actualType instanceof NIL)) &&
      (!(actualType instanceof RECORD)) &&
      (!(actualType instanceof ARRAY)))
      error(pos, "integer, string, nil, record, or array expected");
    return et.exp;
  }

  private Translate.Exp checkInt(ExpTy et, int pos) {
    if (!INT.coerceTo(et.ty))
      error(pos, "integer expected");
    return et.exp;
  }

  private Translate.Exp checkOrderable(ExpTy et, int pos) {
    Type actualType = et.ty.actual();
    if ((!(actualType instanceof INT)) &&
      (!(actualType instanceof STRING)))
      error(pos, "integer or string expected");
    return et.exp;
  }

  private void error(int pos, String msg) {
    this.env.errorMsg.error(pos, msg);
  }

  private void setTypeFields(RECORD f) {
    if (f == null)
      return;
    this.env.venv.put(f.fieldName, new VarEntry(f.fieldType));
    setTypeFields(f.tail);
  }

  private void transArgs(int epos, RECORD formal, ExpList args) {
    if (formal == null) {
      if (args != null)
        error(args.head.pos, "too many arguments");
      return;
    }
    if (args == null) {
      error(epos, "missing argument for " + formal.fieldName);
      return;
    }
    ExpTy e = transExp(args.head);
    if (!e.ty.coerceTo(formal.fieldType))
      error(args.head.pos, "argument type mismatch");
    transArgs(epos, formal.tail, args.tail);
  }

  private RECORD transTypeFields(Hashtable hash, FieldList f) {
    if (f == null)
      return null;
    NAME name = (NAME)this.env.tenv.get(f.typ);
    if (name == null)
      error(f.pos, "undefined type: " + f.typ);
    if (hash.put(f.name, f.name) != null)
      error(f.pos, "function parameter/record field redeclared: " + f.name);
    return new RECORD(f.name, name, transTypeFields(hash, f.tail));
  }

  private void transFields(int epos, RECORD f, FieldExpList exp) {
    if (f == null) {
      if (exp != null)
        error(exp.pos, "too many expressions");
      return;
    }
    if (exp == null) {
      error(epos, "missing expression for " + f.fieldName);
      return;
    }
    ExpTy e = transExp(exp.init);
    if (exp.name != f.fieldName)
      error(exp.pos, "field name mismatch");
    if (!e.ty.coerceTo(f.fieldType))
      error(exp.pos, "field type mismatch");
    transFields(epos, f.tail, exp.tail);
  }
}
