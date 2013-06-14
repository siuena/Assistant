package com.coofee.assistant.util.translate;

public class TranslateSuccessResponse {

	private String from;
	private String to;
	private TranslateResult[] trans_result;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public TranslateResult[] getTrans_result() {
		return trans_result;
	}

	public void setTrans_result(TranslateResult[] trans_result) {
		this.trans_result = trans_result;
	}
}
