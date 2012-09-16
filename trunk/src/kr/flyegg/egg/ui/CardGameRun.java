package kr.flyegg.egg.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import kr.flyegg.egg.R;
import kr.flyegg.egg.cardgame.GameCard;
import kr.flyegg.egg.dao.Card;
import kr.flyegg.egg.dao.CardAccesser;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

/**
 * 카드게임
 * 
 * @author junho85
 * 
 */
public class CardGameRun extends Activity {

	private static final String TAG = "CardGameRun";
	
	private static final int ACTIVITY_CLEAR_POPUP = 1;
	private static final int ACTIVITY_FINISH = 2;
	
//	private Animation mAnimFade;	// 카드뒤집기 페이드 애니메이션

	// ------------------------
	// 카드 이미지 정보
	private Bitmap mCardBackSideBitmap;	// 카드 뒷면 이미지
	private static int mCardWidth = 271;	// 가로 사이즈
	private static int mCardHeight = 205;	// 세로 사이즈
	

	// ------------------------
	// 판수 변수들
	private static int mStageNow = 1; // 현재 판수
	private final int TOTAL_STAGE = 3; // 전체 판수

	// ------------------------
	// 게임카드 변수들
	private int pairs = 0; // 카드 짝수 (2짝이면 4장, 3짝이면 6장, 4짝이면 8장이 된다.)
	private ArrayList<GameCard> mGameCardList = new ArrayList<GameCard>(); // 게임용 카드 리스트

	// ------------------------
	// DB의 카드 정보 가져오기 위한 변수들
	private int mLevel;	// 인자로 받은 레벨
	private final int MAX_LEVEL = 3; // 마지막 레벨
	
	private String mCategory;	// 인자로 받은 카테고리
	private String mTag;	// 인자로 받은 테그

	private ArrayList<Card> mCardsListFromDB;	// DB에서 조회해온 카드리스트

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// ------------------------
		// 카드 뒷면 이미지
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.card_back_2);
		mCardBackSideBitmap = Bitmap.createScaledBitmap(bitmap, mCardWidth, mCardHeight, true);
		
		// ------------------------
		// 애니메이션 설정
//		mAnimFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
		// 애니메이션 후처리 리스너 붙이기
