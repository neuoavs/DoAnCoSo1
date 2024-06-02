package application;

import Controller.AccountViewController;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Account {
    private AccountViewController controller = null;
    
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AccountView.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
            Scene scene = new Scene(root, 600, 475.0);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            
            primaryStage.setOnCloseRequest(event -> {
                System.exit(0);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public AccountViewController getController() {
		return controller;
	}

	public void setController(AccountViewController controller) {
		this.controller = controller;
	}
    
    
}


