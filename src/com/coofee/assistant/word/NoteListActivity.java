package com.coofee.assistant.word;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.coofee.assistant.R;
import com.coofee.assistant.provider.Assistant.ZhEn;
import com.coofee.assistant.provider.Assistant.ZhEn.Note;
import com.coofee.assistant.provider.AssistantSQLiteHelper;

public class NoteListActivity extends Activity {

	private ListView mNoteListView;
	private NoteCursorAdapter mNoteAdapter;
	private Button mAdd;

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
		((TextView) findViewById(R.id.title)).setText("单词本");
		
		mNoteListView = (ListView) findViewById(R.id.note_listview);
		mNoteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cursor cursor = mNoteAdapter.getCursor();
				final long noteId = cursor.getLong(cursor
						.getColumnIndex(ZhEn.Note._ID));
				final String noteName = cursor.getString(cursor.getColumnIndex(Note.NAME));
				Intent intent = new Intent(NoteListActivity.this, WordListActivity.class);
				intent.putExtra(NoteActivity.EXTRA_NOTE_ID, noteId);
				intent.putExtra(NoteActivity.EXTRA_NOTE_NAME, noteName);
				NoteListActivity.this.startActivity(intent);
			}
		});
		
		mNoteListView
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						Cursor cursor = mNoteAdapter.getCursor();
						final long noteId = cursor.getLong(cursor
								.getColumnIndex(ZhEn.Note._ID));
						final String noteName = cursor.getString(cursor
								.getColumnIndex(ZhEn.Note.NAME));
						final String noteMemo = cursor.getString(cursor
								.getColumnIndex(ZhEn.Note.MEMO));

						new AlertDialog.Builder(NoteListActivity.this)
								.setTitle("单词本").setItems(
										new String[] { "添加单词本", "修改单词本",
												"删除单词本" },
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												Intent intent = null;
												switch (which) {
												case 0:
													intent = new Intent(
															NoteListActivity.this,
															NoteActivity.class);
													NoteListActivity.this
															.startActivity(intent);
													break;
												case 1:
													intent = new Intent(
															NoteListActivity.this,
															NoteActivity.class);
													intent.putExtra(
															NoteActivity.EXTRA_NOTE_ID,
															noteId);
													intent.putExtra(
															NoteActivity.EXTRA_NOTE_NAME,
															noteName);
													intent.putExtra(
															NoteActivity.EXTRA_NOTE_MEMO,
															noteMemo);
													NoteListActivity.this
															.startActivity(intent);
													break;
												case 2:
													AssistantSQLiteHelper
															.deleteNoteById(
																	NoteListActivity.this,
																	noteId);
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
				Intent intent = new Intent(
						NoteListActivity.this,
						NoteActivity.class);
				NoteListActivity.this
						.startActivity(intent);
			}
		});
	}

	private void initData() {
		Cursor cursor = getContentResolver().query(ZhEn.Note.CONTENT_URI, null,
				null, null, null);

		mNoteAdapter = new NoteCursorAdapter(this, cursor);

		if (mNoteAdapter.getCount() == 0) {
			mAdd.setVisibility(View.VISIBLE);
			mAdd.setText("添加新单词本");
			mNoteListView.setVisibility(View.GONE);
		} else {
			mAdd.setVisibility(View.GONE);
			mNoteListView.setVisibility(View.VISIBLE);
			mNoteListView.setAdapter(mNoteAdapter);
		}
	}
}
