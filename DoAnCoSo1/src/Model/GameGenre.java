package Model;

import java.io.Serializable;

public class GameGenre implements Serializable{
	private static final long serialVersionUID = 1L;
	private int game_id;
	private int genre_id;
	
	public GameGenre(int game_id, int genre_id) {
		super();
		this.game_id = game_id;
		this.genre_id = genre_id;
	}

	public GameGenre() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getGame_id() {
		return game_id;
	}

	public void setGame_id(int game_id) {
		this.game_id = game_id;
	}

	public int getGenre_id() {
		return genre_id;
	}

	public void setGenre_id(int genre_id) {
		this.genre_id = genre_id;
	}

	@Override
	public String toString() {
		return "GameGenre:\ngame_id=" + game_id + "\ngenre_id=" + genre_id + "\n";
	}
	
}
