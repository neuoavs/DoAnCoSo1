package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import Model.CartItem;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class AddCartController {
    @FXML
    private AnchorPane cart_page;
	
	@FXML
    private AnchorPane bill_container;

    @FXML
    private Label bill_value;

    @FXML
    private VBox list_game_container;

    @FXML
    private Label money_value;

    @FXML
    private Label pay_button;

    @FXML
    private ScrollPane scrollCart;

    @FXML
    private Label yourMone;

    @FXML
    private AnchorPane your_money_container;
	
    private HomeController homeController;
	private List<CartItem> listCart = new ArrayList<>();
    private List<Node> listNode = new ArrayList<>();
    private List<Integer> listID = new ArrayList<>();

	// Event Listener on Label.onMouseClicked
	@FXML
	public void pay(MouseEvent event) {
		new Thread(() -> {
			double money = Double.parseDouble(money_value.getText().substring(0,money_value.getText().length() - 1));
			double bill = Double.parseDouble(bill_value.getText().substring(0,bill_value.getText().length() - 1));
			if (bill > money) {
				showError("The money is not enought", "NOT ENOUGHT MONEY");
			} else {
				Connection con = MyController.getConnection();
				try {
					PreparedStatement ps = con.prepareStatement("UPDATE `gamestore`.`users` SET `balance` = ? WHERE (`user_id` = ?);");
					
					
					ps.setDouble(1,formatRealNum(money - bill));
					ps.setInt(2, homeController.getKeyDB().getUser_id());
					Platform.runLater(() -> {
						try {
							if (ps.executeUpdate() == 1) {
								checkPane(listNode, listID);
								removeListItem(listNode, listID);
								if (homeController.getFundsController() != null) {
									homeController.getFundsController().updateMoney();
								}
								updateMoney(homeController.getKeyDB().getUser_id());
								bill_value.setText("0.00$");
								showError("Congratulate! You have successfully payed.", "Congratulate");
							}
							con.close();
							ps.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					});
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public void uploadCart() throws SQLException, IOException {
		Connection con = MyController.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT uc.game_id, g.game_name, g.game_cover_image, g.price FROM user_game_cart uc JOIN games g ON uc.game_id = g.game_id WHERE uc.user_id = ?;");
		ps.setInt(1, homeController.getKeyDB().getUser_id());
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/GameItemCart.fxml"));
			Pane pane = loader.load();
			GameItemCartController cartController = loader.getController();
			cartController.setHomeController(homeController);
			cartController.init(rs.getInt("game_id"));
			int idGame = rs.getInt("game_id");
			Platform.runLater(() -> {
				pane.prefWidthProperty().bind(homeController.getAddCartController().getList_game_container().prefWidthProperty());
				homeController.getAddCartController().getList_game_container().getChildren().add(pane);
				homeController.getAddCartController().getListCart().add(new CartItem(pane, cartController, idGame));
				
			});
		}
		
		con.close();
		ps.close();
		rs.close();
	}
	
	public void checkPane(List<Node> listPane, List<Integer> listGameID) {
		listPane.clear();
		listGameID.clear();
		for (CartItem item : listCart) {
			if (item.getCartController().getCheckGame().isSelected()) {
				listPane.add(item.getPane());
				listGameID.add(item.getId());
			}
		}
	}
	
	public void removeListItem(List<Node> listPane, List<Integer> listGameID) {
		new Thread(() -> {
			 Connection con = MyController.getConnection();
			 List<Integer> temp = new ArrayList<>();
			 for (Integer id : listGameID) {
				 try {
					PreparedStatement ps = con.prepareStatement(createSQLAddGame(id));
					if (ps.executeUpdate() == 1) {
						 System.out.println("Added Game with id = " + id);
						 temp.add(id);
					} else {
						System.out.println("Can not add Game with id = " + id);
					}
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			 
			 for (Integer integer : temp) {
				homeController.updateDownloadTurn(integer);
			 }
			 
			 try {
				con.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			for (Node item : listPane) {
				Platform.runLater(() -> {
					list_game_container.getChildren().remove(item);
				});
			}
			 
		}).start();
		
	}
	
	public String createSQLAddGame(int idGame) {
		return "INSERT INTO `gamestore`.`game_user` (`game_id`, `user_id`) VALUES ('" + idGame + "', '" + homeController.getKeyDB().getUser_id() + "');";
	}
	
	public double formatRealNum(double num) {
    	return Math.round(num * 100.0) / 100.0;
    }
	
	public void init(int userID) throws SQLException {
		new Thread(() -> {
			Platform.runLater(() -> {
				MyController.addEffectButton(pay_button, "#00b4d8", "#0077b6");
				list_game_container.prefWidthProperty().bind(scrollCart.widthProperty());
				scrollCart.prefHeightProperty().bind(scrollCart.widthProperty().multiply(9.0 / 16.0));
				scrollCart.prefHeightProperty().addListener((arg0, arg1, arg2) -> {
					Platform.runLater(() -> {
						AnchorPane.setTopAnchor(your_money_container, arg2.doubleValue() + 15.0);
						AnchorPane.setTopAnchor(bill_container, arg2.doubleValue() + 15.0);
						AnchorPane.setTopAnchor(pay_button, arg2.doubleValue() + 15.0);
					});
				});
			});
			
			try {
				updateMoney(userID);
				uploadCart();
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}).start();
		
		
		
	}

    public void showError(String content, String title) {
    	Platform.runLater(() -> {
    		Alert a = new Alert(Alert.AlertType.ERROR);
        	a.setTitle(title);
        	a.setContentText(content);
        	a.setHeaderText(null);
        	a.setGraphic(null);
        	Optional<ButtonType> result = a.showAndWait();
        	
        	if (result.isEmpty()) {
        		System.out.println("Add fund alert is died");
        	} else if (result.get() == ButtonType.OK) {
        		System.out.println("Add fund alert is died");
        	}
    	});
    }
    
	
	public void updateMoney(int idUser) throws SQLException {
		Connection con = MyController.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE user_id = ?");
		ps.setInt(1, homeController.getKeyDB().getUser_id());
		
		ResultSet rs = ps.executeQuery();
		
		Platform.runLater(() -> {
			try {
				if (rs.next()) {
					money_value.setText(rs.getBigDecimal("balance") + "$");
				}
				con.close();
				ps.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
	
	public VBox getList_game_container() {
		return list_game_container;
	}

	public void setList_game_container(VBox list_game_container) {
		this.list_game_container = list_game_container;
	}

	public Label getBill_value() {
		return bill_value;
	}


	public void setBill_value(Label bill_value) {
		this.bill_value = bill_value;
	}


	public HomeController getHomeController() {
		return homeController;
	}

	public void setHomeController(HomeController homeController) {
		this.homeController = homeController;
	}


	public List<CartItem> getListCart() {
		return listCart;
	}


	public void setListCart(List<CartItem> listCart) {
		this.listCart = listCart;
	}
	
	
	
}
