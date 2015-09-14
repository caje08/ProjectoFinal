package pt.uc.dei.aor.proj.serv.exceptions;

/*
 *
 */


/**
 * @author
 */
public class EmailDoesNotExistsException extends Exception {


	public EmailDoesNotExistsException() {
		super("User Email doesn't existin the current DB.");
	}


	public EmailDoesNotExistsException(String what_caused) {
		super(what_caused);
	}

}
