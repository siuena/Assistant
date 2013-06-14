package com.coofee.assistant.bus;

import java.util.ArrayList;
import java.util.List;

public class BusProvince {

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public List<String> getCityList() {
		return cityList;
	}

	public void setCityList(List<String> cityList) {
		this.cityList = cityList;
	}
	
	public boolean addCity(String city) {
		synchronized (BusProvince.this) {
			if (cityList == null) {
				cityList = new ArrayList<String>();
			}
		}
		
		return cityList.add(city);
	}
	
	public String getCity(int position) {
		return cityList.get(position);
	}
	
	public int getCityCount() {
		return cityList.size();
	}
	
	private String province;
	private List<String> cityList;

}
