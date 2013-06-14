package com.coofee.assistant.weather;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 用于从sina下载天气图片
 * @author zhao
 *
 */
public class DownloadWeatherSinaImage {

	private static final String DOWNLOAD_78_URL = "http://php.weather.sina.com.cn/images/yb3/78_78/";
	private static final String DOWNLOAD_180_URL = "http://php.weather.sina.com.cn/images/yb3/180_180/";
	private static final String SAVED_78_PATH = "G:/天气查询/新浪天气图标/78_78/";
	private static final String SAVED_180_PATH = "G:/天气查询/新浪天气图标/180_180/";
	private static Executor executor = Executors.newFixedThreadPool(4);	
	private static String[] WEATHER_IMAGE_NAMES = {
		"qing", 
		"duoyun",
		"yin",
		"zhenyu",
		"leizhenyu",
		"bingbao",
		"yujiaxue",
		"xiaoyu", "zhongyu", "dayu", "baoyu", "tedabaoyu",
		"xiaoxue", "zhongxue", "daxue", "baoxue",
		"wu",
		"qingwu",
		"dongyu",
		"nongwu",
		"fuchen",
		"yangsha",  
		"shachenbao",
		"qiangshachenbao",
		"yan",
		"shuangdong",
		"mai"
	};
	
	/**
	 * 下载文件
	 * @param fileName 要下载的文件的名字
	 * @param savedPath 下载完成后的保存路径.
	 */
	private static void downloadFile(String downloadURL, String fileName, 
			String savedPath) {
		final String dayUrl = downloadURL + fileName + "_0.png";
		final String nightUrl = downloadURL + fileName + "_1.png";
		final String savedDay = savedPath + fileName + "_0.png";
		final String savedNight = savedPath + fileName + "_1.png";
		executor.execute(new DownloadRunnable(dayUrl, savedDay));
		executor.execute(new DownloadRunnable(nightUrl, savedNight));
	}
	
	/**
	 * 下载新浪天气预报的图片
	 */
	private static void downloadFiles() {
		System.out.println("共有" + WEATHER_IMAGE_NAMES.length * 2 + "张图片");
		for (int i = 0; i < WEATHER_IMAGE_NAMES.length; i++) {
			downloadFile(DOWNLOAD_78_URL, WEATHER_IMAGE_NAMES[i], SAVED_78_PATH);
			downloadFile(DOWNLOAD_180_URL, WEATHER_IMAGE_NAMES[i], SAVED_180_PATH);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		downloadFiles();
	}	

}

/**
 * 用于下载新浪天气的图片.
 * @author zhao
 *
 */
class DownloadRunnable implements Runnable {
	private static final int TIME_OUT = 200;
	
	private String downloadUrl;
	private String saveFilePath;
	
	public DownloadRunnable(String downloadUrl, String saveFilePath) {
		this.downloadUrl = downloadUrl;
		this.saveFilePath = saveFilePath;
	}
	
	@Override
	public void run() {
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(downloadUrl).openConnection();
			conn.setConnectTimeout(TIME_OUT * 1000);
			conn.setReadTimeout(TIME_OUT * 1000);
			BufferedInputStream in = 
					new BufferedInputStream(conn.getInputStream());
			FileOutputStream fileOut = new FileOutputStream(new File(saveFilePath));
			byte[] buffer = new byte[8192];
			int len = -1;
			while ((len = in.read(buffer)) != -1) {
				fileOut.write(buffer, 0, len);
			}
			fileOut.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
