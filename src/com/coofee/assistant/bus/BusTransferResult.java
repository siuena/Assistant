package com.coofee.assistant.bus;

import java.util.ArrayList;
import java.util.List;

public class BusTransferResult {
	private String city;
	private String startStation;
	private String endStation;

	/** 换乘类型 */
	private int transferType = TransferType.TYPE_DEFAULT;
	private List<BusTransferPlan> transferPlanList = new ArrayList<BusTransferPlan>();

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStartStation() {
		return startStation;
	}

	public void setStartStation(String startStation) {
		this.startStation = startStation;
	}

	public String getEndStation() {
		return endStation;
	}

	public void setEndStation(String endStation) {
		this.endStation = endStation;
	}

	public int getTransferType() {
		return transferType;
	}

	public void setTransferType(int transferType) {
		this.transferType = transferType;
	}

	public List<BusTransferPlan> getTransferPlanList() {
		return transferPlanList;
	}

	public void setTransferPlanList(List<BusTransferPlan> transferPlanList) {
		this.transferPlanList = transferPlanList;
	}

	/**
	 * 换乘方式的排序类型
	 * 
	 * @author zhao
	 * 
	 */
	public static class TransferType {
		/** 综合排序 */
		public static final int TYPE_DEFAULT = 0;
		/** 最少换乘 */
		public static final int TYPE_LEAST_TRANSFER_TIMES = 1;
		/** 步行最短 */
		public static final int TYPE_WALK_DISTANCE_SHORTEST = 2;
		/** 时间最短 */
		public static final int TYPE_TIME_SHORTEST = 3;
		/** 距离最短 */
		public static final int TYPE_DISTANCE_SHORTEST = 4;
		/** 地铁优先 */
		public static final int TYPE_SUBWAY_PRIORITY = 5;
	}
}
