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
 * 카드게임 메인 Activity
 * 
 * @author junho85
 * 
 */
public class CardGameMain extends Activity {

	// Intent request codes
	private static final int LEVEL_1 = 1;
	private static final int LEVEL_2 = 2;
	private static final int LEVEL_3 = 3;
	
	public static final String EXTRA_LEVEL = "LEVEL";
	public static final String EXTRA_CATEGORY = "CATEGORY";
	public static final String EXTRA_TAG = "TAG";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cardgame_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		// 레벨 클릭 처리
		OnClickListener levelClickListener = new OnClickListener() {

			public void onClick(View v) {

				int level = 0; // 선택레벨

				if (v.getId() == R.id.btnGameEasy) {
					level = LEVEL_1;
				} else if (v.getId() == R.id.btnGameNormal) {
					level = LEVEL_2;
				} else if (v.getId() == R.id.btnGameHard) {
					level = LEVEL_3;
				} else {
					Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
					return;
				}
				
				// TODO: 카테고리, 테그를 선택한거 사용하도록
				String category = "test";
				String tag = "test";

				Intent intent = new Intent(getApplicationContext(), CardGameRun.class);
				intent.putExtra(EXTRA_LEVEL, level);
				intent.putExtra(EXTRA_CATEGORY, category);
				intent.putExtra(EXTRA_TAG, tag);

				startActivity(intent);
				// startActivityForResult(intent, level);
			}
		};

		// 레벨 클릭 이벤트 연결
		findViewById(R.id.btnGameEasy).setOnClickListener(levelClickListener);
		findViewById(R.id.btnGameNormal).setOnClickListener(levelClickListener);
		findViewById(R.id.btnGameHard).setOnClickListener(levelClickListener);

	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

}
