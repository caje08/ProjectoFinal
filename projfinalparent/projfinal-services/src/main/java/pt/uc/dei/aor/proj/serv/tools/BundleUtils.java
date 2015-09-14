/*
 */
package pt.uc.dei.aor.proj.serv.tools;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

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
		String out="";
		Locale locale = Locale.getDefault();
		ResourceBundle bundle = ResourceBundle.getBundle("properties.pathProperties", locale);
		out = bundle.getString(message);
		Logger.getLogger(BundleUtils.class.getName()).log(Level.INFO,
				"Inside getSettings() --> bundle.getString(message)="+out);
		return out;
	}
}

