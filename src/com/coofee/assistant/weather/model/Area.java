package com.coofee.assistant.weather.model;

import com.alibaba.fastjson.JSON;

public class Area implements java.io.Serializable {
	private static final long serialVersionUID = 5270035588397952018L;
	/**
	 * 数据库Id.
	 */
	private long id;
	/**
	 * 地区编码
	 */
	private String code;
	/**
	 * 地区类型
	 */
	private AreaType type;
	/**
	 * 城市名称
	 */
	private String name;

	/**
	 * 所属省份名称
	 */
	private String province;

	/**
	 * 邮政编码
	 */
	private String zipcode;

	/**
	 * 电话区号
	 */
	private String phoneAreaCode;

	/**
	 * 地点名称的汉语拼音全拼.
	 */
	private String hypy;

	/**
	 * 地点名称的汉语拼音首字母.
	 */
	private String shortHypy;

	public Area() {
	}

	public Area(String code, String name, AreaType type) {
		this.code = code;
		this.name = name;
		this.type = type;
	}

	public Area(String code, String name, AreaType type, String zipcode,
			String phoneAreaCode) {
		this.code = code;
		this.name = name;
		this.type = type;
		this.zipcode = zipcode;
		this.phoneAreaCode = phoneAreaCode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getHypy() {
		return hypy;
	}

	public void setHypy(String hypy) {
		this.hypy = hypy;
	}

	public String getShortHypy() {
		return shortHypy;
	}

	public void setShortHypy(String shortHypy) {
		this.shortHypy = shortHypy;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getPhoneAreaCode() {
		return phoneAreaCode;
	}

	public void setPhoneAreaCode(String phoneAreaCode) {
		this.phoneAreaCode = phoneAreaCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public AreaType getType() {
		return type;
	}

	public void setType(AreaType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	/**
	 * 地区类型
	 * 
	 * @author zhao
	 * 
	 */
	public static enum AreaType {
		/**
		 * 国家
		 */
		NATION,
		/**
		 * 省
		 */
		PROVINCE,
		/**
		 * 市
		 */
		CITY,
		/**
		 * 县
		 */
		COUNTY
	}
}