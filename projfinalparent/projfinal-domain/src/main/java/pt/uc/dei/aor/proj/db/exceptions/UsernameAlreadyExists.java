package pt.uc.dei.aor.proj.db.exceptions;

/*
 */

/**
 * @author Carlos + Catarina
 */
public class UsernameAlreadyExists extends Exception {


	public UsernameAlreadyExists() {
		super("Username already exists");
	}


	public UsernameAlreadyExists(String what_caused) {
		super(what_caused);
	}

}
