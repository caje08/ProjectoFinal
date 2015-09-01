package pt.uc.dei.aor.proj.db.exceptions;

/*
 */


/**
 * @author Carlos + Catarina
 */
public class EmailAlreadyExistsException extends Exception {


	public EmailAlreadyExistsException() {
		super("Email already exists");
	}


	public EmailAlreadyExistsException(String what_caused) {
		super(what_caused);
	}

}
