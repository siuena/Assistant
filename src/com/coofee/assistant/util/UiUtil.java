package com.coofee.assistant.util;

import com.coofee.assistant.AssistantApp;

import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.Toast;

public class UiUtil {

	public static void toastShort(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	
	public static void toastLong(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}
	
	public static void toast(Context context, String text, int duration) {
		Toast.makeText(context, text, duration).show();
	}	
	
	public static DisplayMetrics getScreenInfo() {
		return AssistantApp.getInstance().getResources().getDisplayMetrics();
	}
}
