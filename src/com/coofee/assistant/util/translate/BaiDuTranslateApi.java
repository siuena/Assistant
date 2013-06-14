package com.coofee.assistant.util.translate;

public class BaiDuTranslateApi {
	public static final String APP_ID =  "mVtllh8GuCISWBn8HrOh9orn";
	public static final String SECRET_KEY = "HYLwYyRbEYMjZp2N08LjVKzuSz0hn752";
	/** 请求翻译的字符串的最大长度限制 */
	public static final int MAX_CHARACTER_LENGTH_OF_QUERY = 5000;
	/**
	 *	翻译API中包含的from和to字段目前仅限以下几种组合。百度翻译将不断增加对其它语言类型的翻译支持，以满足用户的多方面需求。

		from字段 	to字段 		翻译方向
		auto	 		auto	 		自动识别
		zh	  		en		 	中 -> 英
		zh	  		jp		 	中 -> 日
		en	  		zh		 	英 -> 中
		jp	  		zh		 	日 -> 中
		说明
		1.所有参数及其值均为小写字母
		2.所有上表中未列明的方向组合均会被重置为from=auto&to=auto
		3.自动识别的规则如下：
		源语言识别为中文，则翻译方向为“中 -> 英”
		源语言识别为英文，则翻译方向为“英 -> 中”
		源语言识别为日文，则翻译方向为“日 -> 中”
		
		GET请求方式样例：
		http://openapi.baidu.com/public/2.0/bmt/translate?client_id=YourApiKey&q=today&from=auto&to=auto
		
		您可以通过GET或者POST方式提交待翻译的内容，GET方式最大支持2k字符的请求，POST方式最大支持5k字符的请求，超出相应长度的请求可能被截断或无法得到正确的结果，返回的翻译结果则为标准JSON格式。
	 *
	 */
	
	/** 百度翻译API请求URL */
	public static final String TRANSLATE_URL = "http://openapi.baidu.com/public/2.0/bmt/translate";
	/** 其值是app_id */
	public static final String CLIENT_ID = "client_id";
	public static final String QUERY = "q";
	
	// 百度翻译API请求响应参数
	public static final String FROM = "from";
	public static final String TO = "to";

	
	/** 响应参数 */
	public static final String TRANSLATE_RESULT = "translate_result";
	public static final String SRC = "src";
	public static final String DST = "dst";

	/** 错误响应参数 */
	public static final String ERROR_CODE = "error_code";
	public static final String ERROR_MSG = "error_msg";
}
