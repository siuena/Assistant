package com.coofee.assistant.weather;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.coofee.assistant.AssistantApp;
import com.coofee.assistant.R;
import com.coofee.assistant.provider.AssistantSQLiteHelper;
import com.coofee.assistant.util.StringUtil;
import com.coofee.assistant.util.UiUtil;
import com.coofee.assistant.weather.model.RealtimeWeatherInfo;
import com.coofee.assistant.weather.model.WeatherHistory;
import com.coofee.assistant.weather.model.WeatherInfo;
import com.coofee.assistant.weather.model.WeatherModel;

public class WeatherSearchResultActivity extends Activity {
	private static final String TAG = "WeatherSearchResultActivity";

	private static final int REQUEST_WEATHER_SUCCESSED = 0x0001;
	private static final int REQUEST_WEATHER_FAILED = 0x0002;
	private static final int REQUEST_REALTIME_WEATHER_SUCCESSED = 0x0003;
	private static final int REQUEST_REALTIME_WEATHER_FAILED = 0x0004;

	private ViewPager mViewPager;
	private View[] mPages;
	private WeatherInfo mWeatherInfo;
	private RealtimeWeatherInfo mRealtimeWeatherInfo;
	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_result_activity);

		check();
		setTitle();
		initActionBar();
		initViewPager();

		// 获取天气信息
		String cityId = getIntent().getStringExtra(
				WeatherSearchActivity.WEATHER_CITY_CODE);
		requestCityWeather(cityId);
	}

	/**
	 * ActionBar上添加获取实时天气。
	 */
	private void initActionBar() {
		findViewById(R.id.weather_result_weatherDiagram).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						WeatherSearchResultActivity.this,
						HistoryDiagramActivity.class);
				intent.putExtra(
						HistoryDiagramActivity.EXTRA_JSON_WEATHER_INFO,
						JSON.toJSONString(mWeatherInfo));
				WeatherSearchResultActivity.this.startActivity(intent);
			}
		});
		
		findViewById(R.id.weather_result_realtimeWeather).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String cityId = getIntent().getStringExtra(
								WeatherSearchActivity.WEATHER_CITY_CODE);
						requestCityRealtimeWeather(cityId);
					}
				});
	}

	private void check() {
		if (TextUtils.isEmpty(getIntent().getStringExtra(
				WeatherSearchActivity.WEATHER_CITY_NAME))) {
			finish();
		}

		if (TextUtils.isEmpty(getIntent().getStringExtra(
				WeatherSearchActivity.WEATHER_CITY_CODE))) {
			finish();
		}
	}

	private void setTitle() {
		((TextView) findViewById(R.id.weather_result_cityName))
				.setText(getIntent().getStringExtra(
						WeatherSearchActivity.WEATHER_CITY_NAME));
	}

	private void initViewPager() {
		mViewPager = (ViewPager) findViewById(R.id.weather_result_viewPager);
		LayoutInflater inflater = getLayoutInflater();
		mPages = new View[6];
		// 一次可以获取6天的天气信息, 所以需要6个页面.
		for (int i = 0; i < 6; i++) {
			mPages[i] = inflater.inflate(R.layout.weather_page, null);
		}
		mViewPager.setAdapter(new WeatherPageAdapter(mPages));
		mViewPager.setCurrentItem(0);
	}

	/**
	 * 请求获取天气信息
	 */
	private void requestCityWeather(final String cityId) {
		mProgressDialog = new ProgressDialog(WeatherSearchResultActivity.this);
		mProgressDialog.setMessage("正在获取天气信息...");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCancelable(true);
		mProgressDialog.show();

		((AssistantApp) getApplication()).mExecutor.execute(new Runnable() {

			@Override
			public void run() {
				try {
					WeatherModel weatherModel = WeatherHelper
							.getCityWeatherByCityId(cityId);
					Log.d(TAG + " requestCityWeather", "weatherModel"
							+ StringUtil.isNull(weatherModel));
					Log.d(TAG + " requestCityWeather",
							(weatherModel != null ? "weatherinfo"
									+ StringUtil.isNull(weatherModel
											.getWeatherinfo())
									: "weatherModel is null."));

					if (weatherModel != null
							&& weatherModel.getWeatherinfo() != null) {
						mWeatherInfo = weatherModel.getWeatherinfo();
						mWeatherResultHandler
								.sendEmptyMessage(REQUEST_WEATHER_SUCCESSED);
					} else {
						mWeatherResultHandler
								.sendEmptyMessage(REQUEST_WEATHER_FAILED);
					}
				} catch (Exception e) {
					e.printStackTrace();
					mWeatherResultHandler
							.sendEmptyMessage(REQUEST_WEATHER_FAILED);
				}
			}
		});
	}

	private void requestCityRealtimeWeather(final String cityId) {
		mProgressDialog = new ProgressDialog(WeatherSearchResultActivity.this);
		mProgressDialog.setMessage("正在获取天气信息...");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();

		((AssistantApp) getApplication()).mExecutor.execute(new Runnable() {

			@Override
			public void run() {
				try {
					RealtimeWeatherInfo realtimeWeatherInfo = WeatherHelper
							.getCityRealtimeWeatherByCityId(cityId);
					Log.d(TAG + " requestCityRealtimeWeather",
							"realtimeWeather"
									+ StringUtil.isNull(realtimeWeatherInfo));

					if (realtimeWeatherInfo != null) {
						mRealtimeWeatherInfo = realtimeWeatherInfo;
						mWeatherResultHandler.sendEmptyMessage(REQUEST_REALTIME_WEATHER_SUCCESSED);
					} else {
						mWeatherResultHandler
								.sendEmptyMessage(REQUEST_REALTIME_WEATHER_FAILED);
					}
				} catch (Exception e) {
					e.printStackTrace();
					mWeatherResultHandler
							.sendEmptyMessage(REQUEST_REALTIME_WEATHER_FAILED);
				}
			}
		});
	}

	private void onRequestCityWeatherOk() {
		Log.d(TAG, "onRequestCityWeatherOk");
		
		ArrayList<WeatherHistory> historyList = WeatherHelper.toWeatherHistory(mWeatherInfo);
		if (historyList != null && historyList.size() > 0) {
			WeatherHistory currentDayWeather = historyList.get(0);
//			ContentValues values = new ContentValues();
//			values.put(Assistant.Weather.History.DATE, DateUtil.toString(currentDayWeather.getDate()));
//			values.put(Assistant.Weather.History.DAY_STATUS, currentDayWeather.getDayStatus());
//			values.put(Assistant.Weather.History.NIGHT_STATUS, currentDayWeather.getNightStatus());
//			values.put(Assistant.Weather.History.DAY_TEMPERATURE, currentDayWeather.getDayTemperature());
//			values.put(Assistant.Weather.History.NIGHT_TEMPERATURE, currentDayWeather.getNightTemperature());
//			values.put(Assistant.Weather.History.AREA_NAME, currentDayWeather.getWeatherAreaName());
//			
//			getContentResolver().insert(Assistant.Weather.History.CONTENT_URI, values);
			
			AssistantSQLiteHelper.save(WeatherSearchResultActivity.this, currentDayWeather);
		}
		
		
		for (int i = 0; i < 6; i++) {
			Log.d(TAG, "onRequestCityWeatherOk #" + i);
			// 设置当前日期
			((TextView) mPages[i].findViewById(R.id.weather_date))
					.setText(WeatherHelper.getNextDay(mWeatherInfo.getDate_y(),
							i));
			// 设置白天的天气
			mPages[i].findViewById(R.id.weather_day_status_image)
					.setBackgroundResource(mWeatherInfo.getDayImage(i));
			((TextView) mPages[i].findViewById(R.id.weather_day_temperature))
					.setText(mWeatherInfo.getDayTemperature(i));
			((TextView) mPages[i].findViewById(R.id.weather_day_status))
					.setText(mWeatherInfo.getDayStatus(i));
			((TextView) mPages[i].findViewById(R.id.weather_day_wind))
					.setText(mWeatherInfo.getWind(i));
			// 设置夜间的天气情况
			mPages[i].findViewById(R.id.weather_night_status_image)
					.setBackgroundResource(mWeatherInfo.getNightImage(i));
			((TextView) mPages[i].findViewById(R.id.weather_night_temperature))
					.setText(mWeatherInfo.getNightTemperature(i));
			((TextView) mPages[i].findViewById(R.id.weather_night_status))
					.setText(mWeatherInfo.getNightStatus(i));
			((TextView) mPages[i].findViewById(R.id.weather_night_wind))
					.setText(mWeatherInfo.getWind(i));

			if (i == 0) {
				mPages[i].findViewById(R.id.weather_other_title).setVisibility(
						View.VISIBLE);
				// 设置其他信息显示
				((TextView) mPages[i].findViewById(R.id.weather_feel))
						.setText("天气情况: " + mWeatherInfo.getIndex());
				((TextView) mPages[i].findViewById(R.id.weather_tips))
						.setText("穿衣建议: " + mWeatherInfo.getIndex_d());
				((TextView) mPages[i].findViewById(R.id.weather_uv))
						.setText("紫外线强度: " + mWeatherInfo.getIndex_uv());
			} else if (i == 1) {
				mPages[i].findViewById(R.id.weather_other_title).setVisibility(
						View.VISIBLE);
				// 设置其他信息显示
				((TextView) mPages[i].findViewById(R.id.weather_feel))
						.setText("天气情况: " + mWeatherInfo.getIndex48());
				((TextView) mPages[i].findViewById(R.id.weather_tips))
						.setText("穿衣建议: " + mWeatherInfo.getIndex48_d());
				((TextView) mPages[i].findViewById(R.id.weather_uv))
						.setText("紫外线强度: " + mWeatherInfo.getIndex48_uv());
			}
		}
		mViewPager.setCurrentItem(0);
	}

	private void onRequestCityRealtimeWeatherOk() {
		View view = getLayoutInflater().inflate(R.layout.weather_realtime_dialog, null);
		Log.d(TAG, "onRequestCityRealtimeWeatherOk");
		((TextView) view.findViewById(R.id.weather_realtime_temperature)).setText(mRealtimeWeatherInfo.getTemp());
		((TextView) view.findViewById(R.id.weather_realtime_windDirection)).setText(mRealtimeWeatherInfo.getWD());
		((TextView) view.findViewById(R.id.weather_realtime_windSpeed)).setText(mRealtimeWeatherInfo.getWS());
		((TextView) view.findViewById(R.id.weather_realtime_humidity)).setText(mRealtimeWeatherInfo.getSD());
		((TextView) view.findViewById(R.id.weather_realtime_time)).setText(mRealtimeWeatherInfo.getTime());
		
		new AlertDialog.Builder(this)
			.setTitle(R.string.weather_realtime_title)
			.setView(view)
			.create()
			.show();
	}

	private WeatherSearchResultHandler mWeatherResultHandler = new WeatherSearchResultHandler(
			this);

	private static class WeatherSearchResultHandler extends Handler {
		private WeakReference<WeatherSearchResultActivity> _activityRef;

		public WeatherSearchResultHandler(WeatherSearchResultActivity activity) {
			_activityRef = new WeakReference<WeatherSearchResultActivity>(
					activity);
		}

		@Override
		public void handleMessage(Message msg) {
			WeatherSearchResultActivity activity = _activityRef.get();
			activity.mProgressDialog.dismiss();
			switch (msg.what) {
			case REQUEST_WEATHER_SUCCESSED:
				Log.d(TAG + " handleMessage", "request city weather ok.");
				activity.onRequestCityWeatherOk();
				break;
			case REQUEST_WEATHER_FAILED:
				UiUtil.toastShort(activity, "获取城市天气信息失败!");
				Log.d(TAG + " handleMessage", "request city realtime weather failed.");
				break;
			case REQUEST_REALTIME_WEATHER_SUCCESSED:
				Log.d(TAG + " handleMessage", "request city realtime weather ok.");
				activity.onRequestCityRealtimeWeatherOk();
				break;
			case REQUEST_REALTIME_WEATHER_FAILED:
				UiUtil.toastShort(activity, "获取城市实时天气信息失败!");
				Log.d(TAG + " handleMessage", "request city realtime weather failed.");
				break;
			}
			super.handleMessage(msg);
		}
	}

	private class WeatherPageAdapter extends PagerAdapter {

		private View[] _pageViews;

		public WeatherPageAdapter(View[] pageViews) {
			_pageViews = pageViews;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(_pageViews[position]);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(_pageViews[position], 0);
			return _pageViews[position];
		}

		@Override
		public int getCount() {
			return _pageViews.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}
}
