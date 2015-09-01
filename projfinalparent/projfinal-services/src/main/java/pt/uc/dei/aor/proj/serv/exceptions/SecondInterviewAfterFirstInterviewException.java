package pt.uc.dei.aor.proj.serv.exceptions;

/*
 */


/**
 * @author
 */
public class SecondInterviewAfterFirstInterviewException extends Exception {


	public SecondInterviewAfterFirstInterviewException() {
		super("Second Interview must be after of the first interview");
	}


	public SecondInterviewAfterFirstInterviewException(String what_caused) {
		super(what_caused);
	}

}
