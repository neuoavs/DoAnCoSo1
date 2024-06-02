package Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import application.Home;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class NewGameController extends ListGameController {	
	@FXML
	void see_more(MouseEvent event) throws SQLException, IOException {
		if (getHomeController() != null) {
			getHomeController().setSql("SELECT * FROM games ORDER BY release_date DESC LIMIT 20;");
			getHomeController().showSeeAllGame();
		} else {
			System.out.println("Cannot see more in New Update Controller");
		}
	}
}
