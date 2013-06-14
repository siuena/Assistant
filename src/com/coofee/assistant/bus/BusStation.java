package com.coofee.assistant.bus;

public class BusStation {
	private String city;
	private String stationName;
	/** 经度, Longitude */
	private double longitude;
	/** 纬度, Latitude */
	private double latitude;
	private String[] lineNames;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public double getLongitude() {
		return longitude;
	}

	/**
	 * 经度
	 * 
	 * @param longitude
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	/**
	 * 纬度
	 * 
	 * @param latitude
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String[] getLineNames() {
		return lineNames;
	}

	public void setLineNames(String[] lineNames) {
		this.lineNames = lineNames;
	}
}
