package Model;

import Controller.GameItemCartController;
import javafx.scene.Node;


public class CartItem {
	private Node pane;
	private GameItemCartController cartController;
	private int id;
	
	
	public CartItem() {
		super();
		// TODO Auto-generated constructor stub
	}


	public CartItem(Node pane, GameItemCartController cartController, int id) {
		super();
		this.pane = pane;
		this.cartController = cartController;
		this.id = id;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Node getPane() {
		return pane;
	}



	public void setPane(Node pane) {
		this.pane = pane;
	}



	public GameItemCartController getCartController() {
		return cartController;
	}



	public void setCartController(GameItemCartController cartController) {
		this.cartController = cartController;
	}



	
}
