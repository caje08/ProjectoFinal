package pt.uc.dei.aor.proj.ejb;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import com.sun.syndication.io.impl.Base64;

/**
 *
 * @author
 */
@Stateless
public class PasswordEJB {

	public String encrypt(String password) {
		String sha1;
		if (null == password) {
			return null;
		}
		
		try {
			// digest = MessageDigest.getInstance("SHA-1");
			// digest.update(password.getBytes(), 0, password.length());
			// sha1 = new BigInteger(1, digest.digest()).toString(16);
			// return sha1;
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes());
			byte byteData[] = md.digest();
			byte[] data2 = Base64.encode(byteData);
			sha1 = new String(data2);
			return sha1;

		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(PasswordEJB.class.getName()).log(Level.SEVERE,
					null, ex);
			return null;
		}
	}

}
