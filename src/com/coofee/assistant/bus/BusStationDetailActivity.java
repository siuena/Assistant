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

public class BusStationDetailActivity extends Activity {
	private BusStation mBusStation;
	private TextView mTitle;
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
		// 因为站点详细信息页面和线路详细信息页面共用了同一个页面, 而站点信息页面不需要该组件,所以隐藏之.
		findViewById(R.id.bus_line_detail_info).setVisibility(View.GONE);
		mStationListView = (ListView) findViewById(R.id.bus_line_detail_stations);
		mStationListView
		.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String lineName = mAdapter.getItem(position);
				Intent intent = new Intent(BusStationDetailActivity.this,
						BusLineSearchActivity.class);
				intent.putExtra(Bus.EXTRA_USER_CURRENT_CITY,
						mBusStation.getCity());
				intent.putExtra(Bus.EXTRA_BUS_LINE_NAME, lineName);
				BusStationDetailActivity.this.startActivity(intent);
			}
		});
	}

	private void getIntentData() {
		Intent data = getIntent();
		if (data != null) {
			String busStationJson = data
					.getStringExtra(Bus.EXTRA_BUS_STATION_JSON);

			if (TextUtils.isEmpty(busStationJson)) {
				BusStationDetailActivity.this.finish();
			} else {
				try {
					mBusStation = JSON.parseObject(busStationJson,
							BusStation.class);

					mTitle.setText(mBusStation.getCity() + "·"
							+ mBusStation.getStationName());

					mAdapter = new ArrayAdapter<String>(
							BusStationDetailActivity.this,
							R.layout.weather_analogous_city_list_item,
							mBusStation.getLineNames());
					mStationListView.setAdapter(mAdapter);
				} catch (Exception e) {
					e.printStackTrace();
					BusStationDetailActivity.this.finish();
				}
			}
		}
	}
}
