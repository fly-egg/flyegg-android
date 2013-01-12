package kr.flyegg.egg.dao.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DBColumns implements BaseColumns {

	public static final String AUTHORITY = "kr.flyegg.egg.FlyEggProvider";
	
	/*
	String word = null;
	String imgPath = null;
	String[] category = null;
	String[] tags = null;
	Bitmap thumbnail = null;
	*/
	
	// 카드
	public static final Uri CARD_URI		= Uri.parse("content://" + AUTHORITY  + "/CARD"); 
	
	public static final String CARD_TABLE 	= "CARD";//table name
	public static String CARD_ID 			= "_id";
	public static String CARD_WORD			= "word";
//	public static String CARD_IMGPATH		= "imgpath";	// id 로 대체
	public static String CARD_CATEGORY 		= "category";
	public static String CARD_TAGS	 		= "tags";
	//thumbnail byte stream? 
	public static String CARD_CREATEDDATE 	= "createdDate";
	
	
	// 카테고리
	public static final Uri CATEGORY_URI		= Uri.parse("content://" + AUTHORITY  + "/CATEGORY"); 
	
	public static final String CATEGORY_TABLE 	= "CATEGORY";//table name
	public static String CATEGORY_ID 			= "_id";
	public static String CATEGORY_NAME			= "name";
	public static String CATEGORY_CREATEDDATE 	= "createdDate";
	
}
