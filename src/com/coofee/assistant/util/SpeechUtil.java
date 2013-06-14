package com.coofee.assistant.util;

import android.content.Context;

import com.iflytek.speech.SynthesizerPlayer;
import com.iflytek.speech.SynthesizerPlayerListener;

public class SpeechUtil {
	private static SynthesizerPlayer mSpeechPlayer;
	public static void speech(Context context, final String input, SynthesizerPlayerListener listener) {
		getSpeechPlayer(context).playText(input, null, listener);
	}
	
	private static synchronized SynthesizerPlayer getSpeechPlayer(Context context) {
		if (mSpeechPlayer == null) {
			mSpeechPlayer = SynthesizerPlayer.createSynthesizerPlayer(context, "appid=508f9853");
		}
		
		// 设置语速
		mSpeechPlayer.setSpeed(40);
		// 设置声音音量
		mSpeechPlayer.setVolume(50);
		// 设置说话人(xiaoyan: 青年女声, 中英文).
		mSpeechPlayer.setVoiceName("xiaoyan");
		
		return mSpeechPlayer;
	}
}
