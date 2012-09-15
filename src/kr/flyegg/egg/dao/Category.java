package kr.flyegg.egg.dao;

public class Category {

	private String category = null;
	
	public Category(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Category[category=").append(category).append("]");
		
		return sb.toString();
	}
	
}
