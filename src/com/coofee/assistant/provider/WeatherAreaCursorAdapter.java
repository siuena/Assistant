package com.coofee.assistant.provider;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import com.coofee.assistant.MyAbsCursorAdapter;
import com.coofee.assistant.R;
import com.coofee.assistant.weather.model.Area;

public class WeatherAreaCursorAdapter extends MyAbsCursorAdapter {

	public WeatherAreaCursorAdapter(Context context, Cursor c) {
		super(context, R.layout.weather_analogous_city_list_item, c);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		final TextView weatherAreaNameView = (TextView) view
				.findViewById(R.id.analogous_weather_city_list_item_cityName);
		final String areaName = cursor.getString(cursor.getColumnIndex(Assistant.Weather.Area.NAME));
		
		weatherAreaNameView.setText(areaName == null ? "" : areaName);
	}

	public static Area readAreaFromCursor(Cursor cursor) {
		Area area = new Area();
		
		area.setName(cursor.getString(cursor.getColumnIndex(Assistant.Weather.Area.NAME)));
		area.setId(cursor.getLong(cursor.getColumnIndex(Assistant.Weather.Area._ID)));
		area.setCode(cursor.getString(cursor.getColumnIndex(Assistant.Weather.Area.WEATHER_CODE)));
		area.setProvince(cursor.getString(cursor.getColumnIndex(Assistant.Weather.Area.PROVINCE)));
		area.setZipcode(cursor.getString(cursor.getColumnIndex(Assistant.Weather.Area.ZIPCODE)));
		area.setPhoneAreaCode(cursor.getString(cursor.getColumnIndex(Assistant.Weather.Area.PHONE_CODE)));
		area.setHypy(cursor.getString(cursor.getColumnIndex(Assistant.Weather.Area.HYPY)));
		area.setShortHypy(cursor.getString(cursor.getColumnIndex(Assistant.Weather.Area.SHORT_HYPY)));
		
		return area;
	}
}
