package Model;

import java.io.Serializable;
import java.sql.Date;
import java.util.Arrays;

public class Game implements Serializable{
	private static final long serialVersionUID = 1L;
	private int game_id;
	private String game_name;
	private String game_description;
	private byte[] game_cover_image;
	private Date release_date;
	private double price;
	private int min_download_size;
	private int min_ram_requirement;
	
	public Game(int game_id, String game_name, String game_description, byte[] game_cover_image, Date release_date,
			double price, int min_download_size, int min_ram_requirement) {
		super();
		this.game_id = game_id;
		this.game_name = game_name;
		this.game_description = game_description;
		this.game_cover_image = game_cover_image;
		this.release_date = release_date;
		this.price = price;
		this.min_download_size = min_download_size;
		this.min_ram_requirement = min_ram_requirement;
	}

	public Game() {
		super();
	}

	public int getGame_id() {
		return game_id;
	}

	public void setGame_id(int game_id) {
		this.game_id = game_id;
	}

	public String getGame_name() {
		return game_name;
	}

	public void setGame_name(String game_name) {
		this.game_name = game_name;
	}

	public String getGame_description() {
		return game_description;
	}

	public void setGame_description(String game_description) {
		this.game_description = game_description;
	}

	public byte[] getGame_cover_image() {
		return game_cover_image;
	}

	public void setGame_cover_image(byte[] game_cover_image) {
		this.game_cover_image = game_cover_image;
	}

	public Date getRelease_date() {
		return release_date;
	}

	public void setRelease_date(Date release_date) {
		this.release_date = release_date;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getMin_download_size() {
		return min_download_size;
	}

	public void setMin_download_size(int min_download_size) {
		this.min_download_size = min_download_size;
	}

	public int getMin_ram_requirement() {
		return min_ram_requirement;
	}

	public void setMin_ram_requirement(int min_ram_requirement) {
		this.min_ram_requirement = min_ram_requirement;
	}

	@Override
	public String toString() {
		return "Game:\ngame_id=" + game_id + "\ngame_name=" + game_name + "\ngame_description=" + game_description
				+ "\ngame_cover_image=" + Arrays.toString(game_cover_image) + "\nrelease_date=" + release_date
				+ "\nprice=" + price + "\nmin_download_size=" + min_download_size + "\nmin_ram_requirement="
				+ min_ram_requirement + "\n";
	}
	
}
