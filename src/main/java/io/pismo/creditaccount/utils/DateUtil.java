package io.pismo.creditaccount.utils;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

public class DateUtil {

	private DateUtil() {
	}

	public static LocalDateTime[] generateDueDates(LocalDateTime initialDate, Integer qtde) {
		LocalDateTime[] result = new LocalDateTime[qtde];
		IntStream.range(0, qtde).forEach(idx -> result[idx] = initialDate.plusMonths(idx));
		return result;
	}

}
