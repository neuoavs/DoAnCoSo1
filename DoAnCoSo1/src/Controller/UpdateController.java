package Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class UpdateController {
    @FXML
    private Label description_update;

    @FXML
    private Pane game_image;

    @FXML
    private Label update_date;
    
    @FXML
    private AnchorPane update_container;

    public void init() {
    	Platform.runLater(() -> {
    		MyController.radiusForPane(update_container);
    	});
    }
    
    
	public void addImageGame(ImageView imageView) {
		imageView.fitWidthProperty().bind(game_image.widthProperty());
		imageView.fitHeightProperty().bind(game_image.heightProperty());
		game_image.getChildren().clear();
		game_image.getChildren().add(imageView);
	}
	
	public void setDescriptionUpdate(String text) {
		description_update.setText(text);
	}
	
	
	public void setUpdateDate(String date) {
		update_date.setText(date);
	}
	
	
	
}
