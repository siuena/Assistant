package com.coofee.assistant.weather;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteTransactionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.location.ReceiveListener;
import com.coofee.assistant.R;
import com.coofee.assistant.provider.Assistant;
import com.coofee.assistant.provider.AssistantSQLiteHelper;
import com.coofee.assistant.provider.MyAbsSQLiteOpenHelper;
import com.coofee.assistant.provider.WeatherAreaCursorAdapter;
import com.coofee.assistant.util.Pinyin;
import com.coofee.assistant.util.UiUtil;
import com.coofee.assistant.util.location.BaiduLocationModel;
import com.coofee.assistant.util.location.MyBaiduLocation;
import com.coofee.assistant.weather.model.Area;

public class WeatherSearchActivity extends Activity {
	private static final String TAG = "WeatherSearchActivity";
	// 通过关键字获取相似的城市信息成功.
	// private static final int REQUEST_ANALOGOUS_WEATHER_CITY_SUCCESSED =
	// 0x0001;
	// 通过关键字获取相似的城市信息失败.
	// private static final int REQUEST_ANALOGOUS_WEATHER_CITY_FAILED = 0x0002;

	// private ProgressDialog mProgressDialog;

	// private Button mChooseCity;

	// private AnalogousWeatherCityAdapter mAnalogousWeatherCityAdapter = null;

	// private List<WeatherCity> mWeatherCityList = null;

	private EditText mWeatherSearchInput;
	private ListView mWeatherSearchResult;
	private WeatherAreaCursorAdapter mWeatherCursorAdapter;

	private String mUserCurrentCity = null;

	public static final String WEATHER_CITY_NAME = "weatherCityName";
	public static final String WEATHER_CITY_CODE = "weatherCityCode";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_search_activity);

		init();
		setListeners();
		initData();
		
		startGetUserLocation();
	}

	private void init() {
		mWeatherSearchInput = (EditText) findViewById(R.id.weather_search_input);
		mWeatherSearchResult = (ListView) findViewById(R.id.weather_search_listResult);
		// mChooseCity = (Button) findViewById(R.id.weather_search_chooseCity);

		findViewById(R.id.weather_search_weatherHistory).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(WeatherSearchActivity.this,
								WeatherHistoryAreaActivity.class);
						WeatherSearchActivity.this.startActivity(intent);
					}
				});
	}

	// 给各个组件添加事件监听.
	private void setListeners() {

		// mChooseCity.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// String input = mWeatherSearchInput.getText().toString();
		// if (TextUtils.isEmpty(input))
		// return;
		//
		// String keyword = input.trim();
		// if (TextUtils.isEmpty(keyword))
		// return;
		//
		// requestAnalogousWeatherCity(keyword);
		// }
		// });

		mWeatherSearchResult
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// if (mAnalogousWeatherCityAdapter != null
						// && mAnalogousWeatherCityAdapter.getCount() > 0) {
						// WeatherCity city = mAnalogousWeatherCityAdapter
						// .get(position);
						// Intent intent = new Intent(
						// WeatherSearchActivity.this,
						// WeatherSearchResultActivity.class);
						// intent.putExtra(WEATHER_CITY_NAME, city.getName());
						// intent.putExtra(WEATHER_CITY_ID, city.getId());
						// WeatherSearchActivity.this.startActivity(intent);
						// }

						if (mWeatherCursorAdapter != null
								&& mWeatherCursorAdapter.getCount() > 0) {
							final Cursor cursor = mWeatherCursorAdapter
									.getCursor();
							final Area area = WeatherAreaCursorAdapter
									.readAreaFromCursor(cursor);
							Intent intent = new Intent(
									WeatherSearchActivity.this,
									WeatherSearchResultActivity.class);
							intent.putExtra(WEATHER_CITY_NAME, area.getName());
							intent.putExtra(WEATHER_CITY_CODE, area.getCode());
							WeatherSearchActivity.this.startActivity(intent);
						}
					}
				});

		// 仅供测试
		// mWeatherCityList = new ArrayList<WeatherCity>();
		// mWeatherCityList.add(new WeatherCity("北京", "101010100"));
		// mAnalogousWeatherCityAdapter = new AnalogousWeatherCityAdapter(
		// this, mWeatherCityList);
		// mWeatherSearchResult.setAdapter(mAnalogousWeatherCityAdapter);
	}

	private void initData() {
		AssistantSQLiteHelper.initWeatherCityData(
				WeatherSearchActivity.this, new SQLiteTransactionListener() {
					private ProgressDialog progressDialog;

					@Override
					public void onRollback() {
						UiUtil.toastLong(WeatherSearchActivity.this,
								"初始化地区信息失败!");
						if (progressDialog != null) {
							progressDialog.dismiss();
						}

					}

					@Override
					public void onCommit() {
						if (progressDialog != null) {
							progressDialog.dismiss();
						}
					}

					@Override
					public void onBegin() {
						progressDialog = new ProgressDialog(
								WeatherSearchActivity.this);
						progressDialog.setTitle("Assistant");
						progressDialog.setMessage("正在初始化数据库信息...");
						progressDialog.setCancelable(false);
						progressDialog.setCanceledOnTouchOutside(false);
						progressDialog.show();
					}
				});

		Cursor cursor = this.getContentResolver().query(
				Assistant.Weather.Area.CONTENT_URI, null, null, null, null);
		mWeatherCursorAdapter = new WeatherAreaCursorAdapter(
				WeatherSearchActivity.this, cursor);

		mWeatherSearchResult.setAdapter(mWeatherCursorAdapter);

		mWeatherSearchInput.addTextChangedListener(new TextWatcher() {
			private Cursor cursor = null;

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!TextUtils.isEmpty(s)) {
					final String keyword = s.toString();
					if (!TextUtils.isEmpty(keyword)) {
						if (Pinyin.isCn(keyword)) {
							// 如果是汉字,则根据地区名称查询.
							cursor = WeatherSearchActivity.this
									.getContentResolver().query(
											Assistant.Weather.Area.CONTENT_URI,
											null,
											Assistant.Weather.Area.NAME
													+ " like '" + keyword
													+ "%'", null, null);
						} else {
							if (TextUtils.isDigitsOnly(keyword)) {
								// 如果输入的是纯数字则根据邮政编码和地区区号查询.
								if (keyword.startsWith("0")) {
									// 第一个字母是0, 说明是电话区号. 根据电话区号查询
									cursor = WeatherSearchActivity.this
											.getContentResolver()
											.query(Assistant.Weather.Area.CONTENT_URI,
													null,
													Assistant.Weather.Area.PHONE_CODE
															+ " like '"
															+ keyword + "%'",
													null, null);
								} else {
									// 根据邮政编码查询
									cursor = WeatherSearchActivity.this
											.getContentResolver()
											.query(Assistant.Weather.Area.CONTENT_URI,
													null,
													Assistant.Weather.Area.ZIPCODE
															+ " like '"
															+ keyword + "%'",
													null, null);
								}
							} else {
								// 如果输入的是拼音则根据汉语拼音查询.
								cursor = WeatherSearchActivity.this
										.getContentResolver()
										.query(Assistant.Weather.Area.CONTENT_URI,
												null,
												Assistant.Weather.Area.HYPY
														+ " like '"
														+ keyword
														+ "%' OR "
														+ Assistant.Weather.Area.SHORT_HYPY
														+ " like '" + keyword
														+ "%'", null, null);
							}
						}

						mWeatherCursorAdapter.changeCursor(cursor);
					}
				} else {
					cursor = WeatherSearchActivity.this
							.getContentResolver().query(
									Assistant.Weather.Area.CONTENT_URI, null,
									null, null, null);
					mWeatherCursorAdapter.changeCursor(cursor);
				}
			}
		});
	}

	private void startGetUserLocation() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
