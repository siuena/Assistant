package com.coofee.assistant.book;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.coofee.assistant.R;

public class SearchBookByIsbnActivity extends Activity {

	public static final String ISBN = "isbn";
	private final int SCAN_ISBN = 0x0001;
	private EditText mInputIsbn = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_book_by_isbn_activity);
		
		mInputIsbn = (EditText) findViewById(R.id.search_book_by_isbn_edittext_inputIsbn);
		findViewById(R.id.search_book_by_isbn_button_scanIsbn)
			.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction("com.google.zxing.client.android.SCAN");
				startActivityForResult(intent, SCAN_ISBN);
			}
		});
		
		findViewById(R.id.search_book_by_isbn_button_search)
			.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String strIsbn = mInputIsbn.getText().toString();
				if (!TextUtils.isEmpty(strIsbn)) {
					strIsbn = strIsbn.trim();
					Intent intent = new Intent(SearchBookByIsbnActivity.this, 
							BookSearchResultActivity.class);
					intent.putExtra(ISBN, strIsbn);
					startActivity(intent);
				} 
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == SCAN_ISBN) {
			if (resultCode == Activity.RESULT_OK) {
				String strIsbn = data.getStringExtra("SCAN_RESULT");
				Log.d("书籍ISBN扫描结果: ", strIsbn);
				mInputIsbn.setText(strIsbn);
			} else {
				Toast.makeText(this, "扫描书籍ISBN错误!", Toast.LENGTH_SHORT).show();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
