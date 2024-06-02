package application;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Controller.HomeController;
import Model.KeyDB;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Home {
	private HomeController homeController;

	public void start(Stage primaryStage, KeyDB keyDB) throws SQLException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Home.fxml"));
            Parent root = loader.load();
            homeController = loader.getController();
            homeController.setKeyDB(keyDB);
            homeController.init();
            Scene scene = new Scene(root, 1290, 830.4);
            primaryStage.setScene(scene);
            primaryStage.show();
            
            primaryStage.setOnCloseRequest(event -> {
                System.exit(0);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    public HomeController getHomeController() {
        return homeController;
    }

    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

}
