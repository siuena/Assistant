package com.coofee.assistant.weather.model;

import android.text.TextUtils;
import android.util.Log;

import com.coofee.assistant.R;
import com.coofee.assistant.weather.WeatherHelper;

/**
 * 中国天气网的天气信息数据模型
 * 
 * @author zhao
 * 
 */
public class WeatherInfo {
	// 城市名称
	private String city;
	// 城市英语名称或拼音
	private String city_en;
	// 当天日期
	private String date_y;
	//
	private String date;

	// 当天是星期几
	private String week;
	//
	private String fchh;
	// 城市id
	private String cityid;

	// 以下是温度信息
	// 当天的温度范围(以摄氏为单位)
	private String temp1;
	// 明天的温度范围(以摄氏为单位)
	private String temp2;
	// 后天的温度范围(以摄氏为单位)
	private String temp3;
	// 后天的温度范围(以摄氏为单位)
	private String temp4;
	// 第五天的温度范围(以摄氏为单位)
	private String temp5;
	// 第五天的温度范围(以摄氏为单位)
	private String temp6;

	// 当天的温度范围(以华氏为单位)
	private String tempF1;
	private String tempF2;
	private String tempF3;
	private String tempF4;
	private String tempF5;
	private String tempF6;

	// 天气情况描述
	private String weather1;
	private String weather2;
	private String weather3;
	private String weather4;
	private String weather5;
	private String weather6;

	// 天气描述图片序号(白天/晚上)
	private String img1;
	private String img2;
	private String img3;
	private String img4;
	private String img5;
	private String img6;
	private String img7;
	private String img8;
	private String img9;
	private String img10;
	private String img11;
	private String img12;
	private String img_single;

	// 天气图片名称
	private String img_title1;
	private String img_title2;
	private String img_title3;
	private String img_title4;
	private String img_title5;
	private String img_title6;
	private String img_title7;
	private String img_title8;
	private String img_title9;
	private String img_title10;
	private String img_title11;
	private String img_title12;
	private String img_title_single;

	// 风速描述
	private String wind1;
	private String wind2;
	private String wind3;
	private String wind4;
	private String wind5;
	private String wind6;

	// 风速级别描述
	private String fx1;
	private String fx2;
	private String fl1;
	private String fl2;
	private String fl3;
	private String fl4;
	private String fl5;
	private String fl6;

	// 今天穿衣指数
	private String index;
	// 今天穿衣提醒
	private String index_d;
	// 48小时穿衣指数
	private String index48;
	// 48小时穿衣提醒
	private String index48_d;
	// 紫外线强弱
	private String index_uv;
	// 48小时紫外线强弱
	private String index48_uv;
	// 洗车
	private String index_xc;
	// 旅游
	private String index_tr;
	// 舒适指数
	private String index_co;

	//
	private String st1;
	private String st2;
	private String st3;
	private String st4;
	private String st5;
	private String st6;

	// 晨练
	private String index_cl;
	// 晾晒
	private String index_ls;
	// 过敏
	private String index_ag;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCity_en() {
		return city_en;
	}

	public void setCity_en(String city_en) {
		this.city_en = city_en;
	}

	public String getDate_y() {
		return date_y;
	}

