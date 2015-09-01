/*
 */
package pt.uc.dei.aor.proj.serv.exceptions;

/**
 * @author
 */
public class DoNotUploadCoverLetterException extends Exception {


	public DoNotUploadCoverLetterException() {
		super("Must upload a Cover Letter file ");
	}

}
