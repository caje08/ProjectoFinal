package pt.uc.dei.aor.proj.serv.exceptions;

/*
 */


/**
 * @author
 */
public class NumberOfMobilePhoneDigitsException extends Exception {


	public NumberOfMobilePhoneDigitsException() {
		super("Mobile Phone must be 9 ore more digits");
	}


	public NumberOfMobilePhoneDigitsException(String what_caused) {
		super(what_caused);
	}

}
