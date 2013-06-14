package com.coofee.assistant.bus;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.coofee.assistant.AssistantApp;
import com.coofee.assistant.R;
import com.coofee.assistant.provider.AssistantSQLiteHelper;
import com.coofee.assistant.util.UiUtil;

public class BusHistoryActivity extends Activity {
	private static final String TAG = BusHistoryActivity.class.getSimpleName();
	private TextView mClearView;
	private View mNoDataView;
	private ListView mBusHistoryListView;
	private List<BusHistoryRecord> mBusHistoryRecords;
	private BusHistoryAdapter mAdapter;

	private int mTransferType = BusTransferResult.TransferType.TYPE_DEFAULT;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_history_area_list_activity);

		initViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshData();
	}
	
	private void initViews() {
		((TextView) findViewById(R.id.title)).setText("公交查询历史");
		mClearView = (TextView) findViewById(R.id.clear_data_action);
		mClearView.setVisibility(View.VISIBLE);
		mClearView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bus.openDeleteBusHistoryDialog(BusHistoryActivity.this,
						new Bus.OnDeleteBusHistoryListener() {
							@Override
							public void onDeleteCompleted() {
								BusHistoryActivity.this.refreshData();
							}

							@Override
							public void onDeleteCanceld() {
								BusHistoryActivity.this.refreshData();
							}
						});
			}
		});

		mNoDataView = findViewById(R.id.weather_history_no_data);
		mBusHistoryListView = (ListView) findViewById(R.id.weather_history_areaList);

		mBusHistoryListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						BusHistoryRecord record = mAdapter.getItem(position);
						BusHistoryRecord.startActivity(BusHistoryActivity.this,
								record, mTransferType);
					}
				});
		mBusHistoryListView
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						BusHistoryRecord record = mAdapter.getItem(position);
						if (record.getType() == BusHistoryRecord.Type.TYPE_TRANSFER) {
							Bus.openChooseTransferTypeDialog(
									BusHistoryActivity.this,
									new Bus.OnTransferTypeChangedListener() {
										@Override
										public void onTransferTypeChange(
												int transferType) {
											mTransferType = transferType;
										}
									});
							return true;
						} else {
							return false;
						}
					}
				});
	}

	private ProgressDialog mProgressDialog;

	private void refreshData() {
		Log.d(TAG, "refresh data.");
		mProgressDialog = new ProgressDialog(BusHistoryActivity.this);
		mProgressDialog.setTitle("数据加载中...");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(true);
		mProgressDialog
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						mHandler.sendEmptyMessage(MSG_CANCELED);
					}
				});
		mProgressDialog.show();

		AssistantApp.getInstance().mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Log.d(TAG, "try to get history from db.");
					mBusHistoryRecords = AssistantSQLiteHelper
							.getBusHisBusHistoryRecords(BusHistoryActivity.this);
					mHandler.sendEmptyMessage(MSG_COMPLETED);
				} catch (Exception e) {
					e.printStackTrace();
					mHandler.sendEmptyMessage(MSG_FAILED);
				}
			}
		});
	}

	private final int MSG_COMPLETED = 0x0001;
	private final int MSG_FAILED = 0x0002;
	private final int MSG_CANCELED = 0x0003;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
			
			switch (msg.what) {
			case MSG_COMPLETED:
				Log.d(TAG, "receive msg: " + MSG_COMPLETED + ", and bus history records is " + mBusHistoryRecords);
				if (mBusHistoryRecords == null) {
					UiUtil.toastLong(BusHistoryActivity.this, "加载数据失败!");
				} else if (mBusHistoryRecords.isEmpty()) {
					mBusHistoryListView.setVisibility(View.GONE);
					mNoDataView.setVisibility(View.VISIBLE);
				} else {
					mNoDataView.setVisibility(View.GONE);
					mBusHistoryListView.setVisibility(View.VISIBLE);
					if (mAdapter == null) {
						mAdapter = new BusHistoryAdapter(BusHistoryActivity.this,
								mBusHistoryRecords);
						mBusHistoryListView.setAdapter(mAdapter);
					} else {
						mAdapter.setData(mBusHistoryRecords);
					}
				}
				break;
			case MSG_FAILED:
				Log.d(TAG, "receive msg: " + MSG_FAILED);
				UiUtil.toastLong(BusHistoryActivity.this, "加载数据失败!");
				break;
			case MSG_CANCELED:
				BusHistoryActivity.this.finish();
				break;
			}
		}
	};

	private class BusHistoryAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private List<BusHistoryRecord> mBusHistoryRecordList;
		private float mTextSize = 14;

		public BusHistoryAdapter(Context context,
				List<BusHistoryRecord> hitoryRecordList) {
			mInflater = LayoutInflater.from(context);
			mBusHistoryRecordList = hitoryRecordList;

			mTextSize = context.getResources().getDimension(
					R.dimen.textsize_small);
		}

		public void setData(List<BusHistoryRecord> historys) {
			Log.d(TAG, "data changed.");
			mBusHistoryRecordList = historys;
			notifyDataSetChanged();
		}
		
		@Override
		public int getCount() {
			return mBusHistoryRecordList.size();
		}

		@Override
		public BusHistoryRecord getItem(int position) {
			return mBusHistoryRecordList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.weather_analogous_city_list_item, null);
				holder = new ViewHolder();
				holder.recordDesc = (TextView) convertView
						.findViewById(R.id.analogous_weather_city_list_item_cityName);
				holder.recordDesc.setTextSize(mTextSize);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			BusHistoryRecord record = mBusHistoryRecordList.get(position);
			holder.recordDesc.setText(record.toString());
			return convertView;
		}

	}

	private static class ViewHolder {
		TextView recordDesc;
	}
}
