package com.coofee.assistant.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.coofee.assistant.provider.Assistant.BusHistory;
import com.coofee.assistant.provider.Assistant.Weather;

public class AssistantProvider extends ContentProvider {
	// private static final String TAG = Weather.class.getSimpleName();
	private static final UriMatcher URI_MATCHER = new UriMatcher(
			UriMatcher.NO_MATCH);

	static {
		URI_MATCHER.addURI(MyAbsSQLiteOpenHelper.AUTOHORITY,
				Assistant.Weather.Area.TABLE_NAME, Assistant.WEATHER_AREA_ITEM);
		URI_MATCHER.addURI(MyAbsSQLiteOpenHelper.AUTOHORITY,
				Assistant.Weather.Area.TABLE_NAME + "/#",
				Assistant.WEATHER_AREA_ITEM_ID);

		URI_MATCHER.addURI(MyAbsSQLiteOpenHelper.AUTOHORITY,
				Assistant.Weather.History.TABLE_NAME,
				Assistant.WEATHER_HISTORY_ITEM);
		URI_MATCHER.addURI(MyAbsSQLiteOpenHelper.AUTOHORITY,
				Assistant.Weather.History.TABLE_NAME + "/#",
				Assistant.WEATHER_HISTORY_ITEM_ID);

		URI_MATCHER.addURI(MyAbsSQLiteOpenHelper.AUTOHORITY,
				Assistant.Book.BookBasicInfo.TABLE_NAME,
				Assistant.BOOK_BASIC_ITEM);
		URI_MATCHER.addURI(MyAbsSQLiteOpenHelper.AUTOHORITY,
				Assistant.Book.BookBasicInfo.TABLE_NAME + "/#",
				Assistant.BOOK_BASIC_ITEM_ID);

		URI_MATCHER.addURI(MyAbsSQLiteOpenHelper.AUTOHORITY,
				Assistant.ZhEn.Note.TABLE_NAME, Assistant.NOTE_ITEM);
		URI_MATCHER.addURI(MyAbsSQLiteOpenHelper.AUTOHORITY,
				Assistant.ZhEn.Note.TABLE_NAME + "/#", Assistant.NOTE_ITEM_ID);

		URI_MATCHER.addURI(MyAbsSQLiteOpenHelper.AUTOHORITY,
				Assistant.ZhEn.Word.TABLE_NAME, Assistant.WORD_ITEM);
		URI_MATCHER.addURI(MyAbsSQLiteOpenHelper.AUTOHORITY,
				Assistant.ZhEn.Word.TABLE_NAME + "/#", Assistant.WORD_ITEM_ID);

		URI_MATCHER.addURI(MyAbsSQLiteOpenHelper.AUTOHORITY,
				BusHistory.BusLine.TABLE_NAME, Assistant.BUS_LINE_ITEM);
		URI_MATCHER.addURI(MyAbsSQLiteOpenHelper.AUTOHORITY,
				BusHistory.BusLine.TABLE_NAME + "/#",
				Assistant.BUS_LINE_ITEM_ID);

		URI_MATCHER.addURI(MyAbsSQLiteOpenHelper.AUTOHORITY,
				BusHistory.BusSation.TABLE_NAME, Assistant.BUS_STATION_ITEM);
		URI_MATCHER.addURI(MyAbsSQLiteOpenHelper.AUTOHORITY,
				BusHistory.BusSation.TABLE_NAME + "/#",
				Assistant.BUS_STATION_ITEM_ID);

		URI_MATCHER.addURI(MyAbsSQLiteOpenHelper.AUTOHORITY,
				BusHistory.BusTransfer.TABLE_NAME, Assistant.BUS_TRANSFER_ITEM);
		URI_MATCHER.addURI(MyAbsSQLiteOpenHelper.AUTOHORITY,
				BusHistory.BusTransfer.TABLE_NAME + "/#",
				Assistant.BUS_TRANSFER_ITEM_ID);

	}

	private AssistantSQLiteHelper mWeatherSqliteHelper;

