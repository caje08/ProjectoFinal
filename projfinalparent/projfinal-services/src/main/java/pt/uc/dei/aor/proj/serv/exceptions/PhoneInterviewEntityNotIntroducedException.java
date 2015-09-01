package pt.uc.dei.aor.proj.serv.exceptions;

/*
 */


/**
 * @author
 */
public class PhoneInterviewEntityNotIntroducedException extends Exception {


	public PhoneInterviewEntityNotIntroducedException() {
		super("Phone Interview not selected");
	}


	public PhoneInterviewEntityNotIntroducedException(String what_caused) {
		super(what_caused);
	}

}
