package kr.flyegg.egg.dao;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import kr.flyegg.egg.dao.db.DBColumns;

public class CategoryAccesser {
	
	private Context mContext = null;

	public CategoryAccesser(Context context) {
		mContext  = context;
	}
	
	public void insert(Category category) {
		ContentValues values = new ContentValues();
    	values.clear();
    	
   		values.put(DBColumns.CATEGORY_NAME, category.getCategory());
    	values.put(DBColumns.CATEGORY_CREATEDDATE, System.currentTimeMillis());
    	
    	ContentResolver resolver = mContext.getContentResolver();
		resolver.insert(DBColumns.CATEGORY_URI, values);
	}
	
	public void delete(String category) {
		Cursor cursor= null;
		
		try {
			cursor = mContext.getContentResolver().query(DBColumns.CATEGORY_URI, 
				null, 
				DBColumns.CATEGORY_NAME + " == ? ", 
				new String[]{ category },  
				null);
		
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(cursor != null)
				cursor.close();
			cursor = null;
		}
	}
	
	public void deleteAll() {
		ContentResolver resolver = mContext.getContentResolver();
		resolver.delete(DBColumns.CATEGORY_URI, null, null);
	}
	
	
	public List<Category> getCategory() {
		List<Category> list = new LinkedList<Category>();
		
		Cursor cursor= null;
		
		try {
			cursor = mContext.getContentResolver().query(DBColumns.CATEGORY_URI, 
				null, 
				null, 
				null, 
				null);
		
		if(cursor.getCount() > 0 && cursor.moveToFirst()) {
			while(true) {
				String name = cursor.getString(cursor.getColumnIndex(DBColumns.CATEGORY_NAME));

				list.add(new Category(name));
				
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
