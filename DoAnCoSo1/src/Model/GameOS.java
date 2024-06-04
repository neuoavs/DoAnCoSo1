package Model;

import java.io.Serializable;

public class GameOS implements Serializable{
	private static final long serialVersionUID = 1L;
	private int game_id;
	private int os_id;
	
	public GameOS() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GameOS(int game_id, int os_id) {
		super();
		this.game_id = game_id;
		this.os_id = os_id;
	}

	public int getGame_id() {
		return game_id;
	}

	public void setGame_id(int game_id) {
		this.game_id = game_id;
	}

	public int getOs_id() {
		return os_id;
	}

	public void setOs_id(int os_id) {
		this.os_id = os_id;
	}

	@Override
	public String toString() {
		return "GameOS:\ngame_id=" + game_id + "\nos_id=" + os_id + "\n";
	}
	
	
}
