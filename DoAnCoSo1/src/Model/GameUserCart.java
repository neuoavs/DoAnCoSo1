package Model;

import java.io.Serializable;
import java.sql.Timestamp;

public class GameUserCart implements Serializable{
	private static final long serialVersionUID = 1L;
	private int cart_id;
	private int game_id;
	private int user_id;
	private Timestamp cart_date;
	
	public GameUserCart(int cart_id, int game_id, int user_id, Timestamp cart_date) {
		super();
		this.cart_id = cart_id;
		this.game_id = game_id;
		this.user_id = user_id;
		this.cart_date = cart_date;
	}
	
	public GameUserCart() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getCart_id() {
		return cart_id;
	}

	public void setCart_id(int cart_id) {
		this.cart_id = cart_id;
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

	public Timestamp getCart_date() {
		return cart_date;
	}

	public void setCart_date(Timestamp cart_date) {
		this.cart_date = cart_date;
	}

	@Override
	public String toString() {
		return "GameUserCart:\ncart_id=" + cart_id + "\ngame_id=" + game_id + "\nuser_id=" + user_id + "\ncart_date="
				+ cart_date + "\n";
	}
}
