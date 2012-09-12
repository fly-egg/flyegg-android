package kr.flyegg.egg.domain;

import android.graphics.Bitmap;

public class Card {

	String word = null;
	String imgPath = null;
	String[] category = null;
	String[] tags = null;
	//�ϴ� �̷��� �س�. jpec �����ϸ� �ٲ㵵 ������.
	//�ȵ���̵�� ��� �̹�����db�� path�� ������ ����. �װͰ� ���õ� api�� �����ȴٸ� bitmap�� 
	//���� ���� ����.
	Bitmap thumbnail = null;
	
	//card manager? �� �����غ��� ������ �ʿ��ϸ� �������.
	//����� ����� �Ŵ����� �δ°� ��.. �׷���. �ٵ� ���踦 �׷��� �س��� ���ϱ⵵ ���ϰ�
	//�ʿ��ϸ� �� ������ �̸� ���ؼ� �ϴ°ɷ�~
	
	public Card() {
	}

	public Card(String word, String imgPath, String[] category, String[] tags, Bitmap thumbnail) {
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
	public String[] getCategory() {
		return category;
	}
	public void setCategory(String[] category) {
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
