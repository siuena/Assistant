package com.coofee.assistant.util.location;

/**
 * 天气查询中用来保存当前位置信息
 * @author zhao
 */
public class Address {
	private String country;
	private String province;
	private String city;
	/** 地区 **/
	private String district;
	/** 道路名 **/
	private String street;
	/** 门牌号码 **/
	private String street_number;

	/**
	 * 详见API文档.
	 * http://dev.baidu.com/wiki/geolocation/index.php?title=AndroidAPI%E6%8E%A5%E5%8F%A3%E6%96%87%E6%A1%A31.x
	 *   这样，返回地址类型的请求就有两种用法
  	（1）快速用法：
      	&addr=detail 想要获取完整地址（结果会返回中国北京北京海淀上地十街10号）
      	&addr=rough 想要获得到城市一个级别的地址（结果会返回中国北京北京）
 	（2）定制用法
    	它们用管道符号连接起来，如&addr=street|street_number，
    	这样就可以获取任意感兴趣的内容，而抛弃任何不感兴趣的内容（
    	例如我们确定一定在北京，则没有必要返回中国）。
	 */
	
	/** 要获取的位置的全部信息 **/
	private String detail;
	/** 获取的位置信息仅到城市(也就是精确到城市) **/
	private String rough;

	public Address() {
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreet_number() {
		return street_number;
	}

	public void setStreet_number(String street_number) {
		this.street_number = street_number;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getRough() {
		return rough;
	}

	public void setRough(String rough) {
		this.rough = rough;
	}
}
