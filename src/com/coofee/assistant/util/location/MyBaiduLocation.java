package com.coofee.assistant.util.location;

import android.util.Log;

import com.baidu.location.LocServiceMode;
import com.baidu.location.LocationClient;
import com.baidu.location.ReceiveListener;
import com.coofee.assistant.AssistantApp;


/**
 * 百度API文档说明
 * @see also
 * http://dev.baidu.com/wiki/geolocation/index.php?title=AndroidAPI%E6%8E%A5%E5%8F%A3%E6%96%87%E6%A1%A31.x
 * 
 * @author zhao
 */
public class MyBaiduLocation {
	
	private static final String TAG = MyBaiduLocation.class.getCanonicalName();
	
	private LocationClient mLocationClient;
	private ReceiveListener mReceiveListener;
	
	/** 正常**/
	private final int SUCCESS = 0;
	private final int SDK_NOT_START = 1;
	private final int NOT_LISTENER_FUNCTION = 2;
	private final int REQUEST_INTERVAL_TOO_SHORT = 6;
	
	private MyBaiduLocation() {
		mLocationClient = AssistantApp.getInstance().getLocationClient();
	}
	
	public static MyBaiduLocation getInstance() {
		return new MyBaiduLocation();
	}

	public void setReceiveListener(ReceiveListener receiveListener) {
		mReceiveListener = receiveListener;
	}
	
	/**
	 * 查询用户当前所在的城市
	 */
	public void startQueryUserCity() {
		Log.d(TAG, "添加参数...");
		// 设置只查询城市信息
		mLocationClient.setAddrType("&addr=city");
		mLocationClient.setProdName("coofee");
		mLocationClient.setServiceMode(LocServiceMode.Immediat);
		mLocationClient.setCoorType("bd0911");

		Log.d(TAG, "添加接收位置信息监听...");
		mLocationClient.addRecerveListener(mReceiveListener);
		Log.d(TAG, "开启百度定位SDK...");
		mLocationClient.start();
		
		// 异步获取当前位置。可以理解为发起一次定位，
		// 而且是异步的，所以立即返回，不会引起阻塞。
		getLocation();
	}
	
	/**
	 * 关闭百度定位
	 */
	public void close() {
		mLocationClient.stop();
		Log.d(TAG, "关闭定位SDK.");
		mLocationClient.removeLocationChangedLiteners();
		Log.d(TAG, "清除接收位置信息监听.");
		mLocationClient.removeReceiveListeners();
		Log.d(TAG, "清除位置改变监听.");
		mLocationClient.closeGPS();
		Log.d(TAG, "关闭GPS.");
	}
	
	public void getLocation() {
		/**
		 *  异步获取当前位置。可以理解为发起一次定位，而且是异步的，所以立即返回，不会引起阻塞。
		 *  定位结果在ReceiveListener的方法OnReceive方法的参数中返回。
		 *  但是如果定位失败，OnReceive方法的参数为null。定位失败的原因可能有网络异常，
		 *  获取定位依据偶然失效等，这时可以根据需要再次调用getlocation发起一次定位。
		 *  getlocation被调用后，立即返回，返回值为整数. 0：正常。1：SDK还未启动。
		 *  2：没有监听函数。6：请求间隔过短。
		 */
		switch(mLocationClient.getLocation()) {
		case SUCCESS:
			Log.d(TAG, "getLocation() result is SUCCESS.");
			break;
		case SDK_NOT_START:
			Log.d(TAG, "getLocation() result is sdk未启动.");
			break;
		case NOT_LISTENER_FUNCTION:
			Log.d(TAG, "getLocation() result is 没有监听函数.");
			break;
		case REQUEST_INTERVAL_TOO_SHORT:
			Log.d(TAG, "getLocation() result is 请求间隔过短.");
			break;
		}
	}
}