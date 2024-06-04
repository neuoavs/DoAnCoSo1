package application;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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

public class ServerProcess extends Thread{
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
	private DataInputStream in;
	private ObjectOutputStream out;
	private String messFromClient;
	
	public ServerProcess() {
		super();
	}

	public ServerProcess(Socket clientSocket, List<Game> listGame, List<User> listUser, List<Author> listAuthor,
			List<FAQ> listFAQ, List<OS> listOS, List<Genre> listGenre, List<GameAuthor> listGameAuthor,
			List<GameGenre> listGameGenre, List<GameOS> listGameOS, List<GameUser> listGameUser,
			List<GameUserCart> listGameUserCarts) {
		super();
		this.clientSocket = clientSocket;
		this.listGame = listGame;
		this.listUser = listUser;
		this.listAuthor = listAuthor;
		this.listFAQ = listFAQ;
		this.listOS = listOS;
		this.listGenre = listGenre;
		this.listGameAuthor = listGameAuthor;
		this.listGameGenre = listGameGenre;
		this.listGameOS = listGameOS;
		this.listGameUser = listGameUser;
		this.listGameUserCarts = listGameUserCarts;
		this.in = null;
		this.out = null;
		this.messFromClient = "";
	}

	@Override
	public void run() {
		try {
			in = new DataInputStream(clientSocket.getInputStream());
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			
			messFromClient = in.readUTF();
			
			while(messFromClient == null) {
				messFromClient = in.readUTF();
			}
			
			switch (messFromClient) {
				case "Upload all list": {
					updateAllList();
					break;
				}
				
				default:
					break;
			}
			
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
	
	public void updateAllList() throws IOException {
		List<Object> listObjects = new ArrayList<>();
		listObjects.add(listGame);
		listObjects.add(listUser);
		listObjects.add(listAuthor);
		listObjects.add(listFAQ);
		listObjects.add(listOS);
		listObjects.add(listGenre);
		listObjects.add(listGameAuthor);
		listObjects.add(listGameGenre);
		listObjects.add(listGameOS);
		listObjects.add(listGameUser);
		listObjects.add(listGameUserCarts);
		out.writeObject(listObjects);
//		out.writeObject(listGame);
//		out.writeObject(listUser);
//		out.writeObject(listAuthor);
//		out.writeObject(listFAQ);
//		out.writeObject(listOS);
//		out.writeObject(listGenre);
//		out.writeObject(listGameAuthor);
//		out.writeObject(listGameGenre);
//		out.writeObject(listGameOS);
//		out.writeObject(listGameUser);
//		out.writeObject(listGameUserCarts);
	}
}
