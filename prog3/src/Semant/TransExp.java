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
}
