package kr.flyegg.egg.dao.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class FlyEggProvider extends ContentProvider {

	private FlyEggDBHelper mOpenHelper;
	private static final int CARD 						= 1;
	private static final int CATEGORY					= 2;
	
	private static final UriMatcher sUriMatcher;
	
    static {
    	sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    	sUriMatcher.addURI(DBColumns.AUTHORITY, DBColumns.CARD_TABLE, CARD);
    	sUriMatcher.addURI(DBColumns.AUTHORITY, DBColumns.CATEGORY_TABLE, CATEGORY);
    }
    
    
    
	@Override
	public boolean onCreate() {
		mOpenHelper = new FlyEggDBHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		
		if(sUriMatcher.match(uri) == CARD) {
			qb.setTables(DBColumns.CARD_TABLE);
			
		} else if(sUriMatcher.match(uri) == CATEGORY) {
			qb.setTables(DBColumns.CATEGORY_TABLE);
		}
		
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		
		return c;
	}

	
	
	
	@Override
	public String getType(Uri uri) {
		return null;
	}

	
	/**
	 * 삽입
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId = 0;
		
		switch (sUriMatcher.match(uri)) {
		case CARD : 
			rowId = db.insert(DBColumns.CARD_TABLE, null, values);
			if(rowId > 0) {
				Uri retUri = ContentUris.withAppendedId(DBColumns.CARD_URI, rowId);
				getContext().getContentResolver().notifyChange(retUri, null);
				return retUri;
			}
			break;
			
		case CATEGORY : 
			rowId = db.insert(DBColumns.CATEGORY_TABLE, null, values);
			if(rowId > 0) {
				Uri retUri = ContentUris.withAppendedId(DBColumns.CATEGORY_URI, rowId);
				getContext().getContentResolver().notifyChange(retUri, null);
				return retUri;
			}
			break;
		
		default: throw new IllegalArgumentException("Unknown URI " + uri);
		}
		throw new SQLException("Failed to insert row into " + uri); 
	}

	/**
	 * 삭제
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count = 0;
		
		switch(sUriMatcher.match(uri)) {
		case CARD : 
			count = db.delete(DBColumns.CARD_TABLE, selection, selectionArgs);
			break;
			
		case CATEGORY : 
			count = db.delete(DBColumns.CATEGORY_TABLE, selection, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknwon URI " + uri);
		}
		
		return count;
	}

	
	/**
	 * 수정
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count = 0;
		
		switch (sUriMatcher.match(uri)) {
		case CARD : 
			count = db.update(DBColumns.CARD_TABLE, values, selection, selectionArgs);
			break;
			
		case CATEGORY : 
			count = db.update(DBColumns.CATEGORY_TABLE, values, selection, selectionArgs);
			break;
		default: throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
