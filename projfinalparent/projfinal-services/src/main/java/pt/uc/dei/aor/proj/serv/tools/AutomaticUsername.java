/*
 */
package pt.uc.dei.aor.proj.serv.tools;

/**
 * @author
 */
public class AutomaticUsername {

	/**
	 * Get Username from first Letter of first Name, all letters from Last Name
	 * and 3 numbers from mobile phone, from position 6,7 and 8
	 *
	 * @param firstName
	 * @param lastName
	 * @param mobilPhone
	 * @return
	 */
	public static String getUsername(String firstName, String lastName, String mobilPhone) {

		return firstName.substring(0, 1) + lastName + mobilPhone.substring(5, 8);
	}

	public AutomaticUsername() {
	}

}
