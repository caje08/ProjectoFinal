/*
 */
package pt.uc.dei.aor.proj.serv.exceptions;

/**
 * @author
 */
public class UserNotFoundException extends Exception {


	public UserNotFoundException() {
		super("User not found");
	}


	public UserNotFoundException(String what_caused) {
		super(what_caused);
	}

}
