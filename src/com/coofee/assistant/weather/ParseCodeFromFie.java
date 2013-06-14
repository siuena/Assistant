package com.coofee.assistant.weather;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.coofee.assistant.util.Pinyin;
import com.coofee.assistant.weather.model.Area;
import com.coofee.assistant.weather.model.Area.AreaType;

/**
 * 合并文件, 获取天气查询需要的信息.
 * @author zhao
 *
 */
public class ParseCodeFromFie {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		saveWeatherCityAsFile();
	}

	public static void saveWeatherCityAsFile() {
		Map<String, ArrayList<Area>> areaMap = parseAreaFromWeather();
		parseAreaFromZipcode(areaMap);

		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(
							"G:/Graduation_Design/weather/weather_data"), "gbk"));
			StringBuilder cityInfo = new StringBuilder();
//			List<Area> allAreaList = new ArrayList<Area>();
			for (String province : areaMap.keySet()) {
				ArrayList<Area> areaList = areaMap.get(province);
				for (Area area : areaList) {
					area.setHypy(Pinyin.getPinYin(area.getName()));
					area.setShortHypy(Pinyin.getPinYinHeadChar(area.getName()));
					area.setProvince(province);
//					allAreaList.add(area);
					
					// 保存的内容以":"作为分隔符; 依次为: 省,天气编码,地区名称,邮政编码,区号,汉语拼音全拼,汉语拼音首字母
//					cityInfo.append(province).append(":")
//						.append(area.getCode()).append(":")
//						.append(area.getName()).append(":");
//					
//					if (area.getZipcode() != null && area.getZipcode().trim().length() != 0) {
//						cityInfo.append(area.getZipcode()).append(":");
//					}
//					
//					if (area.getPhoneAreaCode() != null && area.getPhoneAreaCode().trim().length() != 0) {
//						cityInfo.append(area.getPhoneAreaCode()).append(":");
//					}
//					
//					cityInfo.append(Pinyin.getPinYin(area.getName())).append(":");
//					cityInfo.append(Pinyin.getPinYinHeadChar(area.getName())).append("\n");
//					
//					cityInfo.trimToSize();
//					System.out.println(cityInfo);
//					out.write(cityInfo.toString());
//					cityInfo.setLength(0);
					
					cityInfo.append(area.toString()).append("\n");
					cityInfo.trimToSize();
					out.write(cityInfo.toString());
					cityInfo.setLength(0);
				}
			}

//			out.write(JSON.toJSONString(allAreaList));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static Map<String, ArrayList<Area>> parseAreaFromWeather() {
		String filePath = "G:/Graduation_Design/weather/中国天气网_id.txt";
		Map<String, ArrayList<Area>> weatherCityList = new HashMap<String, ArrayList<Area>>();
		String line = null;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(
					filePath), "gbk"));
			boolean isPreviousLineEmpty = false;
			String provinceName = null;
			while ((line = in.readLine()) != null) {
				if ("".equals(line) || line.trim().length() == 0) {
					isPreviousLineEmpty = true;
					continue;
				}

				if (line.contains("=")) {
					String[] values = line.split("=");
					if (values != null) {
						Area area = null;
						if (values.length == 4) {
							area = new Area();
							area.setCode(values[0].trim());
							area.setName(values[1].trim());
							area.setPhoneAreaCode(values[2].trim());
							area.setZipcode(values[3].trim());
						} else if (values.length == 3) {
							area = new Area();
							area.setCode(values[0].trim());
							area.setName(values[1].trim());
							area.setZipcode(values[2].trim());
						} else if (values.length == 2) {
							area = new Area();
							area.setCode(values[0].trim());
							area.setName(values[1].trim());
						} else if (values.length == 1) {
							area = new Area();
							area.setName(values[0].trim());
						}

						if (area != null) {
							if (isPreviousLineEmpty) {
								provinceName = area.getName();
								System.out.println("get a province: "
										+ provinceName);
								area.setType(AreaType.PROVINCE);
								ArrayList<Area> cityList = new ArrayList<Area>();
								if (values.length != 1) {
									cityList.add(area);
								}
								weatherCityList.put(provinceName, cityList);
								
							} else {
								System.out.println("get a city named "
										+ area.getName() + " belongs to "
										+ provinceName);
								weatherCityList.get(provinceName).add(area);
							}
						}

						isPreviousLineEmpty = false;
					}
				}
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
		return weatherCityList;
	}

	public static Map<String, ArrayList<Area>> parseAreaFromZipcode(
			Map<String, ArrayList<Area>> map) {
		String filePath = "G:/Graduation_Design/weather/中国邮政编码.txt";
		String cityLine = null;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(
					filePath), "gbk"));
			List<String> cityList = new ArrayList<String>();
			while ((cityLine = in.readLine()) != null) {
				if (cityLine.startsWith("//")) {
					continue;
				}
				cityList.add(cityLine.trim());
			}
			in.close();
			
			String provinceName = null;
			String cityName = null;

			for (String line : cityList) {
				String[] values = line.split("\t");
				if (values != null && values.length == 4) {
					provinceName = values[0].trim();
					cityName = values[1].trim();

					if (map.containsKey(provinceName)) {
						ArrayList<Area> areaList = map.get(provinceName);
						areaList.trimToSize();
						Area area = null;
						for (int i = 0; i < areaList.size(); i++) {
							area = areaList.get(i);
							
							if (area == null) continue;
							
							if (area.getName().startsWith(cityName)) {
								if (area.getZipcode() == null
										|| area.getZipcode().trim().length() == 0) {
									System.out.println("get zipcode "
											+ values[2] + " with "
											+ area.getName());
									area.setZipcode(values[2].trim());
								}

								if (area.getPhoneAreaCode() == null
										|| area.getPhoneAreaCode().trim()
												.length() == 0) {
									if (values[3].trim().length() != 4) {
										System.out.println("get phoneCode "
												+ values[3] + " with "
												+ area.getName());
										area.setPhoneAreaCode("0"
												+ values[3].trim());
									}
								}

								break;
							}
						}
					}
				}
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

		return map;
	}
}