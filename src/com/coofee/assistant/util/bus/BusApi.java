package com.coofee.assistant.util.bus;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpHost;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.aibang.open.client.AibangApi;
import com.alibaba.fastjson.JSON;
import com.coofee.assistant.AssistantApp;
import com.coofee.assistant.bus.BusLine;
import com.coofee.assistant.bus.BusLineResult;
import com.coofee.assistant.bus.BusProvince;
import com.coofee.assistant.bus.BusStation;
import com.coofee.assistant.bus.BusStationResult;
import com.coofee.assistant.bus.BusTransferPlan;
import com.coofee.assistant.bus.BusTransferPlan.BusTransferSegment;
import com.coofee.assistant.bus.BusTransferResult;

public class BusApi {
	private static final String TAG = BusApi.class.getSimpleName();
	private static final String API_KEY = "f41c8afccc586de03a99c86097e98ccb";
	private static final String MY_API_KEY = "a4e44cdf01e847aa96d98173372f9377";
	private static final String FORMAT_XML = "xml";

	private static AibangApi mAiBangApi;
	private ProgressDialog mProgressDialog;
	private BusHandler mBusHandler;

	public void getStation(Context context, final String city,
			final String stationName, BusListener listener) {
		init(context, listener);

		AssistantApp.getInstance().mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					InputStream in = getAiBangApiByXml().busStats(city,
							stationName);
					if (in != null) {
						BusStationResult stationResult = parseStation(in, city,
								stationName);
						mBusHandler.obtainMessage(
								BusRequestAction.ACTION_STATION, stationResult)
								.sendToTarget();
					} else {
						mBusHandler.obtainMessage(MSG_BUS_UNCOMPLETED)
								.sendToTarget();
					}
				} catch (Exception e) {
					e.printStackTrace();
					mBusHandler.obtainMessage(MSG_BUS_UNCOMPLETED,
							e.getMessage()).sendToTarget();
				}
			}
		});
	}

	public void getLine(Context context, final String city,
			final String lineName, BusListener listener) {
		init(context, listener);

		AssistantApp.getInstance().mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					InputStream in = getAiBangApiByXml().busLines(city,
							lineName, 0);

					if (in != null) {
						BusLineResult lineResult = parseLine(in, city, lineName);
						mBusHandler.obtainMessage(BusRequestAction.ACTION_LINE,
								lineResult).sendToTarget();
					} else {
						mBusHandler.obtainMessage(MSG_BUS_UNCOMPLETED)
								.sendToTarget();
					}
				} catch (Exception e) {
					e.printStackTrace();
					mBusHandler.obtainMessage(MSG_BUS_UNCOMPLETED,
							e.getMessage()).sendToTarget();
				}
			}
		});

	}

	public void getTransfer(Context context, final String city,
			final String from, final String to, final int transferType,
			BusListener listener) {
		init(context, listener);

		AssistantApp.getInstance().mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					InputStream in = getAiBangApiByXml().bus(city, from, to,
							null, null, null, null, transferType, null, 0);

					if (in != null) {
						BusTransferResult transferResult = parseTransfer(in,
								city, transferType, from, to);
						mBusHandler.obtainMessage(
								BusRequestAction.ACTION_TRANSFER,
								transferResult).sendToTarget();
					} else {
						mBusHandler.obtainMessage(MSG_BUS_UNCOMPLETED)
								.sendToTarget();
					}
				} catch (Exception e) {
					e.printStackTrace();
					mBusHandler.obtainMessage(MSG_BUS_UNCOMPLETED,
							e.getMessage()).sendToTarget();
				}
			}
		});
	}

	private synchronized static AibangApi getAiBangApiByXml() {
		if (mAiBangApi == null) {
			mAiBangApi = new AibangApi(API_KEY, FORMAT_XML);
		}

		mAiBangApi.setProxy(getProxy());
		return mAiBangApi;
	}

	private static void changeAiBangApi() {
		if (mAiBangApi != null) {
			if (mAiBangApi.mApiKey.equals(API_KEY)) {
				mAiBangApi = new AibangApi(MY_API_KEY, FORMAT_XML);
			} else {
				mAiBangApi = new AibangApi(API_KEY, FORMAT_XML);
			}
		} else {
			mAiBangApi = new AibangApi(API_KEY, FORMAT_XML);
		}
	}

	private void init(Context context, BusListener listener) {
		mBusHandler = new BusHandler(BusApi.this);
		mBusHandler.addBusListener(listener);

		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setTitle("请求中...");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(true);
		mProgressDialog
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						mBusHandler.obtainMessage(MSG_BUS_CANCELED)
								.sendToTarget();
					}
				});
		mProgressDialog.show();
	}

	private BusStationResult parseStation(InputStream in, String city,
			String stationName) {
		try {
			XmlPullParser xmlParser = Xml.newPullParser();
			xmlParser.setInput(in, "utf-8");

			BusStationResult result = null;
			BusStation station = null;
			String tagName = null;
			for (int event = xmlParser.next(), type = TYPE_UNKNOWN; event != XmlPullParser.END_DOCUMENT; event = xmlParser
					.next()) {
				switch (event) {
				case XmlPullParser.START_TAG:
					tagName = xmlParser.getName();

					if (MESSAGE.equals(tagName)) {
						throw new Exception(xmlParser.nextText());
					} else if (STATS.equals(tagName)) {
						result = new BusStationResult();
						result.setCity(city);
						result.setStationName(stationName);

						type = TYPE_IN_STATS;
					} else {
						switch (type) {
						case TYPE_IN_STATS:
							if (STAT.equals(tagName)) {
								station = new BusStation();
								station.setCity(city);
								type = TYPE_IN_STAT;
							}
							break;
						case TYPE_IN_STAT:
							if (NAME.equals(tagName)) {
								station.setStationName(xmlParser.nextText());
							} else if (XY.equals(tagName)) {

								try {
									String[] xy = xmlParser.nextText().split(
											",");
									station.setLongitude(Float
											.parseFloat(xy[0]));
									station.setLatitude(Float.parseFloat(xy[1]));
								} catch (Exception e) {
								}

							} else if (LINE_NAMES.equals(tagName)) {
								station.setLineNames(xmlParser.nextText()
										.split(";"));
							}
							break;
						}
					}
					break;
				case XmlPullParser.END_TAG:
					tagName = xmlParser.getName();

					if (STAT.equals(tagName)) {
						result.getStationList().add(station);
						type = TYPE_IN_STATS;
					}
				}
			}

			Log.d(TAG, JSON.toJSONString(result));
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private BusTransferResult parseTransfer(InputStream in, String city,
			int transferType, String startStation, String endStation) {
		try {
			XmlPullParser xmlParser = Xml.newPullParser();
			xmlParser.setInput(in, "utf-8");

			BusTransferResult result = null;
			BusTransferPlan plan = null;
			BusTransferSegment segment = null;
			String tagName = null;
			for (int event = xmlParser.next(), type = TYPE_UNKNOWN; event != XmlPullParser.END_DOCUMENT; event = xmlParser
					.next()) {
				switch (event) {
				case XmlPullParser.START_TAG:
					tagName = xmlParser.getName();
					if (MESSAGE.equals(tagName)) {
						throw new Exception(xmlParser.nextText());
					} else if (BUSES.equals(tagName)) {
						result = new BusTransferResult();
						result.setCity(city);
						result.setStartStation(startStation);
						result.setEndStation(endStation);
						result.setTransferType(transferType);

						type = TYPE_IN_BUSES;
					} else {
						switch (type) {
						case TYPE_IN_BUSES:
							if (BUS.equals(tagName)) {
								plan = new BusTransferPlan();
								type = TYPE_IN_BUS;
							}
							break;
						case TYPE_IN_BUS:
							if (DIST.equals(tagName)) {
								plan.setDistance(Integer.parseInt(xmlParser
										.nextText()));
							} else if (TIME.equals(tagName)) {
								plan.setTime(Integer.parseInt(xmlParser
										.nextText()));
							} else if (FOOT_DIST.equals(tagName)) {
								plan.setFootDistance(Integer.parseInt(xmlParser
										.nextText()));
							} else if (SEGMENTS.equals(tagName)) {
								type = TYPE_IN_SEGMENTS;
							}
							break;
						case TYPE_IN_SEGMENTS:
							if (SEGMENT.equals(tagName)) {
								segment = new BusTransferSegment();
								type = TYPE_IN_SEGMENT;
							}
							break;
						case TYPE_IN_SEGMENT:
							if (START_STATS.equals(tagName)) {
								segment.setStartStation(xmlParser.nextText());
							} else if (END_STATS.equals(tagName)) {
								segment.setEndStation(xmlParser.nextText());
							} else if (LINE_NAME.equals(tagName)) {
								segment.setLineName(xmlParser.nextText());
							} else if (LINE_DIST.equals(tagName)) {
								segment.setLineDistance(Integer
										.parseInt(xmlParser.nextText()));
							} else if (STATS.equals(tagName)) {
								segment.setStationNames(xmlParser.nextText()
										.split(";"));
							} else if (FOOT_DIST.equals(tagName)) {
								segment.setFootDistance(Integer
										.parseInt(xmlParser.nextText()));
							}
							break;
						}
					}

					break;
				case XmlPullParser.END_TAG:
					tagName = xmlParser.getName();

					if (SEGMENT.equals(tagName)) {
						plan.getTransferSegments().add(segment);
						type = TYPE_IN_SEGMENTS;
					} else if (SEGMENTS.equals(tagName)) {
						type = TYPE_IN_BUS;
					} else if (BUS.equals(tagName)) {
						result.getTransferPlanList().add(plan);
						type = TYPE_IN_BUSES;
					}
					break;
				}
			}

			Log.d(TAG, JSON.toJSONString(result));
			return result;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private BusLineResult parseLine(InputStream in, String city, String lineName) {
		try {
			XmlPullParser xmlParser = Xml.newPullParser();
			xmlParser.setInput(in, "utf-8");

			BusLineResult result = null;
			BusLine line = null;
			String tagName = null;
			for (int event = xmlParser.next(), type = TYPE_UNKNOWN; event != XmlPullParser.END_DOCUMENT; event = xmlParser
					.next()) {
				switch (event) {
				case XmlPullParser.START_TAG:
					tagName = xmlParser.getName();

					if (MESSAGE.equals(tagName)) {
						throw new Exception(xmlParser.nextText());
					} else if (LINES.equals(tagName)) {
						type = TYPE_IN_LINES;
						result = new BusLineResult();
						result.setCity(city);
						result.setLineName(lineName);
					} else {
						switch (type) {
						case TYPE_IN_LINES:
							if (LINE.equals(tagName)) {
								type = TYPE_IN_LINE;
								line = new BusLine();
							}
							break;
						case TYPE_IN_LINE:
							if (NAME.equals(tagName)) {
								line.setCity(city);
								line.setName(xmlParser.nextText());
							} else if (INFO.equals(tagName)) {
								line.setLineInfo(xmlParser.nextText());
							} else if (STATS.equals(tagName)) {
								line.setStationNames(xmlParser.nextText()
										.split(";"));
							}
							break;
						}
					}
					break;
				case XmlPullParser.END_TAG:
					tagName = xmlParser.getName();

					if (LINE.equals(tagName)) {
						result.getLineList().add(line);
						line = null;

						type = TYPE_IN_LINES;
					}
					break;
				}
			}

			Log.d(TAG, JSON.toJSONString(result));
			return result;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 获取公交省份地区列表
	 * 
	 * @param context
	 * @return
	 */
	public static List<BusProvince> getBusProvinceList(Context context) {
		List<BusProvince> busProvinceList = new ArrayList<BusProvince>();

		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(context.getAssets()
					.open("bus_city.data", Context.MODE_WORLD_READABLE)));
			String line = null;
			BusProvince province = null;
			Pattern p = Pattern.compile("[\\u4E00-\\u9FA5]+");
			while ((line = in.readLine()) != null) {
				Matcher m = p.matcher(line);

				boolean first = true;
				while (m.find()) {
					if (first) {
						// 文件中的第一个匹配到的是省份名称
						province = new BusProvince();
						province.setProvince(m.group());
						first = false;
					} else {
						province.addCity(m.group());
					}
				}

				if (province != null) {
					busProvinceList.add(province);
					province = null;
				}
			}

			for (BusProvince busProvince : busProvinceList) {
				if (busProvince.getCityList() == null) {
					busProvince.addCity(busProvince.getProvince());
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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

		return busProvinceList;
	}

	private static HttpHost getProxy() {
		ConnectivityManager cm = (ConnectivityManager) AssistantApp
				.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
		HttpHost none_host = null;
		if (cm == null) {
			return none_host;
		}

		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			return none_host;
		}

		if (ni.getType() == ConnectivityManager.TYPE_WIFI) {
			return null;
		} else if (ni.getType() == ConnectivityManager.TYPE_MOBILE) {
			String extra = ni.getExtraInfo();
			if (TextUtils.isEmpty(extra)) {
				return none_host;
			}

			extra = extra.toLowerCase();
			if (extra.contains("cmnet") || extra.contains("ctnet")
					|| extra.contains("uninet") || extra.contains("3gnet")) {
				return none_host;
			} else if (extra.contains("cmwap") || extra.contains("uniwap")
					|| extra.contains("3gwap")) {
				return new HttpHost("10.0.0.172");
			} else if (extra.contains("ctwap")) {
				return new HttpHost("10.0.0.200");
			}
		}

		return none_host;
	}

	private static final String MESSAGE = "message";
	private static final String LINES = "lines";
	private static final String LINE = "line";
	private static final String NAME = "name";
	private static final String INFO = "info";
	private static final String STATS = "stats";

	private static final String BUSES = "buses";
	private static final String BUS = "bus";
	private static final String DIST = "dist";
	private static final String TIME = "time";
	private static final String FOOT_DIST = "foot_dist";
	private static final String SEGMENTS = "segments";
	private static final String SEGMENT = "segment";
	private static final String START_STATS = "start_stat";
	private static final String END_STATS = "end_stat";
	private static final String LINE_NAME = "line_name";
	private static final String LINE_DIST = "line_dist";

	private static final String STAT = "stat";
	private static final String XY = "xy";
	private static final String LINE_NAMES = "line_names";

	/** 未知类型 */
	private static final int TYPE_UNKNOWN = -1;
	/** 线路查询结果，表示处于线路结果集中 */
	private static final int TYPE_IN_LINES = 1;
	/** 线路查询结果， 表示处于一个线路信息中 */
	private static final int TYPE_IN_LINE = 2;
	/** 换乘查询结果， 表示处于换乘结果集中 */
	private static final int TYPE_IN_BUSES = 3;
	/** 换乘查询结果， 表示处于一个换乘信息中 */
	private static final int TYPE_IN_BUS = 4;
	private static final int TYPE_IN_SEGMENTS = 5;
	private static final int TYPE_IN_SEGMENT = 6;
	private static final int TYPE_IN_STATS = 7;
	private static final int TYPE_IN_STAT = 8;

	private static final int MSG_BUS_UNCOMPLETED = 0x0006;
	private static final int MSG_BUS_CANCELED = 0x0007;

	private static class BusRequestAction {
		/** 换乘 */
		public static final int ACTION_TRANSFER = 0x0001;
		/** 线路 */
		public static final int ACTION_LINE = 0x0002;
		/** 站点 */
		public static final int ACTION_STATION = 0x0003;
		/** 周边站点 */
		// public static final int ACTION_STATION_AROUND = 0x0004;
	}

	private static class BusHandler extends Handler {
		private WeakReference<BusApi> busRef;
		private BusListener busListener;

		public BusHandler(BusApi bus) {
			busRef = new WeakReference<BusApi>(bus);
		}

		public void addBusListener(BusListener listener) {
			busListener = listener;
		}

		@Override
		public void handleMessage(Message msg) {
			if (busRef != null && busRef.get() != null) {
				busRef.get().mProgressDialog.dismiss();
			}

			switch (msg.what) {
			case BusRequestAction.ACTION_LINE:
				busListener.onLine((BusLineResult) msg.obj);
				break;
			case BusRequestAction.ACTION_STATION:
				busListener.onStation((BusStationResult) msg.obj);
				break;
			case BusRequestAction.ACTION_TRANSFER:
				busListener.onTransfer((BusTransferResult) msg.obj);
				break;
			case MSG_BUS_UNCOMPLETED:
				changeAiBangApi();
				String errorMessage = (String) msg.obj;
				busListener.onError(TextUtils.isEmpty(errorMessage) ? "查询失败!"
						: errorMessage);
				break;
			case MSG_BUS_CANCELED:
				break;
			}
		}
	}
}
