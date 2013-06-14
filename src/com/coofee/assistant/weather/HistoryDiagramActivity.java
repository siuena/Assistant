package com.coofee.assistant.weather;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.coofee.assistant.R;
import com.coofee.assistant.provider.Assistant;
import com.coofee.assistant.provider.MyAbsSQLiteOpenHelper;
import com.coofee.assistant.util.DateUtil;
import com.coofee.assistant.weather.model.WeatherHistory;
import com.coofee.assistant.weather.model.WeatherInfo;

public class HistoryDiagramActivity extends Activity {
	public static final String EXTRA_HISTORY_AREA_NAME = "area_name";
	public static final String EXTRA_JSON_WEATHER_INFO = "weather_info";

	private WeatherHistoryView mHistoryView;
	private String mAreaName;
	private String mJsonWeatherInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_history_activity);

		initViews();
		check();
	}

	private void check() {
		Intent data = getIntent();

		mAreaName = data.getStringExtra(EXTRA_HISTORY_AREA_NAME);
		if (mAreaName != null) {
			ArrayList<WeatherHistory> weatherHistoryList = getWeatherHistoryByAreaName(this, mAreaName);
			mHistoryView.init(weatherHistoryList);
		} else {
			mJsonWeatherInfo = data.getStringExtra(EXTRA_JSON_WEATHER_INFO);
			if (!TextUtils.isEmpty(mJsonWeatherInfo)) {
				WeatherInfo weatherInfo = JSON.parseObject(mJsonWeatherInfo, WeatherInfo.class);
				mHistoryView.init(weatherInfo);
			}
		}
	}

	private void initViews() {
		mHistoryView = (WeatherHistoryView) findViewById(R.id.weatherHistoryView);
	}
	
	public static ArrayList<WeatherHistory> getWeatherHistoryByAreaName(Context context, String areaName) {
		Cursor cursor = null;
		try {
			cursor = context.getContentResolver().query(
					Assistant.Weather.History.CONTENT_URI, null,
					Assistant.Weather.History.AREA_NAME + " = ?",
					new String[] { areaName },
					Assistant.Weather.History.DATE + " ASC");
			
			if (cursor != null && cursor.getCount() > 0) {
				ArrayList<WeatherHistory> weatherHistoryList = new ArrayList<WeatherHistory>();
				
				cursor.moveToPrevious();
				while (cursor.moveToNext()) {
					weatherHistoryList.add(readHistoryFromCursor(cursor, areaName));
				}
				
				return weatherHistoryList;
			}
		} finally {
			MyAbsSQLiteOpenHelper.closeCursor(cursor);
		}
		
		return null;
	}
	
	public static WeatherHistory readHistoryFromCursor(Cursor cursor, final String areaName) {
		String dateStr = cursor.getString(cursor.getColumnIndex(Assistant.Weather.History.DATE));
		float dayTemp = cursor.getFloat(cursor.getColumnIndex(Assistant.Weather.History.DAY_TEMPERATURE));
		float nightTemp = cursor.getFloat(cursor.getColumnIndex(Assistant.Weather.History.NIGHT_TEMPERATURE));
		String dayStatus = cursor.getString(cursor.getColumnIndex(Assistant.Weather.History.DAY_STATUS));
		String nightStatus = cursor.getString(cursor.getColumnIndex(Assistant.Weather.History.NIGHT_STATUS));
		
		
		WeatherHistory history = new WeatherHistory();
		history.setDate(DateUtil.toDate(dateStr));
		history.setDayStatus(dayStatus);
		history.setNightStatus(nightStatus);
		history.setDayTemperature(dayTemp);
		history.setNightTemperature(nightTemp);
		history.setWeatherAreaName(areaName);
		
		return history;
	}
}
