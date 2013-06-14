package com.coofee.assistant.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static Date toDate(String dateStr) {
		SimpleDateFormat spf = new SimpleDateFormat("yyyy年MM月dd日");
		try {
			return spf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String toString(Date date) {
		SimpleDateFormat spf = new SimpleDateFormat("yyyy年MM月dd日");
		return spf.format(date);
	}

}
