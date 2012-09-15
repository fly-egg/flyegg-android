package kr.flyegg.egg.ui.tabview;

import kr.flyegg.egg.R;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

public class TabViewActivity extends Activity {

	public static String tab1 = null;
	public static String tab2 = null;
	public static String tab3 = null;
	public static String tab4 = null;
	public static String tab5 = null;
	public static String tab6 = null;
	public static String tab7 = null;
	public static String tab8 = null;
	public static String tab9 = null;
	public static String tab10 = null;
	
	public static final String PROROSAL = "tab_name"; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabs);
		
		///////////////////////////////////////////////////////////////////////////////////
		//sticker
		Gallery g = (Gallery) findViewById(R.id.gallery);
        // Set the adapter to our custom adapter (below)
        g.setAdapter(new ImageAdapter(this));
        
        // Set a item click listener, and just Toast the clicked position
        g.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(TabViewActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
	}
	
	
	
	
	@Override
	protected void onStart() {
		super.onStart();
		
		Bundle bundle = getIntent().getExtras();
		if(bundle != null) {
			String type = bundle.getString(PROROSAL);
			//type에 따라 이미지 바꿔 로딩하기
			Log.d(getClass().getName(), "********************" + type);
			loadImage();
		}
		
	}


	private void loadImage() {
		
	}
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	
	
}
