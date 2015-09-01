package pt.uc.dei.aor.proj.serv.exceptions;

/*
 */


/**
 * @author
 */
public class FirstInterviewAfterAtualDateException extends Exception {


	public FirstInterviewAfterAtualDateException() {
		super("Interview must be after the atual date");
	}


	public FirstInterviewAfterAtualDateException(String what_caused) {
		super(what_caused);
	}

}
