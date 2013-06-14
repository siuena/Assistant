package com.coofee.assistant.util.location;

/**
 * 一个地理位置的坐标 查询的地点对应的坐标, 默认返回百度坐标系, 如果不要求返回坐标 , 则需要设置coorType=0. 共支持一下几种坐标 : 1>
 * 返回国测局经纬度坐标系 coor=gcj02; 2> 返回百度墨卡托坐标系 coor=bd09; 3> 返回百度经纬度坐标系 coor=bd09ll.
 * 
 * @author zhao
 * 
 */
public class Coordinate {
	private String x;
	private String y;

	public Coordinate() {
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}
}