//				Looper.prepare();
				getUserCurrentLocation();
//				Looper.loop();
			}
		});
	}
	
	private void gotoWeatherResultActivity(String cityName, String weatherCode) {
		Intent intent = new Intent(WeatherSearchActivity.this,
				WeatherSearchResultActivity.class);
		intent.putExtra(WEATHER_CITY_NAME, cityName);
		intent.putExtra(WEATHER_CITY_CODE, weatherCode);
		WeatherSearchActivity.this.startActivity(intent);
	}

	/**
	 * 使用百度定位获取用户当前所在地.
	 */
	private void getUserCurrentLocation() {
		// 添加获取用户当前所在地成功时的监听.
		final MyBaiduLocation baiduLocationClient = MyBaiduLocation
				.getInstance();
		baiduLocationClient.setReceiveListener(new ReceiveListener() {
			/** 查询用户当前所在地的最大次数 **/
			private final int MAX_QUERY_LOCATION_COUNT = 4;
			/** 记录已查询次数 **/
			private int hasQueryCount = 0;

			@Override
			public void onReceive(String strData) {
				// 当位置没有发生变化，而发起定位或者API尚未生成定位依据而发起定位时, 服务器返回null。
				// 当超时时, 百度服务器会返回"InternetException".
				if (!TextUtils.isEmpty(strData)
						&& !strData.equals("InternetException")) {
					// 解析百度服务器返回的数据, 获取用户所在城市
					BaiduLocationModel locationData = JSON.parseObject(strData,
							BaiduLocationModel.class);

					// “error”为161表示定位成功，其他值为失败。
					if (locationData.getResult().getError() == 161) {
						// 发送消息, 用于关闭百度定位
						mSearchWeatherCityHandler
								.sendEmptyMessage(CLOSE_BAIDU_LOCATING);

						// 获取用户当前所在地.
						mUserCurrentCity = locationData.getContent().getAddr()
								.getCity().split("市")[0];

					}
				} else if (hasQueryCount < MAX_QUERY_LOCATION_COUNT) {
					hasQueryCount++;
					// 如果未能查询到当前地点并且已查询的次数小于最大允许查询次数, 则继续查询.
					try {
						// 暂停2s后, 继续查询用户地点.
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					// 重新发起一次定位
					baiduLocationClient.getLocation();
				} else {
					// 超过最大查询次数时, 发送消息, 关闭百度定位
					mSearchWeatherCityHandler
							.sendEmptyMessage(CLOSE_BAIDU_LOCATING);
				}
			}
		});
		baiduLocationClient.startQueryUserCity();
	}

	// private void requestAnalogousWeatherCity(final String keyword) {
	// mProgressDialog = new ProgressDialog(this);
	// mProgressDialog.setMessage("正在获取城市信息...");
	// mProgressDialog.setIndeterminate(true);
	// mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	// mProgressDialog.setCancelable(true);
	// mProgressDialog.show();
	//
	// ((AssistantApp) getApplication()).mExecutor.execute(new Runnable() {
	// @Override
	// public void run() {
	// try {
	// mWeatherCityList = WeatherHelper
	// .getAnalogousWeatherCityByKeyword(keyword);
	// if (mWeatherCityList != null) {
	// mSearchWeatherCityHandler
	// .sendEmptyMessage(REQUEST_ANALOGOUS_WEATHER_CITY_SUCCESSED);
	// } else {
	// mSearchWeatherCityHandler
	// .sendEmptyMessage(REQUEST_ANALOGOUS_WEATHER_CITY_FAILED);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// mSearchWeatherCityHandler
	// .sendEmptyMessage(REQUEST_ANALOGOUS_WEATHER_CITY_FAILED);
	// }
	// }
	// });
	// }

	// private void onRequstAnalogousWeatherCityOk() {
	// if (mAnalogousWeatherCityAdapter == null) {
	// mAnalogousWeatherCityAdapter = new AnalogousWeatherCityAdapter(
	// this, mWeatherCityList);
	// mWeatherSearchResult.setAdapter(mAnalogousWeatherCityAdapter);
	// } else {
	// mAnalogousWeatherCityAdapter.setWeatherCityList(mWeatherCityList);
	// }
	// }

	private void onGetUserCurrentLocationSuccess() {
		Log.d(TAG, "获取当前所在地: " + mUserCurrentCity);
		Cursor cursor = getContentResolver().query(
				Assistant.Weather.Area.CONTENT_URI,
				new String[] { Assistant.Weather.Area.WEATHER_CODE },
				Assistant.Weather.Area.NAME + " = ?",
				new String[] { mUserCurrentCity }, null);

		if (cursor != null) {
			if (cursor.getCount() == 1) {
				cursor.moveToFirst();
				final String weatherCode = cursor.getString(0);
				Log.d(TAG, "user current location weather code: " + weatherCode);

				if (!TextUtils.isEmpty(weatherCode)) {
					TextView userCurrentLocation = (TextView) findViewById(R.id.weather_search_userCurrentLocation);
					userCurrentLocation.setVisibility(View.VISIBLE);
					userCurrentLocation.setText("当前所在地：" + mUserCurrentCity);
					userCurrentLocation
							.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									gotoWeatherResultActivity(mUserCurrentCity,
											weatherCode);
								}
							});
				}
			}

			MyAbsSQLiteOpenHelper.closeCursor(cursor);
		}
	}

	private SearchWeatherCityHandler mSearchWeatherCityHandler = new SearchWeatherCityHandler(
			this);

	/** 关闭百度定位 **/
	private static final int CLOSE_BAIDU_LOCATING = 0x0003;

	private static class SearchWeatherCityHandler extends Handler {
		private WeakReference<WeatherSearchActivity> _activityRef;

		public SearchWeatherCityHandler(WeatherSearchActivity activity) {
			_activityRef = new WeakReference<WeatherSearchActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			final WeatherSearchActivity activity = _activityRef.get();
			if (activity != null) {
				// activity.mProgressDialog.dismiss();
				switch (msg.what) {
				// case CLOSE_BAIDU_LOCATING:
				// Log.d(TAG + " handleMessage", "request ok.");
				// activity.onRequstAnalogousWeatherCityOk();
				// break;
				// case REQUEST_ANALOGOUS_WEATHER_CITY_FAILED:
				// Log.d(TAG + " handleMessage", "request failed.");
				// UiUtil.toastShort(activity, "获取城市名称失败!");
				// break;
				case CLOSE_BAIDU_LOCATING:
//					MyBaiduLocation.getInstance().close();
					
					if (TextUtils.isEmpty(activity.mUserCurrentCity)) {
						Log.d(TAG, "获取用户当前所在第失败!");
					} else {
						activity.onGetUserCurrentLocationSuccess();
					}
					break;
				}
			}
			super.handleMessage(msg);
		}
	}
}
