package kr.flyegg.egg.ui;

import kr.flyegg.egg.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * 카드게임 3판 끝내고 나오는 액티비티
 * 메인, 다시하기, 다음단계로 이동
 * 
 * @author junho85
 * 
 */
public class CardGameFinish extends Activity {

	private static final String TAG = "CardGameFinish";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		// ------------------------
		// 화면 설정
		setContentView(R.layout.activity_cardgame_finish);
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}
	
	/**
	 * 버튼 클릭
	 * @param v
	 */
	public void onClick(View v) {

		if (v.getId() == R.id.btnMain) {
			finish();
			
			
		} else if (v.getId() == R.id.btnRetry) {
			
		}
		else if (v.getId() == R.id.btnNext) {
			
			finish();
		} else {
			Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
			return;
		}
	}
}
