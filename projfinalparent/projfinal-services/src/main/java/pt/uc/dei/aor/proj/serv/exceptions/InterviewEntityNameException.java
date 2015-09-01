package pt.uc.dei.aor.proj.serv.exceptions;

/*
 */


/**
 * @author
 */
public class InterviewEntityNameException extends Exception {


	public InterviewEntityNameException() {
		super("This guide Name already exists");
	}


	public InterviewEntityNameException(String what_caused) {
		super(what_caused);
	}

}
