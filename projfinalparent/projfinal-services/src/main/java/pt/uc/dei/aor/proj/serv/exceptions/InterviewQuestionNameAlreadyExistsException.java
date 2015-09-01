/*
 */
package pt.uc.dei.aor.proj.serv.exceptions;

/**
 * @author
 */
public class InterviewQuestionNameAlreadyExistsException extends Exception {


	public InterviewQuestionNameAlreadyExistsException() {
		super("This question name already exists for this interview guide");
	}


	public InterviewQuestionNameAlreadyExistsException(String what_caused) {
		super(what_caused);
	}

}
