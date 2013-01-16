package kr.flyegg.egg.ui;

import java.util.List;

import kr.flyegg.egg.R;
import kr.flyegg.egg.dao.Card;
import kr.flyegg.egg.dao.CardAccesser;
import kr.flyegg.egg.ui.event.DisplayNextView;
import kr.flyegg.egg.ui.event.Flip3dAnimation;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
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
        setContentView(R.layout.activity_smartcard_main);
        
        
        m_viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
		m_viewFlipper.setOnTouchListener(MyTouchListener);	// 터치 리스너
		m_viewFlipper.removeAllViews();	// 초기화

		// 임시 카드
		setCardFromResource(mStrIds[0], mImageIds[0]);
		setCardFromResource(mStrIds[1], mImageIds[1]);
		setCardFromResource(mStrIds[2], mImageIds[2]);
		setCardFromResource(mStrIds[3], mImageIds[3]);
		setCardFromResource(mStrIds[4], mImageIds[4]);
		
		// 카드 리스트 불러오기
		// TODO: 지정된 카테고리의 카드만 불러오도록 해야 됨
		CardAccesser cardAccesser = new CardAccesser(getApplicationContext());
        List<Card> list = cardAccesser.getCardList();

		for (Card card : list) {
			setCard(card);
		}
		
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
						RelativeLayout layout = (RelativeLayout)m_viewFlipper.getCurrentView();
						ImageView imageView = (ImageView)layout.getChildAt(0);
						TextView textView = (TextView)layout.getChildAt(1);

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
					RelativeLayout layout = (RelativeLayout)m_viewFlipper.getCurrentView();
					ImageView imageView = (ImageView)layout.getChildAt(0);
					TextView textView = (TextView)layout.getChildAt(1);

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
    
    
    /**
     * viewFlipper 에 카드 추가
     * @param cardWord 단어
     * @param img_id image resource id
     */
    private void setCardFromResource(String cardWord, int img_id) {
    	RelativeLayout layout = new RelativeLayout(getApplicationContext());

		///////////////////////////////////
    	// 카드앞면 - 이미지
    	final ImageView imgCardFront = new ImageView(getApplicationContext());
		imgCardFront.setBackgroundResource(R.drawable.card_front);	// 앞면 카드를 배경으로 지정
    	imgCardFront.setPadding(30,30,30,30);	// 여백
    	imgCardFront.setImageResource(img_id);
		
		///////////////////////////////////
    	// 카드뒷면 - 단어
		final TextView tvCardBack = new TextView(getApplicationContext());
		tvCardBack.setBackgroundResource(R.drawable.card_back);
		tvCardBack.setText(cardWord);
		tvCardBack.setTextSize(140);
		tvCardBack.setTextColor(getResources().getColor(R.color.card_name_gray));
		tvCardBack.setGravity(Gravity.CENTER);
		tvCardBack.setVisibility(View.GONE);

		// 카드 앞/뒷 면 등록
		layout.addView(imgCardFront);
		layout.addView(tvCardBack);
		
		m_viewFlipper.addView(layout);
	}

    /**
     * viewFlipper 에 카드 추가
     * @param card
     */
    private void setCard(Card card) {

		///////////////////////////////////
    	// 카드앞면 - 이미지
//    	final ImageView imgCardFront = new ImageView(getApplicationContext());
		ImageView ivCardFront = new ImageView(this);

		ivCardFront.setTag(card);
		
		ivCardFront.setMinimumWidth(640);
		ivCardFront.setMinimumHeight(480);

		ivCardFront.setBackgroundResource(R.drawable.card_front);	// 앞면 카드를 배경으로 지정
//    	FrameLayout.LayoutParams lp = (LayoutParams) imgCardFront.getLayoutParams();
//    	lp.width = 640;
//    	lp.height = 480;
//    	imgCardFront.setLayoutParams(lp);
//    	
    	ivCardFront.setScaleType(ImageView.ScaleType.FIT_CENTER);
//    	imgCardFront.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    	//android:layout_width="640dip" 
    	
		Bitmap bitmap = BitmapFactory.decodeFile(getFilesDir().getAbsolutePath() + "/cards/" + card.get_id() + ".png");
		ivCardFront.setImageBitmap(bitmap);
		
    	ivCardFront.setPadding(30,30,30,30);	// 여백
    	
		
		///////////////////////////////////
    	// 카드뒷면 - 단어
    	RelativeLayout layout = new RelativeLayout(getApplicationContext());
		layout.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

    	final TextView tvCardBack = new TextView(getApplicationContext());
		tvCardBack.setBackgroundResource(R.drawable.card_back);
		tvCardBack.setText(card.getWord());
		tvCardBack.setTextSize(140);
		tvCardBack.setTextColor(getResources().getColor(R.color.card_name_gray));
		tvCardBack.setGravity(Gravity.CENTER);
		tvCardBack.setVisibility(View.GONE);

		// 카드 앞/뒷 면 등록
		layout.addView(ivCardFront);
		layout.addView(tvCardBack);
		
		m_viewFlipper.addView(layout);
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
