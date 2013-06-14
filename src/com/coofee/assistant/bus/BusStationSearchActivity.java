package com.coofee.assistant.bus;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.coofee.assistant.R;
import com.coofee.assistant.provider.Assistant.BusHistory;
import com.coofee.assistant.provider.AssistantSQLiteHelper;
import com.coofee.assistant.provider.MyAbsSQLiteOpenHelper;
import com.coofee.assistant.util.UiUtil;
import com.coofee.assistant.util.bus.BusApi;
import com.coofee.assistant.util.bus.BusListener;

public class BusStationSearchActivity extends Activity {
	private static final String TAG = BusStationSearchActivity.class
			.getSimpleName();
	private TextView mTitle;
	private View mInputLayout;
	private EditText mInput;
	private ListView mResultListView;
	private BusStationResultAdapter mAdapter;

	private String mUserCurrentCity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bus_search_activity);

		initViews();
		getIntentData();
	}

	private void initViews() {
		mTitle = (TextView) findViewById(R.id.bus_search_title);
		((TextView) findViewById(R.id.bus_search_input_tip)).setText("请输入站点名称");
		mInput = (EditText) findViewById(R.id.bus_search_input);
		mInputLayout = findViewById(R.id.bus_search_input_layout);
		mResultListView = (ListView) findViewById(R.id.bus_search_resultList);

		mResultListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						BusStation busStation = (BusStation) mAdapter
								.getItem(position);
						Intent intent = new Intent(
								BusStationSearchActivity.this,
								BusStationDetailActivity.class);
						intent.putExtra(Bus.EXTRA_BUS_STATION_JSON,
								JSON.toJSONString(busStation));
						BusStationSearchActivity.this.startActivity(intent);
					}
				});

		findViewById(R.id.bus_search).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (TextUtils.isEmpty(mInput.getText())
								|| TextUtils.isEmpty(mInput.getText()
										.toString().trim())) {
							return;
						}

						String inputStationName = mInput.getText().toString()
								.trim();
						search(inputStationName);
					}
				});
	}

	private void getIntentData() {
		Intent data = getIntent();
		String stationName = null;
		if (data != null) {
			mUserCurrentCity = data.getStringExtra(Bus.EXTRA_USER_CURRENT_CITY);
			stationName = data.getStringExtra(Bus.EXTRA_BUS_STATION_NAME);
		}

		if (mUserCurrentCity == null) {
			BusStationSearchActivity.this.finish();
		} else {
			if (mUserCurrentCity != null && stationName == null) {
				// 说明是从BusActivity页面跳转过来的.
				mTitle.setText(mUserCurrentCity + "·站点查询");
			} else {
				// 从其他页面跳转过来的, 用于直接查询线路信息.
				mTitle.setText(stationName);
				mInputLayout.setVisibility(View.GONE);
				search(stationName);
			}
		}
	}

	/**
	 * 刷新ListView的数据
	 * 
	 * @param stationResult
	 */
	private void refreshData(BusStationResult stationResult) {
		if (mAdapter == null) {
			mAdapter = new BusStationResultAdapter(
					BusStationSearchActivity.this, stationResult);
			mResultListView.setAdapter(mAdapter);
		} else {
			mAdapter.setData(stationResult);
		}
	}

	/**
	 * 保存查询的结果.
	 * 
	 * @param stationResult
	 */
	private void saveData(BusStationResult stationResult) {
		Log.d(TAG, "save to db");
		AssistantSQLiteHelper.insertBusStationResult(
				BusStationSearchActivity.this, stationResult);
	}

	/**
	 * 从网络请求站点信息
	 * 
	 * @param stationName
	 */
	private void request(String stationName) {
		new BusApi().getStation(BusStationSearchActivity.this,
				mUserCurrentCity, stationName, new BusListener() {

					@Override
					public void onTransfer(BusTransferResult transferResult) {

					}

					@Override
					public void onStation(BusStationResult stationResult) {
						saveData(stationResult);
						refreshData(stationResult);
					}

					@Override
					public void onLine(BusLineResult lineResult) {

					}

					@Override
					public void onError(String errorMessage) {
						UiUtil.toastLong(BusStationSearchActivity.this,
								errorMessage);
					}
				});
	}

	private Cursor mCursor = null;

	private void search(String stationName) {
		try {
			mCursor = getContentResolver().query(
					BusHistory.BusSation.CONTENT_URI,
					null,
					BusHistory.BusSation.CITY + " = ? AND "
							+ BusHistory.BusSation.NAME + " = ?",
					new String[] { mUserCurrentCity, stationName }, null);

			if (mCursor != null && mCursor.getCount() > 0) {
				try {
					mCursor.moveToFirst();

					String stationResultJson = mCursor
							.getString(mCursor
									.getColumnIndex(BusHistory.BusSation.STATIONS_JSON));
					if (stationResultJson != null) {
						Log.d(TAG, "read from db");
						BusStationResult stationResult = JSON.parseObject(
								stationResultJson, BusStationResult.class);
						refreshData(stationResult);
					} else {
						request(stationName);
					}
				} catch (Exception e) {
					request(stationName);
				}
			} else {
				request(stationName);
			}
		} finally {
			MyAbsSQLiteOpenHelper.closeCursor(mCursor);
		}
	}
}
