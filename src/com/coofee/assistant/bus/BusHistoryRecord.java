package com.coofee.assistant.bus;

import android.content.Context;
import android.content.Intent;

/**
 * 公交查询历史记录实体
 * 
 * @author zhao
 * 
 */
public class BusHistoryRecord {
	/**
	 * type 是
	 */
	private int type = Type.TYPE_NO_SET;
	private String city;
	private String stationFrom;
	private String lineName;
	private String stationTo;

	/**
	 * 当记录类型是站点查询记录时使用
	 * 
	 * @param station
	 */
	public void setStation(String station) {
		stationFrom = station;
	}

	/**
	 * 当记录类型是站点查询记录时使用
	 * 
	 * @param station
	 */
	public String getStation() {
		return stationFrom;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStationFrom() {
		return stationFrom;
	}

	public void setStationFrom(String stationFrom) {
		this.stationFrom = stationFrom;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getStationTo() {
		return stationTo;
	}

	public void setStationTo(String stationTo) {
		this.stationTo = stationTo;
	}

	@Override
	public String toString() {
		switch (type) {
		case Type.TYPE_LINE:
			if (city != null && lineName != null) {
				return "[" + city + "·线路 ] " + lineName;
			}
			break;
		case Type.TYPE_STATION:
			if (city != null && getStation() != null) {
				return "[" + city + "·站点 ] " + getStation();
			}
			break;
		case Type.TYPE_TRANSFER:
			if (city != null && stationFrom != null && stationTo != null) {
				return "[" + city + "·换乘 ] " + stationFrom + "->" + stationTo;
			}
			break;
		}
		
		return "";
	}
	
	/**
	 * 
	 * @param context
	 * @param record
	 * @param transferType 仅当record的类型是 TYPE_TRANSFER时有效.
	 */
	public static void startActivity(Context context, BusHistoryRecord record, int transferType) {
		Intent intent = null;
		
		switch (record.getType()) {
		case Type.TYPE_LINE:
			intent = new Intent(context, BusLineSearchActivity.class);
			intent.putExtra(Bus.EXTRA_USER_CURRENT_CITY, record.getCity());
			intent.putExtra(Bus.EXTRA_BUS_LINE_NAME, record.getLineName());
			break;
		case Type.TYPE_STATION:
			intent = new Intent(context, BusStationSearchActivity.class);
			intent.putExtra(Bus.EXTRA_USER_CURRENT_CITY, record.getCity());
			intent.putExtra(Bus.EXTRA_BUS_STATION_NAME, record.getStation());
			break;
		case Type.TYPE_TRANSFER:
			intent = new Intent(context, BusTransferSearchActivity.class);
			intent.putExtra(Bus.EXTRA_USER_CURRENT_CITY, record.getCity());
			intent.putExtra(Bus.EXTRA_BUS_FROM, record.getStationFrom());
			intent.putExtra(Bus.EXTRA_BUS_TO, record.getStationTo());
			intent.putExtra(Bus.EXTRA_BUS_TRANSFER_TYPE, transferType);
			break;
		}
		
		if (intent != null) {
			context.startActivity(intent);
		}
	}

	/**
	 * 记录类型
	 * 
	 * @author zhao
	 * 
	 */
	public static final class Type {
		public static final int TYPE_NO_SET = 0;
		/** 线路查询记录 */
		public static final int TYPE_LINE = 1;
		/** 站点查询记录 */
		public static final int TYPE_STATION = 2;
		/** 换乘查询记录 */
		public static final int TYPE_TRANSFER = 3;
	}
}
