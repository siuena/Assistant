package com.coofee.assistant.bus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.coofee.assistant.R;

public class BusLineResultAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private BusLineResult mBusLineResult;
	
	public BusLineResultAdapter(Context context, BusLineResult result) {
		mInflater = LayoutInflater.from(context);
		mBusLineResult = result;
	}
	
	public void setData(BusLineResult result) {
		if (result != null) {
			mBusLineResult = result;
			notifyDataSetChanged();
		}
	}
	
	@Override
	public int getCount() {
		return mBusLineResult.getLineList().size();
	}

	@Override
	public Object getItem(int position) {
		return mBusLineResult.getLineList().get(position);
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
			holder.lineName = (TextView) convertView.findViewById(R.id.analogous_weather_city_list_item_cityName);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.lineName.setText(mBusLineResult.getLineList().get(position).getName());
		return convertView;
	}

	private static class ViewHolder {
		TextView lineName;
	}
}
