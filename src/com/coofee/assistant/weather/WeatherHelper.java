package com.coofee.assistant.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.coofee.assistant.R;
import com.coofee.assistant.util.HttpUtil;
import com.coofee.assistant.weather.model.Area;
import com.coofee.assistant.weather.model.RealtimeWeatherInfo;
import com.coofee.assistant.weather.model.WeatherCity;
import com.coofee.assistant.weather.model.WeatherHistory;
import com.coofee.assistant.weather.model.WeatherInfo;
import com.coofee.assistant.weather.model.WeatherModel;

public class WeatherHelper {

	private static String TAG = "WeatherHelper";

	public static final int TIME_OUT = 100;
	/**
	 * 用来保存天气情况与其相对应的图片的资源id, 其中索引0处的图片是白天的图片, 索引1处的图片是夜晚的图片.
	 */
	public static final HashMap<String, Integer[]> WEATHER_STATUS_IMAGE_MAP = new HashMap<String, Integer[]>();
	static {
		WEATHER_STATUS_IMAGE_MAP.put("晴", new Integer[] { R.drawable.qing_0,
				R.drawable.qing_1 });
		WEATHER_STATUS_IMAGE_MAP.put("多云", new Integer[] { R.drawable.duoyun_0,
				R.drawable.duoyun_1 });
		WEATHER_STATUS_IMAGE_MAP.put("阴", new Integer[] { R.drawable.yin_0,
				R.drawable.yin_1 });
		WEATHER_STATUS_IMAGE_MAP.put("阵雨", new Integer[] { R.drawable.zhenyu_0,
				R.drawable.zhenyu_1 });
		WEATHER_STATUS_IMAGE_MAP.put("雷阵雨", new Integer[] {
				R.drawable.leizhenyu_0, R.drawable.leizhenyu_1 });
		WEATHER_STATUS_IMAGE_MAP.put("雷阵雨伴有冰雹", new Integer[] {
				R.drawable.bingbao_0, R.drawable.bingbao_1 });
		WEATHER_STATUS_IMAGE_MAP.put("雨夹雪", new Integer[] {
				R.drawable.yujiaxue_0, R.drawable.yujiaxue_1 });
		WEATHER_STATUS_IMAGE_MAP.put("小雨", new Integer[] { R.drawable.xiaoyu_0,
				R.drawable.xiaoyu_1 });
		WEATHER_STATUS_IMAGE_MAP.put("中雨", new Integer[] {
				R.drawable.zhongyu_0, R.drawable.zhongyu_1 });
		WEATHER_STATUS_IMAGE_MAP.put("大雨", new Integer[] { R.drawable.dayu_0,
				R.drawable.dayu_1 });
		WEATHER_STATUS_IMAGE_MAP.put("暴雨", new Integer[] { R.drawable.baoyu_0,
				R.drawable.baoyu_1 });
		WEATHER_STATUS_IMAGE_MAP.put("大暴雨", new Integer[] {
				R.drawable.tedabaoyu_0, R.drawable.tedabaoyu_1 });
		WEATHER_STATUS_IMAGE_MAP.put("特大暴雨", new Integer[] {
				R.drawable.tedabaoyu_0, R.drawable.tedabaoyu_1 });
		WEATHER_STATUS_IMAGE_MAP.put("阵雪", new Integer[] {
				R.drawable.zhongxue_0, R.drawable.zhongxue_1 });
		WEATHER_STATUS_IMAGE_MAP.put("小雪", new Integer[] {
				R.drawable.xiaoxue_0, R.drawable.xiaoxue_1 });
		WEATHER_STATUS_IMAGE_MAP.put("中雪", new Integer[] {
				R.drawable.zhongxue_0, R.drawable.zhongxue_1 });
		WEATHER_STATUS_IMAGE_MAP.put("大雪", new Integer[] { R.drawable.daxue_0,
				R.drawable.daxue_1 });
		WEATHER_STATUS_IMAGE_MAP.put("暴雪", new Integer[] { R.drawable.baoxue_0,
				R.drawable.baoxue_1 });
		WEATHER_STATUS_IMAGE_MAP.put("雾", new Integer[] { R.drawable.wu_0,
				R.drawable.wu_1 });
		WEATHER_STATUS_IMAGE_MAP.put("冻雨", new Integer[] { R.drawable.dongyu_0,
				R.drawable.dongyu_1 });
		WEATHER_STATUS_IMAGE_MAP.put("浮尘", new Integer[] { R.drawable.fuchen_0,
				R.drawable.fuchen_1 });
		WEATHER_STATUS_IMAGE_MAP.put("扬沙", new Integer[] {
				R.drawable.yangsha_0, R.drawable.yangsha_1 });
		WEATHER_STATUS_IMAGE_MAP.put("沙尘暴", new Integer[] {
				R.drawable.shachenbao_0, R.drawable.shachenbao_1 });
		WEATHER_STATUS_IMAGE_MAP.put("强沙尘暴", new Integer[] {
				R.drawable.qiangshachenbao_0, R.drawable.qiangshachenbao_1 });
		WEATHER_STATUS_IMAGE_MAP.put("小雨-中雨", new Integer[] {
				R.drawable.zhongyu_0, R.drawable.zhongyu_1 });
		WEATHER_STATUS_IMAGE_MAP.put("中雨-大雨", new Integer[] {
				R.drawable.dayu_0, R.drawable.dayu_1 });
		WEATHER_STATUS_IMAGE_MAP.put("大雨-暴雨", new Integer[] {
				R.drawable.baoyu_0, R.drawable.baoyu_1 });
		WEATHER_STATUS_IMAGE_MAP.put("暴雨-大暴雨", new Integer[] {
				R.drawable.tedabaoyu_0, R.drawable.tedabaoyu_1 });
		WEATHER_STATUS_IMAGE_MAP.put("大暴雨-特大暴雨", new Integer[] {
				R.drawable.tedabaoyu_0, R.drawable.tedabaoyu_1 });
		WEATHER_STATUS_IMAGE_MAP.put("小雪-中雪", new Integer[] {
				R.drawable.zhongxue_0, R.drawable.zhongxue_1 });
		WEATHER_STATUS_IMAGE_MAP.put("中雪-大雪", new Integer[] {
				R.drawable.daxue_0, R.drawable.daxue_1 });
		WEATHER_STATUS_IMAGE_MAP.put("大雪-暴雪", new Integer[] {
				R.drawable.baoxue_0, R.drawable.baoxue_1 });
	}

