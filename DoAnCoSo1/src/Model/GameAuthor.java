package Model;

import java.io.Serializable;

public class GameAuthor implements Serializable{
	private static final long serialVersionUID = 1L;
	private int author_id;
	private int game_id;
	
	public GameAuthor() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public GameAuthor(int author_id, int game_id) {
		super();
		this.author_id = author_id;
		this.game_id = game_id;
	}
	
	public int getAuthor_id() {
		return author_id;
	}
	
	public void setAuthor_id(int author_id) {
		this.author_id = author_id;
	}
	
	public int getGame_id() {
		return game_id;
	}
	
	public void setGame_id(int game_id) {
		this.game_id = game_id;
	}
	
	@Override
	public String toString() {
		return "GameAuthor:\nauthor_id=" + author_id + "\ngame_id=" + game_id + "\n";
	}
	
	
}
