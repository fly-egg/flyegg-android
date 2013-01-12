package kr.flyegg.egg.dao;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import kr.flyegg.egg.dao.db.DBColumns;

public class CategoryAccesser {
	
	private Context mContext = null;

	public CategoryAccesser(Context context) {
		mContext  = context;
	}
	
	/**
	 * 카테고리 등록
	 * @param category
	 * @return
	 */
	public String insert(Category category) {
		ContentValues values = new ContentValues();
    	values.clear();
    	
   		values.put(DBColumns.CATEGORY_NAME, category.getCategory());
    	values.put(DBColumns.CATEGORY_CREATEDDATE, System.currentTimeMillis());
    	
    	ContentResolver resolver = mContext.getContentResolver();
		Uri uri = resolver.insert(DBColumns.CATEGORY_URI, values);
		
		return uri.getLastPathSegment();
	}

	/**
	 * 카테고리 수정
	 * @param category
	 * @return
	 */
	public int update(Category category) {
		ContentValues values = new ContentValues();
    	values.clear();
    	
   		values.put(DBColumns.CATEGORY_NAME, category.getCategory());
    	values.put(DBColumns.CATEGORY_CREATEDDATE, System.currentTimeMillis());
    	
    	ContentResolver resolver = mContext.getContentResolver();
    	String where = "_id = " + category.get_id();
		return resolver.update(DBColumns.CATEGORY_URI, values, where, null);
	}
	
	/**
	 * 카테고리 삭제
	 * @param category
	 */
	public int delete(String _id) {
		ContentResolver resolver = mContext.getContentResolver();
		String where = "_id = " + _id;
		return resolver.delete(DBColumns.CATEGORY_URI, where, null);

		/*
		Cursor cursor = null;
		
		try {
			String selection = DBColumns.CATEGORY_NAME + " == ? ";
			
			cursor = mContext.getContentResolver().query(
					DBColumns.CATEGORY_URI, 
					null, 
					selection, 
					new String[]{ category },  
					null);
		
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(cursor != null)
				cursor.close();
			cursor = null;
		}
		*/
	}
	
	/**
	 * 전체 삭제
	 */
	public void deleteAll() {
		ContentResolver resolver = mContext.getContentResolver();
		resolver.delete(DBColumns.CATEGORY_URI, null, null);
	}

	/**
	 * 조회
	 * @return
	 */
	public Category getCategory(String _id) {
		Category category = null;

		ContentResolver resolver = mContext.getContentResolver();
		String where = "id = " + _id;
		Cursor cursor = resolver.query(DBColumns.CATEGORY_URI, null, where, null, null);
		
		int idIndex = cursor.getColumnIndex(DBColumns.CATEGORY_ID);
		int nameIndex = cursor.getColumnIndex(DBColumns.CATEGORY_NAME);
		
		if (cursor == null) {
			// 조회 된 값이 없거나 오류
		} else {
			String id = cursor.getString(idIndex);
			String name = cursor.getString(nameIndex);
			category = new Category(id, name);
		}

		return category;
	}

	/**
	 * 카테고리들 정보 불러 오기
	 * @return
	 */
	public List<Category> getCategories() {
		List<Category> list = new LinkedList<Category>();
		
		Cursor cursor = null;

		try {
			cursor = mContext.getContentResolver().query(DBColumns.CATEGORY_URI, 
				null, 
				null, 
				null, 
				null);
		
		if (cursor.getCount() > 0 && cursor.moveToFirst()) {
			while(true) {
				String _id = cursor.getString(cursor.getColumnIndex(DBColumns.CATEGORY_ID));
				String name = cursor.getString(cursor.getColumnIndex(DBColumns.CATEGORY_NAME));

				list.add(new Category(_id, name));
				
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
