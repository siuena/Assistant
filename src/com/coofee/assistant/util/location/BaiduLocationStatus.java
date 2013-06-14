package com.coofee.assistant.util.location;

/**
 * 保存从Baidu查询位置信息返回的查询状态信息.
 * @author zhao
 * 
 */
public class BaiduLocationStatus {
	private String time;
	/** 当error等于161时表示请求成功. **/
	private int error;

	public BaiduLocationStatus() {
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

}
