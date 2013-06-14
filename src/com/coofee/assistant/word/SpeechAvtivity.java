package com.coofee.assistant.word;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.coofee.assistant.R;
import com.coofee.assistant.util.SpeechUtil;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SynthesizerPlayerListener;

public class SpeechAvtivity extends Activity {
	public static final String EXTRA_SPEECH_TEXT = "speech_text";
	
	private final int PROGRESS_MAX = 100;
	
	/** 用于显示播放的进度, 其中progress显示的是播放的进度, secondProgress显示的是合成的进度 */
	private ProgressBar mSpeechProgress;
	private EditText mSpeechInput;
	private View mSpeech, mReady;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.speech_activity);

		initViews();
		setListeners();
		initData();
	}

	private void initViews() {
		mSpeechInput = (EditText) findViewById(R.id.speech_input);
		mSpeech = findViewById(R.id.speech_button);
		mReady = findViewById(R.id.speech_ready);
		mSpeechProgress = (ProgressBar) findViewById(R.id.speech_progress);
		mSpeechProgress.setMax(PROGRESS_MAX);
	}

	private void setListeners() {
		mSpeech.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(mSpeechInput.getText())) {
					mReady.setVisibility(View.VISIBLE);
					mSpeechProgress.setVisibility(View.GONE);
					String input = mSpeechInput.getText().toString();
					speech(input);
				}
			}
		});
	}

	private void initData() {
		Intent data = getIntent();
		if (data != null) {
			String speechText = data.getStringExtra(EXTRA_SPEECH_TEXT);
			
			if (!TextUtils.isEmpty(speechText)) {
				mSpeechInput.setText(speechText);
			}
		}
	}
	
	private void speech(final String input) {
		SpeechUtil.speech(SpeechAvtivity.this, input,
				new SynthesizerPlayerListener() {
					public void onPlayBegin() {
						// 播放开始回调，表示已获得第一块缓冲区音频开始播放
						mReady.setVisibility(View.GONE);
						mSpeechProgress.setVisibility(View.VISIBLE);
					}

					public void onBufferPercent(int percent, int beginPos,
							int endPos) {
						// 缓冲回调，通知当前缓冲进度
						mSpeechProgress.setSecondaryProgress(percent);
					}

					public void onPlayPaused() {
						// 暂停通知，表示缓冲数据播放完成，后续的音频数据正在获取
					}

					public void onPlayResumed() {
						// 暂停通知后重新获取到后续音频，表示重新开始播放
					}

					public void onPlayPercent(int percent, int beginPos,
							int endPos) {
						// 播放回调，通知当前播放进度
						mSpeechProgress.setProgress(percent);
					}

					public void onEnd(SpeechError error) {
						mSpeechProgress.setProgress(PROGRESS_MAX);
					}
				});
	}
}
