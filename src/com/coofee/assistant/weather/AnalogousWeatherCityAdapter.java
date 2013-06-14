package com.coofee.assistant.weather;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.coofee.assistant.R;
import com.coofee.assistant.weather.model.WeatherCity;

/**
 * 根据关键字匹配到的相似城市适配器
 * @author zhao
 *
 */
public class AnalogousWeatherCityAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<WeatherCity> mWeatherCityList;
	
	public AnalogousWeatherCityAdapter(Context context, List<WeatherCity> weatherCityList) {
		mInflater = LayoutInflater.from(context);
		mWeatherCityList = weatherCityList;
	}
	
	public void setWeatherCityList(List<WeatherCity> weatherCityList) {
		this.mWeatherCityList = weatherCityList;
		notifyDataSetChanged();
	}
	
	public WeatherCity get(int position) {
		return mWeatherCityList.get(position);
	}
	
	@Override
	public int getCount() {
		return mWeatherCityList.size();
	}

	@Override
	public Object getItem(int position) {
		return mWeatherCityList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.weather_analogous_city_list_item, null);
			holder = new ViewHolder();
			holder.cityName = (TextView) convertView.findViewById(R.id.analogous_weather_city_list_item_cityName);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.cityName.setText(mWeatherCityList.get(position).getName());
		return convertView;
	}
	
	class ViewHolder {
		TextView cityName; 
	}

}
