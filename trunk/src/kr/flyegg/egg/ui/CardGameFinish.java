package kr.flyegg.egg.ui;

import kr.flyegg.egg.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
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
	private int mLevel;	// 인자로 받은 레벨	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		// ------------------------
		// 화면 설정
		setContentView(R.layout.activity_cardgame_finish);
		
		Intent intent = getIntent();
		mLevel = intent.getIntExtra(CardGameMain.EXTRA_LEVEL, 0);
		
		TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("메모리 게임 " + mLevel + "단계");
		
		TextView tvContratulations = (TextView) findViewById(R.id.tvCongratulations);
		tvContratulations.setText(mLevel + "단계를 모두 맞췄군요! 참 잘했어요!!");
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
			// 메인으로
			Intent intent = new Intent();
			intent.putExtra("TO_MAIN", true);
			
			setResult(Activity.RESULT_OK, intent);
			
			finish();
		} else if (v.getId() == R.id.btnRetry) {
			// 다시하기
			Intent intent = new Intent();
			intent.putExtra("RETRY", true);
			
			setResult(Activity.RESULT_OK, intent);
			
			finish();
		}
		else if (v.getId() == R.id.btnNext) {
			// 다음레벨
			Intent intent = new Intent();
			intent.putExtra("NEXT", true);
			
			setResult(Activity.RESULT_OK, intent);
			
			finish();
		} else {
			Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
			return;
		}
	}
}