	public void setDate_y(String date_y) {
		this.date_y = date_y;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getFchh() {
		return fchh;
	}

	public void setFchh(String fchh) {
		this.fchh = fchh;
	}

	public String getCityid() {
		return cityid;
	}

	public void setCityid(String cityid) {
		this.cityid = cityid;
	}

	public String getTemp1() {
		return temp1;
	}

	public void setTemp1(String temp1) {
		this.temp1 = temp1;
	}

	public String getTemp2() {
		return temp2;
	}

	public void setTemp2(String temp2) {
		this.temp2 = temp2;
	}

	public String getTemp3() {
		return temp3;
	}

	public void setTemp3(String temp3) {
		this.temp3 = temp3;
	}

	public String getTemp4() {
		return temp4;
	}

	public void setTemp4(String temp4) {
		this.temp4 = temp4;
	}

	public String getTemp5() {
		return temp5;
	}

	public void setTemp5(String temp5) {
		this.temp5 = temp5;
	}

	public String getTemp6() {
		return temp6;
	}

	public void setTemp6(String temp6) {
		this.temp6 = temp6;
	}

	public String getTempF1() {
		return tempF1;
	}

	public void setTempF1(String tempF1) {
		this.tempF1 = tempF1;
	}

	public String getTempF2() {
		return tempF2;
	}

	public void setTempF2(String tempF2) {
		this.tempF2 = tempF2;
	}

	public String getTempF3() {
		return tempF3;
	}

	public void setTempF3(String tempF3) {
		this.tempF3 = tempF3;
	}

	public String getTempF4() {
		return tempF4;
	}

	public void setTempF4(String tempF4) {
		this.tempF4 = tempF4;
	}

	public String getTempF5() {
		return tempF5;
	}

	public void setTempF5(String tempF5) {
		this.tempF5 = tempF5;
	}

	public String getTempF6() {
		return tempF6;
	}

	public void setTempF6(String tempF6) {
		this.tempF6 = tempF6;
	}

	public String getWeather1() {
		return weather1;
	}

	public void setWeather1(String weather1) {
		this.weather1 = weather1;
	}

	public String getWeather2() {
		return weather2;
	}

	public void setWeather2(String weather2) {
		this.weather2 = weather2;
	}

	public String getWeather3() {
		return weather3;
	}

	public void setWeather3(String weather3) {
		this.weather3 = weather3;
	}

	public String getWeather4() {
		return weather4;
	}

	public void setWeather4(String weather4) {
		this.weather4 = weather4;
	}

	public String getWeather5() {
		return weather5;
	}

	public void setWeather5(String weather5) {
		this.weather5 = weather5;
	}

	public String getWeather6() {
		return weather6;
	}

	public void setWeather6(String weather6) {
		this.weather6 = weather6;
	}

	public String getImg1() {
		return img1;
	}

	public void setImg1(String img1) {
		this.img1 = img1;
	}

	public String getImg2() {
		return img2;
	}

	public void setImg2(String img2) {
		this.img2 = img2;
	}

	public String getImg3() {
		return img3;
	}

	public void setImg3(String img3) {
		this.img3 = img3;
	}

	public String getImg4() {
		return img4;
	}

	public void setImg4(String img4) {
		this.img4 = img4;
	}

	public String getImg5() {
		return img5;
	}

	public void setImg5(String img5) {
		this.img5 = img5;
	}

	public String getImg6() {
		return img6;
	}

	public void setImg6(String img6) {
		this.img6 = img6;
	}

	public String getImg7() {
		return img7;
	}

	public void setImg7(String img7) {
		this.img7 = img7;
	}

	public String getImg8() {
		return img8;
	}

	public void setImg8(String img8) {
		this.img8 = img8;
	}

	public String getImg9() {
		return img9;
	}

	public void setImg9(String img9) {
		this.img9 = img9;
	}

	public String getImg10() {
		return img10;
	}

	public void setImg10(String img10) {
		this.img10 = img10;
	}

	public String getImg11() {
		return img11;
	}

	public void setImg11(String img11) {
		this.img11 = img11;
	}

	public String getImg12() {
		return img12;
	}

	public void setImg12(String img12) {
		this.img12 = img12;
	}

	public String getImg_single() {
		return img_single;
	}

	public void setImg_single(String img_single) {
		this.img_single = img_single;
	}

	public String getImg_title1() {
		return img_title1;
	}

	public void setImg_title1(String img_title1) {
		this.img_title1 = img_title1;
	}

	public String getImg_title2() {
		return img_title2;
	}

	public void setImg_title2(String img_title2) {
		this.img_title2 = img_title2;
	}

	public String getImg_title3() {
		return img_title3;
	}

	public void setImg_title3(String img_title3) {
		this.img_title3 = img_title3;
	}

	public String getImg_title4() {
		return img_title4;
	}

	public void setImg_title4(String img_title4) {
		this.img_title4 = img_title4;
	}

	public String getImg_title5() {
		return img_title5;
	}

	public void setImg_title5(String img_title5) {
		this.img_title5 = img_title5;
	}

	public String getImg_title6() {
		return img_title6;
	}

	public void setImg_title6(String img_title6) {
		this.img_title6 = img_title6;
	}

	public String getImg_title7() {
		return img_title7;
	}

	public void setImg_title7(String img_title7) {
		this.img_title7 = img_title7;
	}

	public String getImg_title8() {
		return img_title8;
	}

	public void setImg_title8(String img_title8) {
		this.img_title8 = img_title8;
	}

	public String getImg_title9() {
		return img_title9;
	}

	public void setImg_title9(String img_title9) {
		this.img_title9 = img_title9;
	}

	public String getImg_title10() {
		return img_title10;
	}

	public void setImg_title10(String img_title10) {
		this.img_title10 = img_title10;
	}

	public String getImg_title11() {
		return img_title11;
	}

	public void setImg_title11(String img_title11) {
		this.img_title11 = img_title11;
	}

	public String getImg_title12() {
		return img_title12;
	}

	public void setImg_title12(String img_title12) {
		this.img_title12 = img_title12;
	}

	public String getImg_title_single() {
		return img_title_single;
	}

	public void setImg_title_single(String img_title_single) {
		this.img_title_single = img_title_single;
	}

	public String getWind1() {
		return wind1;
	}

	public void setWind1(String wind1) {
		this.wind1 = wind1;
	}

	public String getWind2() {
		return wind2;
	}

	public void setWind2(String wind2) {
		this.wind2 = wind2;
	}

	public String getWind3() {
		return wind3;
	}

	public void setWind3(String wind3) {
		this.wind3 = wind3;
	}

	public String getWind4() {
		return wind4;
	}

	public void setWind4(String wind4) {
		this.wind4 = wind4;
	}

	public String getWind5() {
		return wind5;
	}

	public void setWind5(String wind5) {
		this.wind5 = wind5;
	}

	public String getWind6() {
		return wind6;
	}

	public void setWind6(String wind6) {
		this.wind6 = wind6;
	}

	public String getFx1() {
		return fx1;
	}

	public void setFx1(String fx1) {
		this.fx1 = fx1;
	}

	public String getFx2() {
		return fx2;
	}

	public void setFx2(String fx2) {
		this.fx2 = fx2;
	}

	public String getFl1() {
		return fl1;
	}

	public void setFl1(String fl1) {
		this.fl1 = fl1;
	}

	public String getFl2() {
		return fl2;
	}

	public void setFl2(String fl2) {
		this.fl2 = fl2;
	}

	public String getFl3() {
		return fl3;
	}

	public void setFl3(String fl3) {
		this.fl3 = fl3;
	}

	public String getFl4() {
		return fl4;
	}

	public void setFl4(String fl4) {
		this.fl4 = fl4;
	}

	public String getFl5() {
		return fl5;
	}

	public void setFl5(String fl5) {
		this.fl5 = fl5;
	}

	public String getFl6() {
		return fl6;
	}

	public void setFl6(String fl6) {
		this.fl6 = fl6;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getIndex_d() {
		return index_d;
	}

	public void setIndex_d(String index_d) {
		this.index_d = index_d;
	}

	public String getIndex48() {
		return index48;
	}

	public void setIndex48(String index48) {
		this.index48 = index48;
	}

	public String getIndex48_d() {
		return index48_d;
	}

	public void setIndex48_d(String index48_d) {
		this.index48_d = index48_d;
	}

	public String getIndex_uv() {
		return index_uv;
	}

	public void setIndex_uv(String index_uv) {
		this.index_uv = index_uv;
	}

	public String getIndex48_uv() {
		return index48_uv;
	}

	public void setIndex48_uv(String index48_uv) {
		this.index48_uv = index48_uv;
	}

	public String getIndex_xc() {
		return index_xc;
	}

	public void setIndex_xc(String index_xc) {
		this.index_xc = index_xc;
	}

	public String getIndex_tr() {
		return index_tr;
	}

	public void setIndex_tr(String index_tr) {
		this.index_tr = index_tr;
	}

	public String getIndex_co() {
		return index_co;
	}

	public void setIndex_co(String index_co) {
		this.index_co = index_co;
	}

	public String getSt1() {
		return st1;
	}

	public void setSt1(String st1) {
		this.st1 = st1;
	}

	public String getSt2() {
		return st2;
	}

	public void setSt2(String st2) {
		this.st2 = st2;
	}

	public String getSt3() {
		return st3;
	}

	public void setSt3(String st3) {
		this.st3 = st3;
	}

	public String getSt4() {
		return st4;
	}

	public void setSt4(String st4) {
		this.st4 = st4;
	}

	public String getSt5() {
		return st5;
	}

	public void setSt5(String st5) {
		this.st5 = st5;
	}

	public String getSt6() {
		return st6;
	}

	public void setSt6(String st6) {
		this.st6 = st6;
	}

	public String getIndex_cl() {
		return index_cl;
	}

	public void setIndex_cl(String index_cl) {
		this.index_cl = index_cl;
	}

	public String getIndex_ls() {
		return index_ls;
	}

	public void setIndex_ls(String index_ls) {
		this.index_ls = index_ls;
	}

	public String getIndex_ag() {
		return index_ag;
	}

	public void setIndex_ag(String index_ag) {
		this.index_ag = index_ag;
	}

	/**
	 * 获取白天的天气状况
	 * @param index从0到5,分别表示今天,明天...
	 */
	public String getDayStatus(int index) {
		switch (index) {
		case 0:
			return img_title1;
		case 1:
			return img_title3;
		case 2:
			return img_title5;
		case 3:
			return img_title7;
		case 4:
			return img_title9;
		case 5:
			return img_title11;
		default:
			return "";
		}
	}
	
	public float getDayTemp(int index) {
		switch (index) {
		case 0:
			return parseTempFromString(temp1, "~", true);
		case 1:
			return parseTempFromString(temp2, "~", true);
		case 2:
			return parseTempFromString(temp3, "~", true);
		case 3:
			return parseTempFromString(temp4, "~", true);
		case 4:
			return parseTempFromString(temp5, "~", true);
		case 5:
			return parseTempFromString(temp6, "~", true);
		default:
			return 0;
		}
	}
	
	public float getNightTemp(int index) {
		switch (index) {
		case 0:
			return parseTempFromString(temp1, "~", false);
		case 1:
			return parseTempFromString(temp2, "~", false);
		case 2:
			return parseTempFromString(temp3, "~", false);
		case 3:
			return parseTempFromString(temp4, "~", false);
		case 4:
			return parseTempFromString(temp5, "~", false);
		case 5:
			return parseTempFromString(temp6, "~", false);
		default:
			return 0;
		}
	}
	
	/**
	 * 获取白天的温度
	 * @param index从0到5,分别表示今天,明天...
	 */
	public String getDayTemperature(int index) {
		switch (index) {
		case 0:
			return substring(temp1, "~", true);
		case 1:
			return substring(temp2, "~", true);
		case 2:
			return substring(temp3, "~", true);
		case 3:
			return substring(temp4, "~", true);
		case 4:
			return substring(temp5, "~", true);
		case 5:
			return substring(temp6, "~", true);
		default:
			return "";
		}
	}

	/**
	 * 获取夜间的温度
	 * @param index从0到5,分别表示今天,明天...
	 */
	public String getNightTemperature(int index) {
		switch (index) {
		case 0:
			return substring(temp1, "~", false);
		case 1:
			return substring(temp2, "~", false);
		case 2:
			return substring(temp3, "~", false);
		case 3:
			return substring(temp4, "~", false);
		case 4:
			return substring(temp5, "~", false);
		case 5:
			return substring(temp6, "~", false);
		default:
			return "";
		}
	}
	
	private int parseTempFromString(String orgin, String splitString, boolean high) {
		if (TextUtils.isEmpty(orgin) || TextUtils.isDigitsOnly(splitString)) return 0;
		
		String[] temperatures = orgin.split(splitString);
		Log.d("WeatherInfo", temperatures[0] + "; " + temperatures[1]);
		
		int temp0 = Integer.parseInt(temperatures[0].substring(0, temperatures[0].length() - 1));
		int temp1 = Integer.parseInt(temperatures[1].substring(0, temperatures[1].length() - 1));
		
		if (high) 
			return Math.max(temp0, temp1);
		else
			return Math.min(temp0, temp1);
	}
	
	/**
	 * @param orgin 原始字符串
	 * @param splitString 分割字符串
	 * @param high为true则获取当天的最高温度，为false则获取当天的最低温度。
	 */
	private String substring(String orgin, String splitString, boolean high) {
		if (TextUtils.isEmpty(orgin) || TextUtils.isDigitsOnly(splitString)) return "";
		
		String[] temperatures = orgin.split(splitString);
		Log.d("WeatherInfo", temperatures[0] + "; " + temperatures[1]);
		
		int temp0 = Integer.parseInt(temperatures[0].substring(0, temperatures[0].length() - 1));
		int temp1 = Integer.parseInt(temperatures[1].substring(0, temperatures[1].length() - 1));
		if (high) 
			return temp0 > temp1 ? temperatures[0] : temperatures[1];
		else
			return temp0 < temp1 ? temperatures[0] : temperatures[1];
	}
	
	// 获取的风向中只有6个, 以转字为分割, 前面的是白天的风向, 后面的是夜间的风向.
	public String getWind(int index) {
		switch (index) {
		case 0:
			return wind1;
		case 1:
			return wind2;
		case 2:
			return wind3;
		case 3:
			return wind4;
		case 4:
			return wind5;
		case 5:
			return wind6;
		default:
			return "";
		}
	}
	/**
	 * 获取白天的天气状况
	 * @param index从0到5,分别表示今天,明天...
	 */
	public String getNightStatus(int index) {
		switch (index) {
		case 0:
			return img_title2;
		case 1:
			return img_title4;
		case 2:
			return img_title6;
		case 3:
			return img_title8;
		case 4:
			return img_title10;
		case 5:
			return img_title12;
		default:
			return "";
		}
	}
	
	/**
	 * 获取对应日期的白天天气情况的图片资源id;
	 * @param index 从0~5分别表示今天,明天....
	 */
	public int getDayImage(int index) {
		switch (index) {
		case 0:
			return getDayImage1();
		case 1:
			return getDayImage2();
		case 2:
			return getDayImage3();
		case 3:
			return getDayImage4();
		case 4:
			return getDayImage5();
		case 5:
			return getDayImage6();
		default:
			return R.drawable.weather_default;
		}
	}
	
	/**
	 * 获取对应日期的夜间天气情况的图片资源id;
	 * @param index 从0~5分别表示今天,明天....
	 */
	public int getNightImage(int index) {
		switch (index) {
		case 0:
			return getNightImage1();
		case 1:
			return getNightImage2();
		case 2:
			return getNightImage3();
		case 3:
			return getNightImage4();
		case 4:
			return getNightImage5();
		case 5:
			return getNightImage6();
		default:
			return R.drawable.weather_default;
		}
	}
	
	/**
	 * 获取今天白天的天气图片资源id.
	 */
	public int getDayImage1() {
		return getWeatherImage(img_title1, true);
	}

	/**
	 * 获取今天晚上的天气图片资源id.
	 */
	public int getNightImage1() {
		return getWeatherImage(img_title2, false);
	}

	public int getDayImage2() {
		return getWeatherImage(img_title3, true);
	}

	public int getNightImage2() {
		return getWeatherImage(img_title4, false);
	}

	public int getDayImage3() {
		return getWeatherImage(img_title5, true);
	}

	public int getNightImage3() {
		return getWeatherImage(img_title6, false);
	}

	public int getDayImage4() {
		return getWeatherImage(img_title7, true);
	}

	public int getNightImage4() {
		return getWeatherImage(img_title8, false);
	}

	public int getDayImage5() {
		return getWeatherImage(img_title9, true);
	}

	public int getNightImage5() {
		return getWeatherImage(img_title10, false);
	}

	public int getDayImage6() {
		return getWeatherImage(img_title11, true);
	}

	public int getNightImage6() {
		return getWeatherImage(img_title12, false);
	}

	/**
	 * @param imgTitle 天气图标的名称
	 * @param day 如果为true则返回白天的天气图标, 如果为false则返回夜间的天气图标.
	 * @return 返回天气图标名称对应的天气图标, 如果找不到则返回默认图标.
	 */
	private int getWeatherImage(String imgTitle, boolean day) {
		if (!TextUtils.isEmpty(imgTitle)
				&& WeatherHelper.WEATHER_STATUS_IMAGE_MAP.containsKey(imgTitle)) {
			if (day)
				return WeatherHelper.WEATHER_STATUS_IMAGE_MAP.get(imgTitle)[0];
			else
				return WeatherHelper.WEATHER_STATUS_IMAGE_MAP.get(imgTitle)[1];
		}

		return R.drawable.weather_default;
	}
}
