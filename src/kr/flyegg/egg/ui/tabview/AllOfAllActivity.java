package kr.flyegg.egg.ui.tabview;

import kr.flyegg.egg.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AllOfAllActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_tabs);
		
		///////////////////////////////////////////////////////////////////////////////////
		//sticker
		Gallery g = (Gallery) findViewById(R.id.gallery);
        // Set the adapter to our custom adapter (below)
        g.setAdapter(new ImageAdapter(this));
        
        // Set a item click listener, and just Toast the clicked position
        g.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(AllOfAllActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
        
        
        
        
		
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
