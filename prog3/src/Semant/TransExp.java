package Semant;
import Translate.Exp;
import Types.Type;

public class TransExp extends Trans {

	public TransExp(Env e) {
		env = e;
	}

	public ExpTy transExp(Absyn.Exp e) {
		ExpTy result;

		if (e == null)
			return new ExpTy(null, VOID);
		else if (e instanceof Absyn.OpExp)
			result = transExp((Absyn.OpExp)e);
		else if (e instanceof Absyn.LetExp)
			result = transExp((Absyn.LetExp)e);
		else if (e instanceof Absyn.IntExp)
			result = transExp((Absyn.IntExp)e);
		else if (e instanceof Absyn.StringExp)
			result = transExp((Absyn.StringExp)e);
		else if (e instanceof Absyn.SeqExp)
			result = transExp((Absyn.SeqExp)e);
		else if (e instanceof Absyn.ArrayExp)
			result = transExp((Absyn.ArrayExp)e);
		else if (e instanceof Absyn.NilExp)
			result = transExp((Absyn.NilExp)e);
		else if (e instanceof Absyn.RecordExp)
			result = transExp((Absyn.RecordExp)e);
		else if (e instanceof Absyn.AssignExp)
			result = transExp((Absyn.AssignExp)e);
		else if (e instanceof Absyn.BreakExp)
			result = transExp((Absyn.BreakExp)e);
		else if (e instanceof Absyn.CallExp)
			result = transExp((Absyn.CallExp)e);
		else if (e instanceof Absyn.ForExp)
			result = transExp((Absyn.ForExp)e);
		else if (e instanceof Absyn.WhileExp)
			result = transExp((Absyn.WhileExp)e);
		else if (e instanceof Absyn.IfExp)
			result = transExp((Absyn.IfExp)e);
		else
			throw new Error("TransExp.transExp");

		e.type = result.ty;
		return result;
	}

	/*
	 * TransExp Overrides
	 */

	public ExpTy transExp(Absyn.OpExp e) {
		ExpTy left = transExp(e.left);
		ExpTy right = transExp(e.right);

		switch (e.oper) {
			case Absyn.OpExp.PLUS:
			case Absyn.OpExp.MINUS:
			case Absyn.OpExp.MUL:
			case Absyn.OpExp.DIV:
				checkInt(left, e.left.pos);
				checkInt(right, e.right.pos);
				return new ExpTy(null, INT);
			case Absyn.OpExp.EQ:
			case Absyn.OpExp.NE:
				checkComparable(left, e.left.pos);
				checkComparable(right, e.right.pos);
				return new ExpTy(null, INT);
			case Absyn.OpExp.LT:
			case Absyn.OpExp.GT:
			case Absyn.OpExp.LE:
			case Absyn.OpExp.GE:
				checkOrderable(left, e.left.pos);
				checkOrderable(right, e.right.pos);
				return new ExpTy(null, INT);
			default:
				throw new Error("unknown operator");
		}
	}

	public ExpTy transExp(Absyn.ArrayExp e) {
		Types.NAME type = (Types.NAME)env.tenv.get(e.typ);

		ExpTy size = transExp(e.size);
		ExpTy init = transExp(e.init);

		checkInt(size, e.size.pos);

		if (type == null) {
			error(e.pos, "array of type " + e.typ + " undefined");
			return new ExpTy(null, VOID);
		} else if (!(type.actual() instanceof Types.ARRAY)) {
			error(e.pos, "not an array type");
			return new ExpTy(null, VOID);
		}

		Type element = ((Types.ARRAY)type.actual()).element;

		if (!init.ty.coerceTo(element))
			error(e.init.pos, "initial element does not match array's type");

		return new ExpTy(null, type);
	}

	/* TODO: Implement the below stubbed methods */

	public ExpTy transExp(Absyn.AssignExp e) {
		return new ExpTy(null, VOID);
	}

	public ExpTy transExp(Absyn.BreakExp e) {
		return new ExpTy(null, VOID);
	}

	public ExpTy transExp(Absyn.CallExp e) {
		return new ExpTy(null, VOID);
	}

	public ExpTy transExp(Absyn.ForExp e) {
		return new ExpTy(null, VOID);
	}

	public ExpTy transExp(Absyn.WhileExp e) {
		return new ExpTy(null, VOID);
	}

	public ExpTy transExp(Absyn.IfExp e) {
		return new ExpTy(null, VOID);
	}

	public ExpTy transExp(Absyn.IntExp e) {
		return new ExpTy(null, VOID);
	}

	public ExpTy transExp(Absyn.LetExp e) {
		return new ExpTy(null, VOID);
	}

	public ExpTy transExp(Absyn.NilExp e) {
		return new ExpTy(null, VOID);
	}

	public ExpTy transExp(Absyn.RecordExp e) {
		return new ExpTy(null, VOID);
	}

	public ExpTy transExp(Absyn.SeqExp e) {
		return new ExpTy(null, VOID);
	}

	public ExpTy transExp(Absyn.StringExp e) {
		return new ExpTy(null, VOID);
	}

	public ExpTy transExp(Absyn.VarExp e) {
		//return transVar(e.var);
		return new ExpTy(null, VOID);
	}

}
