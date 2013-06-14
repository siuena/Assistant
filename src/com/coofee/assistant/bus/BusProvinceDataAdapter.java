package com.coofee.assistant.bus;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.coofee.assistant.R;

public class BusProvinceDataAdapter extends BaseExpandableListAdapter {
	private LayoutInflater mInflater;
	private List<BusProvince> mProvinceList;
	
	public BusProvinceDataAdapter(Context context, List<BusProvince> provinceList) {
		mInflater = LayoutInflater.from(context);
		mProvinceList = provinceList;
	}
	
	@Override
	public String getChild(int groupPosition, int childPosition) {
		return mProvinceList.get(groupPosition).getCity(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mProvinceList.get(groupPosition).getCityCount();
	}

	@Override
	public BusProvince getGroup(int groupPosition) {
		return mProvinceList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mProvinceList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHolder holder = null;
		if (convertView == null) {
			convertView = generateView();
			holder = new ChildViewHolder();
			holder.childNameView = (TextView) convertView.findViewById(R.id.bus_switch_area_name);
			convertView.setTag(holder);
		} else {
			holder = (ChildViewHolder) convertView.getTag();
		}
		
		holder.childNameView.setText(mProvinceList.get(groupPosition).getCity(childPosition));
		return convertView;

	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupViewHolder holder = null;
		if (convertView == null) {
			convertView = generateView();
			holder = new GroupViewHolder();
			holder.groupNameView = (TextView) convertView.findViewById(R.id.bus_switch_area_name);
			convertView.setTag(holder);
		} else {
			holder = (GroupViewHolder) convertView.getTag();
		}
		
		holder.groupNameView.setText(mProvinceList.get(groupPosition).getProvince());
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	private View generateView() {
		View view = mInflater.inflate(R.layout.bus_province_name, null);
		return view;
	}
	
	private static class GroupViewHolder {
		TextView groupNameView;
	}
	
	private static class ChildViewHolder {
		TextView childNameView;
	}
}
