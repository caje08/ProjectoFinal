/*
 */
package pt.uc.dei.aor.proj.serv.exceptions;

/**
 * @author
 */
public class EmailAndPasswordNotCorrespondingToLinkedinCredentialsException extends Exception {


	public EmailAndPasswordNotCorrespondingToLinkedinCredentialsException() {
		super("Email and Password introduced not corresponding to your Linkedin credentials ");
	}
}
