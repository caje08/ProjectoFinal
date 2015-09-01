package pt.uc.dei.aor.proj.serv.exceptions;

/*
 */

/**
 * @author
 */
public class InterviewerSameDateException extends Exception {


	public InterviewerSameDateException() {
		super("This interviewer has as interview at this time");
	}


	public InterviewerSameDateException(String what_caused) {
		super(what_caused);
	}

}
