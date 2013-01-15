package kr.flyegg.egg;

import kr.flyegg.egg.dao.Category;
import kr.flyegg.egg.dao.CategoryAccesser;
import kr.flyegg.egg.dao.db.FlyEggDBHelper;
import kr.flyegg.egg.ui.CardBoard;
import kr.flyegg.egg.ui.CardGameMain;
import kr.flyegg.egg.ui.SmartCardMain;
import kr.flyegg.egg.ui.MirrorMain;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

/**
 * 가장 기본이 되는 Activity
 */
public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";

	private Button btnSmartCard = null;
	private Button btnGame = null;
	private Button btnMirror = null;
	private Button btnSelectCard = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // prevent to turn off window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        
        btnSmartCard = (Button) findViewById(R.id.btnsmartCard);
        btnSmartCard.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				callCardFlipActivity();
			}
		});
        
        btnGame = (Button) findViewById(R.id.btnGame);
        btnGame.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				callGameCardActivity();
			}
		});
        
        btnMirror = (Button) findViewById(R.id.btnMirror);
        btnMirror.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				callMirrorActivity();
			}
		});
        
        btnSelectCard = (Button) findViewById(R.id.btnSelectCard);
        btnSelectCard.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				callSelectCardActivity();
			}
		});
        
        // DB 생성
        createDB();

        // 등록된 카테고리가 없을 경우 기본 카테고리 등록
    	CategoryAccesser accesser = new CategoryAccesser(getApplicationContext());
    	if (accesser.getCategories().size() == 0) {
            // 초기 실행 시 초기 데이터 생성
	    	// 기본 카테고리 등록
	    	Log.i(TAG, "add default categories");
	    	addCategory("과일야채");
    	}
    }
    
	/**
	 * DB 생성
	 */
	private void createDB() {
		FlyEggDBHelper helper = new FlyEggDBHelper(getApplicationContext());
		helper.getWritableDatabase();
		helper.close();
	}

	/**
	 * 카테고리 추가
	 */
	public void addCategory(String _categoryName) {
    	CategoryAccesser accesser = new CategoryAccesser(getApplicationContext());
    	
    	String categoryName = _categoryName;
    	String result = accesser.insert(new Category(categoryName));
    	Log.d(TAG, "result=" + result);
	}


	
    private void callCardFlipActivity() {
    	Intent i = new Intent(getApplicationContext(), SmartCardMain.class);
    	startActivity(i);
    }
    
    /**
     * Card Matching Game
     */
    private void callGameCardActivity() {
    	Intent i = new Intent(getApplicationContext(), CardGameMain.class);
    	startActivity(i);
    }
    
    /**
     * Talking Mirror
     */
    private void callMirrorActivity() {
    	Intent i = new Intent(getApplicationContext(), MirrorMain.class);
    	startActivity(i);
    }
    
    /**
     * Add Card
     */
    private void callSelectCardActivity() {
    	Intent i = new Intent(getApplicationContext(), CardBoard.class);
    	startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
    
}
