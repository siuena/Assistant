package com.coofee.assistant.weather;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.coofee.assistant.R;
import com.coofee.assistant.util.DimenUtil;
import com.coofee.assistant.util.UiUtil;
import com.coofee.assistant.weather.model.WeatherHistory;
import com.coofee.assistant.weather.model.WeatherInfo;

public class WeatherHistoryView extends View {
	private static final String TAG = WeatherHistoryView.class.getSimpleName();
	
	private final float ANDROID_SYSTEM_TITLE_HIEGHT;
	private final float MARGIN;
	private final DisplayMetrics SCREEN_INFO;

	// 如果有7天的历史天气则显示7天的历史天气; 如果历史天气少于7天, 则有几天就显示几天的天气信息.
	private List<WeatherHistory> mWeatherHistoryList;
	private float mHighestTemp;
	private boolean mIsHighestTempSetted = false;
	private float mLowestTemp;
	private boolean mIsLowestTempSetted = false;
	private float mPiexlsPerTemp;
	private float mPiexlsDayInterval;

	private float mTopY;
	private float mRightX;
	private float mBottomY;
	private float mLeftX;

	private Paint mPaint;

	public WeatherHistoryView(Context context, AttributeSet attrs) {
		super(context, attrs);

		ANDROID_SYSTEM_TITLE_HIEGHT = DimenUtil.getDimension(context,
				R.dimen.android_system_title_height);
		MARGIN = DimenUtil
				.getDimension(context, R.dimen.weather_history_margin);
		SCREEN_INFO = UiUtil.getScreenInfo();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStrokeWidth(1);
		mPaint.setStyle(Style.STROKE);
		mPaint.setColor(Color.GREEN);

		drawXY(canvas);
		drawData(canvas);
	}

	public void init(WeatherInfo weatherInfo) {
		init(WeatherHelper.toWeatherHistory(weatherInfo));
	}

	public void init(List<WeatherHistory> weatherHistoryList) {
		if (weatherHistoryList == null || weatherHistoryList.isEmpty()) {
			Log.d(TAG, "weather history list is null or empty.");
			return;
		}

		for (WeatherHistory history : weatherHistoryList) {
			if (!mIsHighestTempSetted) {
				mHighestTemp = history.getDayTemperature();
				mIsHighestTempSetted = true;
			}else if (mHighestTemp < history.getDayTemperature()) {
				mHighestTemp = history.getDayTemperature();
				mIsHighestTempSetted = true;
			}

			if (!mIsLowestTempSetted) {
				mLowestTemp = history.getNightTemperature();
				mIsLowestTempSetted = true;
			} else if (mLowestTemp > history.getNightTemperature()) {
				mLowestTemp = history.getNightTemperature();
				mIsLowestTempSetted = true;
			}
		}

		this.mWeatherHistoryList = weatherHistoryList;

		mTopY = MARGIN + ANDROID_SYSTEM_TITLE_HIEGHT;
		mBottomY = SCREEN_INFO.heightPixels - mTopY
				- ANDROID_SYSTEM_TITLE_HIEGHT;
		mLeftX = MARGIN;
		mRightX = SCREEN_INFO.widthPixels - MARGIN;

		Log.d(TAG, "top: " + mTopY + ", right: " + mRightX + ", bottom: "
				+ mBottomY + ", left: " + mLeftX);
	}

	/**
	 * 画X/Y轴
	 */
	private void drawXY(Canvas canvas) {
		Path xyPath = new Path();
		// 画Y轴箭头
		xyPath.moveTo(mLeftX - 5, mTopY - 10 + 5);
		xyPath.lineTo(mLeftX, mTopY - 10);
		xyPath.lineTo(mLeftX + 5, mTopY - 10 + 5);

		// 画X轴和Y轴
		// 这里减少10, 是为了让Y轴多画10个像素.
		xyPath.moveTo(mLeftX, mTopY - 10);
		xyPath.lineTo(mLeftX, mBottomY);
		// 这里增加10, 是为了让X轴多画10个像素.
		xyPath.lineTo(mRightX + 30, mBottomY);

		// 画X轴箭头
		xyPath.moveTo(mRightX + 30 - 5, mBottomY - 5);
		xyPath.lineTo(mRightX + 30, mBottomY);
		xyPath.lineTo(mRightX + 30 - 5, mBottomY + 5);

		canvas.drawPath(xyPath, mPaint);
	}