	/**
	 * 将WeatherInfo天气实体转换成历史天气实体
	 * @return
	 */
	public static ArrayList<WeatherHistory> toWeatherHistory(WeatherInfo weatherInfo) {
		ArrayList<WeatherHistory> weatherHistoryList = new ArrayList<WeatherHistory>();
		WeatherHistory history = null;
		for (int i = 0; i < 6; i++) {
			history = new WeatherHistory();
			history.setWeatherAreaName(weatherInfo.getCity());
			history.setDayStatus(weatherInfo.getDayStatus(i));
			history.setNightStatus(weatherInfo.getNightStatus(i));
			history.setDayTemperature(weatherInfo.getDayTemp(i));
			history.setNightTemperature(weatherInfo.getNightTemp(i));
			history.setDate(toDate(weatherInfo.getDate_y(), i));
			weatherHistoryList.add(history);
		}
		
		return weatherHistoryList;
	}
	
	/**
	 * 通过关键字keyword, 获取类似的天气城市信息.
	 */
	public static List<WeatherCity> getAnalogousWeatherCityByKeyword(
			String keyword) {
		if (TextUtils.isEmpty(keyword))
			return null;

		final String SEARCH_URL = "http://toy.weather.com.cn/SearchBox/searchBox?callback=jsonp1343396048201&_=1343396067262&language=zh&keyword=";
		String searchCityUrl = null;
		try {
			searchCityUrl = SEARCH_URL + URLEncoder.encode(keyword, "UTF-8");
			Log.d(TAG, "searchCityUrl: " + searchCityUrl);
			String content = HttpUtil.getResponse(searchCityUrl);
			if (!TextUtils.isEmpty(content)) {
				Log.d(TAG + " getAnalogousWeatherCityByKeyword", content);
				return parseWeatherCity(content);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 通过城市的id, 获取JSON格式的天气信息, 然后解析JSON格式的天气信息, 返回天气信息数据模型.
	 * 
	 * @param cityId
	 *            城市的id
	 */
	public static WeatherModel getCityWeatherByCityId(String cityId) {
		if (!TextUtils.isEmpty(cityId)) {
			final String weatherInfoUrl = "http://m.weather.com.cn/data/"
					+ cityId + ".html";
			String jsonWeatherInfo = HttpUtil.getResponse(weatherInfoUrl);
			Log.d(TAG + " getCityWeatherByCityId", jsonWeatherInfo);
			if (!TextUtils.isEmpty(jsonWeatherInfo)) {
				Log.d(TAG + " getCityWeatherByCityId",
						"try parse to WeatherModel.");
				return JSON.parseObject(jsonWeatherInfo, WeatherModel.class);
			}
		}

		return null;
	}

	/**
	 * 只显示月份和日子
	 * @param currentDay
	 * @param dayCount
	 * @return
	 */
	public static String getDayShort(Date date) {
		SimpleDateFormat spf = new SimpleDateFormat("yyyy年MM月dd日");
		String dateFull = spf.format(date);
		if (TextUtils.isEmpty(dateFull)) {
			return "";
		} else {
			return dateFull.substring(dateFull.indexOf("年") + 1);
		}
	}
	
	public static Date toDate(String currentDay, int dayCount) {
		SimpleDateFormat spf = new SimpleDateFormat("yyyy年MM月dd日");
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(spf.parse(currentDay));
			calendar.set(Calendar.DAY_OF_MONTH,
					calendar.get(Calendar.DAY_OF_MONTH) + dayCount);
			
			return calendar.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * 获取currentDay的后dayCount的日期字符串.
	 * 
	 * @param currentDay
	 *            日期字符串格式如"yyyy年MM月dd日".
	 */
	public static String getNextDay(String currentDay, int dayCount) {
		SimpleDateFormat spf = new SimpleDateFormat("yyyy年MM月dd日");
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(spf.parse(currentDay));
			calendar.set(Calendar.DAY_OF_MONTH,
					calendar.get(Calendar.DAY_OF_MONTH) + dayCount);
			
			return spf.format(calendar.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * 通过cityId获取实时天气信息
	 */
	public static RealtimeWeatherInfo getCityRealtimeWeatherByCityId(
			String cityId) {
		if (!TextUtils.isEmpty(cityId)) {
			final String weatherInfoUrl = "http://www.weather.com.cn/data/sk/"
					+ cityId + ".html";
			String jsonWeatherInfo = HttpUtil.getResponse(weatherInfoUrl);
			Log.d(TAG + " getCityRealtimeWeatherByCityId", jsonWeatherInfo);
			if (!TextUtils.isEmpty(jsonWeatherInfo)) {
				Log.d(TAG + " getCityRealtimeWeatherByCityId",
						"try parse to WeatherModel.");
				try {
					org.json.JSONObject jsonWeatherModel = new org.json.JSONObject(
							jsonWeatherInfo);
					org.json.JSONObject realtimeWeatherObject = jsonWeatherModel
							.getJSONObject("weatherinfo");
					RealtimeWeatherInfo realtimeWeatherInfo = new RealtimeWeatherInfo();
					realtimeWeatherInfo.setCity(realtimeWeatherObject
							.getString("city"));
					realtimeWeatherInfo.setCityid(realtimeWeatherObject
							.getString("cityid"));
					realtimeWeatherInfo.setTemp(realtimeWeatherObject
							.getString("temp") + "℃");
					realtimeWeatherInfo.setWD(realtimeWeatherObject
							.getString("WD"));
					realtimeWeatherInfo.setWS(realtimeWeatherObject
							.getString("WS"));
					realtimeWeatherInfo.setSD(realtimeWeatherObject
							.getString("SD"));
					realtimeWeatherInfo.setTime(realtimeWeatherObject
							.getString("time"));

					return realtimeWeatherInfo;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	/**
	 * 从Asset文件中获取全部的地区信息.
	 * @param context
	 * @return
	 */
	public static List<Area> getAllAreaFromAssert(Context context) {
		List<Area> allAreaList = new ArrayList<Area>();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(context.getAssets()
					.open("weather_data.json", Context.MODE_PRIVATE), "gbk"));
			String areaJson = null;
			while ((areaJson = in.readLine()) != null) {
				allAreaList.add(JSON.parseObject(areaJson, Area.class));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return allAreaList;
	}

	/**
	 * 从获取的服务器响应中获取得到的城市信息
	 */
	private static List<WeatherCity> parseWeatherCity(String content) {
		Log.d(TAG + " parseWeatherCity", "start parse weather city.");

		List<String> cityNames = parseCityNames(content);
		List<String> cityIds = parseCityIds(content);
		// 如果获取的城市名字比城市的id多，则删除城市名字使得城市名字与城市id值相等。
		// 当查询直辖市的城市信息时会出现这种情况。
		for (int i = cityIds.size(); i < cityNames.size(); i++) {
			cityNames.remove(i);
		}

		List<WeatherCity> cityList = new ArrayList<WeatherCity>();
		for (int i = 0; i < cityNames.size(); i++) {
			cityList.add(new WeatherCity(cityNames.get(i), cityIds.get(i)));
		}
		return cityList;
	}

	/**
	 * 从content中获取所有的城市名称;
	 */
	private static List<String> parseCityNames(String content) {
		// 匹配与"n":"汉字"的字符串.
		String regex = "\"n\":\"[\u4E00-\u9FFF]*\"";
		return getAllStringMatched(content, regex);
	}

	/**
	 * 从content中获取所有的城市id;
	 */
	private static List<String> parseCityIds(String content) {
		// 寻找类似与"i":"101110101"的字符串
		String regex = "\"i\":\"[0-9]*\"";
		return getAllStringMatched(content, regex);
	}

	/**
	 * 获取content中所有匹配regex的字符串, 然后处理获取城市名称或id. 针对类似与 "i":"101110101" 和" n":"汉字"
	 * 字符串, 然后从中截取101110101和汉字部分.
	 */
	private static List<String> getAllStringMatched(String content, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		String matched = null;
		List<String> matchedList = new ArrayList<String>();
		while (matcher.find()) {
			matched = content.substring(matcher.start(), matcher.end());
			Log.d(TAG + " getAllStringMatched", matched);
			matchedList.add(matched.substring(5, matched.length() - 1));
		}
		return matchedList;
	}

}