//		mAnimFade.setAnimationListener(mCardFlipAnimationListener);

		// ------------------------
		// 화면 설정
		setContentView(R.layout.activity_cardgame_run);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		// ------------------------
		// 게임 횟수 초기화
		mStageNow = 1;

		Intent intent = getIntent();
		// ------------------------
		// 카테고리, 테그 정보 받아 오기
		mCategory = intent.getStringExtra(CardGameMain.EXTRA_CATEGORY);
		mTag = intent.getStringExtra(CardGameMain.EXTRA_TAG);
		
		// ------------------------
		// 레벨정보 받아오기
		mLevel = intent.getIntExtra(CardGameMain.EXTRA_LEVEL, 0);

		Log.d(TAG, "Level:" + mLevel);

		pairs = getPairs(mLevel);
		
		// ------------------------
		// TODO: DB에서 카드 정보 가져 오기 (parameter: 카테고리, SET)
		// 일단 임시로 맘대로 구현
		mCardsListFromDB = getCardListDB(mCategory, mTag);
		Log.d(TAG, "db size=" + mCardsListFromDB.size());

		// 해당 카테고리/테그의 카드수가 카드게임의 짝(pair) 보다 적으면 게임을 할 수 없음
		if (mCardsListFromDB.size() < pairs) {
			Toast.makeText(getApplicationContext(), "카드가 부족해서 게임을 진행 할 수 없습니다 ㅠㅜ", Toast.LENGTH_SHORT).show();			
			finish();
			return;
		} else {
			// ------------------------
			// 레벨에 맞는 테이블 그리기
			drawGameTable(pairs);
		}
		Log.d(TAG, "OnCreate Done");
	}

	/**
	 * 레벨에 따른 카드 pairs 구하기
	 * @param mLevel
	 * @return
	 */
	private int getPairs(int mLevel) {
		int pairs = 0;
		if (mLevel == 1) {
			pairs = 2;
		} else if (mLevel == 2) {
			pairs = 3;
		} else if (mLevel == 3) {
			pairs = 4;
		}
		return pairs;
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}
	
	/**
	 * DB에서 카드 리스트 가져 오기
	 * @param category
	 * @param tag
	 * @return
	 */
	private ArrayList<Card> getCardListDB(String category, String tag) {

		ArrayList<Card> cardsListFromDB = new ArrayList<Card>();

		// ------------------------
		// 카드 데이터 테스트
		CardAccesser cardAccesser = new CardAccesser(getApplicationContext());
        List<Card> list = cardAccesser.getCardList();
        
        for (Card card : list) {
        	// 축소 이미지 만들기
			Bitmap bitmap = BitmapFactory.decodeFile(card.getImgPath());
			Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, mCardWidth, mCardHeight, true);

        	card.setThumbnail(resizedBitmap);
        	cardsListFromDB.add(card);
        	
        	Log.d(TAG, "word=" + card.getWord());
        }

        return cardsListFromDB;
	}

	/**
	 * 선택된 카드 갯수
	 * 
	 * @return count
	 */
	private int getCheckedCardNum() {
		int count = 0;
		for (int i = 0; i < mGameCardList.size(); i++) {
			if (mGameCardList.get(i).isChecked()) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 완성 여부 확인
	 * 
	 * @return
	 */
	private boolean isAllSolved() {
		for (int i = 0; i < mGameCardList.size(); i++) {
			if (mGameCardList.get(i).isSolved() == false) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 카드패 그리기
	 * 
	 * @param pair
	 */
	public void drawGameTable(int pair) {
		
		// ------------------------
		// 타이틀 변경하기
		TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("짝꿍카드-" + mLevel + "단계");
		
		// 
		{
			ImageView imageView = (ImageView) findViewById(R.id.ivTitle);
			if (mLevel == 1) {
				imageView.setImageResource(R.drawable.info_egg_1);
			} else if (mLevel == 2) {
				imageView.setImageResource(R.drawable.info_egg_2);
			} else if (mLevel == 3) {
				imageView.setImageResource(R.drawable.info_egg_3);
			}
		}

		Log.d(TAG, "DrawTable start");

		int row = 2;
		int col = 0;

		// ------------------------
		// 짝 카드 수에 따른 row, col 정하기
		if (pair == 2) {
			row = 2;
			col = 2;
		} else if (pair == 3) {
			row = 2;
			col = 3;
		} else if (pair == 4) {
			row = 2;
			col = 4;
		}

		Log.d(TAG, "Make Card");
		
		
		// ------------------------
		// card 번호 list 생성
		// 이 번호로 mGameCards 를 생성하며 해당 뷰와 연결 시킴
		
		ArrayList<Integer> cardsList = new ArrayList<Integer>();
		for (int i = 0; i < pair; i++) {
			// 같은 카드를 두장 씩
			cardsList.add(i);
			cardsList.add(i);
		}

		// ------------------------
		// random shuffle
		Log.d(TAG, "Card Shuffle");
		int length = cardsList.size();
		for (int i = 0; i < length * 100; i++) {
			int first = (int) Math.floor(Math.random() * length);
			int second = (int) Math.floor(Math.random() * length);
			int temp = cardsList.get(first);

			cardsList.set(first, cardsList.get(second));
			cardsList.set(second, temp);
		}

		// //////////////////////////////////////////
		// 카드 그리기 (버튼)

		TableLayout tableLayout = (TableLayout) findViewById(R.id.tblGame);

		Log.d(TAG, "Clear Table View and GameCards");

		// clear table view and cards
		tableLayout.removeAllViews();
		mGameCardList.clear();
		
		// DB Card ArrayList Random Shuffle
		long seed = System.nanoTime();
		Collections.shuffle(mCardsListFromDB, new Random(seed));

		Integer cardNo = new Integer(0);
		Log.d(TAG, "cardNo:" + cardNo);

		for (int i = 0; i < row; i++) { // row loop

			Log.d(TAG, "Create a row");
			// create a row
			TableRow tableRow = new TableRow(this);
			tableRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			
			TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);

			int leftMargin = 10;
			int topMargin = 2;
			int rightMargin = 10;
			int bottomMargin = 2;

			tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

			for (int j = 0; j < col; j++) { // column loop

				// Create a Button to be the row-content
				Log.d(TAG, "Create a button");
				
				ViewFlipper viewFlipper = new ViewFlipper(getApplicationContext());

				ImageView imageView = new ImageView(this);

				// 카드 정보
				GameCard gameCard = new GameCard();
				gameCard.setSide(GameCard.SIDE_BACK); // 카드는 기본적으로 뒷면을 향함
				
				imageView.setImageBitmap(mCardBackSideBitmap);	// 뒷면 이미지
				imageView.setPadding(9, 9, 9, 9);

				// 카드 번호 지정
				gameCard.setCardNo(cardsList.get(0));
				cardsList.remove(0);

				// 카드 정보를 태그에 기록
				// btn.setTag(gameCard);
				Log.d(TAG, "setTag cardNo=" + cardNo);
				imageView.setTag(cardNo);
				viewFlipper.setTag(cardNo);

				// DB에서 조회해온 카드를 앞에서 부터 순서대로 넣음
				gameCard.setCard(mCardsListFromDB.get(gameCard.getCardNo()));
				cardNo++;

				gameCard.setView(viewFlipper);
				mGameCardList.add(gameCard);

				// 사이즈
				Log.d(TAG, "Set Card Size");
				
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				imageView.setLayoutParams(layoutParams);
				
				imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//				imageView.setOnClickListener(cardClickListener);
				
				
				// 카드 그림면
				Card card = gameCard.getCard();
				ImageView imageView2 = new ImageView(getApplicationContext());
				imageView2.setImageBitmap(card.getThumbnail());
				imageView2.setPadding(9, 9, 9, 9);
				
				viewFlipper.addView(imageView);
				viewFlipper.addView(imageView2);

//				viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade));
//				viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appear_from_right));
//				viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.disappear_to_left));

//				viewFlipper.showNext();
//				viewFlipper.startFlipping();
				
				viewFlipper.setOnClickListener(cardClickListener);

				Log.d(TAG, "Add Card to row");
				// Add Card Button to row
				tableRow.addView(viewFlipper);
			}

			// Add row to TableLayout
			Log.d(TAG, "Add row to TableLayout");
			tableLayout.addView(tableRow, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
	}

	AnimationListener mCardFlipAnimationListener = new AnimationListener() {

		public void onAnimationEnd(Animation arg0) {
			// 카드가 뒤집히고 나면 후처리를 함.
			afterFlip();
		}

		/**
		 * 카드가 뒤집히고 나면 후처리 함.
		 */
		private void afterFlip() {
			// 두개 선택되어 있는 경우 맞는지 틀린지 검사
			if (getCheckedCardNum() == 2) {
				int preCheckedCardIndex = -1; // 선택된 카드 index

				for (int i = 0; i < mGameCardList.size(); i++) {
					if (mGameCardList.get(i).isChecked()) {
						if (preCheckedCardIndex == -1) {
							preCheckedCardIndex = i;
							continue;
						}

						// -----------------------
						// 이전 선택 카드 번호와 지금 선택 카드 번호가 같으면 맞음
						if (mGameCardList.get(preCheckedCardIndex).getCardNo() == mGameCardList.get(i).getCardNo()) {
							
							mGameCardList.get(preCheckedCardIndex).setSolved(true);
							mGameCardList.get(i).setSolved(true);

							// 선택 해제
							mGameCardList.get(preCheckedCardIndex).setChecked(false);
							mGameCardList.get(i).setChecked(false);

							// -----------------------
							// 전부 해결된 경우
							if (isAllSolved()) {
								
								mStageNow++;

								if (mStageNow > TOTAL_STAGE) {
									// 할당된 게임 모두 클리어 (ex. 3판)
									// 메인, 다시하기, 다음단계 팝업 띄우기
									Intent intent = new Intent(getApplicationContext(), CardGameFinish.class);
									intent.putExtra(CardGameMain.EXTRA_LEVEL, mLevel);
									startActivityForResult(intent, ACTIVITY_FINISH);

									// 일단 게임은 끝났으니 해당 액티비티는 종료 시킴
									// 상위 액티비티도 닫기 위해 플래그를 날림
//									finish();
								} else {

									// 한판 클리어
									// 참 잘했어요! 팝업 띄우기
									Intent intent = new Intent(getApplicationContext(), CardGameClearPopup.class);
									startActivityForResult(intent, ACTIVITY_CLEAR_POPUP);
								}
							}
							// Toast.makeText(getApplicationContext(), "짝짝짝", Toast.LENGTH_SHORT).show();
							break;
						} // end if 두개의 카드가 같을 경우
					}
				} // end for 선택된 카드 두개 찾기용 루프
			} // end if 두개 선택시 맞는지 틀린지 검사
			
			// 이미 선택된 카드가 2개 있는 경우 선택된 카드들은 다시 뒤집음 - 틀린 경우이기 때문
			if (getCheckedCardNum() == 2) {
				flipCheckedCards();
				// 뒤집는 시간차를 주려고 하는데 아래와 같이 하면 죽는다...
				/*
				TimerTask myTask = new TimerTask() {
					public void run() {
						// checked 된 카드들 뒤집기
						flipCheckedCards();
					}
				};
				Timer timer = new Timer();
				timer.schedule(myTask, 500);
				*/
			}
		}

		public void onAnimationRepeat(Animation arg0) {
			
		}

		public void onAnimationStart(Animation arg0) {
			// TODO: lock ?
		}
	};
	
	// 카드 클릭 처리
	private OnClickListener cardClickListener = new OnClickListener() {
		
		public void onClick(View v) {
			Integer cardNo = (Integer) v.getTag();
			
			GameCard gameCard = mGameCardList.get(cardNo);
			
			// 풀었는 카드 선택시 아무것도 하지 않음
			if (gameCard.isSolved()) {
				return;
			}
			
			// X 이미 선택된 카드가 2개 있는 경우 선택된 카드들은 다시 뒤집음
			if (getCheckedCardNum() == 2) {
				return;
				// timer 에 의해 뒤집히기 전까지 다른 작업을 하지 않는다.
//				flipCheckedCards();
			}
			
			// 선택되지 않은 카드 선택시 카드 뒤집음
			if (gameCard.isChecked() == false) {
				gameCard.setSide(GameCard.SIDE_FRONT);
				gameCard.setChecked(true); // 선택처리

				ViewFlipper viewFlipper = (ViewFlipper)v;
				
				viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appear_from_right));
				viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.disappear_to_left));
				
				viewFlipper.getInAnimation().setAnimationListener(mCardFlipAnimationListener);
				
				viewFlipper.showNext();
			} else {
				// 이미 선택된 카드 클릭시 아무것도 하지 않음
				return;
			}
		}
	};	// end 카드클릭처리
	

	/**
	 * 선택된 카드들 뒤로 뒤집기
	 */
	private void flipCheckedCards() {
		
		for (int i = 0; i < mGameCardList.size(); i++) {
			if (mGameCardList.get(i).isChecked()) {
//				View view = mGameCards.get(i).getView();
				ViewFlipper viewFlipper = (ViewFlipper)mGameCardList.get(i).getView();

				// 선택된 카드 다시 뒤집기
				mGameCardList.get(i).setSide(GameCard.SIDE_BACK);
				
				viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appear_from_left));
				viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.disappear_to_right));

				viewFlipper.showNext();
				
				mGameCardList.get(i).setChecked(false);
			}
		}
	}
	/**
	 * ActivityResult
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ACTIVITY_CLEAR_POPUP:
			if (resultCode == Activity.RESULT_OK) {
				// 판 다시 그리기
				drawGameTable(pairs);
			}
			break;
		case ACTIVITY_FINISH:
			if (resultCode == Activity.RESULT_OK){
				boolean isToMain = data.getBooleanExtra("TO_MAIN", false);
				boolean isRetry = data.getBooleanExtra("RETRY", false);
				boolean isNext = data.getBooleanExtra("NEXT", false);
				
				// 스테이지 초기화
				mStageNow = 1;
				
				if (isToMain) {
					Intent intent = new Intent();
					intent.putExtra("TO_MAIN", true);
					
					setResult(Activity.RESULT_OK, intent);

					finish();
				} else if (isRetry) {
					// 판 다시 그리기
					drawGameTable(pairs);
				} else if (isNext) {
					if (mLevel < MAX_LEVEL) {
						mLevel++;
						pairs = getPairs(mLevel);
					} else {
						// TODO: 이미 마지막 레벨인 경우?
						// 그냥 마지막 레벨을 계속 한다?
						// 종료 한다?
					}
					
					// 판 다시 그리기
					drawGameTable(pairs);
				}
			}
			
			break;
		}
	}
}
