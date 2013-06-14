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

public class BusLineSearchActivity extends Activity {
	private String TAG = BusLineSearchActivity.class.getSimpleName();

	private TextView mTitle;
	private View mInputLayout;
	private EditText mInput;
	private ListView mResultListView;
	private BusLineResultAdapter mAdapter;

	private String mUserCurrentCity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bus_search_activity);

		initViews();
		getIntentData();
	}

	private void getIntentData() {
		Intent data = getIntent();
		String lineName = null;
		if (data != null) {
			mUserCurrentCity = data.getStringExtra(Bus.EXTRA_USER_CURRENT_CITY);
			lineName = data.getStringExtra(Bus.EXTRA_BUS_LINE_NAME);
		}

		Log.d(TAG, "get: " + mUserCurrentCity);
		if (mUserCurrentCity == null) {
			BusLineSearchActivity.this.finish();
		} else {
			if (mUserCurrentCity != null && lineName == null) {
				// 说明是从BusActivity页面跳转过来的.
				mTitle.setText(mUserCurrentCity + "·线路查询");
			} else {
				// 从其他页面跳转过来的, 用于直接查询线路信息.
				mTitle.setText(lineName);
				mInputLayout.setVisibility(View.GONE);
				search(lineName);
			}
		}
	}

	private void initViews() {
		mTitle = (TextView) findViewById(R.id.bus_search_title);
		((TextView) findViewById(R.id.bus_search_input_tip)).setText("请输入线路名称");
		mInput = (EditText) findViewById(R.id.bus_search_input);
		mInputLayout = findViewById(R.id.bus_search_input_layout);
		mResultListView = (ListView) findViewById(R.id.bus_search_resultList);

		mResultListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						BusLine busLine = (BusLine) mAdapter.getItem(position);
						Intent intent = new Intent(BusLineSearchActivity.this,
								BusLineDetailActivity.class);
						intent.putExtra(Bus.EXTRA_BUS_LINE_JSON,
								JSON.toJSONString(busLine));
						BusLineSearchActivity.this.startActivity(intent);
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

						String inputLineName = mInput.getText().toString()
								.trim();
						search(inputLineName);
					}
				});
	}

	/**
	 * 刷新ListView的数据
	 * 
	 * @param stationResult
	 */
	private void refreshData(BusLineResult lineResult) {
		if (mAdapter == null) {
			mAdapter = new BusLineResultAdapter(BusLineSearchActivity.this,
					lineResult);
			mResultListView.setAdapter(mAdapter);
		} else {
			mAdapter.setData(lineResult);
		}
	}

	/**
	 * 保存查询的结果.
	 * 
	 * @param stationResult
	 */
	private void saveData(BusLineResult lineResult) {
		Log.d(TAG, "save to db");
		AssistantSQLiteHelper.insertBusLineResult(BusLineSearchActivity.this,
				lineResult);
	}

	/**
	 * 从网络请求线路信息
	 * 
	 * @param lineName
	 */
	private void request(String lineName) {
		new BusApi().getLine(BusLineSearchActivity.this, mUserCurrentCity,
				lineName, new BusListener() {
					@Override
					public void onTransfer(BusTransferResult transferResult) {
					}

					@Override
					public void onStation(BusStationResult stationResult) {
					}

					@Override
					public void onLine(BusLineResult lineResult) {
						saveData(lineResult);
						refreshData(lineResult);
					}

					@Override
					public void onError(String errorMessage) {
						UiUtil.toastLong(BusLineSearchActivity.this,
								errorMessage);
					}
				});
	}

	private Cursor mCursor = null;

	private void search(String lineName) {
		try {
			mCursor = getContentResolver().query(
					BusHistory.BusLine.CONTENT_URI,
					null,
					BusHistory.BusLine.CITY + " = ? AND "
							+ BusHistory.BusLine.NAME + " = ?",
					new String[] { mUserCurrentCity, lineName }, null);

			if (mCursor != null && mCursor.getCount() > 0) {
				try {
					mCursor.moveToFirst();

					String lineResultJson = mCursor.getString(mCursor
							.getColumnIndex(BusHistory.BusLine.LINES_JSON));
					if (lineName != null) {
						Log.d(TAG, "read from db");
						BusLineResult lineResult = JSON.parseObject(
								lineResultJson, BusLineResult.class);
						refreshData(lineResult);
					} else {
						request(lineName);
					}
				} catch (Exception e) {
					request(lineName);
				}
			} else {
				request(lineName);
			}
		} finally {
			MyAbsSQLiteOpenHelper.closeCursor(mCursor);
		}
	}
}
