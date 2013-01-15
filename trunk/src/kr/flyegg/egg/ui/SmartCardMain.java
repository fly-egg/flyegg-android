package kr.flyegg.egg.ui;

import kr.flyegg.egg.R;
import kr.flyegg.egg.ui.event.DisplayNextView;
import kr.flyegg.egg.ui.event.Flip3dAnimation;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * 똑똑카드
 */
public class SmartCardMain extends Activity {

	private boolean isFirstImage = true;
	private ViewFlipper m_viewFlipper;
	
	private int m_nPreTouchPosX = 0;
	
	
	private final Integer[] mImageIds = {
            R.drawable.banana,
            R.drawable.grape,
            R.drawable.kiwi,
            R.drawable.pineapp,
            R.drawable.watermelon
    };
	
    private final String[] mStrIds = {
            "바나나",
            "포도",
            "키위",
            "파인애플",
            "수박"
    };
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_slide);
        
        
        m_viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        
		setUI(mStrIds[0], mImageIds[0]);
		setUI(mStrIds[1], mImageIds[1]);
		setUI(mStrIds[2], mImageIds[2]);
		setUI(mStrIds[3], mImageIds[3]);
		setUI(mStrIds[4], mImageIds[4]);
		
		// 왼쪽 화살표
		Button btLeft = (Button) findViewById(R.id.left);
		btLeft.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if (isFirstImage == false) {
					// 이미 돌아간 경우 역회전 시킴
					RelativeLayout out = (RelativeLayout)m_viewFlipper.getCurrentView();
					ImageView imageView = (ImageView)out.getChildAt(0);
					TextView textView = (TextView)out.getChildAt(1);

					applyRotation(imageView, textView, 0, -90);
					isFirstImage = !isFirstImage;
				}

				MovewPreviousView();
			}
		});
		
		// 오른쪽 화살표
		Button btRight = (Button) findViewById(R.id.right);
		btRight.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if (isFirstImage == false) {
					// 이미 돌아간 경우 역회전 시킴
					RelativeLayout out = (RelativeLayout)m_viewFlipper.getCurrentView();
					ImageView imageView = (ImageView)out.getChildAt(0);
					TextView textView = (TextView)out.getChildAt(1);

					applyRotation(imageView, textView, 0, -90);
					isFirstImage = !isFirstImage;
				}
				
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
    
	OnTouchListener MyTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				m_nPreTouchPosX = (int) event.getX();
			}

			if (event.getAction() == MotionEvent.ACTION_UP) {
				int nTouchPosX = (int) event.getX();

				
				if (Math.abs(nTouchPosX - m_nPreTouchPosX) > 10) {
					// ------------------------------------------
					// 슬라이드
					
					// ------------------------------------------
					// 회전 관련 초기화
					if (isFirstImage == false) {
						// 이미 돌아간 경우 역회전 시킴
						RelativeLayout out = (RelativeLayout)m_viewFlipper.getCurrentView();
						ImageView imageView = (ImageView)out.getChildAt(0);
						TextView textView = (TextView)out.getChildAt(1);

						applyRotation(imageView, textView, 0, -90);
						isFirstImage = !isFirstImage;
					}
					
					if (nTouchPosX < m_nPreTouchPosX) {
						MoveNextView();
					} else if (nTouchPosX > m_nPreTouchPosX) {
						MovewPreviousView();
					}
				} else {
					// 터치
					RelativeLayout out = (RelativeLayout)m_viewFlipper.getCurrentView();
					ImageView imageView = (ImageView)out.getChildAt(0);
					TextView textView = (TextView)out.getChildAt(1);

					if (isFirstImage) {
						applyRotation(imageView, textView, 0, 90);
						isFirstImage = !isFirstImage;
					} else {
						applyRotation(imageView, textView, 0, -90);
						isFirstImage = !isFirstImage;
					}
				}

				m_nPreTouchPosX = nTouchPosX;
			}

			return true;
//			return false;
		}
	};
    
//    View.OnTouchListener MyTouchListener = new View.OnTouchListener() {
//    	public boolean onTouch(View v, MotionEvent event) {
//    		if (event.getAction() == MotionEvent.ACTION_DOWN) {
//    			m_nPreTouchPosX = (int)event.getX();
//    		}
//    		
//    		if (event.getAction() == MotionEvent.ACTION_UP) {
//    			int nTouchPosX = (int)event.getX();
//    			
//    			if (nTouchPosX < m_nPreTouchPosX) {
//    				MoveNextView();
//    			} else if (nTouchPosX > m_nPreTouchPosX) {
//    				MovewPreviousView();
//    			}
//    			
//    			m_nPreTouchPosX = nTouchPosX;
//    		}
//    		
//            return true;
//        }
//    };
    
    
    
    
    private void setUI(String str, int img_id) {
    	RelativeLayout out = new RelativeLayout(getApplicationContext());

    	final ImageView imgCardFront = new ImageView(getApplicationContext());
		imgCardFront.setImageResource(img_id);
		
		final TextView tvCardBack = new TextView(getApplicationContext());
		tvCardBack.setBackgroundResource(R.drawable.card_back);
		tvCardBack.setText(str);
		tvCardBack.setTextSize(140);
		tvCardBack.setTextColor(getResources().getColor(R.color.card_name_gray));
		tvCardBack.setGravity(Gravity.CENTER);
		tvCardBack.setVisibility(View.GONE);

		/*
		imgCardFront.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (isFirstImage) {
					applyRotation(imgCardFront, tvCardBack, 0, 90);
					isFirstImage = !isFirstImage;

				} else {
					applyRotation(imgCardFront, tvCardBack, 0, -90);
					isFirstImage = !isFirstImage;
				}
			}
		});
		*/
		
		out.addView(imgCardFront);
		out.addView(tvCardBack);
		
		m_viewFlipper.addView(out);
		m_viewFlipper.setOnTouchListener(MyTouchListener);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private void applyRotation(View view1, View view2, float start, float end) {
		// Find the center of image
		final float centerX = view1.getWidth() / 2.0f;
		final float centerY = view1.getHeight() / 2.0f;
	
		// Create a new 3D rotation with the supplied parameter
		// The animation listener is used to trigger the next animation
		final Flip3dAnimation rotation = new Flip3dAnimation(start, end, centerX, centerY);
		rotation.setDuration(500);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new DisplayNextView(isFirstImage, view1, view2));
	
		if (isFirstImage) {
			view1.startAnimation(rotation);
		} else {
			view2.startAnimation(rotation);
		}

	}
	
}