package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.Home;
import application.Runner;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;

import javafx.scene.control.ComboBox;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;

public class FilterController {
	@FXML
	private AnchorPane filter_page;
	@FXML
	private ComboBox<String> release_date_value;
	@FXML
	private ComboBox<String> genres_value;
	@FXML
	private ComboBox<String> capacity_value;
	@FXML
	private ComboBox<String> os_value;
	@FXML
	private ComboBox<String> price_value;
	@FXML
	private ComboBox<String> ram_value;
	@FXML
	private StackPane filter_game_container;
	@FXML
	private FlowPane list_game_container;
	@FXML
	private TextField search_value;
    @FXML
    private AnchorPane filter_item_container;
    @FXML
    private AnchorPane item_left;
    @FXML
    private AnchorPane item_right;
    
	private String sql = "";
	
	private HomeController homeController;
	
	
	
	@FXML
	void changeCapacity(ActionEvent event) throws SQLException, IOException {
		updateFilter();
		addGamesFromDataBase(getSql());
	}

	@FXML
	void changeDate(ActionEvent event) throws SQLException, IOException {
		updateFilter();
		addGamesFromDataBase(getSql());
	}

	@FXML
	void changeGenres(ActionEvent event) throws SQLException, IOException {
		updateFilter();
		addGamesFromDataBase(getSql());
	}

	@FXML
	void changeOS(ActionEvent event) throws SQLException, IOException {
		updateFilter();
		addGamesFromDataBase(getSql());
	}

	@FXML
	void changePrice(ActionEvent event) throws SQLException, IOException {
		updateFilter();
		addGamesFromDataBase(getSql());
	}

	@FXML
	void changeRam(ActionEvent event) throws SQLException, IOException {
		updateFilter();
		addGamesFromDataBase(getSql());
	}
	
	
	public void init() throws SQLException, IOException {
		new Thread(() -> {
			addForCapacity();
	        try {
				addForGenres();
				addForOS();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 
	        addForPrice();
	        addForRam();
	        addForRealeaseDate();

	        Platform.runLater(() -> {
	        	item_left.prefWidthProperty().bind(filter_item_container.widthProperty().divide(2.0).subtract(7.5));
			    item_right.prefWidthProperty().bind(filter_item_container.widthProperty().divide(2.0).subtract(7.5));
		        
		        search_value.textProperty().addListener((arg0, arg1, arg2) -> {
		        	Platform.runLater(() -> {
		        		if (!arg2.trim().isEmpty()) {
			        		resetAllFilter();
			        		setDisableForAllFilter(true);
			        		setSql("SELECT * FROM games WHERE game_name LIKE '" + arg2.trim() + "%';");
			        		try {
								addGamesFromDataBase(sql);
							} catch (SQLException | IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			        	} else {
			        		list_game_container.getChildren().clear();
			        		setDisableForAllFilter(false);
			        	}
		        	});
		        	
		        });
		        
				StackPane.setAlignment(list_game_container, Pos.TOP_CENTER);
				filter_game_container.widthProperty().addListener((arg0, arg1, arg2) -> {
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
					list_game_container.setPrefWrapLength(list_game_container.getPrefWidth());
				});
	        });
		}).start();
		
		
	}
	
	public void addGamesFromDataBase(String sql) throws SQLException, IOException {
		if (sql.equals("SELECT * FROM games;") || sql.isEmpty()) {
			return;
		}
		
    	Connection con = MyController.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		Platform.runLater(() -> {
			list_game_container.getChildren().clear();
			
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
				   list_game_container.getChildren().add(pane);
				   homeController.getListGame().add(controller);
			   }
				con.close();
				ps.close();
				rs.close(); 
			} catch (SQLException | IOException e) {
				e.printStackTrace();
			}
        });
	}
	
