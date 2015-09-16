package pt.uc.dei.aor.proj.ejb;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

<<<<<<< HEAD
import java.io.UnsupportedEncodingException;
=======
>>>>>>> 5e823c2f5a61818d78e41e3ca12efeaf180278f6
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
		
<<<<<<< HEAD
		
//			try {
//	            MessageDigest md = MessageDigest.getInstance("SHA-256");
//	            md.update(password.getBytes("UTF-8")); // Change this to "UTF-16" if needed
//	            byte[] digest = md.digest();
//	            BigInteger bigInt = new BigInteger(1, digest);
//	            String output = bigInt.toString(16);
//	            return output;
//	        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
//	            Logger.getLogger(EncryptPassword.class.getName()).log(Level.SEVERE, null, ex);
//	            return ex.getMessage();
//	        }
=======
		try {
>>>>>>> 5e823c2f5a61818d78e41e3ca12efeaf180278f6
			// digest = MessageDigest.getInstance("SHA-1");
			// digest.update(password.getBytes(), 0, password.length());
			// sha1 = new BigInteger(1, digest.digest()).toString(16);
			// return sha1;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes("UTF-8"));
			byte byteData[] = md.digest();
			byte[] data2 = Base64.encode(byteData);
			sha1 = new String(data2);
			return sha1;

		} catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
			Logger.getLogger(PasswordEJB.class.getName()).log(Level.SEVERE,
					null, ex);
			return null;
		}
	}

}
