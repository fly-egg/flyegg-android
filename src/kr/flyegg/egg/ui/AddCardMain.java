package kr.flyegg.egg.ui;

import kr.flyegg.egg.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * 카드추가 메인 Activity
 */
public class AddCardMain extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addcard_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
	    // 레벨 클릭 처리
		OnClickListener levelClickListener = new OnClickListener() {

			public void onClick(View v) {
				
				int level = 0;	// 선택레벨
				
				if (v.getId() == R.id.btnAddCardFromGallery) {
					Toast.makeText(getApplicationContext(), "btnAddCardFromGallery", Toast.LENGTH_SHORT).show();
				} else if (v.getId() == R.id.btnAddCardFromCamera) {
					Toast.makeText(getApplicationContext(), "btnAddCardFromCamera", Toast.LENGTH_SHORT).show();
				} else if (v.getId() == R.id.btnOK) {
					Toast.makeText(getApplicationContext(), "btnOK", Toast.LENGTH_SHORT).show();
				} else if (v.getId() == R.id.btnCancel) {
					finish();
				} else {
					Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
					return;
				}
				
		    	Intent intent = new Intent(getApplicationContext(), CardGameRun.class);
		    	intent.putExtra("LEVEL", level);
		    	
		    	startActivity(intent);
//		    	startActivityForResult(intent, level);
			}
		};
		
		// 레벨 클릭 이벤트 연결
		findViewById(R.id.btnAddCardFromGallery).setOnClickListener(levelClickListener);
		findViewById(R.id.btnAddCardFromCamera).setOnClickListener(levelClickListener);
		findViewById(R.id.btnOK).setOnClickListener(levelClickListener);
		findViewById(R.id.btnCancel).setOnClickListener(levelClickListener);
		
		
	}

	
	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

}
