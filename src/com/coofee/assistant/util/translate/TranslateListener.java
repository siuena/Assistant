package com.coofee.assistant.util.translate;

/**
 * 翻译监听器; 用于监听翻译的结果.
 * @author zhaocongying
 *
 */
public interface TranslateListener {
	/** 翻译结果错误监听事件 */
	public void onTranslateError(TranslateErrorResponse errorResponse);
	/** 翻译结果成功监听事件 */
	public void onTranslateSuccess(TranslateSuccessResponse successResponse);
	/** 翻译结果未知异常 */
	public void onError(String errorMessage);
}
