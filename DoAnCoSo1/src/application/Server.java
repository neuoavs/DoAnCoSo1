package application;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import Controller.MyController;
import Model.Author;
import Model.FAQ;
import Model.Game;
import Model.GameAuthor;
import Model.GameGenre;
import Model.GameOS;
import Model.GameUser;
import Model.GameUserCart;
import Model.Genre;
import Model.OS;
import Model.User;

public class Server extends Thread{
	private List<Game> listGame;
	private List<User> listUser;
	private List<Author> listAuthor;
	private List<FAQ> listFAQ;
	private List<OS> listOS;
	private List<Genre> listGenre;
	private List<GameAuthor> listGameAuthor;
	private List<GameGenre> listGameGenre;
	private List<GameOS> listGameOS;
	private List<GameUser> listGameUser;
	private List<GameUserCart> listGameUserCarts;
	private ServerSocket serverSocket;
	
	public Server() {
		super();
		this.listGame = new ArrayList<>();
		this.listUser = new ArrayList<>();
		this.listAuthor = new ArrayList<>();
		this.listFAQ = new ArrayList<>();
		this.listOS = new ArrayList<>();
		this.listGenre = new ArrayList<>();
		this.listGameAuthor = new ArrayList<>();
		this.listGameGenre = new ArrayList<>();
		this.listGameOS = new ArrayList<>();
		this.listGameUser = new ArrayList<>();
		this.listGameUserCarts = new ArrayList<>();
		this.serverSocket = null;
	}
	
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(1714);
			serverSocket.setReuseAddress(true);
			
			uploadAllList();
			
			while(true) {
				Socket clientSocket = serverSocket.accept();
				if (clientSocket != null) {
					ServerProcess serverProcess = new ServerProcess(clientSocket, listGame, listUser, listAuthor, listFAQ, listOS, listGenre, listGameAuthor, listGameGenre, listGameOS, listGameUser, listGameUserCarts);
					serverProcess.start();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void uploadAllList() throws SQLException {
		Connection con = MyController.getConnection();
		Statement sta = con.createStatement();
		ResultSet rs = sta.executeQuery("SELECT * FROM gamestore.games ORDER BY game_id ASC;");
	
		
		while (rs.next()) {
			listGame.add(new Game(rs.getInt("game_id"), rs.getString("game_name"), rs.getString("game_description"), rs.getBytes("game_cover_image"), rs.getDate("release_date"), rs.getDouble("price"), rs.getInt("min_download_size"), rs.getInt("min_ram_requirement")));
		}
		
		rs = null;
		rs = sta.executeQuery("SELECT * FROM gamestore.authors ORDER BY author_id ASC;");
		
		while (rs.next()) {
			listAuthor.add(new Author(rs.getInt("author_id"), rs.getString("author_name")));
		}
		
		rs = null;
		rs = sta.executeQuery("SELECT * FROM gamestore.users ORDER BY user_id ASC;");
		
		while (rs.next()) {
			listUser.add(new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password"), rs.getString("email"), rs.getDate("date_of_birth"), rs.getString("nationality"), rs.getDouble("balance")));
		}
		
		
		rs = sta.executeQuery("SELECT * FROM gamestore.genres ORDER BY genre_id ASC;");
		
		while (rs.next()) {
			listGenre.add(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
		}
		
		rs = null;
		rs = sta.executeQuery("SELECT * FROM gamestore.faqs ORDER BY id_faq ASC;");

		while (rs.next()) {
			listFAQ.add(new FAQ(rs.getInt("id_faq"), rs.getString("question"), rs.getString("answer")));
		}
		
		rs = null;
		rs = sta.executeQuery("SELECT * FROM gamestore.operating_systems ORDER BY os_id ASC;");
		
		while(rs.next()) {
			listOS.add(new OS(rs.getInt("os_id"), rs.getString("os_name")));
		}
		
		rs = null;
		rs = sta.executeQuery("SELECT * FROM gamestore.game_authors ORDER BY game_id ASC;");
		
		while (rs.next()) {
			listGameAuthor.add(new GameAuthor(rs.getInt("author_id"), rs.getInt("game_id")));
		}
		
		rs = null;
		rs = sta.executeQuery("SELECT * FROM gamestore.game_genre ORDER BY game_id ASC;");
		
		while (rs.next()) {
			listGameGenre.add(new GameGenre(rs.getInt("game_id"),rs.getInt("genre_id")));
		}
		
		rs = null;
		rs = sta.executeQuery("SELECT * FROM gamestore.game_os ORDER BY game_id ASC;");
		
		while (rs.next()) {
			listGameOS.add(new GameOS(rs.getInt("game_id"), rs.getInt("os_id")));
		}
		
		rs = null;
		rs = sta.executeQuery("SELECT * FROM gamestore.game_user ORDER BY user_id ASC;");
		
		while (rs.next()) {
			listGameUser.add(new GameUser(rs.getInt("game_id"), rs.getInt("user_id")));
		}
		
		rs = null;
		rs = sta.executeQuery("SELECT * FROM gamestore.user_game_cart ORDER BY user_id ASC;");
		
		while (rs.next()) {
			listGameUserCarts.add(new GameUserCart(rs.getInt("cart_id"), rs.getInt("game_id"), rs.getInt("user_id"), rs.getTimestamp("cart_date")));
		}
		
		System.out.println("listGame = " + listGame.size());
		System.out.println("listUser = " + listUser.size());
		System.out.println("listAuthor = " + listAuthor.size());
		System.out.println("listFAQ = " + listFAQ.size());
		System.out.println("listOS = " + listOS.size());
		System.out.println("listGameAuthor = " + listGameAuthor.size());
		System.out.println("listGameGenre = " + listGameGenre.size());
		System.out.println("listGameOS = " + listGameOS.size());
		System.out.println("listGameUser = " + listGameUser.size());
		System.out.println("listGameUserCarts = " + listGameUserCarts.size());
		System.out.println("listGenre = " + listGenre.size());
		
		sta.close();
		rs.close();
		con.close();
	}
}
