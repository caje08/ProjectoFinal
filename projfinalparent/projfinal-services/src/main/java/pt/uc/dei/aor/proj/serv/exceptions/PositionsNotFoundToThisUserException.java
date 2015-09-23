package pt.uc.dei.aor.proj.serv.exceptions;

/*
 */


/**
 * @author
 */
public class PositionsNotFoundToThisUserException extends Exception {


	public PositionsNotFoundToThisUserException() {
		super("PositionEntity not selected");
	}


	public PositionsNotFoundToThisUserException(String what_caused) {
		super(what_caused);
	}

}
