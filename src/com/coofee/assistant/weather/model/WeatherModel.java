package com.coofee.assistant.weather.model;

/**
 * 中国天气网的天气信息
 * @author zhao
 */
public class WeatherModel {
	private WeatherInfo weatherinfo;
	
	public void setWeatherinfo(WeatherInfo weatherinfo) {
		this.weatherinfo = weatherinfo;
	}
	
	public WeatherInfo getWeatherinfo() {
		return this.weatherinfo;
	}
}
