package kr.flyegg.egg.ui;

import kr.flyegg.egg.R;
import kr.flyegg.egg.ui.event.DisplayNextView;
import kr.flyegg.egg.ui.event.Flip3dAnimation;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class CardSlideFilpper extends Activity {

	private boolean isFirstImage = true;
	private ViewFlipper m_viewFlipper;
	
	private int m_nPreTouchPosX = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_slide);
        
        
        m_viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        
		setUI("저리가", R.drawable.gallery_photo_2);
		setUI("안녕", R.drawable.gallery_photo_3);
		setUI("123", R.drawable.gallery_photo_4);
		
		Button bt1 = (Button) findViewById(R.id.left);
		bt1.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				MovewPreviousView();
			}
		});
		
		Button bt2 = (Button) findViewById(R.id.right);
		bt2.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				MoveNextView();
			}
		});
		
    }
    
    private void MoveNextView() {
    	m_viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,	R.anim.appear_from_right));
		m_viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.disappear_to_left));
		m_viewFlipper.showNext();
    }
    
    private void MovewPreviousView() {
    	m_viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,	R.anim.appear_from_left));
		m_viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.disappear_to_right));
    	m_viewFlipper.showPrevious();
    }
    
    View.OnTouchListener MyTouchListener = new View.OnTouchListener() {
    	public boolean onTouch(View v, MotionEvent event) {
    		if (event.getAction() == MotionEvent.ACTION_DOWN) {
    			m_nPreTouchPosX = (int)event.getX();
    		}
    		
    		if (event.getAction() == MotionEvent.ACTION_UP) {
    			int nTouchPosX = (int)event.getX();
    			
    			if (nTouchPosX < m_nPreTouchPosX) {
    				MoveNextView();
    			} else if (nTouchPosX > m_nPreTouchPosX) {
    				MovewPreviousView();
    			}
    			
    			m_nPreTouchPosX = nTouchPosX;
    		}
    		
            return true;
        }
    };
    
    
    
    
    private void setUI(String str, int img_id) {
    	RelativeLayout out = new RelativeLayout(getApplicationContext());

    	final ImageView image1 = new ImageView(getApplicationContext());
		image1.setImageResource(img_id);
		
		final TextView tv = new TextView(getApplicationContext());
		tv.setText(str);
		tv.setVisibility(View.GONE);
	
		image1.setOnClickListener(new View.OnClickListener() {
		   public void onClick(View view) {
		    if (isFirstImage) {       
		     applyRotation(image1, tv, 0, 90);
		     isFirstImage = !isFirstImage;
	
		    } else {    
		     applyRotation(image1, tv, 0, -90);
		     isFirstImage = !isFirstImage;
		    }
		   }
		});
		
		out.addView(tv);
		out.addView(image1);
		
		m_viewFlipper.addView(out);
		m_viewFlipper.setOnTouchListener(MyTouchListener);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private void applyRotation(ImageView image, TextView tv1, float start, float end) {
		// Find the center of image
		final float centerX = image.getWidth() / 2.0f;
		final float centerY = image.getHeight() / 2.0f;
	
		// Create a new 3D rotation with the supplied parameter
		// The animation listener is used to trigger the next animation
		final Flip3dAnimation rotation = new Flip3dAnimation(start, end, centerX, centerY);
		rotation.setDuration(500);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new DisplayNextView(isFirstImage, image, tv1));
	
		if (isFirstImage) {
			image.startAnimation(rotation);
		} else {
			tv1.startAnimation(rotation);
		}

	}
	
}
