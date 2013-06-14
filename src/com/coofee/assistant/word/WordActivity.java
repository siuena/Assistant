package com.coofee.assistant.word;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.coofee.assistant.R;

public class WordActivity extends Activity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.word_activity);

		findViewById(R.id.word_notebook).setOnClickListener(this);
		findViewById(R.id.word_translate).setOnClickListener(this);
		findViewById(R.id.word_speech).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		
		switch (v.getId()) {
		case R.id.word_notebook:
			intent = new Intent(WordActivity.this, NoteListActivity.class);
			WordActivity.this.startActivity(intent);
			break;
		case R.id.word_translate:
			intent = new Intent(WordActivity.this, TranslateActivity.class);
			WordActivity.this.startActivity(intent);
			break;
		case R.id.word_speech:
			intent = new Intent(WordActivity.this, SpeechAvtivity.class);
			WordActivity.this.startActivity(intent);
			break;
		}
	}
}
