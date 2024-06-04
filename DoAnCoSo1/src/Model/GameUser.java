package Model;

import java.io.Serializable;

public class GameUser implements Serializable{
	private static final long serialVersionUID = 1L;
	private int game_id;
	private int user_id;
	
	public GameUser(int game_id, int user_id) {
		super();
		this.game_id = game_id;
		this.user_id = user_id;
	}

	public GameUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getGame_id() {
		return game_id;
	}
	
	public void setGame_id(int game_id) {
		this.game_id = game_id;
	}
	
	public int getUser_id() {
		return user_id;
	}
	
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	@Override
	public String toString() {
		return "GameUser:\ngame_id=" + game_id + "\nuser_id=" + user_id + "\n";
	}

}
