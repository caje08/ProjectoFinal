/*
 */
package pt.uc.dei.aor.proj.db.exceptions;

/**
 * @author Carlos + Catarina
 */
public class InvalidAuthException extends Exception {


	public InvalidAuthException() {
		super("User not registered");
	}


	public InvalidAuthException(String what_caused) {
		super(what_caused);
	}

}
