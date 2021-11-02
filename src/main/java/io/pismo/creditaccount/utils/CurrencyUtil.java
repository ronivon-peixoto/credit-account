package io.pismo.creditaccount.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyUtil {

	private CurrencyUtil() {
	}

	public static BigDecimal[] generateInstallmentAmounts(BigDecimal amount, Integer installments) {
		BigDecimal firstAmounts = amount.divide(BigDecimal.valueOf(installments), RoundingMode.DOWN);
		BigDecimal lastAmount = amount.subtract(firstAmounts.multiply(BigDecimal.valueOf(installments).subtract(BigDecimal.ONE)));
		BigDecimal[] result = new BigDecimal[installments];
		for (int i = 0; i < installments - 1; i++) {
			result[i] = firstAmounts;
		}
		result[installments - 1] = lastAmount;
		return result;
	}
}
