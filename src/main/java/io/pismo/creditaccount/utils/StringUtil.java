package io.pismo.creditaccount.utils;

import static org.apache.commons.lang3.StringUtils.isNoneBlank;

public class StringUtil {

	private StringUtil() {
	}

	public static String concat(String separator, String... values) {
		StringBuilder sb = new StringBuilder(values[0]);
		String sepWithSpaces = " ".concat(separator).concat(" ");
		for (int i = 1; i < values.length; i++) {
			if (isNoneBlank(values[i])) {
				sb.append(sepWithSpaces).append(values[i].trim());
			}
		}
		return sb.toString().toUpperCase();
	}

	public static String concatItemInQtde(int item, int qtde) {
		return new StringBuilder().append(" (").append(item).append("/").append(qtde).append(")").toString();
	}

}
