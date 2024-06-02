package Controller;


import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import Model.CartItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;

public class GameInformationController {

	@FXML
    private Label add_carts;

    @FXML
    private AnchorPane add_fund_container;

    @FXML
    private Label capacity_value;

    @FXML
    private Label cost;

    @FXML
    private Text description_game;

    @FXML
    private Label developer;

    @FXML
    private Text game_name;

    @FXML
    private AnchorPane game_name_pane;

    @FXML
    private Pane image_game_pane;

    @FXML
    private AnchorPane infor_container;

    @FXML
    private Label last_update;

    @FXML
    private Label list_os;

    @FXML
    private Label next;

    @FXML
    private AnchorPane page_game_infor;

    @FXML
    private Label ram_value;

    @FXML
    private Label release_date;

    @FXML
    private ScrollPane scroll_pane_update;

    @FXML
    private FlowPane tag_genre_container;

    @FXML
    private AnchorPane update_container;

    @FXML
    private HBox update_list;
	    
    private int id = -1;
    
    private HomeController homeController;;
    
    @FXML
    void nextUpdates(MouseEvent event) {
         animateScroll(0.1);
    }
    
    @FXML
    void addCart(MouseEvent event) {
    	new Thread(() -> {
    		
    		try {
    			if (checkHadGame()) {
					Platform.runLater(() -> {
	    				showError("You had this game befor.", "Nofitication");
	    			});
				} else if (checkInCart()) {
	    			Platform.runLater(() -> {
	    				showError("The game is readly added to your cart befor!", "Nofitication");
	    			});
	    		} else {
	    			try {
	        			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/GameItemCart.fxml"));
	    				Pane pane = loader.load();
	    				GameItemCartController cartController = loader.getController();
	    				cartController.setHomeController(homeController);
	    				cartController.init(this.id);
	    				addCartSQL();
	    				Platform.runLater(() -> {
	    					pane.prefWidthProperty().bind(homeController.getAddCartController().getList_game_container().prefWidthProperty());
	    					homeController.getAddCartController().getList_game_container().getChildren().add(pane);
	    					homeController.getAddCartController().getListCart().add(new CartItem(pane, cartController, this.id));
	    					showError("The game is added to your cart", "Nofitication");
	    					homeController.updateViewTurn(this.id);
	    				});
	        		 } catch (IOException | SQLException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	        		 }
	    		}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		 
    	}).start();
    }
    
    public boolean checkInCart() throws SQLException {
    	Connection con = MyController.getConnection();
    	PreparedStatement ps = con.prepareStatement("SELECT * FROM gamestore.user_game_cart WHERE game_id = ? AND user_id = ?;");
    	ps.setInt(1, this.id);
    	ps.setInt(2, homeController.getKeyDB().getUser_id());
    	
    	if (ps.executeQuery().next()) {
    		con.close();
    		ps.close();
    		return true;
    	}
    	
    	con.close();
    	ps.close();
    	return false;
    }
    
    public boolean checkHadGame() throws SQLException {
    	Connection con = MyController.getConnection();
    	PreparedStatement ps = con.prepareStatement("SELECT * FROM gamestore.game_user WHERE game_id = ? AND user_id = ?;");
    	ps.setInt(1, this.id);
    	ps.setInt(2, homeController.getKeyDB().getUser_id());
    	ResultSet rs = ps.executeQuery();
    	if (rs.next()) {
    		con.close();
    		ps.close();
    		rs.close();
    		return true;
    	}
    	
    	con.close();
		ps.close();
		rs.close();
    	return false;
    }
    
    public void addCartSQL() throws SQLException {
    	Connection con = MyController.getConnection();
    	PreparedStatement ps = con.prepareStatement("INSERT INTO `gamestore`.`user_game_cart` (`user_id`, `game_id`, `cart_date`) VALUES (?, ?, ?);");
    	ps.setInt(1, homeController.getKeyDB().getUser_id());
    	ps.setInt(2, this.id);
    	ps.setDate(3, Date.valueOf(LocalDate.now()));
    	
    	if (ps.executeUpdate() == 1) {
    		System.out.println("Added to cart the game with id = " + this.id);
    	} else {
    		System.out.println("Can not add to cart the game with id = " + this.id);
    	}
    	
    	con.close();
    	ps.close();
    }
    
    public void init() {
    	Platform.runLater(() -> {
	    	developer.setText("");
	    	list_os.setText("");
	    		
	    	MyController.addEffectButton(next, "#90e0ef", "#00b4d8");
	    	MyController.addEffectButton(add_carts, "#90e0ef", "#00b4d8");
	    	
	    	image_game_pane.prefHeightProperty().bind(image_game_pane.prefWidthProperty().divide(16.0 / 9.0));
	    	game_name.wrappingWidthProperty().bind(infor_container.prefWidthProperty());
	    	description_game.wrappingWidthProperty().bind(infor_container.prefWidthProperty());
	    	MyController.radiusForPane(scroll_pane_update);
	    	
	    	image_game_pane.prefWidthProperty().bind(page_game_infor.prefWidthProperty().multiply(0.7).subtract(45.0));
	    	infor_container.prefWidthProperty().bind(page_game_infor.prefWidthProperty().multiply(0.3));
	
	    	image_game_pane.widthProperty().addListener((arg0, arg1, arg2) -> {
	    		Platform.runLater(() -> {
	    			infor_container.setPrefHeight(image_game_pane.getPrefHeight());
	    		});
	    	});
	    	
	    	infor_container.heightProperty().addListener((arg0, arg1, arg2) -> {
	    		Platform.runLater(() -> {
	    			if (arg2.doubleValue() > 0.0) {
		        		updateLayout(arg2.doubleValue());
		    		}
	    		});
	    		
	    	});
	    	
	    	updateLayout(infor_container.getHeight());
    	});
    }
    
    public void updateLayout(double infor_h) {
    	AnchorPane.setTopAnchor(last_update, infor_h + 15.0);
    	AnchorPane.setTopAnchor(update_container, infor_h + 50.0);
    	double up_h = update_container.getHeight();
    	AnchorPane.setTopAnchor(add_fund_container, infor_h + up_h + 65.0);
    }
    
    public void setDescription(String description) {
    	description_game.setText(description);
    }
    
    public void setGameName(String name) {
    	game_name.setText(name);
    }
    
    public void setReleaseDate(String releaseDate) {
    	release_date.setText(releaseDate);
    }
    
    
    public void addTagGenre(String genre) {
    	if (genre != null) {
    		tag_genre_container.getChildren().add(createLabel(genre));
    		return;
    	}
    	System.out.println("genre is null");
    }
    
   
    public void addImage(byte[] dataOfImage) throws IOException {
    	Image ig = MyController.createImage(dataOfImage);
    	ImageView image = new ImageView(ig);
    	
    	image.fitHeightProperty().bind(image_game_pane.heightProperty());
    	image.fitWidthProperty().bind(image_game_pane.widthProperty());
	
    	MyController.radiusForPane(image_game_pane);
    	
    	image_game_pane.getChildren().clear();
    	image_game_pane.getChildren().add(image);
    }
    
    public Label createLabel(String genre) {
    	Label label = new Label(genre);
    	label.setAlignment(Pos.CENTER);
    	label.setStyle("-fx-text-fill: #03045e; -fx-padding: 5px; -fx-background-radius: 5px; -fx-background-color: #00b4d8;");
       
        return label;
    }
    
    public void concat(Label label, String text) {
    	String s = label.getText();
    	s = s + text;
    	label.setText(s);
    }
   

	public void addListOS(List<String> list) {
		
		
		if(list.isEmpty()) {
			list_os.setText("");
			return;
		}
		
		for (int i = 0; i < list.size(); i++) {
			
			if (i == (list.size() - 1)) {
				concat(list_os, list.get(i));
			} else {
				concat(list_os, list.get(i) + ", ");
			}
		}
	}
	
	
	public void addListAuthor(List<String> list) {
		if(list.isEmpty()) {
			developer.setText("");
			return;
		}
		
		for (int i = 0; i < list.size(); i++) {
			
			if (i == (list.size() - 1)) {
				concat(developer, list.get(i));
			} else {
				concat(developer, list.get(i) + "\n");
			}
		}
	}

	public void showError(String content, String type) {
		Alert a = new Alert(Alert.AlertType.ERROR);
		a.setTitle(type);
		a.setHeaderText(null);
		a.setContentText(content);
		a.setGraphic(null);
		a.show();
	}
	
	public void animateScroll(double targetScrollPosition) {
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(scroll_pane_update.hvalueProperty(), targetScrollPosition, Interpolator.EASE_BOTH);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }
   
	public AnchorPane getInfor_container() {
		return infor_container;
	}

	public void setInfor_container(AnchorPane infor_container) {
		this.infor_container = infor_container;
	}

	public Label getCapacity_value() {
		return capacity_value;
	}

	public void setCapacity_value(Label capacity_value) {
		this.capacity_value = capacity_value;
	}

	public Label getCost() {
		return cost;
	}

	public void setCost(Label cost) {
		this.cost = cost;
	}

	public Label getNext() {
		return next;
	}

	public void setNext(Label next) {
		this.next = next;
	}

	public Label getRam_value() {
		return ram_value;
	}

	public void setRam_value(Label ram_value) {
		this.ram_value = ram_value;
	}

	public Label getLast_update() {
		return last_update;
	}

	public void setLast_update(Label last_update) {
		this.last_update = last_update;
	}

	public HBox getUpdate_list() {
		return update_list;
	}

	public void setUpdate_list(HBox update_list) {
		this.update_list = update_list;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public HomeController getHomeController() {
		return homeController;
	}


	public void setHomeController(HomeController homeController) {
		this.homeController = homeController;
	}


}