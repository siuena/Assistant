package com.coofee.assistant.word;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import com.coofee.assistant.MyAbsCursorAdapter;
import com.coofee.assistant.R;
import com.coofee.assistant.provider.Assistant.ZhEn;

public class NoteCursorAdapter extends MyAbsCursorAdapter {

	public NoteCursorAdapter(Context context, Cursor c) {
		super(context, R.layout.note_list_item, c);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		final TextView noteName = (TextView) view.findViewById(R.id.name);
		final TextView noteMemo = (TextView) view.findViewById(R.id.memo);
		
		String name = cursor.getString(cursor.getColumnIndex(ZhEn.Note.NAME));
		String memo = cursor.getString(cursor.getColumnIndex(ZhEn.Note.MEMO));
		
		noteName.setText(name);
		noteMemo.setText(memo);
	}

}
