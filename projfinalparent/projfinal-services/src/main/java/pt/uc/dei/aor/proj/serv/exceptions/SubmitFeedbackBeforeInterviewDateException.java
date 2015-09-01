package pt.uc.dei.aor.proj.serv.exceptions;

/*
 */


/**
 * @author
 */
public class SubmitFeedbackBeforeInterviewDateException extends Exception {


	public SubmitFeedbackBeforeInterviewDateException() {
		super("Just can submit Feedback after Interview Date");
	}


	public SubmitFeedbackBeforeInterviewDateException(String what_caused) {
		super(what_caused);
	}

}
