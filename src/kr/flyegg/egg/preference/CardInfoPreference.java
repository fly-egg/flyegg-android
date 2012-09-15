package kr.flyegg.egg.preference;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class CardInfoPreference {

	private static CardInfoPreference _This 	= null;
	
	private SharedPreferences cardInfo			= null;
	private SharedPreferences.Editor editor 	= null;
	
	private String fileName = "cardinfo";
	
	public CardInfoPreference() {
	}
	
	public CardInfoPreference(Context context) {
		cardInfo = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE);
		editor = cardInfo.edit();
	}
	
	public static CardInfoPreference getInstance(Context context) {
		if(_This == null)
			_This = new CardInfoPreference(context);
		
		return _This;
	}
	
	private final String PROPERTY_SELECTED_CATEGORY 					= "category";
	private final String PROPERTY_SELECTED_TAG 							= "tag";
	
	
	public void setPROPERTY_SELECTED_CATEGORY(String category) {
		editor.putString(PROPERTY_SELECTED_CATEGORY, category);
		editor.commit();
		
	}
	
	public void setPROPERTY_SELECTED_TAG(String tag) {
		editor.putString(PROPERTY_SELECTED_TAG, tag);
		editor.commit();
	}
	
	public String getPROPERTY_SELECTED_CATEGORY() {
		return cardInfo.getString(PROPERTY_SELECTED_CATEGORY, "");
	}

	public String getPROPERTY_SELECTED_TAG() {
		return cardInfo.getString(PROPERTY_SELECTED_TAG, "");
	}

	
}
