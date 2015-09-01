/*
 */
package pt.uc.dei.aor.proj.serv.exceptions;

/**
 * @author
 */
public class EmailOrUsernameNotFoundException extends Exception {


	public EmailOrUsernameNotFoundException() {
		super("It was not found any account with that email and password");
	}


	public EmailOrUsernameNotFoundException(String message) {
		super(message);
	}


}
