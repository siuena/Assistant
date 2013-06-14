package com.coofee.assistant.util.translate;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.alibaba.fastjson.JSON;

public class TranslateUtil {
	private static final String TAG = TranslateUtil.class.getSimpleName();
	private static final int TIME_OUT = 200 * 1000;
	private static final String ENGLISH_SAMPLE_FILE_PATH = "G:/eclipse-sdk-4-2-workspace/BaiduTranslator/english_sample/english_sample.txt";

	/**
	 * 英语翻译为汉语
	 * 
	 * @param query
	 */
	public static String translateEnToZh(final String query) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(BaiDuTranslateApi.FROM, "en");
		params.put(BaiDuTranslateApi.TO, "zh");
		try {
			params.put(BaiDuTranslateApi.QUERY,
					URLEncoder.encode(query, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		String result = getResultByGet(params);
		Log.d(TAG, "result: " + result);
		return result;
	}
	
	/**
	 * 从汉语翻译为英语
	 * 
	 * @param query
	 */
	public static String translateZhToEn(final String query) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(BaiDuTranslateApi.FROM, "zh");
		params.put(BaiDuTranslateApi.TO, "en");
		try {
			params.put(BaiDuTranslateApi.QUERY,
					URLEncoder.encode(query, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}


		String result = getResultByGet(params);
		Log.d(TAG, "result: " + result);
		return result;
	}
	
	/**
	 * 英语翻译为汉语
	 * 
	 * @param query
	 */
	private static void translateEnToZh(final String query,
			TranslateListener translateListener) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(BaiDuTranslateApi.FROM, "en");
		params.put(BaiDuTranslateApi.TO, "zh");
		try {
			params.put(BaiDuTranslateApi.QUERY,
					URLEncoder.encode(query, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		translateByGet(params, translateListener);
	}

	/**
	 * 从汉语翻译为英语
	 * 
	 * @param query
	 */
	private static void translateZhToEn(final String query,
			TranslateListener translateListener) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(BaiDuTranslateApi.FROM, "zh");
		params.put(BaiDuTranslateApi.TO, "en");
		try {
			params.put(BaiDuTranslateApi.QUERY,
					URLEncoder.encode(query, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		translateByGet(params, translateListener);
	}

	public static void main(String[] args) {
		testEnToZh("How are you! \n fine, and you?");
		testZhToEn("你好, 请问你是谁啊? \n 我是谁?");
		testEnToZh("interest");

		String sampleContent = getSampleEnglishFileContent();
		if (sampleContent.length() > BaiDuTranslateApi.MAX_CHARACTER_LENGTH_OF_QUERY) {
			final int count = sampleContent.length()
					/ BaiDuTranslateApi.MAX_CHARACTER_LENGTH_OF_QUERY;
			System.out.println("need slice " + (count + 1) + " times");

			/**
			 * 上一个最后一个单词的位置.
			 */
			int previousLastWordIndex = -1, currentLastWordIndex = -1;
			for (int i = 0; i < count; i++) {
				System.out.println("request " + i + " time.");
				if (i == 0 && previousLastWordIndex == -1) {
					previousLastWordIndex = 0;
				}

				currentLastWordIndex = sampleContent
						.lastIndexOf(
								' ',
								((i + 1) * BaiDuTranslateApi.MAX_CHARACTER_LENGTH_OF_QUERY));
				if (currentLastWordIndex == -1) {
					currentLastWordIndex = sampleContent
							.lastIndexOf(
									'\n',
									((i + 1) * BaiDuTranslateApi.MAX_CHARACTER_LENGTH_OF_QUERY));
				}

				System.out.println("previous last word index is "
						+ previousLastWordIndex
						+ "; current last word index is "
						+ currentLastWordIndex);
				testEnToZh(sampleContent.substring(previousLastWordIndex,
						currentLastWordIndex));

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				previousLastWordIndex = currentLastWordIndex;
			}

			testEnToZh(sampleContent.substring(count
					* BaiDuTranslateApi.MAX_CHARACTER_LENGTH_OF_QUERY));
			System.out.println("request finished.");
		}
	}

	private static String getSampleEnglishFileContent() {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(
					ENGLISH_SAMPLE_FILE_PATH)));

			StringBuilder content = new StringBuilder();
			String line = null;
			while ((line = in.readLine()) != null) {
				content.append(line);
			}

			content.trimToSize();
			System.out.println("sample content length: " + content.length());
			return content.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	private static void testZhToEn(final String query) {
		translateZhToEn(query, new TranslateListener() {

			@Override
			public void onTranslateSuccess(
					TranslateSuccessResponse successResponse) {
				System.out.println("execute success. "
						+ JSON.toJSONString(successResponse));
			}

			@Override
			public void onTranslateError(TranslateErrorResponse errorResponse) {
				System.out.println("execute error."
						+ JSON.toJSONString(errorResponse));
			}

			@Override
			public void onError(String errorMessage) {
				System.out.println("execute error: " + errorMessage);
			}
		});
	}

	private static void testEnToZh(final String query) {
		translateEnToZh(query, new TranslateListener() {

			@Override
			public void onTranslateSuccess(
					TranslateSuccessResponse successResponse) {
				System.out.println("execute success. "
						+ JSON.toJSONString(successResponse));
			}

			@Override
			public void onTranslateError(TranslateErrorResponse errorResponse) {
				System.out.println("execute error."
						+ JSON.toJSONString(errorResponse));
			}

			@Override
			public void onError(String errorMessage) {
				System.out.println("execute error: " + errorMessage);
			}
		});
	}

	/**
	 * 翻译
	 * 
	 * @param params
	 */
	private static void translateByGet(Map<String, String> params,
			TranslateListener translateListener) {
		String result = getResultByGet(params);
		System.out.println("result: " + result);
		executeTranslateResponse(result, translateListener);
	}

	/**
	 * 翻译
	 * 
	 * @param params
	 */
//	private static void translateByPost(Map<String, String> params,
//			TranslateListener translateListener) {
//		String result = getResultByGet(params);
//		System.out.println("result: " + result);
//		executeTranslateResponse(result, translateListener);
//	}

	private static String getResultByGet(Map<String, String> params) {
		if (params == null)
			return null;

		StringBuilder translateUrl = new StringBuilder(
				BaiDuTranslateApi.TRANSLATE_URL);
		translateUrl.append("?");
		params.put(BaiDuTranslateApi.CLIENT_ID, BaiDuTranslateApi.APP_ID);
		for (Map.Entry<String, String> param : params.entrySet()) {
			translateUrl.append(param.getKey()).append("=")
					.append(param.getValue()).append("&");
		}

		translateUrl.trimToSize();

		System.out.println("request url: " + translateUrl);

		HttpURLConnection conn = null;
		BufferedReader in = null;
		try {
			conn = (HttpURLConnection) (new URL(translateUrl.toString()))
					.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(TIME_OUT);
			conn.setReadTimeout(TIME_OUT);
			conn.setDefaultUseCaches(false);
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-type", "text/json; charset=utf-8");

			final int responseCode = conn.getResponseCode();
			System.out.println("response code: " + responseCode);
			if (responseCode == 200) {
				StringBuilder responseContent = new StringBuilder();
				String line = null;
				in = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				while ((line = in.readLine()) != null) {
					responseContent.append(line);
				}

				responseContent.trimToSize();
				return responseContent.toString();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("release resource in finally.");

			if (conn != null) {
				conn.disconnect();
				conn = null;
			}

			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {

					System.out.println("close in stream slient.");
					e.printStackTrace();
				}
				in = null;
			}
		}

		return null;
	}

	// public static final String boundary = "----com.coofee.translate";

	// public static String getResultByPost(Map<String, String> params) {
	// if (params == null)
	// return null;
	//
	// StringBuilder requestContent = new StringBuilder();
	//
	// params.put(BaiDuApi.CLIENT_ID, BaiDuApi.APP_ID);
	// for (Map.Entry<String, String> param : params.entrySet()) {
	// requestContent.append("Content-Disposition: form-data; name=\"")
	// .append(param.getKey()).append("\"\n\n")
	// .append(param.getValue()).append("\n").append(boundary)
	// .append("\n");
	// }
	//
	// if (requestContent.length() > 1
	// && requestContent.charAt(requestContent.length() - 1) == '\n') {
	// requestContent.deleteCharAt(requestContent.length() - 1);
	// requestContent.append("--");
	// }
	// requestContent.trimToSize();
	//
	// System.out.println("request content: \n" + requestContent);
	//
	// HttpURLConnection conn = null;
	// BufferedReader in = null;
	// try {
	// conn = (HttpURLConnection) (new URL(BaiDuApi.TRANSLATE_URL))
	// .openConnection();
	// conn.setRequestMethod("POST");
	// conn.setConnectTimeout(TIME_OUT);
	// conn.setReadTimeout(TIME_OUT);
	// conn.setDefaultUseCaches(false);
	// conn.setDoInput(true);
	// conn.setDoOutput(true);
	// conn.setRequestProperty("Connection", "keep-alive");
	// conn.setRequestProperty("Content-Type",
	// "application/form-data; boundary=" + boundary);
	//
	// conn.getOutputStream().write(
	// requestContent.toString().getBytes("utf-8"));
	// conn.getOutputStream().flush();
	// conn.getOutputStream().close();
	//
	//
	// final int responseCode = conn.getResponseCode();
	// if (responseCode == 200) {
	// StringBuilder responseContent = new StringBuilder();
	// String line = null;
	// in = new BufferedReader(new InputStreamReader(
	// conn.getInputStream()));
	// while ((line = in.readLine()) != null) {
	// responseContent.append(line);
	// }
	//
	// responseContent.trimToSize();
	// return responseContent.toString();
	// }
	// } catch (MalformedURLException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } finally {
	// System.out.println("release resource in finally.");
	//
	// if (conn != null) {
	// conn.disconnect();
	// conn = null;
	// }
	//
	// if (in != null) {
	// try {
	// in.close();
	// } catch (IOException e) {
	//
	// System.out.println("close in stream slient.");
	// e.printStackTrace();
	// }
	// in = null;
	// }
	// }
	//
	// return null;
	// }

	/**
	 * 处理翻译响应信息
	 * 
	 * @param result
	 * @param translateListener
	 */
	public static void executeTranslateResponse(String result,
			TranslateListener translateListener) {
		if (result == null || result.trim().length() == 0) {
			translateListener.onError("翻译失败!");
			return;
		}

		try {
			if (result.indexOf(BaiDuTranslateApi.ERROR_CODE) != -1) {
				// 说明返回的是错误信息
				TranslateErrorResponse errorResponse = JSON.parseObject(result,
						TranslateErrorResponse.class);
				translateListener.onTranslateError(errorResponse);

			} else {
				// 说明返回的是正确的信息
				TranslateSuccessResponse normalResposone = JSON.parseObject(
						result, TranslateSuccessResponse.class);
				translateListener.onTranslateSuccess(normalResposone);
			}
		} catch (Exception e) {
			e.printStackTrace();

			translateListener.onError("翻译失败!");
			e.printStackTrace();
		}
	}
}
