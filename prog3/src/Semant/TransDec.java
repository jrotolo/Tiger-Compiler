package Semant;
import Translate.Exp;
import Types.Type;

public class TransDec extends Trans {
	
	public TransDec(Env e) {
		env = e;
	}

	public void transDec(Absyn.Dec d) {
		if (d instanceof Absyn.FunctionDec)
			transDec((Absyn.FunctionDec)d);
		else if (d instanceof Absyn.VarDec)
			transDec((Absyn.VarDec)d);
		else if (d instanceof Absyn.TypeDec)
			transDec((Absyn.TypeDec)d);
		else
			throw new Error("TransDec.transDec");
	}

	public void transDec(Absyn.FunctionDec d) {

	}

	public void transDec(Absyn.VarDec d) {

	}

	public void transDec(Absyn.TypeDec d) {

	}
}
