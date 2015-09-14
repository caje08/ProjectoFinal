/*
 */
package pt.uc.dei.aor.proj.serv.tools;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;

/**
 * @author
 */
@Stateless
public class EncryptPassword {

	/**
	 *
	 * @param password
	 * @return encrypted password
	 */
	public static String encrypt(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes("UTF-8")); // Change this to "UTF-16" if needed
			byte[] digest = md.digest();
			BigInteger bigInt = new BigInteger(1, digest);
			String output = bigInt.toString(16);
			return output;
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
			Logger.getLogger(EncryptPassword.class.getName()).log(Level.SEVERE, null, ex);
			return ex.getMessage();
		}

	}
}