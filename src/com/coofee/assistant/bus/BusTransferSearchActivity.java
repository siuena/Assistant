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
import com.coofee.assistant.bus.BusTransferResult.TransferType;
import com.coofee.assistant.provider.Assistant.BusHistory;
import com.coofee.assistant.provider.AssistantSQLiteHelper;
import com.coofee.assistant.provider.MyAbsSQLiteOpenHelper;
import com.coofee.assistant.util.UiUtil;
import com.coofee.assistant.util.bus.BusApi;
import com.coofee.assistant.util.bus.BusListener;

public class BusTransferSearchActivity extends Activity {
	private static final String TAG = BusTransferSearchActivity.class
			.getSimpleName();

	private TextView mTitle, mTransferTypeAction;
	private EditText mFrom, mTo;
	private View mInputLayout;
	private ListView mResultListView;
	private BusTransferResultAdapter mAdapter;

	private String mUserCurrentCity;
	/** 换乘查询的类型. */
	private int mTransferType = BusTransferResult.TransferType.TYPE_DEFAULT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bus_transfer_search_activity);

		initViews();
		getIntentData();
	}

	private void initViews() {
		mTitle = (TextView) findViewById(R.id.bus_search_title);
		mInputLayout = findViewById(R.id.bus_search_input_layout);
		mTransferTypeAction = (TextView) findViewById(R.id.bus_transfer_type_action);
		mTransferTypeAction.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bus.openChooseTransferTypeDialog(
						BusTransferSearchActivity.this,
						new Bus.OnTransferTypeChangedListener() {
							@Override
							public void onTransferTypeChange(int transferType) {
								mTransferType = transferType;
							}
						});
			}
		});

		mFrom = (EditText) findViewById(R.id.bus_transfer_search_from);
		mTo = (EditText) findViewById(R.id.bus_transfer_search_to);

		findViewById(R.id.bus_search).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (TextUtils.isEmpty(mFrom.getText())
								|| TextUtils.isEmpty(mTo.getText())) {
							return;
						}

						String from = mFrom.getText().toString().trim();
						String to = mTo.getText().toString().trim();
						search(from, to);
					}
				});

		mResultListView = (ListView) findViewById(R.id.bus_search_resultList);
		mResultListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						BusTransferPlan plan = (BusTransferPlan) mAdapter
								.getItem(position);
						Intent intent = new Intent(
								BusTransferSearchActivity.this,
								BusTransferDetailActivity.class);
						intent.putExtra(Bus.EXTRA_BUS_TRANSFER_JSON,
								JSON.toJSONString(plan));
						// Log.d(TAG, "put: " + JSON.toJSONString(plan))
						BusTransferSearchActivity.this.startActivity(intent);
					}
				});
	}

	private void getIntentData() {
		Intent data = getIntent();
		String from = null, to = null;
		if (data != null) {
			mUserCurrentCity = data.getStringExtra(Bus.EXTRA_USER_CURRENT_CITY);
			mTransferType = data.getIntExtra(Bus.EXTRA_BUS_TRANSFER_TYPE,
					BusTransferResult.TransferType.TYPE_DEFAULT);

			from = data.getStringExtra(Bus.EXTRA_BUS_FROM);
			to = data.getStringExtra(Bus.EXTRA_BUS_TO);
		}

		if (mUserCurrentCity == null) {
			BusTransferSearchActivity.this.finish();
		} else {
			if (mUserCurrentCity != null && (from == null || to == null)) {
				// 说明是从BusActivity页面跳转过来的.
				mTitle.setText(mUserCurrentCity + "·换乘查询");
			} else if (mUserCurrentCity != null && from != null && to != null) {
				// 从其他页面跳转过来的, 用于直接查询线路信息.
				mInputLayout.setVisibility(View.GONE);
				search(from, to);
			} else {
				BusTransferSearchActivity.this.finish();
			}

			mTitle.setText(mUserCurrentCity + "·换乘查询");
		}
	}

	private void refreshData(BusTransferResult transferResult) {
		if (mAdapter == null) {
			mAdapter = new BusTransferResultAdapter(
					BusTransferSearchActivity.this, transferResult);
			mResultListView.setAdapter(mAdapter);
		} else {
			mAdapter.setData(transferResult);
		}
	}

	private void request(final String from, final String to) {
		new BusApi().getTransfer(BusTransferSearchActivity.this,
				mUserCurrentCity, from, to, mTransferType, new BusListener() {

					@Override
					public void onTransfer(BusTransferResult transferResult) {
						saveData(transferResult);
						refreshData(transferResult);
					}

					@Override
					public void onStation(BusStationResult stationResult) {

					}

					@Override
					public void onLine(BusLineResult lineResult) {

					}

					@Override
					public void onError(String errorMessage) {
						UiUtil.toastLong(BusTransferSearchActivity.this,
								errorMessage);
					}
				});
	}

	private void saveData(BusTransferResult transferResult) {
		Log.d(TAG, "save to db");
		AssistantSQLiteHelper.saveBusTranferResult(
				BusTransferSearchActivity.this, transferResult);
	}

	private Cursor mCursor = null;

	private void search(final String from, final String to) {
		try {
			mCursor = getContentResolver().query(
					BusHistory.BusTransfer.CONTENT_URI,
					null,
					BusHistory.BusTransfer.CITY + " = ? AND "
							+ BusHistory.BusTransfer.START_STATION
							+ " = ? AND " + BusHistory.BusTransfer.END_STATION
							+ " = ? ",
					new String[] { mUserCurrentCity, from, to }, null);

			if (mCursor != null && mCursor.getCount() > 0) {
				try {

					mCursor.moveToFirst();

					String transferResultJson = null;
					switch (mTransferType) {
					case TransferType.TYPE_DEFAULT:
						transferResultJson = mCursor
								.getString(mCursor
										.getColumnIndex(BusHistory.BusTransfer.DEFAULT_PLAN_JSON));
						break;
					case TransferType.TYPE_DISTANCE_SHORTEST:
						transferResultJson = mCursor
								.getString(mCursor
										.getColumnIndex(BusHistory.BusTransfer.TOTAL_DISTANCE_SHORTEST_JSON));
						break;
					case TransferType.TYPE_LEAST_TRANSFER_TIMES:
						transferResultJson = mCursor
								.getString(mCursor
										.getColumnIndex(BusHistory.BusTransfer.LEAST_TRANSFER_TIMES_JSON));
						break;
					case TransferType.TYPE_SUBWAY_PRIORITY:
						transferResultJson = mCursor
								.getString(mCursor
										.getColumnIndex(BusHistory.BusTransfer.SUBWAY_PRIORITY_JSON));
						break;
					case TransferType.TYPE_TIME_SHORTEST:
						transferResultJson = mCursor
								.getString(mCursor
										.getColumnIndex(BusHistory.BusTransfer.TIME_SHORTEST_JSON));
						break;
					case TransferType.TYPE_WALK_DISTANCE_SHORTEST:
						transferResultJson = mCursor
								.getString(mCursor
										.getColumnIndex(BusHistory.BusTransfer.WALK_SHORTEST_DISTANCE_JSON));
						break;
					}

					if (transferResultJson != null) {
						Log.d(TAG, "read from db");
						BusTransferResult transferResult = JSON.parseObject(
								transferResultJson, BusTransferResult.class);
						refreshData(transferResult);
					} else {
						request(from, to);
					}
				} catch (Exception e) {
					e.printStackTrace();
					request(from, to);
				}
			} else {
				request(from, to);
			}
		} finally {
			MyAbsSQLiteOpenHelper.closeCursor(mCursor);
		}
	}
}
