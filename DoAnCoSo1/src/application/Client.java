package application;

import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle.Control;

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

public class Client extends Thread{
	private Socket clientSocket;
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
	private ObjectInputStream in;
	private DataOutputStream out;
	private Object object;
	
	public Client() {
		super();
		this.listGame = new ArrayList<>();
		this.listUser = new ArrayList<>();
		this.listAuthor = new ArrayList<>();
		this.listFAQ = new ArrayList<>();
		this.listOS = new ArrayList<>();
		this.listGameAuthor = new ArrayList<>();
		this.listGameGenre = new ArrayList<>();
		this.listGameOS = new ArrayList<>();
		this.listGameUser = new ArrayList<>();
		this.listGenre = new ArrayList<>();
		this.listGameUserCarts = new ArrayList<>();
		this.clientSocket = null;
		this.in = null;
		this.out = null;
		this.object = null;
	}
	
	@Override
	public void run() {
		try {
			clientSocket = new Socket("localhost", 1714);
			
			in = new ObjectInputStream(clientSocket.getInputStream());
			out = new DataOutputStream(clientSocket.getOutputStream());
			
			out.writeUTF("Upload all list");
			
			object = in.readObject();
			
			while (object == null) {
				object = in.readObject();
			}
			

			
			for (Object o : (List<?>) object) {
				if (((List<?>) o).get(0) instanceof Game) {
					listGame = (List<Game>) o;
				} else if (((List<?>) o).get(0) instanceof User) {
					listUser = (List<User>) o;
				} else if (((List<?>) o).get(0) instanceof Author) {
					listAuthor = (List<Author>) o;
				} else if (((List<?>) o).get(0) instanceof FAQ) {
					listFAQ = (List<FAQ>) o;
				} else if (((List<?>) o).get(0) instanceof OS) {
					listOS = (List<OS>) o;
				} else if (((List<?>) o).get(0) instanceof GameAuthor) {
					listGameAuthor = (List<GameAuthor>) o;
				} else if (((List<?>) o).get(0) instanceof GameGenre) {
					listGameGenre = (List<GameGenre>) o;
				} else if (((List<?>) o).get(0) instanceof GameOS) {
					listGameOS = (List<GameOS>) o;
				} else if (((List<?>) o).get(0) instanceof GameUser) {
					listGameUser = (List<GameUser>) o;
				} else if (((List<?>) o).get(0) instanceof GameUserCart) {
					listGameUserCarts = (List<GameUserCart>) o;
				} else {
					listGenre = (List<Genre>) o;
				}
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
			
			if (clientSocket != null) {
				clientSocket.close();
			}
			
			if (in != null) {
				in.close();
			}
			
			if (out != null) {
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
