package com.coofee.assistant.word;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import com.coofee.assistant.MyAbsCursorAdapter;
import com.coofee.assistant.R;
import com.coofee.assistant.provider.Assistant.ZhEn.Word;

public class WordCursorAdapter extends MyAbsCursorAdapter {

	private boolean mShowMeaning = true;
	
	public WordCursorAdapter(Context context, Cursor c) {
		super(context, R.layout.word_list_item, c);
	}

	public void setWordShowMeaning(boolean show) {
		mShowMeaning = show;
		
		notifyDataSetChanged();
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		final TextView wordName = (TextView) view.findViewById(R.id.name);
		
		String name = cursor.getString(cursor.getColumnIndex(Word.NAME));
		
		wordName.setText(name);
		if (mShowMeaning) {
			view.findViewById(R.id.meaningLayout).setVisibility(View.VISIBLE);
			final TextView wordMeaning = (TextView) view.findViewById(R.id.meaning);
			final View speech = view.findViewById(R.id.speech_button);
			final String meaning = cursor.getString(cursor.getColumnIndex(Word.MEANING));
			
			speech.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(v.getContext(), SpeechAvtivity.class);
					intent.putExtra(SpeechAvtivity.EXTRA_SPEECH_TEXT, meaning);
					v.getContext().startActivity(intent);
				}
			});
			
			wordMeaning.setText(meaning);
		} else {
			view.findViewById(R.id.meaningLayout).setVisibility(View.GONE);
		}
		
	}

}
