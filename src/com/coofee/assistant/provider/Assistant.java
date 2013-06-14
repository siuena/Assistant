package com.coofee.assistant.provider;

import android.net.Uri;
import android.provider.BaseColumns;


public class Assistant {
	public static final String DB_NAME = "assitant.db";
	public static final int DB_VERSION = 7;
	public static final int NOT_SET = -1;
	
	// weather_area表的信息
	public static final int WEATHER_AREA_ITEM = 1;
	public static final String TYPE_WEATHER_AREA_ITEM = "vnd.android.cursor.dir/vnd.coofee.assistant.area";
	public static final int WEATHER_AREA_ITEM_ID = 2;
	public static final String TYPE_WEATHER_AREA_ITEM_ID = "vnd.android.cursor.item/vnd.coofee.assistant.area";
	
	// 天气历史信息表
	public static final int WEATHER_HISTORY_ITEM = 3;
	public static final String TYPE_WEATHER_HISTORY_ITEM = "vnd.android.cursor.dir/vnd.coofee.assistant.history";
	public static final int WEATHER_HISTORY_ITEM_ID = 4;
	public static final String TYPE_WEATHER_HISTORY_ITEM_ID = "vnd.android.cursor.item/vnd.coofee.assistant.history";
	
	// 书籍基本信息表
	public static final int BOOK_BASIC_ITEM = 5;
	public static final String TYPE_BOOK_BASIC_ITEM = "vnd.android.cursor.dir/vnd.coofee.assistant.book";
	public static final int BOOK_BASIC_ITEM_ID = 6;
	public static final String TYEP_BOOK_BASIC_ITEM_ID = "vnd.android.cursor.item/vnd.coofee.assistan.book";
	
	// 单词本表
	public static final int NOTE_ITEM = 7;
	public static final String TYPE_NOTE_ITEM = "vnd.android.cursor.dir/vnd.coofee.assistant.note";
	public static final int NOTE_ITEM_ID = 8;
	public static final String TYPE_NOTE_ITEM_ID = "vnd.android.cursor.item/vnd.coofee.assistant.note";
	
	// 单词表
	public static final int WORD_ITEM = 9;
	public static final String TYPE_WORD_ITEM = "vnd.android.cursor.dir/vnd.coofee.assistant.word";
	public static final int WORD_ITEM_ID = 10;
	public static final String TYPE_WORD_ITEM_ID = "vnd.android.cursor.item/vnd.coofee.assistant.word";
	
	// 线路查询历史表
	public static final int BUS_LINE_ITEM = 11;
	public static final String TYPE_BUS_LINE_ITEM = "vnd.android.cursor.dir/vnd.coofee.assistant.busLine";
	public static final int BUS_LINE_ITEM_ID = 12;
	public static final String TYPE_BUS_LINE_ITEM_ID = "vnd.android.cursor.item/vnd.coofee.assistant.busLine";
	// 换乘查询历史表
	public static final int BUS_TRANSFER_ITEM = 13;
	public static final String TYPE_BUS_TRANSFER_ITEM = "vnd.android.cursor.dir/vnd.coofee.assistant.busTransfer";
	public static final int BUS_TRANSFER_ITEM_ID = 14;
	public static final String TYPE_BUS_TRANSFER_ITEM_ID = "vnd.android.cursor.item/vnd.coofee.assistant.busTransfer";
	// 站点查询历史表
	public static final int BUS_STATION_ITEM = 15;
	public static final String TYPE_BUS_STATION_ITEM = "vnd.android.cursor.dir/vnd.coofee.assistant.busStation";
	public static final int BUS_STATION_ITEM_ID = 16;
	public static final String TYPE_BUS_STATION_ITEM_ID = "vnd.android.cursor.item/vnd.coofee.assistant.busStation";
	
	public static final class Weather {
		public static final class Area implements BaseColumns {
			public static final String TABLE_NAME = "weather_area";
			
			/** Weather Area 表的URI地址*/
			public static final Uri CONTENT_URI = Uri.parse("content://" + MyAbsSQLiteOpenHelper.AUTOHORITY + "/" + TABLE_NAME);
			
			/** 地区天气编码 */
			public static final String WEATHER_CODE = "weather_code";
			/** 地区名称 */
			public static final String NAME = "area_name";
			/** 地区所在的省的名称 */
			public static final String PROVINCE = "province";
			/** 地区类型 */
			public static final String TYPE = "type";
			/** 地区邮政编码  */
			public static final String ZIPCODE = "zipcode";
			/** 地区区号 */
			public static final String PHONE_CODE = "phone_code";
			/** 地区名称汉语拼音全拼  */
			public static final String HYPY = "hypy";
			/** 地区名称汉语拼音首字母 */
			public static final String SHORT_HYPY = "short_hypy";
		}
		
