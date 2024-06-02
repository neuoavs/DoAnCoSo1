package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import application.Home;
import application.Runner;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class FAQPageController {
	@FXML
	private VBox list_FAQ_container;
	
	@FXML
	public void initialize() throws SQLException, IOException {
		init();
	}
	
	public void init() throws SQLException, IOException {
		addListFAQ();
	}
	
	public void addListFAQ() throws SQLException, IOException{
    	Connection con = MyController.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT * FROM gamestore.faqs;");
		ResultSet rs = ps.executeQuery();
		
		Platform.runLater(() -> {
			try {
				while (rs.next()) {
				    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/View/FAQ.fxml"));
				    Pane pane = loader.load();

				    FAQController controller = loader.getController();
				    
				    if (controller == null) {
				        System.out.println("Controller is null");
				    }
				    
				    controller.setQuestion(rs.getString("question"));
				    controller.setAnswer(rs.getString("answer"));
				    list_FAQ_container.getChildren().add(pane);
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
}
