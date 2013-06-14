package com.coofee.assistant.bus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.coofee.assistant.R;

public class BusStationResultAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private BusStationResult mBusStationResult;
	
	public BusStationResultAdapter(Context context, BusStationResult result) {
		mInflater = LayoutInflater.from(context);
		mBusStationResult = result;
	}
	
	public void setData(BusStationResult result) {
		if (result != null) {
			mBusStationResult = result;
			notifyDataSetChanged();
		}
	}
	
	@Override
	public int getCount() {
		return mBusStationResult.getStationList().size();
	}

	@Override
	public Object getItem(int position) {
		return mBusStationResult.getStationList().get(position);
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
			holder.stationName = (TextView) convertView.findViewById(R.id.analogous_weather_city_list_item_cityName);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.stationName.setText(mBusStationResult.getStationList().get(position).getStationName());
		return convertView;
	}

	private static class ViewHolder {
		TextView stationName;
	}
}
