package Model;

public class KeyDB {
	private int user_id;
	private int game_id;
	private String username;
	
	public KeyDB() {
		super();
	}

	public KeyDB(int user_id, int game_id, String username) {
		super();
		this.user_id = user_id;
		this.game_id = game_id;
		this.username = username;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getGame_id() {
		return game_id;
	}

	public void setGame_id(int game_id) {
		this.game_id = game_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "IDDatabase [user_id=" + user_id + ", game_id=" + game_id + "]";
	}
	
	
}
