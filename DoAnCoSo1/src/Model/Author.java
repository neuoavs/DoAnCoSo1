package Model;

import java.io.Serializable;

public class Author implements Serializable{
	private static final long serialVersionUID = 1L;
	private int author_id;
	private String author_name;
	
	public Author(int author_id, String author_name) {
		super();
		this.author_id = author_id;
		this.author_name = author_name;
	}

	public Author() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(int author_id) {
		this.author_id = author_id;
	}

	public String getAuthor_name() {
		return author_name;
	}

	public void setAuthor_name(String author_name) {
		this.author_name = author_name;
	}

	@Override
	public String toString() {
		return "Author:\nauthor_id=" + author_id + "\nauthor_name=" + author_name + "\n";
	}	
}
