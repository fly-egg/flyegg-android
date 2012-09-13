package kr.flyegg.egg.domain.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class FlyEggDBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "flyegg";
	private static final int DATABASE_VERSION = 1;
	
	
	private String lock = "db_lock";
	
	public FlyEggDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public FlyEggDBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		synchronized (lock) {
			
			db.execSQL("CREATE TABLE " + DBColumns.CARD_TABLE + "(" +
				DBColumns.CARD_ID + " INTEGER PRIMARY KEY, " + 
				DBColumns.CARD_WORD + " VARCHAR(100), " +  
				DBColumns.CARD_IMGPATH + " VARCHAR(500), " + 
				DBColumns.CARD_CATEGORY + " VARCHAR(500), " +
				DBColumns.CARD_TAGS + " VARCHAR(500), " + 
				//" BIGINT, " +
				//DBColumns.SERVER_MANAGE_APP_LIST_SOFTVERSION + " VARCHAR(50), " +
				//DBColumns.CARD_NUMBER + " TEXT, " + 
				DBColumns.CARD_CREATEDDATE + " BIGINT);" );
		
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		int version = oldVersion;
		
		if(version != DATABASE_VERSION) {
			db.execSQL("DROP TABLE IF EXISTS " + DBColumns.CARD_TABLE);
		}
	}

}
