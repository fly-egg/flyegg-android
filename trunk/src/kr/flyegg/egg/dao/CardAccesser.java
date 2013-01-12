package kr.flyegg.egg.dao;

import java.util.LinkedList;
import java.util.List;

import kr.flyegg.egg.dao.db.DBColumns;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class CardAccesser {
	
	private static final String TAG = "CardAccesser";

	private Context mContext = null;

	public CardAccesser(Context context) {
		mContext  = context;
	}
	
	/**
	 * 등록
	 * @param card
	 * @return 생성된 키값
	 */
	public String insert(Card card) {
		ContentValues values = new ContentValues();
    	values.clear();
    	
   		values.put(DBColumns.CARD_WORD, card.getWord());
		values.put(DBColumns.CARD_CATEGORY, card.getCategory());
    	values.put(DBColumns.CARD_TAGS, card.tagToString(card.getTags()));
    	values.put(DBColumns.CARD_CREATEDDATE, System.currentTimeMillis());
    	
    	ContentResolver resolver = mContext.getContentResolver();
		Uri uri = resolver.insert(DBColumns.CARD_URI, values);
		
		return uri.getLastPathSegment();
	}
	
	/**
	 * 삭제
	 * @param imgpath
	 */
	/*
	public void delete(String imgpath) {
		Cursor cursor= null;
		
		try {
			cursor = mContext.getContentResolver().query(DBColumns.CARD_URI, 
				null, 
				DBColumns.CARD_IMGPATH + " == ? ", 
				new String[]{ imgpath },  
				null);
		
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(cursor != null)
				cursor.close();
			cursor = null;
		}
	}
	*/
	
	public void deleteAll() {
		ContentResolver resolver = mContext.getContentResolver();
		resolver.delete(DBColumns.CARD_URI, null, null);
	}
	
	public List<Card> getCardList(String category) {
		List<Card> list = new LinkedList<Card>();
		
		Cursor cursor= null;
		
		try {
			cursor = mContext.getContentResolver().query(DBColumns.CARD_URI, 
				null, 
				DBColumns.CARD_CATEGORY + " = ? ", 
				new String[]{ category },  
				null);
		
		if(cursor.getCount() > 0 && cursor.moveToFirst()) {
			while(true) {
				String _id			= cursor.getString(cursor.getColumnIndex(DBColumns.CARD_ID));
				String word     	= cursor.getString(cursor.getColumnIndex(DBColumns.CARD_WORD));
				String tags			= cursor.getString(cursor.getColumnIndex(DBColumns.CARD_TAGS));
				String _category 	= cursor.getString(cursor.getColumnIndex(DBColumns.CARD_CATEGORY));
				
//				public Card(String word, String imgPath, String category, String tags) {
				list.add(new Card(_id, word, _category, tags));
				//thumbnail 부분 확인하기
				//list.add(new Card(imgpath, word, tags));
				
				if(cursor.isLast()) {
					break;
				}
				cursor.moveToNext();
			}
		}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(cursor != null)
				cursor.close();
			cursor = null;
		}
			
		return list;	
	}
	
	public List<Card> getCardList() {
		List<Card> list = new LinkedList<Card>();
		
		Cursor cursor= null;
		
		try {
			cursor = mContext.getContentResolver().query(DBColumns.CARD_URI, 
				null, 
				null, 
				null, 
				null);
		
		if(cursor.getCount() > 0 && cursor.moveToFirst()) {
			while(true) {
				String _id			= cursor.getString(cursor.getColumnIndex(DBColumns.CARD_ID));
				String word     	= cursor.getString(cursor.getColumnIndex(DBColumns.CARD_WORD));
				String tags			= cursor.getString(cursor.getColumnIndex(DBColumns.CARD_TAGS));
				String category 	= cursor.getString(cursor.getColumnIndex(DBColumns.CARD_CATEGORY));
				
//				public Card(String word, String imgPath, String category, String tags) {
				list.add(new Card(_id, word, category, tags));
				//thumbnail 부분 확인하기
				//list.add(new Card(imgpath, word, tags));
				
				if(cursor.isLast()) {
					break;
				}
				cursor.moveToNext();
			}
		}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(cursor != null)
				cursor.close();
			cursor = null;
		}
			
		return list;
	}
	
}
