package pt.uc.dei.aor.proj.serv.exceptions;

/*

/**
 *
 * @author
 */
public class BeforeDateNotBeforeClosingDate extends Exception {


	public BeforeDateNotBeforeClosingDate() {
		super("Start Date must be before Closing Date");
	}


	public BeforeDateNotBeforeClosingDate(String what_caused) {
		super(what_caused);
	}

}
