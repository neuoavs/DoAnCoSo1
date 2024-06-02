	package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class GameItemCartController {

    @FXML
    private StackPane checkContainer;
	
    @FXML
    private CheckBox checkGame;

    @FXML
    private Pane gameImage;

    @FXML
    private Text gameName;

    @FXML
    private Label price;

    @FXML
    private HBox selectItem;
    
    private int idGame;

    private HomeController homeController;
    
    private double cost = 0.0;
    private boolean firstChange = true;
    
    @FXML
    void select(MouseEvent event) {
    	if (checkGame.isSelected()) {
    		checkGame.setSelected(false);
    	} else {
    		checkGame.setSelected(true);
    	}
    }
    
    public void init(int id) throws SQLException {
    	idGame = id;
    	
    	Platform.runLater(() -> {
    		gameImage.prefHeightProperty().bind(gameImage.prefWidthProperty().multiply(9.0 / 16.0));
    		checkContainer.prefWidthProperty().bind(selectItem.prefWidthProperty().multiply(0.1));
        	gameImage.prefWidthProperty().bind(selectItem.prefWidthProperty().multiply(0.3));
        	gameName.wrappingWidthProperty().bind(selectItem.prefWidthProperty().multiply(0.45).subtract(5.0));
        	price.prefWidthProperty().bind(selectItem.prefWidthProperty().multiply(0.15));
    	});
    	
    	addCart(id, gameImage);
    	
    	checkGame.selectedProperty().addListener((arg0, arg1, arg2) -> {
    		if (checkGame.isSelected()) {
    			if (firstChange) {
    				firstChange = false;
    			}
    			
    			double bill = formatRealNum(getBeginBill() + cost);
    			updateBill(bill);
        	} else {
        		if (!firstChange) {
        			double bill = formatRealNum(getBeginBill() - cost);
        			updateBill(bill);
        		}
        		System.out.println("UnChecked");
        	}
    	});
    }
    
    public double formatRealNum(double num) {
    	return Math.round(num * 100.0) / 100.0;
    }
    
    public double getBeginBill() {
    	String s = homeController.getAddCartController().getBill_value().getText();
    	return Double.parseDouble(s.substring(0, s.length() - 1));
    }
    
    public void updateBill(double bill) {
    	Platform.runLater(() -> {
    		homeController.getAddCartController().getBill_value().setText(bill + "$");
    	});
    }
    
    public void addCart(int id, Pane pane) throws SQLException { 
    	Connection con = MyController.getConnection();
    	PreparedStatement ps = con.prepareStatement("SELECT * FROM games WHERE game_id = ?;");
    	ps.setInt(1, id);
    	ResultSet rs = ps.executeQuery();
    	
    	Platform.runLater(() -> {
    		try {
				if (rs.next()) {
					MyController.addImage(MyController.createImage(rs.getBytes("game_cover_image")), pane);
					gameName.setText(rs.getString("game_name"));
					price.setText(rs.getBigDecimal("price") + "$");
					cost = Double.parseDouble(rs.getBigDecimal("price").toString());
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
    

	public int getIdGame() {
		return idGame;
	}


	public void setIdGame(int idGame) {
		this.idGame = idGame;
	}


	public CheckBox getCheckGame() {
		return checkGame;
	}


	public void setCheckGame(CheckBox checkGame) {
		this.checkGame = checkGame;
	}


	public HomeController getHomeController() {
		return homeController;
	}


	public void setHomeController(HomeController homeController) {
		this.homeController = homeController;
	}


	public double getCost() {
		return cost;
	}


	public void setCost(double cost) {
		this.cost = cost;
	}


	public boolean isFirstChange() {
		return firstChange;
	}


	public void setFirstChange(boolean firstChange) {
		this.firstChange = firstChange;
	}

}
