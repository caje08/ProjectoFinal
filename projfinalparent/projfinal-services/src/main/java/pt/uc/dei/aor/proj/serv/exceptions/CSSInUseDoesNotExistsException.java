/*
 */
package pt.uc.dei.aor.proj.serv.exceptions;

/**
 * @author
 */
public class CSSInUseDoesNotExistsException extends Exception {


	public CSSInUseDoesNotExistsException() {
		super("There are no CSSInUse results from the query in CSSFacade");
	}

}