	@Override
	public boolean onCreate() {
		mWeatherSqliteHelper = new AssistantSQLiteHelper(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		final int type = URI_MATCHER.match(uri);

		switch (type) {
		case Assistant.WEATHER_AREA_ITEM:
			return Assistant.TYPE_WEATHER_AREA_ITEM;
		case Assistant.WEATHER_AREA_ITEM_ID:
			return Assistant.TYPE_WEATHER_AREA_ITEM_ID;
		case Assistant.WEATHER_HISTORY_ITEM:
			return Assistant.TYPE_WEATHER_HISTORY_ITEM;
		case Assistant.WEATHER_HISTORY_ITEM_ID:
			return Assistant.TYPE_WEATHER_HISTORY_ITEM_ID;
		case Assistant.BOOK_BASIC_ITEM:
			return Assistant.TYPE_BOOK_BASIC_ITEM;
		case Assistant.BOOK_BASIC_ITEM_ID:
			return Assistant.TYEP_BOOK_BASIC_ITEM_ID;
		case Assistant.NOTE_ITEM:
			return Assistant.TYPE_NOTE_ITEM;
		case Assistant.NOTE_ITEM_ID:
			return Assistant.TYPE_NOTE_ITEM_ID;
		case Assistant.WORD_ITEM:
			return Assistant.TYPE_WORD_ITEM;
		case Assistant.WORD_ITEM_ID:
			return Assistant.TYPE_WORD_ITEM_ID;
		case Assistant.BUS_LINE_ITEM:
			return Assistant.TYPE_BUS_LINE_ITEM;
		case Assistant.BUS_LINE_ITEM_ID:
			return Assistant.TYPE_BUS_LINE_ITEM_ID;
		case Assistant.BUS_STATION_ITEM:
			return Assistant.TYPE_BUS_STATION_ITEM;
		case Assistant.BUS_STATION_ITEM_ID:
			return Assistant.TYPE_BUS_STATION_ITEM_ID;
		case Assistant.BUS_TRANSFER_ITEM:
			return Assistant.TYPE_BUS_TRANSFER_ITEM;
		case Assistant.BUS_TRANSFER_ITEM_ID:
			return Assistant.TYPE_BUS_TRANSFER_ITEM_ID;
		}

		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mWeatherSqliteHelper.getWritableDatabase();

		switch (URI_MATCHER.match(uri)) {
		case Assistant.WEATHER_AREA_ITEM:
			return MyAbsSQLiteOpenHelper.insertRecordToTable(getContext(), db,
					Assistant.Weather.Area.TABLE_NAME, null, values,
					Assistant.Weather.Area.CONTENT_URI);
		case Assistant.BOOK_BASIC_ITEM:
			return MyAbsSQLiteOpenHelper.insertRecordToTable(getContext(), db,
					Assistant.Book.BookBasicInfo.TABLE_NAME, null, values,
					Assistant.Book.BookBasicInfo.CONTENT_URI);
		case Assistant.WEATHER_HISTORY_ITEM:
			return MyAbsSQLiteOpenHelper.insertRecordToTable(getContext(), db,
					Assistant.Weather.History.TABLE_NAME, null, values,
					Assistant.Weather.History.CONTENT_URI);
		case Assistant.NOTE_ITEM:
			return MyAbsSQLiteOpenHelper.insertRecordToTable(getContext(), db,
					Assistant.ZhEn.Note.TABLE_NAME, null, values,
					Assistant.ZhEn.Note.CONTENT_URI);
		case Assistant.WORD_ITEM:
			return MyAbsSQLiteOpenHelper.insertRecordToTable(getContext(), db,
					Assistant.ZhEn.Word.TABLE_NAME, null, values,
					Assistant.ZhEn.Word.CONTENT_URI);
		case Assistant.BUS_LINE_ITEM:
			return MyAbsSQLiteOpenHelper.insertRecordToTable(getContext(), db,
					BusHistory.BusLine.TABLE_NAME, null, values,
					BusHistory.BusLine.CONTENT_URI);
		case Assistant.BUS_STATION_ITEM:
			return MyAbsSQLiteOpenHelper.insertRecordToTable(getContext(), db,
					BusHistory.BusSation.TABLE_NAME, null, values,
					BusHistory.BusSation.CONTENT_URI);
		case Assistant.BUS_TRANSFER_ITEM:
			return MyAbsSQLiteOpenHelper.insertRecordToTable(getContext(), db,
					BusHistory.BusTransfer.TABLE_NAME, null, values,
					BusHistory.BusTransfer.CONTENT_URI);
		}

		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mWeatherSqliteHelper.getWritableDatabase();
		int rowAffacted = 0;

		switch (URI_MATCHER.match(uri)) {
		case Assistant.WEATHER_AREA_ITEM:
			rowAffacted = db.delete(Weather.Area.TABLE_NAME, selection,
					selectionArgs);
			break;
		case Assistant.WEATHER_AREA_ITEM_ID:
			rowAffacted = db.delete(Weather.Area.TABLE_NAME,
					MyAbsSQLiteOpenHelper.appendSelectionWithId(
							Weather.Area._ID, uri, selection), selectionArgs);
			break;
		case Assistant.WEATHER_HISTORY_ITEM:
			rowAffacted = db.delete(Weather.History.TABLE_NAME, selection,
					selectionArgs);
			break;
		case Assistant.WEATHER_HISTORY_ITEM_ID:
			rowAffacted = db
					.delete(Weather.History.TABLE_NAME, MyAbsSQLiteOpenHelper
							.appendSelectionWithId(Weather.History._ID, uri,
									selection), selectionArgs);
			break;
		case Assistant.BOOK_BASIC_ITEM:
			rowAffacted = db.delete(Assistant.Book.BookBasicInfo.TABLE_NAME,
					selection, selectionArgs);
			break;
		case Assistant.BOOK_BASIC_ITEM_ID:
			rowAffacted = db.delete(Assistant.Book.BookBasicInfo.TABLE_NAME,
					MyAbsSQLiteOpenHelper.appendSelectionWithId(
							Assistant.Book.BookBasicInfo._ID, uri, selection),
					selectionArgs);
			break;
		case Assistant.NOTE_ITEM:
			rowAffacted = db.delete(Assistant.ZhEn.Note.TABLE_NAME, selection,
					selectionArgs);
			break;
		case Assistant.NOTE_ITEM_ID:
			rowAffacted = db.delete(Assistant.ZhEn.Note.TABLE_NAME,
					MyAbsSQLiteOpenHelper.appendSelectionWithId(
							Assistant.ZhEn.Note._ID, uri, selection),
					selectionArgs);
			break;
		case Assistant.WORD_ITEM:
			rowAffacted = db.delete(Assistant.ZhEn.Word.TABLE_NAME, selection,
					selectionArgs);
			break;
		case Assistant.WORD_ITEM_ID:
			rowAffacted = db.delete(Assistant.ZhEn.Word.TABLE_NAME,
					MyAbsSQLiteOpenHelper.appendSelectionWithId(
							Assistant.ZhEn.Word._ID, uri, selection),
					selectionArgs);
			break;
		case Assistant.BUS_LINE_ITEM:
			rowAffacted = db.delete(BusHistory.BusLine.TABLE_NAME, selection,
					selectionArgs);
			break;
		case Assistant.BUS_LINE_ITEM_ID:
			rowAffacted = db.delete(BusHistory.BusLine.TABLE_NAME,
					MyAbsSQLiteOpenHelper.appendSelectionWithId(
							BusHistory.BusLine._ID, uri, selection),
					selectionArgs);
			break;
		case Assistant.BUS_STATION_ITEM:
			rowAffacted = db.delete(BusHistory.BusSation.TABLE_NAME, selection,
					selectionArgs);
			break;
		case Assistant.BUS_STATION_ITEM_ID:
			rowAffacted = db.delete(BusHistory.BusSation.TABLE_NAME,
					MyAbsSQLiteOpenHelper.appendSelectionWithId(
							BusHistory.BusSation._ID, uri, selection),
					selectionArgs);
			break;
		case Assistant.BUS_TRANSFER_ITEM:
			rowAffacted = db.delete(BusHistory.BusTransfer.TABLE_NAME,
					selection, selectionArgs);
			break;
		case Assistant.BUS_TRANSFER_ITEM_ID:
			rowAffacted = db.delete(BusHistory.BusTransfer.TABLE_NAME,
					MyAbsSQLiteOpenHelper.appendSelectionWithId(
							BusHistory.BusTransfer._ID, uri, selection),
					selectionArgs);
			break;
		}

		if (rowAffacted != 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}

		return rowAffacted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = mWeatherSqliteHelper.getWritableDatabase();
		int rowAffacted = 0;

		switch (URI_MATCHER.match(uri)) {
		case Assistant.WEATHER_AREA_ITEM:
			rowAffacted = db.update(Weather.Area.TABLE_NAME, values, selection,
					selectionArgs);
			break;
		case Assistant.WEATHER_AREA_ITEM_ID:
			rowAffacted = db.update(Weather.Area.TABLE_NAME, values,
					MyAbsSQLiteOpenHelper.appendSelectionWithId(
							Weather.Area._ID, uri, selection), selectionArgs);
			break;
		case Assistant.WEATHER_HISTORY_ITEM:
			rowAffacted = db.update(Weather.History.TABLE_NAME, values,
					selection, selectionArgs);
			break;
		case Assistant.WEATHER_HISTORY_ITEM_ID:
			rowAffacted = db
					.update(Weather.History.TABLE_NAME, values,
							MyAbsSQLiteOpenHelper.appendSelectionWithId(
									Weather.History._ID, uri, selection),
							selectionArgs);
			break;
		case Assistant.BOOK_BASIC_ITEM:
			rowAffacted = db.update(Assistant.Book.BookBasicInfo.TABLE_NAME,
					values, selection, selectionArgs);
			break;
		case Assistant.BOOK_BASIC_ITEM_ID:
			rowAffacted = db.update(Assistant.Book.BookBasicInfo.TABLE_NAME,
					values, MyAbsSQLiteOpenHelper.appendSelectionWithId(
							Assistant.Book.BookBasicInfo._ID, uri, selection),
					selectionArgs);
			break;
		case Assistant.NOTE_ITEM:
			rowAffacted = db.update(Assistant.ZhEn.Note.TABLE_NAME, values,
					selection, selectionArgs);
			break;
		case Assistant.NOTE_ITEM_ID:
			rowAffacted = db.update(Assistant.ZhEn.Note.TABLE_NAME, values,
					MyAbsSQLiteOpenHelper.appendSelectionWithId(
							Assistant.ZhEn.Note._ID, uri, selection),
					selectionArgs);
			break;
		case Assistant.WORD_ITEM:
			rowAffacted = db.update(Assistant.ZhEn.Word.TABLE_NAME, values,
					selection, selectionArgs);
			break;
		case Assistant.WORD_ITEM_ID:
			rowAffacted = db.update(Assistant.ZhEn.Word.TABLE_NAME, values,
					MyAbsSQLiteOpenHelper.appendSelectionWithId(
							Assistant.ZhEn.Word._ID, uri, selection),
					selectionArgs);
			break;
		case Assistant.BUS_LINE_ITEM:
			rowAffacted = db.update(BusHistory.BusLine.TABLE_NAME, values,
					selection, selectionArgs);
			break;
		case Assistant.BUS_LINE_ITEM_ID:
			rowAffacted = db.update(BusHistory.BusLine.TABLE_NAME, values,
					MyAbsSQLiteOpenHelper.appendSelectionWithId(
							BusHistory.BusLine._ID, uri, selection),
					selectionArgs);
			break;
		case Assistant.BUS_STATION_ITEM:
			rowAffacted = db.update(BusHistory.BusSation.TABLE_NAME, values,
					selection, selectionArgs);
			break;
		case Assistant.BUS_STATION_ITEM_ID:
			rowAffacted = db.update(BusHistory.BusSation.TABLE_NAME, values,
					MyAbsSQLiteOpenHelper.appendSelectionWithId(
							BusHistory.BusSation._ID, uri, selection),
					selectionArgs);
			break;
		case Assistant.BUS_TRANSFER_ITEM:
			rowAffacted = db.update(BusHistory.BusTransfer.TABLE_NAME, values,
					selection, selectionArgs);
			break;
		case Assistant.BUS_TRANSFER_ITEM_ID:
			rowAffacted = db.update(BusHistory.BusTransfer.TABLE_NAME, values,
					MyAbsSQLiteOpenHelper.appendSelectionWithId(
							BusHistory.BusTransfer._ID, uri, selection),
					selectionArgs);
			break;
		}

		if (rowAffacted != 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}

		return rowAffacted;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = mWeatherSqliteHelper.getWritableDatabase();
		Cursor cursor = null;

		switch (URI_MATCHER.match(uri)) {
		case Assistant.WEATHER_AREA_ITEM:
			cursor = db.query(Weather.Area.TABLE_NAME, projection, selection,
					selectionArgs, null, null, null);
			break;
		case Assistant.WEATHER_AREA_ITEM_ID:
			cursor = db.query(Weather.Area.TABLE_NAME, projection,
					MyAbsSQLiteOpenHelper.appendSelectionWithId(
							Weather.Area._ID, uri, selection), selectionArgs,
					null, null, null);
			break;
		case Assistant.WEATHER_HISTORY_ITEM:
			cursor = db.query(Weather.History.TABLE_NAME, projection,
					selection, selectionArgs, null, null, null);
			break;
		case Assistant.WEATHER_HISTORY_ITEM_ID:
			cursor = db.query(Weather.History.TABLE_NAME, projection,
					MyAbsSQLiteOpenHelper.appendSelectionWithId(
							Weather.History._ID, uri, selection),
					selectionArgs, null, null, null);
			break;
		case Assistant.BOOK_BASIC_ITEM:
			cursor = db.query(Assistant.Book.BookBasicInfo.TABLE_NAME,
					projection, selection, selectionArgs, null, null, null);
			break;
		case Assistant.BOOK_BASIC_ITEM_ID:
			cursor = db.query(Assistant.Book.BookBasicInfo.TABLE_NAME,
					projection, MyAbsSQLiteOpenHelper.appendSelectionWithId(
							Assistant.Book.BookBasicInfo._ID, uri, selection),
					selectionArgs, null, null, null);
			break;
		case Assistant.NOTE_ITEM:
			cursor = db.query(Assistant.ZhEn.Note.TABLE_NAME, projection,
					selection, selectionArgs, null, null, null);
			break;
		case Assistant.NOTE_ITEM_ID:
			cursor = db.query(Assistant.ZhEn.Note.TABLE_NAME, projection,
					MyAbsSQLiteOpenHelper.appendSelectionWithId(
							Assistant.ZhEn.Note._ID, uri, selection),
					selectionArgs, null, null, null);
			break;
		case Assistant.WORD_ITEM:
			cursor = db.query(Assistant.ZhEn.Word.TABLE_NAME, projection,
					selection, selectionArgs, null, null, null);
			break;
		case Assistant.WORD_ITEM_ID:
			cursor = db.query(Assistant.ZhEn.Word.TABLE_NAME, projection,
					MyAbsSQLiteOpenHelper.appendSelectionWithId(
							Assistant.ZhEn.Word._ID, uri, selection),
					selectionArgs, null, null, null);
			break;
		case Assistant.BUS_LINE_ITEM:
			cursor = db.query(BusHistory.BusLine.TABLE_NAME, projection,
					selection, selectionArgs, null, null, null);
			break;
		case Assistant.BUS_LINE_ITEM_ID:
			cursor = db.query(BusHistory.BusLine.TABLE_NAME, projection,
					MyAbsSQLiteOpenHelper.appendSelectionWithId(
							BusHistory.BusLine._ID, uri, selection),
					selectionArgs, null, null, null);
			break;
		case Assistant.BUS_STATION_ITEM:
			cursor = db.query(BusHistory.BusSation.TABLE_NAME, projection,
					selection, selectionArgs, null, null, null);
			break;
		case Assistant.BUS_STATION_ITEM_ID:
			cursor = db.query(BusHistory.BusSation.TABLE_NAME, projection,
					MyAbsSQLiteOpenHelper.appendSelectionWithId(
							BusHistory.BusSation._ID, uri, selection),
					selectionArgs, null, null, null);
			break;
		case Assistant.BUS_TRANSFER_ITEM:
			cursor = db.query(BusHistory.BusTransfer.TABLE_NAME, projection,
					selection, selectionArgs, null, null, null);
			break;
		case Assistant.BUS_TRANSFER_ITEM_ID:
			cursor = db.query(BusHistory.BusTransfer.TABLE_NAME, projection,
					MyAbsSQLiteOpenHelper.appendSelectionWithId(
							BusHistory.BusTransfer._ID, uri, selection),
					selectionArgs, null, null, null);
			break;
		}

		if (cursor != null) {
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
		}

		return cursor;
	}

}
