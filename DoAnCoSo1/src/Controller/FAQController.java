package Controller;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class FAQController {

	  @FXML
	    private VBox FAQ_container;

	    @FXML
	    private Text answer;

	    @FXML
	    private VBox answer_container;

	    @FXML
	    private Text question;

	    @FXML
	    private VBox question_container;

	    @FXML
	    private AnchorPane question_pane;

	    @FXML
	    private ImageView arrow;

	    private Image down;
	    private Image up;
	    @FXML
		public void initialize() {
	    	new Thread(new Runnable() {
				@Override
	            public void run() {
					init();
					System.out.println("Inited FAQ controller");
	            }
			}).start();
	    }
	    
	    public void init() {
	    	
	    	Platform.runLater(() -> {
	    		arrow.setFitWidth(30);
		    	arrow.setFitHeight(30);
		    	up = new Image("file:F:\\Documents\\JavaStudy\\DoAnCoSo1\\src\\Image\\up.png");
		    	down = new Image("file:F:\\Documents\\JavaStudy\\DoAnCoSo1\\src\\Image\\down.png");
		    	
		    	
		    	answer_container.prefWidthProperty().addListener((arg0, arg1, arg2) -> {
		    		answer.setWrappingWidth(arg2.doubleValue() - 20);
		    	});
		    	
		    	question_container.prefWidthProperty().addListener((arg0, arg1, arg2) -> {
		    		question.setWrappingWidth(arg2.doubleValue() - 20);
		    	});
		    	
		    	arrow.setImage(down);
		    	FAQ_container.getChildren().clear();
		    	
		    	FAQ_container.getChildren().add(question_pane);
		    	
		    	answer_container.setVisible(false);
	    	});
	    }
	    
    @FXML
    void show_hide(MouseEvent event) {
    	Platform.runLater(() -> {
    		if (answer_container.isVisible()) {
        		FAQ_container.getChildren().remove(answer_container);
        		answer_container.setVisible(false);
        		arrow.setImage(down);
        	} else {
        		answer_container.setVisible(true);
            	FAQ_container.getChildren().add(answer_container);
            	arrow.setImage(up);
        	}
    	});
    }
    
    public void setQuestion(String text) {
    	question.setText(text);
    }
    
    public void setAnswer(String text) {
    	answer.setText(text);
    }
    
    
    
}
