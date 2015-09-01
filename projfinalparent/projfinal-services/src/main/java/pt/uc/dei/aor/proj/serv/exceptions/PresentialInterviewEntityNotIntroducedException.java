package pt.uc.dei.aor.proj.serv.exceptions;

/*
 */

/**
 * @author
 */
public class PresentialInterviewEntityNotIntroducedException extends Exception {


	public PresentialInterviewEntityNotIntroducedException() {
		super("Presential Interview not selected");
	}


	public PresentialInterviewEntityNotIntroducedException(String what_caused) {
		super(what_caused);
	}

}
