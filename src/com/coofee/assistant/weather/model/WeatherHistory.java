package com.coofee.assistant.weather.model;

import java.util.Date;

public class WeatherHistory {
	private Long sqliteId;
	private String weatherAreaName;
	private Date date;
	private String dayStatus;
	private String nightStatus;
	private float dayTemperature;
	private float nightTemperature;

	public Long getSqliteId() {
		return sqliteId;
	}

	public void setSqliteId(Long sqliteId) {
		this.sqliteId = sqliteId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public float getDayTemperature() {
		return dayTemperature;
	}

	public void setDayTemperature(float dayTemperature) {
		this.dayTemperature = dayTemperature;
	}

	public float getNightTemperature() {
		return nightTemperature;
	}

	public void setNightTemperature(float nightTemperature) {
		this.nightTemperature = nightTemperature;
	}

	public String getWeatherAreaName() {
		return weatherAreaName;
	}

	public void setWeatherAreaName(String weatherAreaName) {
		this.weatherAreaName = weatherAreaName;
	}

	public String getDayStatus() {
		return dayStatus;
	}

	public void setDayStatus(String dayStatus) {
		this.dayStatus = dayStatus;
	}

	public String getNightStatus() {
		return nightStatus;
	}

	public void setNightStatus(String nightStatus) {
		this.nightStatus = nightStatus;
	}

}
