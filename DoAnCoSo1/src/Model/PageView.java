package Model;

public class PageView {
	private String src;
	private int index;
	
	

	public PageView() {
		this.src = "";
		this.index = -1;
	}



	public PageView(String src, int index) {
		super();
		this.src = src;
		this.index = index;
	}



	public String getSrc() {
		return src;
	}



	public void setSrc(String src) {
		this.src = src;
	}



	public int getIndex() {
		return index;
	}



	public void setIndex(int index) {
		this.index = index;
	}

}
