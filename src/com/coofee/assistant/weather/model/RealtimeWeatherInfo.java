package com.coofee.assistant.weather.model;

public class RealtimeWeatherInfo {
	// 城市名
	private String city;
	// 城市id
	private String cityid;
	// 当前温度
	private String temp;
	// 当前风向
	private String WD;
	// 当前风速(风级)
	private String WS;
	// 当前湿度
	private String SD;
	private String WSE;
	// 天气情况的具体检测时间
	private String time;
	// 是否是雷达
	private String isRadar;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCityid() {
		return cityid;
	}

	public void setCityid(String cityid) {
		this.cityid = cityid;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getWD() {
		return WD;
	}

	public void setWD(String WD) {
		this.WD = WD;
	}

	public String getWS() {
		return WS;
	}

	public void setWS(String WS) {
		this.WS = WS;
	}

	public String getSD() {
		return SD;
	}

	public void setSD(String SD) {
		this.SD = SD;
	}

	public String getWSE() {
		return WSE;
	}

	public void setWSE(String WSE) {
		this.WSE = WSE;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getIsRadar() {
		return isRadar;
	}

	public void setIsRadar(String isRadar) {
		this.isRadar = isRadar;
	}
}
