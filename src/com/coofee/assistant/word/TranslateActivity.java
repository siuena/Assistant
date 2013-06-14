package com.coofee.assistant.word;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.coofee.assistant.R;
import com.coofee.assistant.util.UiUtil;
import com.coofee.assistant.util.translate.TranslateApi;
import com.coofee.assistant.util.translate.TranslateErrorResponse;
import com.coofee.assistant.util.translate.TranslateListener;
import com.coofee.assistant.util.translate.TranslateResult;
import com.coofee.assistant.util.translate.TranslateSuccessResponse;

public class TranslateActivity extends Activity {

	private RadioButton mZhToEn;
//	private RadioButton mEnToZh;
	private View mTranslate, mSpeech, mResultLayout;
	private EditText mTranslateInput, mTranslateResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.translate_activity);

		initViews();
		setListeners();
	}

	private void initViews() {
		mZhToEn = (RadioButton) findViewById(R.id.translate_zh_to_en);
//		mEnToZh = (RadioButton) findViewById(R.id.translate_en_to_zh);
		mTranslate = findViewById(R.id.translate_button);
		mSpeech = findViewById(R.id.speech_button);
		mResultLayout = findViewById(R.id.translate_result_layout);
		mTranslateInput = (EditText) findViewById(R.id.translate_input);
		mTranslateResult = (EditText) findViewById(R.id.translate_result);
	}

	private void setListeners() {
		mTranslate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(mTranslateInput.getText())) {
					final String input = mTranslateInput.getText().toString();
					if (mZhToEn.isChecked()) {
						new TranslateApi().translateZhToEn(TranslateActivity.this, input,
								mTranslateListener);
					} else {
						new TranslateApi().translateEnToZh(TranslateActivity.this, input,
								mTranslateListener);
					}
				}
			}
		});
		
		mSpeech.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(mTranslateResult.getText())) {
					Intent intent = new Intent(TranslateActivity.this, SpeechAvtivity.class);
					intent.putExtra(SpeechAvtivity.EXTRA_SPEECH_TEXT, mTranslateResult.getText().toString());
					TranslateActivity.this.startActivity(intent);
				}
			}
		});
	}

	private TranslateListener mTranslateListener = new TranslateListener() {

		@Override
		public void onTranslateSuccess(TranslateSuccessResponse successResponse) {
			TranslateResult[] results = successResponse.getTrans_result();
			if (results != null) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < results.length; i++) {
					sb.append(results[i].getDst());
					
					if (i != (results.length - 1)) {
						sb.append("\n");
					}
				}
				
				sb.trimToSize();
				mResultLayout.setVisibility(View.VISIBLE);
				mTranslateResult.setText(sb);
			}
		}

		@Override
		public void onTranslateError(TranslateErrorResponse errorResponse) {
			mResultLayout.setVisibility(View.GONE);
			UiUtil.toastShort(TranslateActivity.this,
					errorResponse.getError_msg() == null ? "翻译失败!"
							: errorResponse.getError_msg());
		}

		@Override
		public void onError(String errorMessage) {
			mResultLayout.setVisibility(View.GONE);
			UiUtil.toastShort(TranslateActivity.this, errorMessage);
		}
	};

}
