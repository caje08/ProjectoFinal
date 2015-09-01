/*
 */
package pt.uc.dei.aor.proj.serv.tools;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * @author
 */
public class JSFUtil {

	/**
	 * Returns a error message.
	 *
	 * @param msg
	 */
	public static void addErrorMessage(String msg) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, "ERROR");
		FacesContext.getCurrentInstance().addMessage(null, facesMsg);
	}

	/**
	 * Returns a information message.
	 *
	 * @param msg
	 */
	public static void addSuccessMessage(String msg) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO", msg);
		FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
	}

	/**
	 * Returns a warning message.
	 *
	 * @param msg
	 */
	public static void addWarningMessage(String msg) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, "ERROR");
		FacesContext.getCurrentInstance().addMessage("warningMessage", facesMsg);
	}

	/**
	 * Returns a string with the path to other navigation page.
	 *
	 * @param destination
	 * @throws IOException
	 */
	public static void addNavigation(String destination) throws IOException {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(ec.getRequestContextPath() + destination);
	}
}