	private void drawData(Canvas canvas) {
		if (mWeatherHistoryList != null && !mWeatherHistoryList.isEmpty()) {

			int historyListSize = mWeatherHistoryList.size();
			// 计算两天天气之间的间隔
			mPiexlsDayInterval = historyListSize > 1 ? (mRightX - mLeftX)
					/ (mWeatherHistoryList.size() - 1) : 0;
					
			// 1度代表多少像素
			mPiexlsPerTemp = (mHighestTemp != mLowestTemp) ? (mBottomY - mTopY) / (mHighestTemp - mLowestTemp) : 0;
			Log.d(TAG, "mHighestTemp: " + mHighestTemp + ", mLowestTemp: "
					+ mLowestTemp + ", piexlsPerTemp: " + mPiexlsPerTemp);
			Log.d(TAG, "xInterval: " + mPiexlsDayInterval + ", yInterval: "
					+ mPiexlsPerTemp);

			/**
			 * x: 某一天温度的横坐标 highY: 某一天最高温度的纵坐标 lowY: 某一天最低温度的纵坐标
			 */
			float x, highY, lowY;
			final int valueSize = mWeatherHistoryList.size();
			WeatherHistory currentHistoryValue = null;
			mPaint.setStyle(Style.FILL);

			Path highTempPath = new Path();
			Path lowTempPath = new Path();
			TextPaint textPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG
					| TextPaint.FAKE_BOLD_TEXT_FLAG);
			textPaint.setColor(Color.GREEN);
			textPaint.setTextAlign(Align.CENTER);
			textPaint.setTextSize(getResources().getDimension(
					R.dimen.weather_history_text_size));
			final float radius = getResources().getDimension(
					R.dimen.weather_history_cycle_radius);

			for (int i = 0; i < valueSize; i++) {
				// 当前温度的X坐标
				x = mLeftX + i * mPiexlsDayInterval;

				currentHistoryValue = mWeatherHistoryList.get(i);

				// 当天最高温度的Y坐标
				highY = mTopY
						+ (mHighestTemp - currentHistoryValue
								.getDayTemperature()) * mPiexlsPerTemp;
				// path.addCircle(pointX, pointY, 5, Path.Direction.CW);
				canvas.drawCircle(x, highY, radius, mPaint);
				if (mHighestTemp - currentHistoryValue.getDayTemperature() < 0.1) {
					// 如果是最高温度, 则显示在右下方
					canvas.drawText(currentHistoryValue.getDayTemperature()
							+ "℃", x + radius * 2, highY + radius * 4,
							textPaint);
				} else {
					// 文字显示在文字的上方.
					canvas.drawText(currentHistoryValue.getDayTemperature()
							+ "℃", x + radius, highY - radius * 2, textPaint);
				}

				Log.d(TAG, "#" + i + ", mHighestTemp(x=" + x + ", y=" + highY
						+ ")");

				// 当天最低温度的Y坐标
				lowY = mTopY
						+ (mHighestTemp - currentHistoryValue
								.getNightTemperature()) * mPiexlsPerTemp;
				canvas.drawCircle(x, lowY, radius, mPaint);
				if (currentHistoryValue.getNightTemperature() - mLowestTemp < 0.1) {
					// 如果是最低温度, 则文字写的稍微向上一些.
					canvas.drawText(currentHistoryValue.getNightTemperature()
							+ "℃", x + radius, lowY - radius * 2, textPaint);
				} else {
					// 文字显示在温度下方.
					canvas.drawText(currentHistoryValue.getNightTemperature()
							+ "℃", x + radius, lowY + radius * 4, textPaint);
				}

				Log.d(TAG, "#" + i + ", mLowestTemp(x=" + x + ", y=" + lowY
						+ ")");

				if (i == 0) {
					highTempPath.moveTo(x, highY);
					lowTempPath.moveTo(x, lowY);
					canvas.drawText(WeatherHelper
							.getDayShort(currentHistoryValue.getDate()), x,
							mBottomY + 20 + textPaint.getTextSize(), textPaint);
					canvas.drawText(currentHistoryValue.getDayStatus(), x,
							mTopY - 15, textPaint);
					canvas.drawText(currentHistoryValue.getNightStatus(), x,
							mBottomY + 20, textPaint);
				} else {
					highTempPath.lineTo(x, highY);
					lowTempPath.lineTo(x, lowY);
					canvas.drawText(WeatherHelper
							.getDayShort(currentHistoryValue.getDate()), x,
							mBottomY + 20 + textPaint.getTextSize(), textPaint);
					canvas.drawText(currentHistoryValue.getDayStatus(), x,
							mTopY - 15, textPaint);
					canvas.drawText(currentHistoryValue.getNightStatus(), x,
							mBottomY + 20, textPaint);
				}
			}

			mPaint.setStyle(Style.STROKE);
			mPaint.setAntiAlias(true);
			canvas.drawPath(highTempPath, mPaint);
			canvas.drawPath(lowTempPath, mPaint);
		}
	}
}