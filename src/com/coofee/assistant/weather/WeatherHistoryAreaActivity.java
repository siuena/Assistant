package com.coofee.assistant.weather;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.coofee.assistant.R;
import com.coofee.assistant.provider.Assistant;
import com.coofee.assistant.provider.AssistantSQLiteHelper;

public class WeatherHistoryAreaActivity extends Activity {
//	private static final String TAG = WeatherHistoryAreaActivity.class
//			.getSimpleName();

	private View mNoDataView;
	private ListView mAreaListView;
//	private WeatherHistoryAreaAdapter mAreaAdapter;
	private ArrayAdapter<String> mAreaAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_history_area_list_activity);

		initViews();
		initData();
	}

	private void initViews() {
		((TextView) findViewById(R.id.title)).setText(R.string.weather_history_title_label);
		mNoDataView = findViewById(R.id.weather_history_no_data);
		mAreaListView = (ListView) findViewById(R.id.weather_history_areaList);
	}

	private void initData() {
		SQLiteDatabase db = AssistantSQLiteHelper.getInstance()
				.getReadableDatabase();
		Cursor cursor = db.rawQuery("select distinct " + Assistant.Weather.History.AREA_NAME
				+ " from " + Assistant.Weather.History.TABLE_NAME + ";", null);
		
		if (cursor != null) {
			mAreaAdapter = new ArrayAdapter<String>(WeatherHistoryAreaActivity.this, R.layout.weather_analogous_city_list_item);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				mAreaAdapter.add(cursor.getString(0));
			}
		}


		// Cursor cursor = getContentResolver().query(
		// Assistant.Weather.History.CONTENT_URI, null, null, null, null);
//		mAreaAdapter = new WeatherHistoryAreaAdapter(this, cursor);

		if (mAreaAdapter.getCount() == 0) {
			mNoDataView.setVisibility(View.VISIBLE);
			mAreaListView.setVisibility(View.GONE);
		} else {
			mNoDataView.setVisibility(View.GONE);
			mAreaListView.setVisibility(View.VISIBLE);
			attachDataToViews();
		}
	}

	private void attachDataToViews() {
		mAreaListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
//						Cursor cursor = mAreaAdapter.getCursor();
//						String areaName = WeatherHistoryAreaAdapter
//								.readAreaNameFromCursor(cursor);

						String areaName = mAreaAdapter.getItem(position);
						Intent intent = new Intent(
								WeatherHistoryAreaActivity.this,
								HistoryDiagramActivity.class);
						intent.putExtra(
								HistoryDiagramActivity.EXTRA_HISTORY_AREA_NAME,
								areaName);
						WeatherHistoryAreaActivity.this.startActivity(intent);
					}
				});

		mAreaListView.setAdapter(mAreaAdapter);
	}

}
