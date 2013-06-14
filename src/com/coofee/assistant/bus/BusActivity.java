package com.coofee.assistant.bus;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.location.ReceiveListener;
import com.coofee.assistant.R;
import com.coofee.assistant.util.location.BaiduLocationModel;
import com.coofee.assistant.util.location.MyBaiduLocation;

public class BusActivity extends Activity implements View.OnClickListener {
	private String TAG = BusActivity.class.getSimpleName();

	private TextView mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bus_activity);

		mTitle = (TextView) findViewById(R.id.title);
		mTitle.setText(getPreviousUserLocation());

		setListeners();

		if (TextUtils.isEmpty(getPreviousUserLocation())) {
			startGetUserLocation();
		}
	}

	private void setListeners() {
		findViewById(R.id.bus_transfer).setOnClickListener(this);
		findViewById(R.id.bus_history).setOnClickListener(this);
		findViewById(R.id.bus_line).setOnClickListener(this);
		findViewById(R.id.bus_station).setOnClickListener(this);
		mTitle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BusActivity.this,
						BusSwitchCityActivity.class);
				BusActivity.this.startActivityForResult(intent,
						Bus.REQUEST_SWITCH_CITY);

				// final View dialogView =
				// BusActivity.this.getLayoutInflater().inflate(R.layout.dialog_switch_city,
				// null);
				// new
				// AlertDialog.Builder(BusActivity.this).setView(dialogView).setPositiveButton("确定",
				// new DialogInterface.OnClickListener() {
				// @Override
				// public void onClick(DialogInterface dialog, int which) {
				// EditText switchCityInput = (EditText)
				// dialogView.findViewById(R.id.switch_city);
				// if (!TextUtils.isEmpty(switchCityInput.getText())
				// &&
				// !TextUtils.isEmpty(switchCityInput.getText().toString().trim()))
				// {
				// mUserCurrentCity =
				// switchCityInput.getText().toString().trim();
				// saveUserCurrentLocation();
				// }
				// }
				// }).create().show();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK
				&& requestCode == Bus.REQUEST_SWITCH_CITY) {
			mUserCurrentCity = data.getStringExtra(Bus.EXTRA_BUS_CITY_NAME);
			saveUserCurrentLocation();
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.bus_transfer:
			intent = new Intent(BusActivity.this,
					BusTransferSearchActivity.class);
			break;
		case R.id.bus_history:
			intent = new Intent(BusActivity.this, BusHistoryActivity.class);
			break;
		case R.id.bus_line:
			intent = new Intent(BusActivity.this, BusLineSearchActivity.class);
			break;
		case R.id.bus_station:
			intent = new Intent(BusActivity.this,
					BusStationSearchActivity.class);
			break;
		}

		if (intent != null) {
			if (TextUtils.isEmpty(mUserCurrentCity)) {
				// 如果定位出现错误， 则使用用户最近一次的定位到的城市。
				mUserCurrentCity = getPreviousUserLocation();
			}

			Log.d(TAG, "put: " + mUserCurrentCity);
			intent.putExtra(Bus.EXTRA_USER_CURRENT_CITY, mUserCurrentCity);
			BusActivity.this.startActivity(intent);
		}
	}

	private void startGetUserLocation() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				getUserCurrentLocation();
			}
		});
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
			private final int MAX_QUERY_LOCATION_COUNT = 5;
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
						mBusHandler.sendEmptyMessage(CLOSE_BAIDU_LOCATING);

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
					mBusHandler.sendEmptyMessage(CLOSE_BAIDU_LOCATING);
				}
			}
		});
		baiduLocationClient.startQueryUserCity();
	}

	private String getPreviousUserLocation() {
		return getSharedPreferences(Bus.BUS_PREF_NAME, MODE_PRIVATE).getString(
				Bus.EXTRA_USER_CURRENT_CITY, null);
	}

	/**
	 * 保存用户当前所在地
	 */
	private void saveUserCurrentLocation() {
		Log.d(TAG, "用户当前所在地： " + mUserCurrentCity);
		SharedPreferences sp = getSharedPreferences(Bus.BUS_PREF_NAME,
				MODE_PRIVATE);
		sp.edit().putString(Bus.EXTRA_USER_CURRENT_CITY, mUserCurrentCity)
				.commit();

		mTitle.setText(mUserCurrentCity);
	}

	private String mUserCurrentCity = null;
	/** 关闭百度定位 **/
	private static final int CLOSE_BAIDU_LOCATING = 0x0003;

	private Handler mBusHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CLOSE_BAIDU_LOCATING:
				// MyBaiduLocation.getInstance().close();

				if (TextUtils.isEmpty(mUserCurrentCity)) {
					mTitle.setText(TextUtils.isEmpty(getPreviousUserLocation()) ? "公交"
							: getPreviousUserLocation());
					Log.d(TAG, "获取用户当前所在第失败!");
				} else {
					mTitle.setText(mUserCurrentCity);
					saveUserCurrentLocation();
				}
				break;
			}
		}
	};
}
