package Semant;
import Translate.Exp;
import Types.Type;

public class Semant {
	TransExp transExpObj;

  Env env;
  public Semant(ErrorMsg.ErrorMsg err) {
    this(new Env(err));
  }
  Semant(Env e) {
    env = e;
		transExpObj = new TransExp(e);
  }

  public void transProg(Absyn.Exp exp) {
    transExp(exp);
  }

  static final Types.VOID   VOID   = new Types.VOID();
  static final Types.INT    INT    = new Types.INT();
  static final Types.STRING STRING = new Types.STRING();
  static final Types.NIL    NIL    = new Types.NIL();

	public ExpTy transExp(Absyn.Exp e) {
		return transExpObj.transExp(e);
	}
}
