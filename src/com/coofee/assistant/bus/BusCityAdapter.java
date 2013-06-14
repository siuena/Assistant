package com.coofee.assistant.bus;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.coofee.assistant.R;

public class BusCityAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<String> mCityList;
	
	public BusCityAdapter(Context context, List<String> cityList) {
		mInflater = LayoutInflater.from(context);
		mCityList = cityList;
	}
	
	public void replacePreviousData(List<String> cityNames) {
		mCityList = cityNames;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mCityList.size();
	}

	@Override
	public String getItem(int position) {
		return mCityList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.bus_province_name, null);
			holder = new ViewHolder();
			holder.cityName = (TextView) convertView.findViewById(R.id.bus_switch_area_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		String cityName = getItem(position);
		holder.cityName.setText(cityName);
		return convertView;
	}

	private static class ViewHolder {
		TextView cityName;
	}
}
