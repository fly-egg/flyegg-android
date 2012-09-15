package kr.flyegg.egg.dao;

import android.graphics.Bitmap;

public class Card {

	private String word = null;
	private String imgPath = null;
	//일단 array 바뀔수도 있음.
	private String category = null;
	private String[] tags = null;
	private Bitmap thumbnail = null;
	
	public Card() {
	}
	

	/**
	 * 
	 * @param word
	 * @param imgPath
	 * @param category
	 * @param tags
	 */
	public Card(String word, String imgPath, String category, String tags) {
		this.word = word;
		this.imgPath = imgPath;
		this.category = category;
		this.tags = tagToArray(tags);
	}
	
	/**
	 * 
	 * @param word
	 * @param imgPath
	 * @param category
	 * @param tags
	 */
	public Card(String word, String imgPath, String category, String[] tags) {
		this.word = word;
		this.imgPath = imgPath;
		this.category = category;
		this.tags = tags;
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
	
	public String tagToString(String[] tags) {
		StringBuffer sb = new StringBuffer();
		for(String str : tags) {
			sb.append(str).append(",");
		}
		return sb.toString();
	}
	
	public String[] tagToArray(String tags) {
		return tags.split(",");
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
		sb.append(",category=").append(category).append(",tags=").append(tagToString(tags));
		sb.append("]");
		
		return sb.toString();
	}
	
}
