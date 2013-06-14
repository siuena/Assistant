package com.coofee.assistant.bus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.coofee.assistant.R;

public class BusLineDetailActivity extends Activity {
	private BusLine mBusLine;
	private TextView mTitle, mLineInfo;
	private ListView mStationListView;
	private ArrayAdapter<String> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bus_line_detail_activity);

		initViews();
		getIntentData();
	}

	private void initViews() {
		mTitle = (TextView) findViewById(R.id.bus_line_detail_title);
		mLineInfo = (TextView) findViewById(R.id.bus_line_detail_info);
		mStationListView = (ListView) findViewById(R.id.bus_line_detail_stations);
		mStationListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						String stationName = mAdapter.getItem(position);
						Intent intent = new Intent(BusLineDetailActivity.this,
								BusStationSearchActivity.class);
						intent.putExtra(Bus.EXTRA_USER_CURRENT_CITY,
								mBusLine.getCity());
						intent.putExtra(Bus.EXTRA_BUS_STATION_NAME, stationName);
						BusLineDetailActivity.this.startActivity(intent);
					}
				});
	}

	private void getIntentData() {
		Intent data = getIntent();
		if (data != null) {
			String busLineJson = data.getStringExtra(Bus.EXTRA_BUS_LINE_JSON);

			if (TextUtils.isEmpty(busLineJson)) {
				BusLineDetailActivity.this.finish();
			} else {
				try {
					mBusLine = JSON.parseObject(busLineJson, BusLine.class);

					mTitle.setText(mBusLine.getCity() + "·"
							+ mBusLine.getName());
					String lineInfo = mBusLine.getLineInfo();
					if (!TextUtils.isEmpty(lineInfo)) {
						lineInfo = lineInfo.replace("。", "\n");
						mLineInfo.setText(lineInfo);
					}

					mAdapter = new ArrayAdapter<String>(
							BusLineDetailActivity.this,
							R.layout.weather_analogous_city_list_item,
							mBusLine.getStationNames());
					mStationListView.setAdapter(mAdapter);
				} catch (Exception e) {
					e.printStackTrace();
					BusLineDetailActivity.this.finish();
				}
			}
		}
	}
}
