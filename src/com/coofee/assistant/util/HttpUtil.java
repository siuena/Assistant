package com.coofee.assistant.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.jsoup.Jsoup;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.coofee.assistant.AssistantApp;

public class HttpUtil {
	private static final String TAG = "HttpUtil";

	public static final int TIME_OUT = 200 * 1000;
	public static final String CTWAP = "ctwap";
	public static final String CMWAP = "cmwap";
	public static final String WAP_3G = "3gwap";
	public static final String UNIWAP = "uniwap";
	public static final int TYPE_CM_CU_WAP = 4;// 移动联通wap10.0.0.172
	public static final int TYPE_CT_WAP = 5;// 电信wap 10.0.0.200
	public static final int TYPE_OTHER_NET = 6;// 电信,移动,联通,wifi 等net网络
	public static Uri PREFERRED_APN_URI = Uri
			.parse("content://telephony/carriers/preferapn");

	/**
	 * 判断Network具体类型（联通移动wap，电信wap，其他net）
	 * 
	 **/
	public static int checkNetworkType() {
		Context context = AssistantApp.getInstance().getApplicationContext();
		try {
			final ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo mobNetInfoActivity = connectivityManager
					.getActiveNetworkInfo();
			if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
				// 注意一：
				// NetworkInfo 为空或者不可以用的时候正常情况应该是当前没有可用网络，
				// 但是有些电信机器，仍可以正常联网，
				// 所以当成net网络处理依然尝试连接网络。
				// （然后在socket中捕捉异常，进行二次判断与用户提示）。
				return TYPE_OTHER_NET;
			} else {
				// NetworkInfo不为null开始判断是网络类型
				int netType = mobNetInfoActivity.getType();
				if (netType == ConnectivityManager.TYPE_WIFI) {
					// wifi net处理
					return TYPE_OTHER_NET;
				} else if (netType == ConnectivityManager.TYPE_MOBILE) {
					// 注意二：
					// 判断是否电信wap:
					// 不要通过getExtraInfo获取接入点名称来判断类型，
					// 因为通过目前电信多种机型测试发现接入点名称大都为#777或者null，
					// 电信机器wap接入点中要比移动联通wap接入点多设置一个用户名和密码,
					// 所以可以通过这个进行判断！
					final Cursor c = context.getContentResolver().query(
							PREFERRED_APN_URI, null, null, null, null);
					if (c != null) {
						c.moveToFirst();
						final String user = c.getString(c
								.getColumnIndex("user"));
						if (!TextUtils.isEmpty(user)) {
							if (user.startsWith(CTWAP)) {
								return TYPE_CT_WAP;
							}
						}
					}
					c.close();
					// 注意三：
					// 判断是移动联通wap:
					// 其实还有一种方法通过getString(c.getColumnIndex("proxy")获取代理ip
					// 来判断接入点，10.0.0.172就是移动联通wap，10.0.0.200就是电信wap，但在
					// 实际开发中并不是所有机器都能获取到接入点代理信息，例如魅族M9 （2.2）等...
					// 所以采用getExtraInfo获取接入点名字进行判断
					String netMode = mobNetInfoActivity.getExtraInfo();
					if (netMode != null) {
						// 通过apn名称判断是否是联通和移动wap
						netMode = netMode.toLowerCase();
						if (netMode.equals(CMWAP) || netMode.equals(WAP_3G)
								|| netMode.equals(UNIWAP)) {
							return TYPE_CM_CU_WAP;
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return TYPE_OTHER_NET;
		}
		return TYPE_OTHER_NET;
	}

	/**
	 * 从url获取服务器返回的字符串.
	 */
	public static String getResponse(String url) {
//		InputStream in = null;
//		try {
//			URL _url = new URL(url);
//			HttpURLConnection urlConn = null;
//			
//			int apn = checkNetworkType();
//			Proxy proxy = null;
//			if (TYPE_CT_WAP == apn) {
//				proxy = new Proxy(java.net.Proxy.Type.HTTP,
//						new InetSocketAddress(CDMA_HOSTNAME, PORT));
//				// urlConn = (HttpURLConnection) _url.openConnection();
//				// urlConn.setRequestProperty("X-Online-Host", CDMA_HOSTNAME);
//			} else if (TYPE_CM_CU_WAP == apn) {
//				proxy = new Proxy(java.net.Proxy.Type.HTTP,
//						new InetSocketAddress(GSM_HOSTNAME, PORT));
//			}
//			if (proxy != null) {
//				urlConn = (HttpURLConnection) _url.openConnection(proxy);
//			} else {
//				urlConn = (HttpURLConnection) _url.openConnection();
//			}
//			
//			in = urlConn.getInputStream();
//			if (in == null) return null;
//			BufferedReader reader = new BufferedReader(
//										new InputStreamReader(
//												new BufferedInputStream(in)));
//			String line = null;
//			StringBuilder content = new StringBuilder(200);
//			while ((line = reader.readLine()) != null) {
//				content.append(line);
//			}
//			
//			return content.toString();
//		} catch (MalformedURLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}

		HttpClient httpClient = getHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			Log.d(TAG + " getResponse ", url);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			Log.d(TAG + " getResponse status code: ", ""
					+ httpResponse.getStatusLine().getStatusCode());
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				BasicResponseHandler responseHandler = new BasicResponseHandler();
				return responseHandler.handleResponse(httpResponse);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static InputStream getInputStream(String url) {
		HttpClient httpClient = getHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			Log.d("response status code", ""
					+ httpResponse.getStatusLine().getStatusCode());
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return httpResponse.getEntity().getContent();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static HttpClient getHttpClient() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpParams params = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, HttpUtil.TIME_OUT);
		HttpConnectionParams.setSoTimeout(params, HttpUtil.TIME_OUT);
		params.setParameter("Connection", "keep-alive");
		params.setParameter("Accept-Encoding", "gzip,deflate");
		params.setParameter("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
		params.setParameter("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		params.setParameter("Accept-Language",
				"zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		params.setParameter(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31");
		return httpClient;
	}

	/**
	 * 返回一个配置好报头的Jsoup连接.
	 */
	public static org.jsoup.Connection getJsoupConnection(String url) {
		return Jsoup
				.connect(url)
				.header("Connection", "keep-alive")
				.header("Accept-Encoding", "gzip,deflate")
				.header("Accept",
						"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
				.header("Accept-Language",
						"zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3")
				.userAgent(
						"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
				.timeout(HttpUtil.TIME_OUT);
	}
}
