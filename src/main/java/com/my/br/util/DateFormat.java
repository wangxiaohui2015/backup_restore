package com.my.br.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {

	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String TIME_FORMAT = "HH:mm:ss";

	public static String dateToString(Date date, String type) {
		SimpleDateFormat format = new SimpleDateFormat(type);
		String result = format.format(date);
		return result;
	}

	public static String dateToString(Date date) {
		return dateToString(date, DATE_FORMAT);
	}

	public static Date stringToDate(String strDate, String type) {
		SimpleDateFormat format = new SimpleDateFormat(type);
		Date date = new Date();
		try {
			date = format.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date stringToDate(String strDate) {
		return stringToDate(strDate, DATE_FORMAT);
	}

	public static String dateFormat(String strDate) {
		String result = "";
		Date date = stringToDate(strDate, DATE_FORMAT);
		result = dateToString(date, DATE_FORMAT);
		return result;
	}

	public static String calcElapsed(Date begin, Date end) {
		long diff = end.getTime() - begin.getTime();
		long days = diff / (1000 * 60 * 60 * 24);
		long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
				* (1000 * 60 * 60))
				/ (1000 * 60);
		long seconds = (diff - days * (1000 * 60 * 60 * 24) - hours
				* (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000;
		String daysString = "";
		if (days != 0) {
			daysString = days + " days, ";
		}
		return daysString + (hours < 10 ? "0" + hours : hours) + ":"
				+ (minutes < 10 ? "0" + minutes : minutes) + ":"
				+ (seconds < 10 ? "0" + seconds : seconds);
	}
}
