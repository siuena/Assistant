package com.coofee.assistant.provider;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import com.coofee.assistant.MyAbsCursorAdapter;
import com.coofee.assistant.R;

public class WeatherHistoryAreaAdapter extends MyAbsCursorAdapter {

	public WeatherHistoryAreaAdapter(Context context, Cursor c) {
		super(context, R.layout.weather_analogous_city_list_item, c);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		final TextView weatherAreaNameView = (TextView) view
				.findViewById(R.id.analogous_weather_city_list_item_cityName);
		final String areaName = readAreaNameFromCursor(cursor);
		
		weatherAreaNameView.setText(areaName == null ? "" : areaName);
	}
	
	public static final String readAreaNameFromCursor(Cursor cursor) {
		return cursor.getString(cursor.getColumnIndex(Assistant.Weather.History.AREA_NAME));
	}
}
