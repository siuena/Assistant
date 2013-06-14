package com.coofee.assistant.bus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.coofee.assistant.provider.AssistantSQLiteHelper;

public final class Bus {
	public static final String BUS_PREF_NAME = "bus";
	public static final String EXTRA_USER_CURRENT_CITY = "userCurrentCity";
	public static final String EXTRA_BUS_LINE_JSON = "bus_line_json";
	public static final String EXTRA_BUS_LINE_NAME = "bus_line_name";

	public static final String EXTRA_BUS_STATION_JSON = "bus_station_json";
	public static final String EXTRA_BUS_STATION_NAME = "bus_station_name";

	public static final String EXTRA_BUS_TRANSFER_JSON = "bus_transfer_json";
	public static final String EXTRA_BUS_FROM = "start_station";
	public static final String EXTRA_BUS_TO = "end_station";
	public static final String EXTRA_BUS_TRANSFER_TYPE = "transfer_type";
	public static final String EXTRA_BUS_CITY_NAME = "city_name";
	
	public static final int REQUEST_SWITCH_CITY = 0x0001;
	
	public interface OnTransferTypeChangedListener {
		public void onTransferTypeChange(int transferType);
	}

	/**
	 * 打开选择换乘类型对话框
	 * 
	 * @param context
	 * @param listener
	 */
	public static void openChooseTransferTypeDialog(Context context,
			final OnTransferTypeChangedListener listener) {
		new AlertDialog.Builder(context)
				.setTitle("设置换乘查询")
				.setItems(
						new String[] { "综合排序 ", "最少换乘 ", "步行最短 ", "时间最短 ",
								"距离最短 ", "地铁优先 " },
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									listener.onTransferTypeChange(BusTransferResult.TransferType.TYPE_DEFAULT);
									break;
								case 1:
									listener.onTransferTypeChange(BusTransferResult.TransferType.TYPE_LEAST_TRANSFER_TIMES);
									break;
								case 2:
									listener.onTransferTypeChange(BusTransferResult.TransferType.TYPE_WALK_DISTANCE_SHORTEST);
									break;
								case 3:
									listener.onTransferTypeChange(BusTransferResult.TransferType.TYPE_TIME_SHORTEST);
									break;
								case 4:
									listener.onTransferTypeChange(BusTransferResult.TransferType.TYPE_DISTANCE_SHORTEST);
									break;
								case 5:
									listener.onTransferTypeChange(BusTransferResult.TransferType.TYPE_SUBWAY_PRIORITY);
									break;
								}
								dialog.dismiss();
							}
						}).create().show();
	}

	public interface OnDeleteBusHistoryListener {
		public void onDeleteCompleted();

		public void onDeleteCanceld();
	}

	public static void openDeleteBusHistoryDialog(final Context context,
			final OnDeleteBusHistoryListener listener) {
		new AlertDialog.Builder(context)
				.setTitle("清除缓存")
				.setItems(
						new String[] { "清空线路信息", "清空站点信息", "清空换乘信息", "清空全部信息" },
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();

								try {
									switch (which) {
									case 0:
										AssistantSQLiteHelper
												.deleteBusLineHistory(context);
										break;
									case 1:
										AssistantSQLiteHelper
												.deleteBusStationHistory(context);
										break;
									case 2:
										AssistantSQLiteHelper
												.deleteBusTransferHistory(context);
										break;
									case 3:
										AssistantSQLiteHelper
												.deleteAllBusHistory(context);
										break;
									}
								} finally {
									listener.onDeleteCompleted();
								}
							}
						})
				.setOnCancelListener(new DialogInterface.OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						listener.onDeleteCanceld();
					}
				}).create().show();
	}
}
