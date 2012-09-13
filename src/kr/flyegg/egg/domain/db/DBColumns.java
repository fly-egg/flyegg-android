package kr.flyegg.egg.domain.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DBColumns implements BaseColumns {

	public static final String AUTHORITY = "kr.flyegg.egg";
	
	
	public static final Uri CARD_URI		= Uri.parse("content://" + AUTHORITY  + "/CARD"); 
	
	public static final String CARD_TABLE 	= "CARD";//table name
	public static String CARD_ID 			= "_id";
	public static String CARD_TYPE 			= "CARDType";
	public static String CARD_DURATION 		= "duration";
	public static String CARD_NUMBER 		= "number";
	public static String CARD_CREATEDDATE 	= "createdDate";
	
}