		/**
		 * 天气历史表
		 * @author zhao
		 *
		 */
		public static final class History implements BaseColumns {
			public static final String TABLE_NAME = "weather_history";
			
			public static final Uri CONTENT_URI = Uri.parse("content://" + MyAbsSQLiteOpenHelper.AUTOHORITY + "/" + TABLE_NAME);
			
			/** 地区名称 */
			public static final String AREA_NAME = "weather_area_name";
			/** 天气时间 */
			public static final String DATE = "weather_date";
			/** 白天的天气状态  */
			public static final String DAY_STATUS = "day_status";
			/** 晚上的天气状态 */
			public static final String NIGHT_STATUS = "night_status";
			/** 白天的温度 */
			public static final String DAY_TEMPERATURE = "day_temp";
			/** 晚上的温度 */
			public static final String NIGHT_TEMPERATURE = "night_temp";
		}
	}
	
	
	public static final class Book {
		/**
		 * 书籍表
		 * @author zhao
		 *
		 */
		public static final class BookBasicInfo implements BaseColumns {
			public static final String TABLE_NAME = "book_basic_info";
			public static final Uri CONTENT_URI = Uri.parse("content://" + MyAbsSQLiteOpenHelper.AUTOHORITY + "/" + TABLE_NAME);
			/** 书籍的ISBN */
			public static final String ISBN = "isbn";
			/** 书名 */
			public static final String NAME = "name";
			/** 作者信息 */
			public static final String AUTHOR_INTRO = "author_intro";
			/** 书籍的基本信息 */
			public static final String BOOK_BASIC_INFO_JSON = "book_basic_info_json";
		}
	}
	
	public static final class ZhEn {
		
		public static final class Note implements BaseColumns {
			public static final String TABLE_NAME = "note";
			public static final Uri CONTENT_URI = Uri.parse("content://" + MyAbsSQLiteOpenHelper.AUTOHORITY + "/" + TABLE_NAME);
			
			/** 单词本名称 */
			public static final String NAME = "name";
			/** 单词本备注 */
			public static final String MEMO = "memo";
		}
		
		public static final class Word implements BaseColumns {
			public static final String TABLE_NAME = "word";
			public static final Uri CONTENT_URI = Uri.parse("content://" + MyAbsSQLiteOpenHelper.AUTOHORITY + "/" + TABLE_NAME);
			
			/** 单词名称 */
			public static final String NAME = "name";
			/** 单词释义 */
			public static final String MEANING = "meaning";
			/** 样例 */
			public static final String SAMPLE = "sample";
			/** 备注 */
			public static final String MEMO = "memo";
			
			/** Note外键id */
			public static final String NOTE_ID = "note_id";
			
		}
	}
	
	public static final class BusHistory {
		public static final class BusLine implements BaseColumns {
			public static final String TABLE_NAME = "bus_line_results";
			public static final Uri CONTENT_URI = Uri.parse("content://" + MyAbsSQLiteOpenHelper.AUTOHORITY + "/" + TABLE_NAME);
		
			/** 线路关键字 */
			public static final String NAME = "name";
			public static final String CITY = "city";
			public static final String LINES_JSON = "lines";
		}
		
		public static final class BusSation implements BaseColumns {
			public static final String TABLE_NAME = "bus_station_results";
			public static final Uri CONTENT_URI = Uri.parse("content://" + MyAbsSQLiteOpenHelper.AUTOHORITY + "/" + TABLE_NAME);
			
			/** 站点查询关键字 */
			public static final String NAME = "name";
			public static final String CITY = "city";
			public static final String STATIONS_JSON = "stations";
		}
		
		public static final class BusTransfer implements BaseColumns {
			public static final String TABLE_NAME = "bus_transfer_results";
			public static final Uri CONTENT_URI = Uri.parse("content://" + MyAbsSQLiteOpenHelper.AUTOHORITY + "/" + TABLE_NAME);
			
			public static final String CITY = "city";
			public static final String START_STATION = "start_station";
			public static final String END_STATION = "end_station";
			/** 默认换乘方案的JSON文本 */
			public static final String DEFAULT_PLAN_JSON = "default_plan";
			/** 换乘次数最少方案的JSON文本 */
			public static final String LEAST_TRANSFER_TIMES_JSON = "least_Transfer_Times_Json";
			/** 步行距离最短方案的JSON文本 */
			public static final String WALK_SHORTEST_DISTANCE_JSON = "walk_Shortest_Distance_Json";
			/** 时间最短方案的JSON文本 */
			public static final String TIME_SHORTEST_JSON = "time_Shortest_Json";
			/** 总距离最短方案的JSON文本 */
			public static final String TOTAL_DISTANCE_SHORTEST_JSON = "total_Distance_Shortest_Json";
			/** 地铁优先方案的JSON文本 */
			public static final String SUBWAY_PRIORITY_JSON = "subway_Priority_Json";
		}
	}
}
