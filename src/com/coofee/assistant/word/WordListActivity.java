package com.coofee.assistant.word;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.coofee.assistant.R;
import com.coofee.assistant.provider.Assistant;
import com.coofee.assistant.provider.Assistant.ZhEn;
import com.coofee.assistant.provider.Assistant.ZhEn.Word;
import com.coofee.assistant.provider.AssistantSQLiteHelper;

public class WordListActivity extends Activity {

	private long mNoteId = Assistant.NOT_SET;
	private TextView mShowMeanView;
	private ListView mWordListView;
	private WordCursorAdapter mWordAdapter;
	private View mAdd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_list_activity);

		initViews();
	}

	@Override
	protected void onResume() {
		super.onResume();

		initData();
	}

	private void initViews() {
		mWordListView = (ListView) findViewById(R.id.note_listview);
		mWordListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Cursor cursor = mWordAdapter.getCursor();
						gotoModifyWordActivity(cursor);
					}
				});

		mWordListView
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						final Cursor cursor = mWordAdapter.getCursor();
						final long wordId = cursor.getLong(cursor
								.getColumnIndex(Word._ID));

						new AlertDialog.Builder(WordListActivity.this)
								.setTitle("单词本").setItems(
										new String[] { "添加新单词", "修改单词",
												"删除单词" },
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												switch (which) {
												case 0:
													gotoAddWordActivity();
													break;
												case 1:
													gotoModifyWordActivity(cursor);
													break;
												case 2:
													AssistantSQLiteHelper
															.deleteWordById(
																	WordListActivity.this,
																	wordId);
													break;
												}
											}
										}).create().show();

						return true;
					}
				});

		mAdd = (Button) findViewById(R.id.add);
		mAdd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				gotoAddWordActivity();
			}
		});
		
		mShowMeanView = (TextView) findViewById(R.id.showMeaning);
		mShowMeanView.setText("背单词");
		mShowMeanView.setOnClickListener(new View.OnClickListener() {
			private boolean show = true;
			
			@Override
			public void onClick(View v) {
				show = !show;
				
				if (show) {
					mShowMeanView.setText("背单词");
				} else {
					mShowMeanView.setText("显示释义");
				}
				
				mWordAdapter.setWordShowMeaning(show);
			}
		});
	}

	private void initData() {
		Intent data = getIntent();
		if (data == null) {
			WordListActivity.this.finish();
		} else {
			mNoteId = data.getLongExtra(NoteActivity.EXTRA_NOTE_ID,
					Assistant.NOT_SET);
			String noteName = data.getStringExtra(NoteActivity.EXTRA_NOTE_NAME);
			((TextView) findViewById(R.id.title)).setText(TextUtils
					.isEmpty(noteName) ? "单词本" : noteName);
			
			Cursor cursor = getContentResolver().query(ZhEn.Word.CONTENT_URI,
					null, Word.NOTE_ID + " = ? ",
					new String[] { String.valueOf(mNoteId) }, null);
			mWordAdapter = new WordCursorAdapter(this, cursor);
			if (mWordAdapter.getCount() == 0) {
				mAdd.setVisibility(View.VISIBLE);
				mWordListView.setVisibility(View.GONE);
			} else {
				mAdd.setVisibility(View.GONE);
				mWordListView.setVisibility(View.VISIBLE);
				mWordListView.setAdapter(mWordAdapter);
			}
		}
	}

	private void gotoAddWordActivity() {
		Intent intent = new Intent(WordListActivity.this, OneWordActivity.class);
		intent.putExtra(NoteActivity.EXTRA_NOTE_ID, mNoteId);
		WordListActivity.this.startActivity(intent);
	}

	private void gotoModifyWordActivity(Cursor cursor) {
		long wordId = cursor.getLong(cursor.getColumnIndex(ZhEn.Word._ID));
		String wordName = cursor.getString(cursor.getColumnIndex(Word.NAME));
		String wordMeaning = cursor.getString(cursor
				.getColumnIndex(Word.MEANING));
		String wordSample = cursor
				.getString(cursor.getColumnIndex(Word.SAMPLE));
		String wordMemo = cursor.getString(cursor.getColumnIndex(Word.MEMO));

		Intent intent = new Intent(WordListActivity.this, OneWordActivity.class);
		intent.putExtra(Word._ID, wordId);
		intent.putExtra(NoteActivity.EXTRA_NOTE_ID, mNoteId);
		intent.putExtra(Word.NAME, wordName);
		intent.putExtra(Word.MEANING, wordMeaning);
		intent.putExtra(Word.SAMPLE, wordSample);
		intent.putExtra(Word.MEMO, wordMemo);

		WordListActivity.this.startActivity(intent);
	}
}