	public void resetAllFilter() {
		genres_value.setValue(null);
		os_value.setValue(null);
		capacity_value.setValue(null);
		ram_value.setValue(null);
		release_date_value.setValue(null);
		price_value.setValue(null);
	}
	
	
	public void setDisableForAllFilter(boolean status) {
		genres_value.setDisable(status);
		os_value.setDisable(status);
		capacity_value.setDisable(status);
		ram_value.setDisable(status);
		release_date_value.setDisable(status);
		price_value.setDisable(status);
	}
	
	public void addForRealeaseDate() {
		Platform.runLater(() -> {
			for (int i = 2024; 1924 <= i; i--) {
				release_date_value.getItems().add("" + i);
			}
        });
	}
	
	public void addForGenres() throws SQLException {
		Connection con = MyController.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT * FROM genres;");
		ResultSet rs = ps.executeQuery();
		
		Platform.runLater(() -> {
			try {
				while (rs.next()) {
					genres_value.getItems().add(rs.getString("genre_name"));
				}
				
				con.close();
				ps.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
        });
	}
	
	public void addForOS() throws SQLException {
		Connection con = MyController.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT * FROM operating_systems;");
		ResultSet rs = ps.executeQuery();
		
		Platform.runLater(() -> {
			try {
				while (rs.next()) {
					os_value.getItems().add(rs.getString("os_name"));
				}
				
				con.close();
				ps.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
        });
	}
	
	public void addForPrice() {
		Platform.runLater(() -> {
			for (int i = 1000; 100 <= i; i = i - 100) {
				price_value.getItems().add("Under " + i + "$");
			}
			
			price_value.getItems().addAll("Under 50$", "Under 25$");
        });
	}
	
	public void updateFilter() {
		setSql(createSQL());
	}
	
	public String createSQL() {
        String sql = "SELECT * FROM games";
        StringBuilder addWhere = new StringBuilder();
        
        String releaseDate = release_date_value.getValue();
		String genres = genres_value.getValue();
		String os = os_value.getValue();
		
		String capacity = capacity_value.getValue();
		if (capacity != null) {
			capacity = capacity.replaceAll("[^0-9]", "");
		}
		
		String ram = ram_value.getValue();
		if (ram != null) {
			ram = ram.replaceAll("[^0-9]", "");
		}
		
		String price = price_value.getValue();
		if (price != null) {
			price = price.replaceAll("[^0-9]", "");
		}
        
        if (releaseDate != null) {
        	addWhere.append("YEAR(release_date) = '").append(releaseDate).append("' AND ");
        }
        if (genres != null) {
            addWhere.append("game_id IN (SELECT game_id FROM game_genre WHERE genre_id = (SELECT genre_id FROM genres WHERE genre_name = '").append(genres).append("')) AND ");
        }
        if (os != null) {
            addWhere.append("game_id IN (SELECT game_id FROM game_os WHERE os_id = (SELECT os_id FROM operating_systems WHERE os_name = '").append(os).append("')) AND ");
        }
        if (capacity != null) {
            addWhere.append("min_download_size <= ").append(capacity).append(" AND ");
        }
        if (ram != null) {
            addWhere.append("min_ram_requirement <= ").append(ram).append(" AND ");
        }
        if (price != null) {
            addWhere.append("price <= ").append(price).append(" AND ");
        }

        if (addWhere.length() > 0) {
            addWhere.setLength(addWhere.length() - 5);
            sql += " WHERE " + addWhere.toString();
        }
        
        return sql + ";";
    }
	
	public void addForCapacity() {
		Platform.runLater(() -> {
			for (int i = 200; 20 <= i; i = i - 20) {
				capacity_value.getItems().add("Under " + i + "GB");
			}
			
			capacity_value.getItems().addAll("Under 10GB", "Under 5GB", "Under 2GB");
        });
	}
	
	public void addForRam() {
		Platform.runLater(() -> {
			for (int i = 64; 4 <= i; i = i - 12) {
				ram_value.getItems().add("Under " + i + "GB");
			}
			
			ram_value.getItems().add("Under 2GB");
        });
	}
	
	public String getSql() { 
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public HomeController getHomeController() {
		return homeController;
	}

	public void setHomeController(HomeController homeController) {
		this.homeController = homeController;
	}
	
	
}

