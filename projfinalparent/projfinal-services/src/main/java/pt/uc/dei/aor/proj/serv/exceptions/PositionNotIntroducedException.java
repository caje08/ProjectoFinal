package pt.uc.dei.aor.proj.serv.exceptions;

/*
 */


/**
 * @author
 */
public class PositionNotIntroducedException extends Exception {


	public PositionNotIntroducedException() {
		super("PositionEntity not selected");
	}


	public PositionNotIntroducedException(String what_caused) {
		super(what_caused);
	}

}
