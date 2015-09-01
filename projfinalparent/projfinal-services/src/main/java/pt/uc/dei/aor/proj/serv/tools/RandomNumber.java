/*
 */
package pt.uc.dei.aor.proj.serv.tools;

import java.util.Random;

/**
 * @author Carlos + Catarina
 */
public class RandomNumber {

	public static int randInt(int min, int max) {

		Random rand = new Random();
		// generate random number from a max and a min
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}
}
