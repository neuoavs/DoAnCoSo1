package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import application.Home;
import application.Runner;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;

public class ListGameController {
	@FXML
	private HBox game_container;
	
    @FXML
    private AnchorPane list_pane;
	
    @FXML
    private ScrollPane scroll_games;
	
    @FXML
    private Label next;
    
    @FXML
    private Label back;
 
    private HomeController homeController = null;
    
    
    private int currentIndex = 0;
    private int totalPanes = 0;
    private int panesPerPage = 4;
    
    public void init(String sql){
    	new Thread(() -> {
    		Platform.runLater(() -> {
    			 MyController.addEffectButton(next, "#90e0ef", "#00b4d8");
    			 MyController.addEffectButton(back, "#90e0ef", "#00b4d8");
    			 MyController.radiusForPane(scroll_games);
    		});
    		 try {
				addGamesFromDataBase(sql);
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}).start();
    }
	
	@FXML
    public void next_games(MouseEvent event) {
        int nextIndex = currentIndex + panesPerPage;
        if (nextIndex < totalPanes) {
            double scrollPosition = (double) nextIndex / (totalPanes - panesPerPage);
            animateScroll(scrollPosition);
            currentIndex = nextIndex;
        }
    }

    @FXML
    public void back_games(MouseEvent event) {
        int previousIndex = currentIndex - panesPerPage;
        if (previousIndex >= 0) {
            double scrollPosition = (double) previousIndex / (totalPanes - panesPerPage);
            animateScroll(scrollPosition);
            currentIndex = previousIndex;
        }
    }

    
    public void addGamesFromDataBase(String sql) throws SQLException, IOException {
    	Connection con = MyController.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        Platform.runLater(() -> {
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
				     game_container.getChildren().add(pane);
				     totalPanes++;
				     homeController.getListGame().add(controller);
				 }
				con.close();
	             ps.close();
	             rs.close();
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
      
        });
    }
    
    public void animateScroll(double targetScrollPosition) {
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(scroll_games.hvalueProperty(), targetScrollPosition, Interpolator.EASE_BOTH);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }
    
    public HomeController getHomeController() {
		return homeController;
	}

	public void setHomeController(HomeController homeController) {
		if (homeController != null) {
			this.homeController = homeController;
			System.out.println("Added home Controller to List Game Controller");
		} else {
			System.out.println("Cannot add home Controller in List Game Controller");
		}
	}
}