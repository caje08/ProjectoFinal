/*
 */
package pt.uc.dei.aor.proj.serv.tools;

import javax.faces.context.FacesContext;

/**
 * @author
 */
public class MaintainSession {

	public static String getRequestParameter(String name) {
		return (String) FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get(name);
	}

	public static Object getSessionMapValue(String key) {
		return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(key);
	}

	// Setters -----------------------------------------------------------------------------------
	public static void setSessionMapValue(String key, Object value) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(key, value);
	}

	public static boolean isKeyInSessionMap(String key) {
		return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey(key);
	}

	public static void clear(){
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
	}

}

