/*
 */

package pt.uc.dei.aor.proj.serv.tools;

import java.util.Random;

/**
 * @author Carlos + Catarina
 */
public class RandomName {

	public RandomName() {
	}

	/**
	 *
	 * @param numchars
	 * @return a Random Name
	 */
	public static String getRandomName(int numchars) {
		char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 20; i++) {
			//generate a random character to a String list
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		String output = sb.toString();
		return output;
	}
}