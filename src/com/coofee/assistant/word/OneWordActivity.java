package com.coofee.assistant.word;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;

import com.coofee.assistant.R;
import com.coofee.assistant.provider.Assistant;
import com.coofee.assistant.provider.Assistant.ZhEn.Word;
import com.coofee.assistant.provider.AssistantSQLiteHelper;
import com.coofee.assistant.util.UiUtil;

public class OneWordActivity extends Activity {
	public static final String EXTRA_WORD_ID = "word_id";

	private EditText mWordName, mWordMeaning, mWordSample, mWordMemo;
	private long mNoteId = Assistant.NOT_SET;
	private long mWordId = Assistant.NOT_SET;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_word_activity);
		
		initViews();
		initData();
	}
	
	private void initViews() {
		mWordName = (EditText) findViewById(R.id.new_word_name);
		mWordMeaning = (EditText) findViewById(R.id.new_word_meaning);
		mWordSample = (EditText) findViewById(R.id.new_word_sample);
		mWordMemo = (EditText) findViewById(R.id.new_word_memo);
	}
	
	private void initData() {
		Intent data = getIntent();
		if (data != null) {
			mNoteId = data.getLongExtra(NoteActivity.EXTRA_NOTE_ID, Assistant.NOT_SET);
			mWordId = data.getLongExtra(Word._ID, Assistant.NOT_SET);
			mWordName.setText(data.getStringExtra(Word.NAME));
			mWordMeaning.setText(data.getStringExtra(Word.MEANING));
			mWordSample.setText(data.getStringExtra(Word.SAMPLE));
			mWordMemo.setText(data.getStringExtra(Word.MEMO));
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			if (mNoteId != Assistant.NOT_SET) {
				// 修改单词或者新增单词(在单词列表页面添加新单词, 而不是在新增单词本页面.).
				
				if (mWordId == Assistant.NOT_SET) {
					if (!TextUtils.isEmpty(mWordName.getText())) {
						// 如果名称不为空的话, 则保存新增单词
						AssistantSQLiteHelper.insertNewWord(OneWordActivity.this,
								mWordName.getText().toString(), mNoteId, mWordMeaning
										.getText().toString(), mWordSample.getText()
										.toString(), mWordMemo.getText().toString());
					}
				} else {
					// 修改单词时, 名称不能改为空.
					if (TextUtils.isEmpty(mWordName.getText())) {
						UiUtil.toastLong(OneWordActivity.this, "单词名称不能为空!");
						return true;
					}
					
					// 修改单词
					AssistantSQLiteHelper.updateWord(OneWordActivity.this, mWordId,
							mWordName.getText().toString(), mNoteId, mWordMeaning
									.getText().toString(), mWordSample.getText()
									.toString(), mWordMemo.getText().toString());
				}
			} else {
				// 在新增单词本时新增单词
				if (TextUtils.isEmpty(mWordName.getText())) {
					setResult(RESULT_CANCELED);
				} else {
					Intent data = new Intent();
					data.putExtra(Word.NAME, mWordName.getText().toString());
					data.putExtra(Word.MEANING, mWordMeaning.getText().toString());
					data.putExtra(Word.SAMPLE, mWordSample.getText().toString());
					data.putExtra(Word.MEMO, mWordMemo.getText().toString());
					setResult(RESULT_OK, data);
				}
			}
		}
		
		return super.onKeyDown(keyCode, event);
	}
}
