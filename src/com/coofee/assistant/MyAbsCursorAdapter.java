package com.coofee.assistant;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Handler;
import android.widget.ResourceCursorAdapter;

/**
 * 用于使用数据库的适配器;
 * @author zhao
 *
 */
public abstract class MyAbsCursorAdapter extends ResourceCursorAdapter {

	public MyAbsCursorAdapter(Context context, int layout, Cursor c) {
		super(context, layout, c);
		c.registerDataSetObserver(new MyDataSetObserver());
		c.registerContentObserver(new ChangeObserver());
	}

	private class ChangeObserver extends ContentObserver {
		public ChangeObserver() {
			super(new Handler());
		}

		@Override
		public boolean deliverSelfNotifications() {
			return true;
		}

		@Override
		public void onChange(boolean selfChange) {
			onContentChanged();
		}
	}

	private class MyDataSetObserver extends DataSetObserver {
		@Override
		public void onChanged() {
			notifyDataSetChanged();
		}

		@Override
		public void onInvalidated() {
			notifyDataSetInvalidated();
		}
	}
}
