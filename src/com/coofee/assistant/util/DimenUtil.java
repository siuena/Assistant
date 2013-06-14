package com.coofee.assistant.util;

import android.content.Context;

public class DimenUtil {

	/**
	 * 获取资源文件中定义的尺寸.
	 * @param context
	 * @param dimensionId
	 * @return
	 */
	public static final float getDimension(Context context, int dimensionId) {
		return context.getResources().getDimension(dimensionId);
	}
}
