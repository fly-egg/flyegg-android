package kr.flyegg.egg;

import kr.flyegg.egg.dao.db.FlyEggDBHelper;
import kr.flyegg.egg.ui.AddCardMain;
import kr.flyegg.egg.ui.CardBoard;
import kr.flyegg.egg.ui.CardGameMain;
import kr.flyegg.egg.ui.CardSlideFilpper;
import kr.flyegg.egg.ui.MirrorMain;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button wordCard = null;
	private Button btnGame = null;
	private Button btnMirror = null;
	private Button btnAddCard = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //prevent to turn off window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        wordCard = (Button) findViewById(R.id.workd_card);
        wordCard.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				callWordCardActivity();
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
        
        btnAddCard = (Button) findViewById(R.id.btnAddCard);
        btnAddCard.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		callAddCardActivity();
        	}
        });
        
        Button bt2 = (Button) findViewById(R.id.btntest);
        bt2.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				test();
			}
		});
        
        
        createDB();
    }
    
    private void test() {
    	Intent i = new Intent(getApplicationContext(), CardSlideFilpper.class);
    	startActivity(i);
    }
    
    
	private void createDB() {
		FlyEggDBHelper helper = new FlyEggDBHelper(getApplicationContext());
		helper.getWritableDatabase();
		helper.close();
	}
    
    private void callWordCardActivity() {
    	Intent i = new Intent(getApplicationContext(), CardBoard.class);
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
    private void callAddCardActivity() {
    	Intent i = new Intent(getApplicationContext(), AddCardMain.class);
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
