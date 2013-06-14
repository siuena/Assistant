package com.coofee.assistant.word;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.coofee.assistant.R;
import com.coofee.assistant.provider.Assistant;
import com.coofee.assistant.provider.Assistant.ZhEn.Word;
import com.coofee.assistant.provider.AssistantSQLiteHelper;
import com.coofee.assistant.util.UiUtil;

public class NoteActivity extends Activity {
	public static final String EXTRA_NOTE_ID = "note_id";
	public static final String EXTRA_NOTE_NAME = "note_name";
	public static final String EXTRA_NOTE_MEMO = "note_memo";
	private static final int REQUEST_NEW_WORD = 0x0001;

	private long mNoteId = Assistant.NOT_SET;
	private EditText mNoteNameView, mNoteMemoView;
	private View mAddNewWord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_note_activity);

		initViews();
		setListeners();
		initData();
	}

	private void initViews() {
		mNoteNameView = (EditText) findViewById(R.id.new_note_name);
		mNoteMemoView = (EditText) findViewById(R.id.new_note_memo);
		mAddNewWord = findViewById(R.id.new_note_addNewWord);
	}

	private void setListeners() {
		mAddNewWord.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(mNoteMemoView.getText())
						&& !TextUtils.isEmpty(mNoteNameView.getText())) {
					Intent intent = new Intent(NoteActivity.this,
							OneWordActivity.class);
					NoteActivity.this.startActivityForResult(intent,
							REQUEST_NEW_WORD);
				}
			}
		});
	}

	private void initData() {
		Intent data = getIntent();
		if (data != null) {
			mNoteId = data.getLongExtra(EXTRA_NOTE_ID, Assistant.NOT_SET);
			mNoteNameView.setText(data.getStringExtra(EXTRA_NOTE_NAME));
			mNoteMemoView.setText(data.getStringExtra(EXTRA_NOTE_MEMO));

			if (mNoteId != Assistant.NOT_SET) {
				// 修改单词本时隐藏新增单词按钮. (只有新添加一个单词本时, 才显示添加新单词按钮)
				mAddNewWord.setVisibility(View.GONE);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && data != null) {
			final String wordName = data.getStringExtra(Word.NAME);
			final String wordMeaning = data.getStringExtra(Word.MEANING);
			final String wordSample = data.getStringExtra(Word.SAMPLE);
			final String wordMemo = data.getStringExtra(Word.MEMO);

			if (!TextUtils.isEmpty(wordName)) {
				Uri uri = AssistantSQLiteHelper.insertNewNote(
						NoteActivity.this, mNoteNameView.getText().toString(),
						mNoteMemoView.getText().toString());
				long noteId = ContentUris.parseId(uri);

				AssistantSQLiteHelper.insertNewWord(NoteActivity.this,
						wordName, noteId, wordMeaning, wordSample, wordMemo);

				Intent intent = new Intent(NoteActivity.this,
						WordListActivity.class);
				intent.putExtra(NoteActivity.EXTRA_NOTE_ID, noteId);
				NoteActivity.this.startActivity(intent);
			}
		}

		NoteActivity.this.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mNoteId != Assistant.NOT_SET) {
			// 修改单词本时, 不允许将单词本的名称改为空值.
			if (TextUtils.isEmpty(mNoteNameView.getText())) {
				UiUtil.toastLong(NoteActivity.this, "单词本名称不能为空!");
				return true;
			}

			// 更新单词本.
			AssistantSQLiteHelper.updateNote(NoteActivity.this, mNoteId,
					mNoteNameView.getText().toString(), mNoteMemoView.getText()
							.toString());
		}

		return super.onKeyDown(keyCode, event);
	}
}
