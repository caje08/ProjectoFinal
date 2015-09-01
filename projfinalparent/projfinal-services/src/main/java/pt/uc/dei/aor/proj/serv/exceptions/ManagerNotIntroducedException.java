package pt.uc.dei.aor.proj.serv.exceptions;

/*
 */


/**
 * @author
 */
public class ManagerNotIntroducedException extends Exception {


	public ManagerNotIntroducedException() {
		super("ManagerEntity not selected");
	}


	public ManagerNotIntroducedException(String what_caused) {
		super(what_caused);
	}

}
