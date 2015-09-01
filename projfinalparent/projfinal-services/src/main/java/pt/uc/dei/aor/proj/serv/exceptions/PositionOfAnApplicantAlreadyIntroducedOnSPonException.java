package pt.uc.dei.aor.proj.serv.exceptions;

/*
 */


/**
 * @author
 */
public class PositionOfAnApplicantAlreadyIntroducedOnSPonException extends Exception {


	public PositionOfAnApplicantAlreadyIntroducedOnSPonException() {
		super("This position was already added, for this application");
	}


}
