package kr.flyegg.egg.domain;

import android.graphics.Bitmap;

public class Card {

	String word = null;
	String imgPath = null;
	String category = null;
	String[] tags = null;

	// 일단 이렇게 해놈. jpeg 가능하면 바꿔도 무방함.
	// 안드로이드는 모든 이미지를db에 path를 저장해 놓음. 그것과 관련된 api가 지원된다면 bitmap이
	// 편리할 수도 있음.
	Bitmap thumbnail = null;

	// card manager? 는 생각해보고 구현시 필요하면 만들겟음.
	// 팀장님 말대로 매니저를 두는게 좀.. 그래서. 근데 설계를 그렇게 해놔서 안하기도 뭣하고
	// 필요하면 그 때가서 이름 정해서 하는걸로~

	public Card() {
	}

	public Card(String word, String imgPath, String category, String[] tags, Bitmap thumbnail) {
		this.word = word;
		this.imgPath = imgPath;
		this.category = category;
		this.tags = tags;
		this.thumbnail = thumbnail;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public Bitmap getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(Bitmap thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Card[word=").append(word).append(",imgPath=").append(imgPath);
		sb.append(",category=").append(category).append(",tags=").append(tags);
		sb.append("]");

		return sb.toString();
	}

}
