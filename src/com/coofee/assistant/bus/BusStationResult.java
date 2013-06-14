package com.coofee.assistant.bus;

import java.util.ArrayList;
import java.util.List;

public class BusStationResult {
	private String city;
	private String stationName;
	private List<BusStation> stationList = new ArrayList<BusStation>();

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

	public List<BusStation> getStationList() {
		return stationList;
	}

	public void setStationList(List<BusStation> stationList) {
		this.stationList = stationList;
	}
}
