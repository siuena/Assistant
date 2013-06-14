package com.coofee.assistant.book.model;

/**
 * 书籍的总评
 */
public class Rating {
	/**
	 * 最大等级
	 */
	private int max;
	/**
	 * 最低等级
	 */
	private int min;
	/**
	 * 评价的总人数
	 */
	private int numRaters;
	/**
	 * 平均分
	 */
	private float average;

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getNumRaters() {
		return numRaters;
	}

	public void setNumRaters(int numRaters) {
		this.numRaters = numRaters;
	}

	public float getAverage() {
		return average;
	}

	public void setAverage(float average) {
		this.average = average;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer info = new StringBuffer();
		info.append("{max: ").append(max)
			.append("; min: ").append(min)
			.append("; average").append(average)
			.append("; numRaters: ").append(numRaters)
			.append("}");
		return info.toString();
	}

}
