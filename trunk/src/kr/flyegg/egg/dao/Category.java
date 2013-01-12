package kr.flyegg.egg.dao;

public class Category {

	private String _id = null;
	private String category = null;
	
	public Category(String _id, String category) {
		this.set_id(_id);
		this.category = category;
	}

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

	public void set_id(String _id) {
		this._id = _id;
	}

	public String get_id() {
		return _id;
	}
	
}
