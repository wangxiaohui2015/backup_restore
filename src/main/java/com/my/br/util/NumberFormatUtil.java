package com.my.br.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NumberFormatUtil {

	public static String formatDoubleNumber(String doubleNum, String formatStr) {
		NumberFormat format = new DecimalFormat(formatStr);
		String str = format.format(Double.parseDouble(doubleNum));
		return str;

	}

	public static String formatDoubleNumber(String doubleNum) {
		return formatDoubleNumber(doubleNum, "0.###");

	}

	public static String formatDoubleNumber(double doubleNum) {
		return formatDoubleNumber(String.valueOf(doubleNum), "0.###");

	}
}
