package com.coofee.assistant.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import android.text.TextUtils;
import android.util.Log;

public class StringUtil {
	private static final String TAG = "StringUtil";
	
	/**
	 * 将2010-02-12T19:56:44+08:00字符串转换为2010-02-12 19:56:44
	 */
	public static String parseToDateString(String string) {
		if (TextUtils.isEmpty(string))
			return null;
		return string.substring(0, string.lastIndexOf('+')).replace('T', ' ');
	}

	public static int parseInt(String string) {
		if (TextUtils.isEmpty(string))
			return 0;

		return Integer.parseInt(string);
	}

	public static String isNull(Object x) {
		if (x == null)
			return " is null ? true. ";
		else
			return " is null ? false. ";
	}

	public static String arrayToString(String[] array) {
		if (array == null || array.length == 0) {
			return "";
		}

		StringBuilder builder = new StringBuilder();
		for (String s : array) {
			builder.append(s).append("; ");
		}
		builder.delete(builder.length() - 2, builder.length());

		return builder.toString();
	}

	public static String arrayToString(Object[] array) {
		if (array == null || array.length == 0)
			return "[]";

		StringBuffer strings = new StringBuffer();
		strings.append("[");
		for (Object x : array) {
			strings.append(x.toString()).append("; ");
		}
		strings.delete(strings.length() - 2, strings.length());
		strings.append("]");
		return strings.toString();
	}

	public static String stringArrayToString(String[] array) {
		return arrayToString(array);
	}

	public static String streamToString(InputStream in, String charset) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					in, charset));
			StringBuffer buffer = new StringBuffer();
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return buffer.toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}

	public static void println(String[][] multiArray) {
		for (int i = 0; i < multiArray.length; i++) {
			String[] row = multiArray[i];
			for (int j = 0; j < row.length; j++) {
				Log.d("StringUtil println", row[j]);
			}
		}
	}

	public static void printList(List<?> list) {
		if (list == null || list.isEmpty()) {
			Log.d(TAG, "list is null or empty.");
			return;
		}

		int i = 0;
		for (Object obj : list) {
			Log.d(TAG, "#" + (i++) + ">" + obj);
		}
	}
}
