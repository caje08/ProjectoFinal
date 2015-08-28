package pt.uc.dei.aor.proj.db.exceptions;

/*
 */

/**
 * @author Carlos + Catarina
 */
public class UserAlreadyExists extends Exception {


	public UserAlreadyExists() {
		super("User already exists");
	}


	public UserAlreadyExists(String what_caused) {
		super(what_caused);
	}

}
