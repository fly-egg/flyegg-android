package kr.flyegg.egg.ui;

import kr.flyegg.egg.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * 카드게임 클리어 팝업
 * 
 * @author junho85
 * 
 */
public class CardGameClearPopup extends Activity {

	private static final String TAG = "CardGameClearPopup";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		WindowManager.LayoutParams window = new WindowManager.LayoutParams();
		window.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		window.dimAmount = 0.75f;
		getWindow().setAttributes(window);
		
		// ------------------------
		// 화면 설정
		setContentView(R.layout.activity_cardgame_clear_popup);
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

		if (v.getId() == R.id.btnNext) {
			finish();
		} else {
			Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
			return;
		}
	}
}
