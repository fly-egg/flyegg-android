package kr.flyegg.egg.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import kr.flyegg.egg.R;
import kr.flyegg.egg.cardgame.GameCard;
import kr.flyegg.egg.dao.Card;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 카드게임
 * 
 * @author junho85
 * 
 */
public class CardGameRun extends Activity {

	private static final String TAG = "CardGameRun";

	// ------------------------
	// 판수 변수들
	private static int stageNow = 1; // 현재 판수
	private final int stageTotal = 3; // 전체 판수

	// ------------------------
	// 게임카드 변수들
	private int pairs = 0; // 카드 짝수 (2짝이면 4장, 3짝이면 6장, 4짝이면 8장이 된다.)
	private ArrayList<GameCard> mGameCards = new ArrayList<GameCard>(); // 게임용 카드 리스트

	// ------------------------
	// DB의 카드 정보 가져오기 위한 변수들
	private int mLevel;	// 인자로 받은 레벨
	private String mCategory;	// 인자로 받은 카테고리
	private String mTag;	// 인자로 받은 테그

	private ArrayList<Card> mCardsListFromDB;	// DB에서 조회해온 카드리스트

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ------------------------
		// 화면 설정
		setContentView(R.layout.activity_cardgame_run);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		// ------------------------
		// 게임 횟수 초기화
		stageNow = 1;

		Intent intent = getIntent();
		// ------------------------
		// 카테고리, 테그 정보 받아 오기
		mCategory = intent.getStringExtra(CardGameMain.EXTRA_LEVEL);
		mTag = intent.getStringExtra(CardGameMain.EXTRA_TAG);
		
		// ------------------------
		// 레벨정보 받아오기
		mLevel = intent.getIntExtra(CardGameMain.EXTRA_LEVEL, 0);

		Log.d(TAG, "Level:" + mLevel);

		if (mLevel == 1) {
			pairs = 2;
		} else if (mLevel == 2) {
			pairs = 3;
		} else if (mLevel == 3) {
			pairs = 4;
		}
		
		// ------------------------
		// TODO: DB에서 카드 정보 가져 오기 (parameter: 카테고리, SET)
		// 일단 임시로 맘대로 구현
		mCardsListFromDB = getCardListDB(mCategory, mTag);
		Log.d(TAG, "db size=" + mCardsListFromDB.size());

		// 해당 카테고리/테그의 카드수가 카드게임의 짝(pair) 보다 적으면 게임을 할 수 없음
		if (mCardsListFromDB.size() < pairs) {
			Toast.makeText(getApplicationContext(), "카드가 부족해서 게임을 진행 할 수 없습니다 ㅠㅜ", Toast.LENGTH_SHORT).show();			
			finish();
		}

		// ------------------------
		// 레벨에 맞는 테이블 그리기
		drawGameTable(pairs);

