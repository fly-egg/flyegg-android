package kr.flyegg.egg.domain.db;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.BaseColumns;

public class DBColumns implements BaseColumns {

	public static final String AUTHORITY = "kr.flyegg.egg";
	
	/*
	String word = null;
	String imgPath = null;
	String[] category = null;
	String[] tags = null;
	Bitmap thumbnail = null;
	*/
	
	public static final Uri CARD_URI		= Uri.parse("content://" + AUTHORITY  + "/CARD"); 
	
	public static final String CARD_TABLE 	= "CARD";//table name
	public static String CARD_ID 			= "_id";
	public static String CARD_WORD			= "word";
//	public static String CARD_TYPE 			= "CARDType";
	public static String CARD_IMGPATH		= "imgpath";
	public static String CARD_CATEGORY 		= "category";
	public static String CARD_TAGS	 		= "tags";
	//thumbnail byte stream? 
	public static String CARD_CREATEDDATE 	= "createdDate";
	
}
