package com.coofee.assistant.bus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.coofee.assistant.R;

public class BusTransferResultAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private BusTransferResult mBusTransferResult;
	
	public BusTransferResultAdapter(Context context, BusTransferResult result) {
		mInflater = LayoutInflater.from(context);
		mBusTransferResult = result;
	}
	
	public void setData(BusTransferResult result) {
		if (result != null) {
			mBusTransferResult = result;
			notifyDataSetChanged();
		}
	}
	
	@Override
	public int getCount() {
		return mBusTransferResult.getTransferPlanList().size();
	}

	@Override
	public Object getItem(int position) {
		return mBusTransferResult.getTransferPlanList().get(position);
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
			holder.planDesc = (TextView) convertView.findViewById(R.id.analogous_weather_city_list_item_cityName);
			holder.planDesc.setTextSize(14);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.planDesc.setText(mBusTransferResult.getTransferPlanList().get(position).shortSolutionDesc());
		return convertView;
	}

	private static class ViewHolder {
		TextView planDesc;
	}
}

