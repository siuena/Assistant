package com.coofee.assistant.bus;

public class BusLine {
	private String city;
	private String name;
	private String lineInfo;
	private String[] stationNames;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLineInfo() {
		return lineInfo;
	}

	public void setLineInfo(String lineInfo) {
		this.lineInfo = lineInfo;
	}

	public String[] getStationNames() {
		return stationNames;
	}

	public void setStationNames(String[] stationNames) {
		this.stationNames = stationNames;
	}
}
