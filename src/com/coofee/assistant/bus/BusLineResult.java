package com.coofee.assistant.bus;

import java.util.ArrayList;
import java.util.List;

public class BusLineResult {
	private String city;
	private String lineName;
	private List<BusLine> lineList = new ArrayList<BusLine>();
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public List<BusLine> getLineList() {
		return lineList;
	}

	public void setLineList(List<BusLine> lineList) {
		this.lineList = lineList;
	}

}
