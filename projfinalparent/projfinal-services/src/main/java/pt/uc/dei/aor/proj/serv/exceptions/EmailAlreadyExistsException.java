package pt.uc.dei.aor.proj.serv.exceptions;

/*
 *
 */


/**
 * @author
 */
public class EmailAlreadyExistsException extends Exception {


	public EmailAlreadyExistsException() {
		super("Email already exists");
	}


	public EmailAlreadyExistsException(String what_caused) {
		super(what_caused);
	}

}
