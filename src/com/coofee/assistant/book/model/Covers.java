package com.coofee.assistant.book.model;

import android.text.TextUtils;

/**
 * 一个书籍不同大小封面的地址
 * @author zhao
 *
 */
public class Covers {

	private String small;
	private String medium;
	private String large;
	
	public String getSmall() {
		return small;
	}

	public void setSmall(String small) {
		if (!TextUtils.isEmpty(small)) {
			this.small = small.replace("\\", "");
			return; 
		}
		this.small = small;
	}

	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		if (!TextUtils.isEmpty(medium)) {
			this.medium = medium.replace("\\", "");
			return; 
		}
		this.medium = medium;
	}

	public String getLarge() {
		return large;
	}

	public void setLarge(String large) {
		if (!TextUtils.isEmpty(large)) {
			this.large = large.replace("\\", "");
			return; 
		}
		this.large = large;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer info = new StringBuffer();
		info.append("{small: ").append(small)
		.append("; medium: ").append(medium)
		.append("; large:").append(large).append("}");
		return info.toString();
	}
}
