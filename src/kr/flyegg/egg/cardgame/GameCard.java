package kr.flyegg.egg.cardgame;

import kr.flyegg.egg.dao.Card;
import android.view.View;

/**
 * 게임용 카드 객체
 * 
 * @author junho85
 *
 */
public class GameCard {

	public static final String SIDE_FRONT = "front";
	public static final String SIDE_BACK = "back";

	private boolean solved;		// 해결여부
	private boolean checked;	// 선택여부
	private String side;		// 앞/뒤
	private int cardNo;			// 카드번호
	private Card card;			// 카드객체
	
	private View view;	// 연결된 View Object
	
	public boolean isSolved() {
		return solved;
	}
	public void setSolved(boolean isSolved) {
		this.solved = isSolved;
	}
	
	
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	public String getSide() {
		return side;
	}
	public void setSide(String side) {
		this.side = side;
	}
	
	
	public int getCardNo() {
		return cardNo;
	}
	public void setCardNo(int cardNo) {
		this.cardNo = cardNo;
	}
	
	
	public Card getCard() {
		return card;
	}
	public void setCard(Card card) {
		this.card = card;
	}
	
	public View getView() {
		return view;
	}
	public void setView(View view) {
		this.view = view;
	}
	
	

	
}
