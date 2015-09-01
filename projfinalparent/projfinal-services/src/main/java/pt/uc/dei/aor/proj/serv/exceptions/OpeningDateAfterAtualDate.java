package pt.uc.dei.aor.proj.serv.exceptions;

/*
 */


/**
 * @author
 */
public class OpeningDateAfterAtualDate extends Exception {


	public OpeningDateAfterAtualDate() {
		super("The opening position must be after of the present date");
	}


	public OpeningDateAfterAtualDate(String what_caused) {
		super(what_caused);
	}

}
