package com.coofee.assistant.provider;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteTransactionListener;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.coofee.assistant.AssistantApp;
import com.coofee.assistant.bus.BusHistoryRecord;
import com.coofee.assistant.bus.BusLineResult;
import com.coofee.assistant.bus.BusStationResult;
import com.coofee.assistant.bus.BusTransferResult;
import com.coofee.assistant.bus.BusTransferResult.TransferType;
import com.coofee.assistant.provider.Assistant.Book;
import com.coofee.assistant.provider.Assistant.BusHistory;
import com.coofee.assistant.provider.Assistant.Weather;
import com.coofee.assistant.provider.Assistant.ZhEn;
import com.coofee.assistant.provider.Assistant.ZhEn.Note;
import com.coofee.assistant.provider.Assistant.ZhEn.Word;
import com.coofee.assistant.util.DateUtil;
import com.coofee.assistant.weather.WeatherHelper;
import com.coofee.assistant.weather.model.Area;
import com.coofee.assistant.weather.model.WeatherHistory;

public class AssistantSQLiteHelper extends MyAbsSQLiteOpenHelper {
	private static final String TAG = AssistantSQLiteHelper.class
			.getSimpleName();
	private static final int WEATHER_AREA_COUNT = 2586;

	public AssistantSQLiteHelper(Context context) {
		super(context, Assistant.DB_NAME, Assistant.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createWeatherAreaTable(db);

		// 创建天气历史信息表
		db.execSQL("CREATE TABLE IF NOT EXISTS " + Weather.History.TABLE_NAME
				+ "(" + Weather.History._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ Weather.History.AREA_NAME + " TEXT," + Weather.History.DATE
				+ " TEXT," + Weather.History.DAY_STATUS + " TEXT,"
				+ Weather.History.NIGHT_STATUS + " TEXT,"
				+ Weather.History.DAY_TEMPERATURE + " TEXT,"
				+ Weather.History.NIGHT_TEMPERATURE + " TEXT" + ");");

		// 创建书籍基本信息表.
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ Book.BookBasicInfo.TABLE_NAME + "("
				+ Assistant.Book.BookBasicInfo._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ Assistant.Book.BookBasicInfo.ISBN + " TEXT,"
				+ Assistant.Book.BookBasicInfo.NAME + " TEXT,"
				+ Assistant.Book.BookBasicInfo.AUTHOR_INTRO + " TEXT,"
				+ Assistant.Book.BookBasicInfo.BOOK_BASIC_INFO_JSON + " TEXT);");

		// 创建单词本表
		db.execSQL("CREATE TABLE IF NOT EXISTS " + ZhEn.Note.TABLE_NAME + "("
				+ ZhEn.Note._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ ZhEn.Note.NAME + " TEXT, " + ZhEn.Note.MEMO + " TEXT);");

		// 创建单词表
		db.execSQL("CREATE TABLE IF NOT EXISTS " + ZhEn.Word.TABLE_NAME + "("
				+ ZhEn.Word._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ ZhEn.Word.NOTE_ID + " INTEGER," + ZhEn.Word.NAME + " TEXT,"
				+ ZhEn.Word.MEANING + " TEXT," + ZhEn.Word.SAMPLE + " TEXT,"
				+ ZhEn.Word.MEMO + " TEXT);");

		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ BusHistory.BusLine.TABLE_NAME + "(" + BusHistory.BusLine._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ BusHistory.BusLine.CITY + " TEXT," + BusHistory.BusLine.NAME
				+ " TEXT," + BusHistory.BusLine.LINES_JSON + " TEXT);");

		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ BusHistory.BusSation.TABLE_NAME + "("
				+ BusHistory.BusSation._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ BusHistory.BusSation.CITY + " TEXT,"
				+ BusHistory.BusSation.NAME + " TEXT,"
				+ BusHistory.BusSation.STATIONS_JSON + " TEXT);");

		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ BusHistory.BusTransfer.TABLE_NAME + "("
				+ BusHistory.BusTransfer._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ BusHistory.BusTransfer.CITY + " TEXT,"
				+ BusHistory.BusTransfer.START_STATION + " TEXT,"
				+ BusHistory.BusTransfer.END_STATION + " TEXT,"
				+ BusHistory.BusTransfer.DEFAULT_PLAN_JSON + " TEXT,"
				+ BusHistory.BusTransfer.LEAST_TRANSFER_TIMES_JSON + " TEXT,"
				+ BusHistory.BusTransfer.WALK_SHORTEST_DISTANCE_JSON + " TEXT,"
				+ BusHistory.BusTransfer.TIME_SHORTEST_JSON + " TEXT,"
				+ BusHistory.BusTransfer.TOTAL_DISTANCE_SHORTEST_JSON
				+ " TEXT," + BusHistory.BusTransfer.SUBWAY_PRIORITY_JSON
				+ " TEXT);");
		// 创建触发器, 保证新插入单词在一个单词本中.
		// 触发器有错, 先屏蔽掉.
		// db.execSQL("CREATE TRIGGER trg_insert_word BEFORE INSERT ON "
		// + ZhEn.Word.TABLE_NAME + " FOR　EACH ROW BEGIN "
		// + " SELECT CASE WHEN (( SELECT _id FROM "
		// + ZhEn.Note.TABLE_NAME + " WHERE _id = new._id) IS NULL)"
		// + " THEN RAISE(ABORT, 'Foreign Key Violation') END;" + " END;");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "
				+ Assistant.Weather.History.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + Assistant.Weather.Area.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS "
				+ Assistant.Book.BookBasicInfo.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + Word.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NAME);

		// Cursor cursor = null;
		// try {
		// // 找到所有的表, 并删除表.
		// cursor = db.rawQuery(
		// "select tbl_name from sqlite_master where type = ? ",
		// new String[] { "table" });
		//
		// cursor.moveToPrevious();
		// while (cursor.moveToNext()) {
		// db.execSQL("DROP TABLE IF EXISTS " + cursor.getString(0));
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// closeCursor(cursor);
		// }

		onCreate(db);
	}

	private static AssistantSQLiteHelper mInstance;

	public static AssistantSQLiteHelper getInstance() {
		synchronized (AssistantSQLiteHelper.class) {
			if (mInstance == null) {
				mInstance = new AssistantSQLiteHelper(
						AssistantApp.getInstance());
			}
		}

		return mInstance;
	}

	/**
	 * 创建天气信息表
	 * 
	 * @param db
	 */
	private static void createWeatherAreaTable(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + Weather.Area.TABLE_NAME
				+ "(" + Weather.Area._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + Weather.Area.NAME
				+ " TEXT," + Weather.Area.WEATHER_CODE + " TEXT,"
				+ Weather.Area.PROVINCE + " TEXT," + Weather.Area.TYPE
				+ " TEXT," + Weather.Area.ZIPCODE + " TEXT,"
				+ Weather.Area.PHONE_CODE + " TEXT," + Weather.Area.HYPY
				+ " TEXT," + Weather.Area.SHORT_HYPY + " TEXT);");
	}

	/**
	 * 初始化城市信息数据(如果没有初始化过城市信息)
	 * 
	 * @param context
	 * @param db
	 * @param transactionListener
	 */
	public static void initWeatherCityData(final Context context,
			SQLiteTransactionListener transactionListener) {
		final SQLiteDatabase db = getInstance().getWritableDatabase();

		if (isTableExists(db, Weather.Area.TABLE_NAME)) {
			Log.d(TAG, "weather area table exists.");
			if (!isWeatherDataInit(db)) {
				Log.d(TAG, "weather area data has not been inited.");
				// 如果表存在,但是数据没有初始化成功,则删除表中的全部数据然后在插入数据.
				deleteAll(db, Weather.Area.TABLE_NAME);
				insertWeatherCityIntoDb(context, db, transactionListener);
			} else {
				Log.d(TAG, "weather area data has been inited.");
			}
		} else {
			Log.d(TAG, "weather area table does not exists.");
			createWeatherAreaTable(db);
			// 如果表不存在则直接插入数据.
			insertWeatherCityIntoDb(context, db, transactionListener);
		}
	}

	private static boolean isWeatherDataInit(final SQLiteDatabase db) {
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("select count(_id) from "
					+ Weather.Area.TABLE_NAME, null);

			if (cursor != null) {

				cursor.moveToFirst();
				final int recordCount = cursor.getInt(0);
				Log.d(TAG, "record count is " + recordCount);
				if (recordCount == WEATHER_AREA_COUNT) {
					return true;
				}
			}
		} catch (Exception e) {

		} finally {
			closeCursor(cursor);
		}

		return false;
	}

	/**
	 * 初始化数据库数据
	 * 
	 * @param context
	 * @param db
	 */
	private static void insertWeatherCityIntoDb(final Context context,
			final SQLiteDatabase db,
			SQLiteTransactionListener transactionListener) {

		List<Area> allAreaList = WeatherHelper.getAllAreaFromAssert(context);
		Log.d(TAG, "read " + allAreaList.size() + " record from assert.");

		int counter = 0;
		long startTime = System.currentTimeMillis();
		Log.d(TAG, "try to insert record data.");
		db.beginTransactionWithListener(transactionListener);
		try {
			ContentValues values = null;
			for (Area area : allAreaList) {
				values = new ContentValues();
				values.put(Weather.Area.NAME, area.getName());
				values.put(Weather.Area.WEATHER_CODE, area.getCode());
				values.put(Weather.Area.PROVINCE, area.getProvince());
				values.put(Weather.Area.ZIPCODE, area.getZipcode());
				values.put(Weather.Area.PHONE_CODE, area.getPhoneAreaCode());
				values.put(Weather.Area.HYPY, area.getHypy());
				values.put(Weather.Area.SHORT_HYPY, area.getShortHypy());

				db.insert(Weather.Area.TABLE_NAME, null, values);
				counter++;
			}
			// 设置事务成功.
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			Log.d(TAG, "end insert, has insert " + counter
					+ " record and take "
					+ (System.currentTimeMillis() - startTime) / 1000 + "s");
		}

	}

	public static ContentValues convertAreaToValuesWithId(Area area) {
		ContentValues values = new ContentValues();

		values = new ContentValues();
		values.put(Weather.Area._ID, area.getId());
		values.put(Weather.Area.NAME, area.getName());
		values.put(Weather.Area.WEATHER_CODE, area.getCode());
		values.put(Weather.Area.PROVINCE, area.getProvince());
		values.put(Weather.Area.ZIPCODE, area.getZipcode());
		values.put(Weather.Area.PHONE_CODE, area.getPhoneAreaCode());
		values.put(Weather.Area.HYPY, area.getHypy());
		values.put(Weather.Area.SHORT_HYPY, area.getShortHypy());

		return values;
	}

	public static ContentValues convertAreaToValuesWithNoId(Area area) {
		ContentValues values = new ContentValues();

		values = new ContentValues();
		values.put(Weather.Area.NAME, area.getName());
		values.put(Weather.Area.WEATHER_CODE, area.getCode());
		values.put(Weather.Area.PROVINCE, area.getProvince());
		values.put(Weather.Area.ZIPCODE, area.getZipcode());
		values.put(Weather.Area.PHONE_CODE, area.getPhoneAreaCode());
		values.put(Weather.Area.HYPY, area.getHypy());
		values.put(Weather.Area.SHORT_HYPY, area.getShortHypy());

		return values;
	}

	/**
	 * 通过ISBN从数据库查询书籍的基本信息
	 * 
	 * @param isbn
	 * @return
	 */
	public static com.coofee.assistant.book.model.Book getBookBasicInfoByIsbn(
			Context context, String isbn) {
		Cursor cursor = null;
		try {
			cursor = context
					.getContentResolver()
					.query(Assistant.Book.BookBasicInfo.CONTENT_URI,
							new String[] {
									Assistant.Book.BookBasicInfo.ISBN,
									Assistant.Book.BookBasicInfo.BOOK_BASIC_INFO_JSON },
							Assistant.Book.BookBasicInfo.ISBN + " = ?",
							new String[] { isbn }, null);
			Log.d(TAG, "try to query book basic info which isbn is " + isbn);
			if (cursor != null && cursor.getCount() > 1) {
				cursor.moveToFirst();
				String bookInfoJson = cursor.getString(1);
				Log.d(TAG, "book json: " + bookInfoJson);
				if (!TextUtils.isEmpty(bookInfoJson)) {
					return JSON.parseObject(bookInfoJson,
							com.coofee.assistant.book.model.Book.class);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			MyAbsSQLiteOpenHelper.closeCursor(cursor);
		}

		return null;
	}

	/**
	 * 保存历史天气信息
	 * 
	 * @param context
	 * @param history
	 */
	public static void save(Context context, WeatherHistory history) {
		ContentValues values = new ContentValues();
		values.put(Assistant.Weather.History.DATE,
				DateUtil.toString(history.getDate()));
		values.put(Assistant.Weather.History.DAY_STATUS, history.getDayStatus());
		values.put(Assistant.Weather.History.NIGHT_STATUS,
				history.getNightStatus());
		values.put(Assistant.Weather.History.DAY_TEMPERATURE,
				history.getDayTemperature());
		values.put(Assistant.Weather.History.NIGHT_TEMPERATURE,
				history.getNightTemperature());
		values.put(Assistant.Weather.History.AREA_NAME,
				history.getWeatherAreaName());

		Cursor cursor = null;
		try {
			cursor = context.getContentResolver().query(
					Assistant.Weather.History.CONTENT_URI,
					null,
					Assistant.Weather.History.AREA_NAME + " = ? AND "
							+ Assistant.Weather.History.DATE + " = ?",
					new String[] { history.getWeatherAreaName(),
							DateUtil.toString(history.getDate()) },
					Assistant.Weather.History.DATE + " ASC");

			if (cursor != null && cursor.getCount() > 0) {
				// 说明已经保存过了, 则直接更新.
				cursor.moveToFirst();
				long existsSqliteId = cursor.getLong(cursor
						.getColumnIndex(Weather.History._ID));
				context.getContentResolver().update(
						ContentUris.withAppendedId(Weather.History.CONTENT_URI,
								existsSqliteId),
						values,
						Assistant.Weather.History.AREA_NAME + " = ? AND "
								+ Assistant.Weather.History.DATE + " = ?",
						new String[] { history.getWeatherAreaName(),
								DateUtil.toString(history.getDate()) });
			} else {
				context.getContentResolver().insert(
						Weather.History.CONTENT_URI, values);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeCursor(cursor);
		}
	}

	/**
	 * 保存书籍基本信息
	 * 
	 * @param isbn
	 * @return
	 */
	public static Uri save(Context context,
			com.coofee.assistant.book.model.Book book) {

		try {
			ContentValues values = new ContentValues();

			Log.d(TAG, "try to save book: " + JSON.toJSONString(book));

			values.put(Assistant.Book.BookBasicInfo.ISBN, book.getIsbn13());
			values.put(Assistant.Book.BookBasicInfo.NAME, book.getTitle());
			values.put(Assistant.Book.BookBasicInfo.AUTHOR_INTRO,
					book.getAuthor_intro());
			values.put(Assistant.Book.BookBasicInfo.BOOK_BASIC_INFO_JSON,
					JSON.toJSONString(book));

			return context.getContentResolver().insert(
					Assistant.Book.BookBasicInfo.CONTENT_URI, values);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}

		return null;
	}

	/**
	 * 删除单词本
	 * 
	 * @param context
	 * @param noteId
	 */
	public static final void deleteNoteById(Context context, final long noteId) {
		context.getContentResolver().delete(
				ContentUris.withAppendedId(ZhEn.Note.CONTENT_URI, noteId),
				null, null);
		context.getContentResolver().delete(ZhEn.Word.CONTENT_URI,
				ZhEn.Word.NOTE_ID + " = ?",
				new String[] { String.valueOf(noteId) });

	}

	public static final Uri insertNewNote(Context context, String noteName,
			String noteMemo) {
		ContentValues values = new ContentValues();
		values.put(Note.NAME, noteName);
		values.put(Note.MEMO, noteMemo);

		return context.getContentResolver().insert(Note.CONTENT_URI, values);
	}

	public static final int updateNote(Context context, long noteId,
			String noteName, String noteMemo) {
		ContentValues values = new ContentValues();
		values.put(Note.NAME, noteName);
		values.put(Note.MEMO, noteMemo);

		return context.getContentResolver().update(
				ContentUris.withAppendedId(Note.CONTENT_URI, noteId), values,
				null, null);
	}

	public static final Uri insertNewWord(Context context, String wordName,
			long noteId, String wordMeaning, String wordSample, String wordMemo) {
		ContentValues values = new ContentValues();
		values.put(Word.NAME, wordName);
		values.put(Word.NOTE_ID, noteId);
		values.put(Word.MEANING, wordMeaning);
		values.put(Word.SAMPLE, wordSample);
		values.put(Word.MEMO, wordMemo);

		return context.getContentResolver().insert(Word.CONTENT_URI, values);
	}

	public static final int updateWord(Context context, long wordId,
			String wordName, long noteId, String wordMeaning,
			String wordSample, String wordMemo) {
		ContentValues values = new ContentValues();
		values.put(Word.NAME, wordName);
		values.put(Word.NOTE_ID, noteId);
		values.put(Word.MEANING, wordMeaning);
		values.put(Word.SAMPLE, wordSample);
		values.put(Word.MEMO, wordMemo);

		return context.getContentResolver().update(
				ContentUris.withAppendedId(Word.CONTENT_URI, wordId), values,
				null, null);

	}

	public static final int deleteWordById(Context context, long wordId) {
		return context.getContentResolver().delete(
				ContentUris.withAppendedId(Word.CONTENT_URI, wordId), null,
				null);
	}

	public static final Uri insertBusLineResult(Context context,
			BusLineResult lineResult) {
		ContentValues values = new ContentValues();
		values.put(BusHistory.BusLine.CITY, lineResult.getCity());
		values.put(BusHistory.BusLine.NAME, lineResult.getLineName());
		values.put(BusHistory.BusLine.LINES_JSON, JSON.toJSONString(lineResult));

		return context.getContentResolver().insert(
				BusHistory.BusLine.CONTENT_URI, values);
	}

	public static final Uri insertBusStationResult(Context context,
			BusStationResult stationResult) {
		ContentValues values = new ContentValues();
		values.put(BusHistory.BusSation.CITY, stationResult.getCity());
		values.put(BusHistory.BusSation.NAME, stationResult.getStationName());
		values.put(BusHistory.BusSation.STATIONS_JSON,
				JSON.toJSONString(stationResult));

		return context.getContentResolver().insert(
				BusHistory.BusSation.CONTENT_URI, values);
	}

	public static final Uri saveBusTranferResult(Context context,
			BusTransferResult transferResult) {
		ContentValues values = new ContentValues();
		values.put(BusHistory.BusTransfer.CITY, transferResult.getCity());
		values.put(BusHistory.BusTransfer.START_STATION,
				transferResult.getStartStation());
		values.put(BusHistory.BusTransfer.END_STATION,
				transferResult.getEndStation());
		switch (transferResult.getTransferType()) {
		case TransferType.TYPE_DEFAULT:
			values.put(BusHistory.BusTransfer.DEFAULT_PLAN_JSON,
					JSON.toJSONString(transferResult));
			break;
		case TransferType.TYPE_DISTANCE_SHORTEST:
			values.put(BusHistory.BusTransfer.TOTAL_DISTANCE_SHORTEST_JSON,
					JSON.toJSONString(transferResult));
			break;
		case TransferType.TYPE_LEAST_TRANSFER_TIMES:
			values.put(BusHistory.BusTransfer.LEAST_TRANSFER_TIMES_JSON,
					JSON.toJSONString(transferResult));
			break;
		case TransferType.TYPE_SUBWAY_PRIORITY:
			values.put(BusHistory.BusTransfer.SUBWAY_PRIORITY_JSON,
					JSON.toJSONString(transferResult));
			break;
		case TransferType.TYPE_TIME_SHORTEST:
			values.put(BusHistory.BusTransfer.TIME_SHORTEST_JSON,
					JSON.toJSONString(transferResult));
			break;
		case TransferType.TYPE_WALK_DISTANCE_SHORTEST:
			values.put(BusHistory.BusTransfer.WALK_SHORTEST_DISTANCE_JSON,
					JSON.toJSONString(transferResult));
			break;
		}

		Cursor cursor = context.getContentResolver().query(
				BusHistory.BusTransfer.CONTENT_URI,
				new String[] { BusHistory.BusTransfer._ID },
				BusHistory.BusTransfer.CITY + " = ? AND "
						+ BusHistory.BusTransfer.START_STATION + " = ? AND "
						+ BusHistory.BusTransfer.END_STATION + " = ?",
				new String[] { transferResult.getCity(),
						transferResult.getStartStation(),
						transferResult.getEndStation() }, null);
		if (cursor != null && cursor.getCount() == 1) {
			cursor.moveToFirst();

			long sqliteId = cursor.getLong(cursor
					.getColumnIndex(BusHistory.BusTransfer._ID));

			Uri updateUri = ContentUris.withAppendedId(
					BusHistory.BusTransfer.CONTENT_URI, sqliteId);
			context.getContentResolver().update(updateUri, values, null, null);

			return updateUri;
		} else {
			return context.getContentResolver().insert(
					BusHistory.BusTransfer.CONTENT_URI, values);
		}
	}

	public static List<BusHistoryRecord> getBusHisBusHistoryRecords(
			Context context) {
		List<BusHistoryRecord> recordList = new ArrayList<BusHistoryRecord>();

		Cursor cursor = null;
		try {
			// 获取线路查询历史记录
			cursor = context.getContentResolver().query(
					BusHistory.BusLine.CONTENT_URI,
					new String[] { BusHistory.BusLine.CITY,
							BusHistory.BusLine.NAME }, null, null, null);
			BusHistoryRecord historyRecord = null;
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToPrevious();

				while (cursor.moveToNext()) {
					historyRecord = new BusHistoryRecord();
					historyRecord.setType(BusHistoryRecord.Type.TYPE_LINE);
					historyRecord.setCity(cursor.getString(0));
					historyRecord.setLineName(cursor.getString(1));
					recordList.add(historyRecord);
				}
			}

			// 获取站点查询历史记录
			cursor = context.getContentResolver().query(
					BusHistory.BusSation.CONTENT_URI,
					new String[] { BusHistory.BusSation.CITY,
							BusHistory.BusSation.NAME }, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToPrevious();

				while (cursor.moveToNext()) {
					historyRecord = new BusHistoryRecord();
					historyRecord.setType(BusHistoryRecord.Type.TYPE_STATION);
					historyRecord.setCity(cursor.getString(0));
					historyRecord.setStation(cursor.getString(1));
					recordList.add(historyRecord);
				}
			}

			// 获取换乘查询历史记录
			cursor = context.getContentResolver().query(
					BusHistory.BusTransfer.CONTENT_URI,
					new String[] { BusHistory.BusTransfer.CITY,
							BusHistory.BusTransfer.START_STATION,
							BusHistory.BusTransfer.END_STATION }, null, null,
					null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToPrevious();

				while (cursor.moveToNext()) {
					historyRecord = new BusHistoryRecord();
					historyRecord.setType(BusHistoryRecord.Type.TYPE_TRANSFER);
					historyRecord.setCity(cursor.getString(0));
					historyRecord.setStationFrom(cursor.getString(1));
					historyRecord.setStationTo(cursor.getString(2));
					recordList.add(historyRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			MyAbsSQLiteOpenHelper.closeCursor(cursor);
		}

		return recordList;
	}

	public static void deleteBusLineHistory(Context context) {
		AssistantSQLiteHelper.getInstance().getWritableDatabase()
				.delete(BusHistory.BusLine.TABLE_NAME, null, null);
	}

	public static void deleteBusStationHistory(Context context) {
		AssistantSQLiteHelper.getInstance().getWritableDatabase()
				.delete(BusHistory.BusSation.TABLE_NAME, null, null);
	}

	public static void deleteBusTransferHistory(Context context) {
		AssistantSQLiteHelper.getInstance().getWritableDatabase()
				.delete(BusHistory.BusTransfer.TABLE_NAME, null, null);
	}

	public static void deleteAllBusHistory(Context context) {
		deleteBusLineHistory(context);
		deleteBusStationHistory(context);
		deleteBusTransferHistory(context);
	}
}
