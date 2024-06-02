package Controller;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SeeAllController {
    @FXML
    private StackPane see_game_container;

    @FXML
    private FlowPane list_game_container;

    private String sql = "";

    private HomeController homeController = null;

    public void init(){
    	new Thread(() -> {
    		Platform.runLater(() -> {
        		StackPane.setAlignment(list_game_container, Pos.TOP_CENTER);
                see_game_container.widthProperty().addListener((arg0, arg1, arg2) -> {
                    Platform.runLater(() -> {
                    	if (arg2.doubleValue() >= 854.08 && arg2.doubleValue() < 1092.6) {
                            list_game_container.setPrefWidth(854.08);
                            list_game_container.setHgap(26.56);
                            list_game_container.setVgap(26.56);
                        }
                        if (arg2.doubleValue() >= 1092.6 && arg2.doubleValue() < 1152.0) {
                            list_game_container.setPrefWidth(1017.28);
                            list_game_container.setHgap(12.32);
                            list_game_container.setVgap(12.32);
                        }
                        if (arg2.doubleValue() == 1152.0) {
                            list_game_container.setPrefWidth(3270.0 / 3);
                            list_game_container.setHgap(92.0 / 3);
                            list_game_container.setVgap(92.0 / 3);
                        }
                    });
                });
                
                list_game_container.widthProperty().addListener((arg0, arg1, arg2) -> {
                	Platform.runLater(() -> {
                		   list_game_container.setPrefWrapLength(list_game_container.getPrefWidth());
                	});
                });
        	});
        	
            try {
				addGamesFromDataBase(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}).start();
    }
    
    public void addGamesFromDataBase(String sql) throws SQLException {
    	Connection con = MyController.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        Platform.runLater(() -> {
            try {
                list_game_container.getChildren().clear();
                while (rs.next()) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Game.fxml"));
                    Pane pane = loader.load();
                    GameController controller = loader.getController();
                    if (controller != null) {
                        controller.setGameName(rs.getString("game_name"));
                        controller.addImageForImagePane(rs.getBytes("game_cover_image"));
                        controller.setGame_id(rs.getInt("game_id"));
                        controller.setHomeController(homeController);
                        controller.init();
                        list_game_container.getChildren().add(pane);
                        homeController.getListGame().add(controller);
                    } else {
                        System.out.println("Controller is null");
                    }
                }
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public HomeController getHomeController() {
        return homeController;
    }

    public void setHomeController(HomeController homeController) {
        if (homeController != null) {
            this.homeController = homeController;
            System.out.println("Added home Controller to See All Controller");
        } else {
            System.out.println("Cannot add home Controller in See All Controller");
        }
    }
}

