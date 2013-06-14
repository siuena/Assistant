package com.coofee.assistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.coofee.assistant.book.SearchBookByIsbnActivity;
import com.coofee.assistant.bus.BusActivity;
import com.coofee.assistant.weather.WeatherSearchActivity;
import com.coofee.assistant.word.WordActivity;

public class MainActivity extends Activity implements View.OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		setOnClickListeners();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_about:
			gotoAboutActivity();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_textview_word:
			gotoWordActivity();
			break;
		case R.id.main_textview_book:
			gotoSearchBookByIsbnActivity();
			break;
		case R.id.main_textview_weather:
			gotoWeatherActivity();
			break;
		case R.id.main_textview_bus:
			gotoBusActivity();
			break;
		}
	}

	/**
	 * 添加单击事件.
	 */
	private void setOnClickListeners() {
		findViewById(R.id.main_textview_word).setOnClickListener(this);
		findViewById(R.id.main_textview_bus).setOnClickListener(this);
		findViewById(R.id.main_textview_book).setOnClickListener(this);
		findViewById(R.id.main_textview_weather).setOnClickListener(this);
	}

	private void gotoBusActivity() {
		Intent intent = new Intent(this, BusActivity.class);
		startActivity(intent);
	}
	/**
	 * 跳转到号码归属地页面
	 */
	private void gotoWordActivity() {
		Intent intent = new Intent(this, WordActivity.class);
		startActivity(intent);
	}

	/**
	 * 跳转到图书搜索页面
	 */
	private void gotoSearchBookByIsbnActivity() {
		Intent intent = new Intent(this, SearchBookByIsbnActivity.class);
		startActivity(intent);
	}

	/**
	 * 跳转到天气页面
	 */
	private void gotoWeatherActivity() {
		Intent intent = new Intent(this, WeatherSearchActivity.class);
		startActivity(intent);
	}

	/**
	 * 跳转到关于页面
	 */
	private void gotoAboutActivity() {
		Intent intent = new Intent(this, AboutActivity.class);
		startActivity(intent);
	}

}
