package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.Home;
import application.Runner;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;

import javafx.scene.input.MouseEvent;

import javafx.scene.layout.StackPane;

public class PersonalController {
	 @FXML
	 private Label birth_value;

	 @FXML
	 private Label change;

	 @FXML
	 private Label email_value;

	 @FXML
	 private AnchorPane infor_left;

	 @FXML
	 private AnchorPane infor_right;

	 @FXML
	 private AnchorPane infor_user_container;

	 @FXML
	 private FlowPane list_game_container;

	 @FXML
	 private Label nationality_value;

	 @FXML
	 private Label username_value;

	 @FXML
	 private StackPane your_game_container;
	 
	 private HomeController homeController = null;

	 @FXML
	 void changeMenu(MouseEvent event) throws IOException, SQLException {
		 homeController.showChangePage();
	 }


	
	public void init() throws SQLException, IOException {
		Platform.runLater(() -> {
			infor_right.prefWidthProperty().bind(infor_user_container.widthProperty().divide(2.0).subtract(7.5));
			infor_left.prefWidthProperty().bind(infor_user_container.widthProperty().divide(2.0).subtract(7.5));
			MyController.addEffectButton(change, "#00b4d8", "#0077b6");
			list_game_container.prefWrapLengthProperty().bind(list_game_container.prefWidthProperty());
			StackPane.setAlignment(list_game_container, Pos.TOP_CENTER);
			your_game_container.widthProperty().addListener((arg0, arg1, arg2) -> {
				Platform.runLater(() -> {
					double newWidth = arg2.doubleValue();
					
					if (newWidth >= 854.08 && newWidth < 1092.6) {
						list_game_container.setPrefWidth(854.08);
						list_game_container.setHgap(26.56);
						list_game_container.setVgap(26.56);
					}
					
					if (newWidth >= 1092.6 && newWidth < 1152.0) {
						list_game_container.setPrefWidth(1017.28);
						list_game_container.setHgap(12.32);
						list_game_container.setVgap(12.32);
					}
					
					if (newWidth == 1152.0) {
						list_game_container.setPrefWidth(3270.0 / 3);
						list_game_container.setHgap(92.0 / 3);
						list_game_container.setVgap(92.0 / 3);
					}
				});
			});
		});
		addGamesFromDataBase();
		updateInformation();
		
	}
	
	public void addGamesFromDataBase() throws SQLException, IOException {
		new Thread(() -> {
			try {
				Connection con = MyController.getConnection();
				PreparedStatement ps = con.prepareStatement("SELECT * FROM games g JOIN game_user gu ON g.game_id = gu.game_id WHERE gu.user_id = ?;");
				ps.setInt(1, homeController.getKeyDB().getUser_id());
				ResultSet rs = ps.executeQuery();
				Platform.runLater(() -> {
					list_game_container.getChildren().clear();
					try {
		        	   while (rs.next()) {
						   FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/View/Game.fxml"));
						   Pane pane = loader.load();

						   GameController controller = loader.getController();
						    
						   if (controller == null) {
						       System.out.println("Controller is null");
						   }
						    
						   controller.setGameName(rs.getString("game_name"));
						   controller.addImageForImagePane(rs.getBytes("game_cover_image"));
						   controller.setGame_id(rs.getInt("game_id"));
						   controller.setHomeController(homeController);
						   controller.init();
						   list_game_container.getChildren().add(pane);
						   homeController.getListGame().add(controller);
					   }
		        	   
						con.close();
						ps.close();
						rs.close();
			
					} catch (SQLException | IOException e) {
						e.printStackTrace();
					}
		        });
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
	}
	
	public void updateInformation() throws SQLException, IOException{
		new Thread(() -> {
			try {
				Connection con = MyController.getConnection();
				PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE user_id = ?;");
				ps.setInt(1, homeController.getKeyDB().getUser_id());
				ResultSet rs = ps.executeQuery();
				Platform.runLater(() -> {
					try {
		        	   if (rs.next()) {
						  username_value.setText(rs.getString("username"));
						  email_value.setText(rs.getString("email"));
						  String date = rs.getString("date_of_birth");
						  if (date.isEmpty() || date == null) {
							  birth_value.setText("None");
						  } else {
							  birth_value.setText(date);
						  }
						  String nat = rs.getString("nationality");
						  if (nat.isEmpty() || nat == null) {
							  nationality_value.setText("None");
						  } else {
							  nationality_value.setText(nat);
						  }
					   }
						con.close();
						ps.close();
						rs.close();
						Thread.interrupted();
					} catch (SQLException e) {
						e.printStackTrace();
					}
		        });
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
		
	}
	
	public HomeController getHomeController() {
		return homeController;
	}

	public void setHomeController(HomeController homeController) {
		if (homeController != null) {
			this.homeController = homeController;
			System.out.println("Added home Controller to Add Fund Controller");
		} else {
			System.out.println("Cannot add home Controller in Add Fund Controller");
		}
	}
}
