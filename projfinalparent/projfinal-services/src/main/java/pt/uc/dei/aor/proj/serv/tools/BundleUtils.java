/*
 */
package pt.uc.dei.aor.proj.serv.tools;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author
 */
public class BundleUtils {

	/**
	 *
	 * @param message
	 * @return
	 */
	public static String getSettings(String message) {
		Locale locale = Locale.getDefault();
		ResourceBundle bundle = ResourceBundle.getBundle("properties.pathProperties", locale);
		return bundle.getString(message);
	}
}

