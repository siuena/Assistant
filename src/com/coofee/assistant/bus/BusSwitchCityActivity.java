package com.coofee.assistant.bus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.coofee.assistant.R;
import com.coofee.assistant.util.bus.BusApi;

public class BusSwitchCityActivity extends Activity {
	private static final String TAG = BusSwitchCityActivity.class
			.getSimpleName();
	private EditText mSearchInput;
	private ExpandableListView mProvinceDataView;
	private BusProvinceDataAdapter mProvinceDataAdapter;
	private ListView mSearchResultListView;
	private BusCityAdapter mSearchResultAdapter;

	private List<BusProvince> mProvinceDataList;
	private HashSet<String> mBusCitySet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bus_switch_city);

		initViews();
		initData();
	}

	private void initViews() {
		mSearchInput = (EditText) findViewById(R.id.bus_switch_city_searchInput);
		mProvinceDataView = (ExpandableListView) findViewById(R.id.bus_switch_city_provinceDataView);
		mProvinceDataView
				.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

					@Override
					public boolean onChildClick(ExpandableListView parent,
							View v, int groupPosition, int childPosition,
							long id) {
						String cityName = mProvinceDataAdapter.getChild(
								groupPosition, childPosition);
						BusSwitchCityActivity.this.finishBySetResult(cityName);
						return false;
					}
				});

		mSearchResultListView = (ListView) findViewById(R.id.bus_switch_city_searchResultListView);
		mSearchResultListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						String cityName = mSearchResultAdapter
								.getItem(position);
						BusSwitchCityActivity.this.finishBySetResult(cityName);
					}
				});

		addSearchListener();
	}

	private void initData() {
		mSearchResultListView.setVisibility(View.GONE);
		mProvinceDataView.setVisibility(View.VISIBLE);
		mProvinceDataList = BusApi
				.getBusProvinceList(BusSwitchCityActivity.this);
		mBusCitySet = new HashSet<String>();
		for (BusProvince province : mProvinceDataList) {
			for (String cityName : province.getCityList()) {
				mBusCitySet.add(cityName);
			}
		}
		Log.d(TAG, "bus city set: " + mBusCitySet);

		mProvinceDataAdapter = new BusProvinceDataAdapter(
				BusSwitchCityActivity.this, mProvinceDataList);
		mProvinceDataView.setAdapter(mProvinceDataAdapter);
	}

	private void addSearchListener() {
		mSearchInput.addTextChangedListener(new TextWatcher() {
			private List<String> mMatchedCityNames = new ArrayList<String>();

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(s)
						|| TextUtils.isEmpty(s.toString().trim())) {
					Log.d(TAG, "input string is: " + s);
					mSearchResultListView.setVisibility(View.GONE);
					mProvinceDataView.setVisibility(View.VISIBLE);
				} else {
					mSearchResultListView.setVisibility(View.VISIBLE);
					mProvinceDataView.setVisibility(View.GONE);
					mMatchedCityNames.clear();
					String input = s.toString().trim();
					for (String city : mBusCitySet) {
						if (city.contains(input)) {
							mMatchedCityNames.add(city);
						}
					}

					Log.d(TAG, "matched cities: " + mMatchedCityNames);
					if (mSearchResultAdapter == null) {
						mSearchResultAdapter = new BusCityAdapter(
								BusSwitchCityActivity.this, mMatchedCityNames);
						mSearchResultListView.setAdapter(mSearchResultAdapter);
					} else {
						mSearchResultAdapter.replacePreviousData(mMatchedCityNames);
					}
				}
			}
		});
	}

	private void finishBySetResult(String cityName) {
		Intent data = new Intent();
		data.putExtra(Bus.EXTRA_BUS_CITY_NAME, cityName);
		BusSwitchCityActivity.this.setResult(Activity.RESULT_OK, data);
		BusSwitchCityActivity.this.finish();
	}
}
