package pt.uc.dei.aor.proj.serv.exceptions;

/*
 */


/**
 * @author
 */
public class InterviewsNotFoundToThisUserException extends Exception {


	public InterviewsNotFoundToThisUserException() {
		super("PositionEntity not selected");
	}


	public InterviewsNotFoundToThisUserException(String what_caused) {
		super(what_caused);
	}

}