		Log.d(TAG, "OnCreate Done");
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}
	
	/**
	 * DB에서 카드 리스트 가져 오기
	 * TODO: 임시로 사용함. 실제 DB 쪽 가져 오는거 나오면 그거 쓰면 됨.
	 * @param category
	 * @param tag
	 * @return
	 */
	private ArrayList<Card> getCardListDB(String category, String tag) {
		ArrayList<Card> cardsListFromDB = new ArrayList<Card>();
		
		int num = 8;
        final Integer[] imageIds = {
                R.drawable.gallery_photo_1,
                R.drawable.gallery_photo_2,
                R.drawable.gallery_photo_3,
                R.drawable.gallery_photo_4,
                R.drawable.gallery_photo_5,
                R.drawable.gallery_photo_6,
                R.drawable.gallery_photo_7,
                R.drawable.gallery_photo_8
        };
        
        final String[] words = {
        		"토끼", "거북이", "고양이", "말", "호랑이", "사자", "곰", "강아지"
        };
        
        final String[] tags = {
        	"철수"	
        };
        
        for (int i=0; i<num; i++) {
        	Card card = new Card();
        	
        	card.setWord(words[i]);
        	card.setImgPath("" + imageIds[i]);
        	card.setCategory(category);
        	card.setTags(tags);
//        	card.setThumbnail(thumbnail);	// 썸네일은 생략
        	
        	cardsListFromDB.add(card);
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
		for (int i = 0; i < mGameCards.size(); i++) {
			if (mGameCards.get(i).isChecked()) {
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
		for (int i = 0; i < mGameCards.size(); i++) {
			if (mGameCards.get(i).isSolved() == false) {
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
		tvTitle.setText("메모리 게임 " + mLevel + "단계 " + stageNow + "/" + stageTotal);

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
		// card list 생성
		// TODO: 카드 이미지의 아이디? 파일명?
		
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
		mGameCards.clear();
		
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

			for (int j = 0; j < col; j++) { // column loop

				// Create a Button to be the row-content
				Log.d(TAG, "Create a button");
				Button btn = new Button(this);

				// 카드 정보
				GameCard gameCard = new GameCard();
				gameCard.setSide(GameCard.SIDE_BACK); // 카드는 기본적으로 뒷면을 향함
				btn.setBackgroundResource(R.drawable.ic_launcher); // 뒷면 이미지 // TODO: 뒷면 이미지 리소스로 변경

				// 카드 번호 지정
				gameCard.setCardNo(cardsList.get(0));
				cardsList.remove(0);

				// 카드 정보를 태그에 기록
				// btn.setTag(gameCard);
				Log.d(TAG, "setTag cardNo=" + cardNo);
				btn.setTag(cardNo);

				// DB에서 조회해온 카드를 앞에서 부터 순서대로 넣음
				gameCard.setCard(mCardsListFromDB.get(gameCard.getCardNo()));
				cardNo++;

				gameCard.setView(btn);
				mGameCards.add(gameCard);

				// 사이즈
				Log.d(TAG, "Set Card Size");
				btn.setWidth(200);
				btn.setHeight(200);

				btn.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				btn.setOnClickListener(cardClickListener);

				Log.d(TAG, "Add Card to row");
				// Add Card Button to row
				tableRow.addView(btn);
			}

			// Add row to TableLayout
			Log.d(TAG, "Add row to TableLayout");
			tableLayout.addView(tableRow, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
	}

	// 카드 클릭 처리
	private OnClickListener cardClickListener = new OnClickListener() {

		public void onClick(View v) {
			Log.d(TAG, "Get Card Tag");
			// GameCard gameCard = (GameCard) v.getTag();
			Integer cardNo = (Integer) v.getTag();

			Log.d(TAG, "CardNo:" + cardNo);
			// Toast.makeText(getApplicationContext(), "CardNo:" + cardNo, Toast.LENGTH_SHORT).show();
			GameCard gameCard = mGameCards.get(cardNo);

			// 풀었는 카드 선택시 아무것도 하지 않음
			if (gameCard.isSolved()) {
				return;
			}

			// 이미 선택된 카드가 2개 있는 경우 선택된 카드들은 다시 뒤집음
			if (getCheckedCardNum() == 2) {

				for (int i = 0; i < mGameCards.size(); i++) {
					if (mGameCards.get(i).isChecked()) {
						// 선택된 카드 다시 뒤집기
						mGameCards.get(i).setSide(GameCard.SIDE_BACK);
						mGameCards.get(i).getView().setBackgroundResource(R.drawable.ic_launcher);
//						((Button) mGameCards.get(i).getView()).setText("");

						mGameCards.get(i).setChecked(false);
					}
				}
			}

			// 선택되지 않은 카드 선택시 카드 뒤집음
			if (gameCard.isChecked() == false) {
				gameCard.setSide(GameCard.SIDE_FRONT);
				gameCard.setChecked(true); // 선택처리

//				((Button) v).setText("no:" + gameCard.getCardNo());
				Card card = gameCard.getCard();
				
				// TODO: 실제 이미지인 경우에 대한 처리 필요
				//v.setBackgroundDrawable(background) 로 해야 되나?
				v.setBackgroundResource(Integer.parseInt(card.getImgPath()));
			} else {
				// 이미 선택된 카드 클릭시 아무것도 하지 않음
				return;
			}

			// 두개 선택되어 있는 경우 맞는지 틀린지 검사
			if (getCheckedCardNum() == 2) {
				int preCheckedCardIndex = -1; // 선택된 카드 index

				for (int i = 0; i < mGameCards.size(); i++) {
					if (mGameCards.get(i).isChecked()) {
						if (preCheckedCardIndex == -1) {
							preCheckedCardIndex = i;
							continue;
						}

						// 이전 선택 카드 번호와 지금 선택 카드 번호가 같으면 맞음
						if (mGameCards.get(preCheckedCardIndex).getCardNo() == mGameCards.get(i).getCardNo()) {
							mGameCards.get(preCheckedCardIndex).setSolved(true);
							mGameCards.get(i).setSolved(true);

							// 선택 해제
							mGameCards.get(preCheckedCardIndex).setChecked(false);
							mGameCards.get(i).setChecked(false);

							// Toast.makeText(getApplicationContext(), "짝짝짝", Toast.LENGTH_SHORT).show();
							break;
						}
					}
				}
			} // end if 두개 선택시 맞는지 틀린지 검사

			// 전부 해결
			if (isAllSolved()) {

				// 스테이지를 다음으로 넘김
				stageNow++;
				
				if (stageNow > stageTotal) {
					// 메인, 다시하기, 다음단계 팝업 띄우기
					Intent intent = new Intent(getApplicationContext(), CardGameFinish.class);
					startActivity(intent);

					// 일단 게임은 끝났으니 해당 액티비티는 종료 시킴
					// 상위 액티비티도 닫기 위해 플래그를 날림
					finish();
				} else {
					// 참 잘했어요! 팝업 띄우기
					Intent intent = new Intent(getApplicationContext(), CardGameClearPopup.class);
					startActivity(intent);
					
					// 판 다시 그리기
					drawGameTable(pairs);
				}
			}

		}

	};

}
