package com.coofee.assistant.util.location;


/**
 * { "result":{"time":"2011-08-28 17:29:45","error":"161"}, "content":{
 * "point":{"x":"12947422.359722","y":"4846434.0501397"}, "radius":"130",
 * "addr":{"detail":"北京市海淀区上地九街"} } }
 * 从baidu返回的位置信息模型.
 * @author zhao
 *
 */
public class BaiduLocationModel {
	/** 本次查询的状态 **/
	private BaiduLocationStatus result;
	/** 查询到的结果详细信息 **/
	private BaiduLocationData content;

	public BaiduLocationModel() {
	}

	public BaiduLocationStatus getResult() {
		return result;
	}

	public void setResult(BaiduLocationStatus result) {
		this.result = result;
	}

	public BaiduLocationData getContent() {
		return content;
	}

	public void setContent(BaiduLocationData content) {
		this.content = content;
	}

}
