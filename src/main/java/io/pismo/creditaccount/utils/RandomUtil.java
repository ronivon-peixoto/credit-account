package io.pismo.creditaccount.utils;

import java.util.Random;

public class RandomUtil {

	private static final Random RANDOM = new Random();

	private RandomUtil() {
	}

	public static String generateRandomNumber() {
		return String.format("%014d", (RANDOM.nextLong() % 100000000000000L) + 5200000000000000L);
	}

}
