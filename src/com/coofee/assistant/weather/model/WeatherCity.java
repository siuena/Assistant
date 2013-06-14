package com.coofee.assistant.weather.model;

public class WeatherCity {
	// 城市的名称, 便于用户选择城市.
	private String name;
	// 城市的编号, 通过编号获取天气信息.
	private String id;

	public WeatherCity() {}
	
	public WeatherCity(String name, String id) {
		this.name = name;
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
