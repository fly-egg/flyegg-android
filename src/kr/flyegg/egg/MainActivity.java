package kr.flyegg.egg;

import kr.flyegg.egg.dao.db.FlyEggDBHelper;
import kr.flyegg.egg.ui.CardBoard;
import kr.flyegg.egg.ui.CardGameMain;
import kr.flyegg.egg.ui.CardSlideFilpper;
import kr.flyegg.egg.ui.MirrorMain;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button btnSmartCard = null;
	private Button btnGame = null;
	private Button btnMirror = null;
	private Button btnSelectCard = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //prevent to turn off window
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
        
        
        createDB();
    }
    
    
	private void createDB() {
		FlyEggDBHelper helper = new FlyEggDBHelper(getApplicationContext());
		helper.getWritableDatabase();
		helper.close();
	}
    
    private void callCardFlipActivity() {
    	Intent i = new Intent(getApplicationContext(), CardSlideFilpper.class);
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
