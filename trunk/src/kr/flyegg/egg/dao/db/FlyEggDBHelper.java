package kr.flyegg.egg.dao.db;

import kr.flyegg.egg.dao.Category;
import kr.flyegg.egg.dao.CategoryAccesser;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FlyEggDBHelper extends SQLiteOpenHelper {
	private static final String TAG = "FlyEggDBHelper";

	private static final String DATABASE_NAME = "flyegg";
	private static final int DATABASE_VERSION = 1;

	private String lock = "db_lock";
	
	public FlyEggDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public FlyEggDBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	/**
	 * 최초 테이블 생성
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		synchronized (lock) {
			if (!db.isReadOnly()) {
				// Enable foreign key constraints
				db.execSQL("PRAGMA foreign_keys=ON;");
			}

			// 카테고리테이블 생성
			String categoryScheme = "CREATE TABLE " + DBColumns.CATEGORY_TABLE + "(\n" +
									DBColumns.CATEGORY_ID + " INTEGER PRIMARY KEY, \n" + 
									DBColumns.CATEGORY_NAME + " VARCHAR(300), \n" +  
									DBColumns.CATEGORY_CREATEDDATE + " BIGINT \n" +
									");";
			Log.d(TAG, categoryScheme);
			db.execSQL(categoryScheme);

			
			// 카드 테이블 생성
			String cardScheme = "CREATE TABLE " + DBColumns.CARD_TABLE + "(\n" +
								DBColumns.CARD_ID + " INTEGER PRIMARY KEY, \n" + 
								DBColumns.CARD_WORD + " VARCHAR(100), \n" +  
					//			DBColumns.CARD_IMGPATH + " VARCHAR(500), " + 	// imgpath 는 card_id 로 대체함. Internal Storage 에 id.png 로 저장하도록 변경
					//			DBColumns.CARD_CATEGORY + " VARCHAR(500), " +	// 카테고리와 연결하기 위해 카테고리 키와 자료형 통일
								DBColumns.CARD_CATEGORY + " INTEGER NOT NULL, \n" +	// INTEGER 라고 선언은 했지만 문자열도 들어가니 주의
//								DBColumns.CARD_CATEGORY + " INTEGER REFERENCES " + DBColumns.CATEGORY_TABLE + "(" + DBColumns.CATEGORY_ID + "), \n" +
								DBColumns.CARD_TAGS + " VARCHAR(500), \n" + 
								//" BIGINT, " +
								//DBColumns.SERVER_MANAGE_APP_LIST_SOFTVERSION + " VARCHAR(50), " +
								//DBColumns.CARD_NUMBER + " TEXT, " + 
								DBColumns.CARD_CREATEDDATE + " BIGINT, \n" +
								// 적어 두긴 했지만 foreign key 를 지원하지 않음..
								"FOREIGN KEY(" + DBColumns.CARD_CATEGORY + ") REFERENCES " + DBColumns.CATEGORY_TABLE + "(" + DBColumns.CATEGORY_ID + ") \n" + 
								");";
			Log.d(TAG, cardScheme);
			db.execSQL(cardScheme);
			
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		int version = oldVersion;
		
		if(version != DATABASE_VERSION) {
			db.execSQL("DROP TABLE IF EXISTS " + DBColumns.CARD_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + DBColumns.CATEGORY_TABLE);
		}
	}

}

