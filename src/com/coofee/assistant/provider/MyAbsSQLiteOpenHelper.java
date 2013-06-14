package com.coofee.assistant.provider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public abstract class MyAbsSQLiteOpenHelper extends SQLiteOpenHelper {
	public static final String AUTOHORITY = "com.coofee.assistant";
	
	private static final String TAG = MyAbsSQLiteOpenHelper.class
			.getSimpleName();

	public MyAbsSQLiteOpenHelper(Context context, String name, int version) {
		super(context, name, null, version);
	}
	
	/**
	 * 关闭游标
	 * @param cursor
	 */
	public static void closeCursor(Cursor cursor) {
		if (cursor != null) {
			cursor.close();
		}
	}
	
	/**
	 * 清空一个表中的所有数据
	 * @param db
	 * @param tableName
	 */
	public static void deleteAll(final SQLiteDatabase db, final String tableName) {
		db.execSQL("delete from " + tableName);
	}
	
	/**
	 * 删除一个表
	 * @param db
	 */
	public static void drop(final SQLiteDatabase db, final String tableName) {
		db.execSQL("DROP TABLE IF EXISTS " + tableName + ";");
	}

	/**
	 * 判断一个数据库表是否存在.
	 * @param db
	 * @param tableName
	 * @return
	 */
	public static boolean isTableExists(final SQLiteDatabase db, final String tableName) {
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("select count(*) from sqlite_master where type ='table' and name = ?", 
					new String[]{tableName});
			if (cursor != null) {
				
				cursor.moveToFirst();
				if (cursor.getInt(0) == 1) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeCursor(cursor);
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param primaryColumnName  一个表的主键列名称; android中默认为_id列.
	 * @param contentUriWithId 
	 * @param selection
	 * @return
	 */
	public static String appendSelectionWithId(String primaryColumnName,
			Uri contentUriWithId, String selection) {
		return primaryColumnName
				+ "="
				+ ContentUris.parseId(contentUriWithId)
				+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")"
						: "");
	}

	/**
	 * 
	 * @param primaryColumnName
	 *            一个表的主键列名称; android中默认为_id列.
	 * @param id
	 *            数据库id.
	 * @param selection
	 *            其他条件
	 * @return
	 */
	public static String appendSelectionWithId(String primaryColumnName,
			long id, String selection) {
		return primaryColumnName
				+ "="
				+ id
				+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")"
						: "");
	}

	/**
	 * 
	 * @param tableName
	 *            表名
	 * @param nullColumnHack
	 * @param values
	 * @param contentUri
	 *            与该表对应的contentUri
	 */
	public static final Uri insertRecordToTable(Context context,
			SQLiteDatabase db, String tableName, String nullColumnHack,
			ContentValues values, Uri contentUri) {
		long id = db.insert(tableName, nullColumnHack, values);
		Log.d(TAG, "insert a record to " + tableName + ", row id is " + id);
		if (id != -1) {
			Uri insertUri = ContentUris.withAppendedId(contentUri, id);

			if (insertUri != null) {
				context.getContentResolver().notifyChange(insertUri, null);
			}

			return insertUri;
		}
		return null;
	}
}
