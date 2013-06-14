package com.coofee.assistant.util.location;

/**
 * 保存从baidu查询到的位置信息.
 * @author zhao
 *
 */
public class BaiduLocationData {
	private Coordinate point;
	/** 误差半径 **/
	private String radius;
	private Address addr;

	public BaiduLocationData() {
	}

	public Coordinate getPoint() {
		return point;
	}

	public void setPoint(Coordinate point) {
		this.point = point;
	}

	public String getRadius() {
		return radius;
	}

	public void setRadius(String radius) {
		this.radius = radius;
	}

	public Address getAddr() {
		return addr;
	}

	public void setAddr(Address addr) {
		this.addr = addr;
	}
}
