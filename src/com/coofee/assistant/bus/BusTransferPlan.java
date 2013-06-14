package com.coofee.assistant.bus;

import java.util.ArrayList;
import java.util.List;

public class BusTransferPlan {
	/** 总距离, 单位米 */
	private int distance;
	/** 总的步行距离, 单位米 */
	private int footDistance;
	/** 大约花费的时间, 单位分钟 */
	private int time;
	/** 换乘的段路 */
	private List<BusTransferSegment> transferSegments = new ArrayList<BusTransferSegment>();

	/** 换乘方案描述 */
	private String solutionDesc;

	public String solutionDesc() {
		if (solutionDesc == null && transferSegments != null) {
			StringBuilder sb = new StringBuilder();
			int segmentCount = transferSegments.size();
			BusTransferSegment preSegment = null, currentSegment = null;
			// 生成换乘方案.
			for (int i = 0; i < segmentCount; i++) {
				currentSegment = transferSegments.get(i);
				if (preSegment == null) {
					sb.append("从").append(currentSegment.getStartStation())
							.append("'上车,乘坐'")
							.append(currentSegment.getLineName()).append("'到'")
							.append(currentSegment.getEndStation())
							.append("'下车");
				} else {
					if (preSegment.getEndStation().equals(
							currentSegment.getStartStation())) {
						// 上一段的终点与当前段的起始点相同.
						sb.append("\n,");
					} else {
						// 上一段的终点与当前段的起始点不相同.
						sb.append("\n,步行至'").append(
								currentSegment.getStartStation());

					}

					sb.append("'换乘'").append(currentSegment.getLineName())
							.append("'到'")
							.append(currentSegment.getEndStation())
							.append("'下车");
				}

				preSegment = currentSegment;
			}

			sb.append("即到.\n总距离大约有").append(distance).append("米,大约需要步行")
					.append(footDistance).append("米, 共计大约耗时").append(time)
					.append("分钟.");

			sb.trimToSize();
			solutionDesc = sb.toString();
		}

		return solutionDesc;
	}

	public String shortSolutionDesc() {
		final int segmentCount = transferSegments.size();
		// 换乘次数,
		int transferCount = 0;
		if (segmentCount > 0) {
			transferCount = segmentCount - 1;
		}
		// 站点个数
		int stationCount = 0;
		BusTransferSegment preSegment = null, currentSegment = null;
		StringBuilder solutionDesc = new StringBuilder();
		// 生成换乘方案.
		for (int i = 0; i < segmentCount; i++) {
			currentSegment = transferSegments.get(i);
			if (currentSegment.getStationNames() != null) {
				stationCount += currentSegment.getStationNames().length;
			}

			if (preSegment == null) {
				solutionDesc.append(currentSegment.getStartStation())
						.append("->").append(currentSegment.getEndStation());
			} else {
				if (!preSegment.getEndStation().equals(
						currentSegment.getStartStation())) {
					solutionDesc.append("->").append(
							currentSegment.getStartStation());
				}

				solutionDesc.append("->")
						.append(currentSegment.getEndStation());
			}
		}

		solutionDesc
				.append("\n")
				.append(transferCount == 0 ? "直达  " : "共需要换乘" + transferCount
						+ "次  ").append("共" + stationCount + "站.");
		solutionDesc.trimToSize();
		return solutionDesc.toString();
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getFootDistance() {
		return footDistance;
	}

	public void setFootDistance(int footDistance) {
		this.footDistance = footDistance;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public List<BusTransferSegment> getTransferSegments() {
		return transferSegments;
	}

	public void setTransferSegments(List<BusTransferSegment> transferSegments) {
		this.transferSegments = transferSegments;
	}

	public static class BusTransferSegment {
		private String startStation;
		private String endStation;

		/** 乘坐线路 */
		private String lineName;
		/** 沿途站点名称 */
		private String[] stationNames;

		/** 行驶距离, 单位米 */
		private int lineDistance;
		/** 总的步行距离, 单位米 */
		private int footDistance;

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

		public String getLineName() {
			return lineName;
		}

		public void setLineName(String lineName) {
			this.lineName = lineName;
		}

		public int getLineDistance() {
			return lineDistance;
		}

		public void setLineDistance(int lineDistance) {
			this.lineDistance = lineDistance;
		}

		public String[] getStationNames() {
			return stationNames;
		}

		public void setStationNames(String[] stationNames) {
			this.stationNames = stationNames;
		}

		public int getFootDistance() {
			return footDistance;
		}

		public void setFootDistance(int footDistance) {
			this.footDistance = footDistance;
		}
	}
}
