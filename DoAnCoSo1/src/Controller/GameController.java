package Controller;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class GameController {
    @FXML
    private Pane image_pane;
    @FXML
    private Label game_name;
    @FXML
    private Label download_turns;
    @FXML
    private Label view_turns;
    
    @FXML
    private AnchorPane game_container;
    
    private int game_id = -1;
    
    private HomeController homeController = null;
    
    public void init() {
    	new Thread(() -> {
    		Platform.runLater(() -> {
        		MyController.radiusForPane(game_container);
            	
            	game_container.setOnMouseEntered(arg0 -> {
            		Platform.runLater(() -> {
            			game_container.setStyle("-fx-background-color:  #0077b6;");
            		});
            		
            	});
            	
            	game_container.setOnMouseExited(arg0 -> {
            		Platform.runLater(() -> {
            			game_container.setStyle("-fx-background-color:  #00b4d8;");
            		});
            	});
        	});
    		
    		try {
    			updateViewTurn();
    			updateDownloadTurn();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}).start();
    }
    
    
    @FXML
    void Game_Pane(MouseEvent event) throws SQLException, IOException {
    	if (game_id == -1) {
            System.out.println("Game ID is not set");
            return;
        }
    	
    	
    	new Thread(new Runnable() {
            @Override
            public void run() {
            	Connection connect = MyController.getConnection();
            	try {
					updateDownloadTurn(connect);
					updateViewTurn(connect);
					connect.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	try {
            		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/View/GameInformation.fxml"));
            	    Pane pane = loader.load();
            	    GameInformationController controller = loader.getController();

            	    FXMLLoader loader1 = new FXMLLoader(this.getClass().getResource("/View/Update.fxml"));
					Pane pane1 = loader1.load();
					UpdateController controller1 = loader1.getController();
            		
                	Connection con = MyController.getConnection();
            	    PreparedStatement ps = con.prepareStatement("SELECT * FROM games WHERE game_id = ?;");
                	PreparedStatement ps1 = con.prepareStatement("SELECT g.genre_name "
                	        + "FROM games ga "
                	        + "INNER JOIN game_genre gg ON ga.game_id = gg.game_id "
                	        + "INNER JOIN genres g ON gg.genre_id = g.genre_id "
                	        + "WHERE ga.game_id = ?;");
                	PreparedStatement ps2 = con.prepareStatement("SELECT u.update_description, u.update_date, g.game_cover_image FROM updates u JOIN games g ON u.game_id = g.game_id WHERE u.game_id = ? ORDER BY u.update_date DESC;");
                    PreparedStatement ps3 = con.prepareStatement("SELECT * FROM games WHERE game_id = ?;");
                    PreparedStatement ps4 = con.prepareStatement("SELECT * FROM operating_systems os JOIN game_os gos ON os.os_id = gos.os_id WHERE gos.game_id = ?;");
                    PreparedStatement ps5 = con.prepareStatement("SELECT * FROM authors au JOIN game_authors gau ON au.author_id = gau.author_id WHERE gau.game_id = ?;");
                    
                	ps.setInt(1, game_id);
                	ps1.setInt(1, game_id);
                    ps2.setInt(1, game_id);
                    ps3.setInt(1, game_id);
                    ps4.setInt(1, game_id);
                    ps5.setInt(1, game_id);
                    
                    
                    
                    ResultSet rs = ps.executeQuery();
                 	ResultSet rs1 = ps1.executeQuery();
                    ResultSet rs2 = ps2.executeQuery();
             		ResultSet rs3 = ps3.executeQuery();
             		ResultSet rs4 = ps4.executeQuery();
             		ResultSet rs5 = ps5.executeQuery();
             		
             		List<String> date_list = new ArrayList<>();
             		List<String> dev_list = new ArrayList<>();
             		List<String> os_list = new ArrayList<>();
                	

             		
                    Platform.runLater(() -> {
                    	try {
                    		boolean check = false;
                    		
							if (rs.next()) {
								check = true;
								controller.setDescription(rs.getString("game_description"));
								controller.setGameName(rs.getString("game_name"));
								controller.setReleaseDate(MyController.changeDateToString(rs.getDate("release_date")));
								controller.addImage(rs.getBytes("game_cover_image"));
								controller.setHomeController(homeController);
								controller.setId(game_id);
								
							}
							
							if (check) {
								while (rs1.next()) {
									controller.addTagGenre(rs1.getString("genre_name"));
								}
							}
							  
		                     while (rs5.next()) {
									dev_list.add(rs5.getString("author_name"));
								}
		                     	controller.addListAuthor(dev_list);
								
								while (rs4.next()) {
									os_list.add(rs4.getString("os_name"));
								}
								
								controller.addListOS(os_list);
								
								if (rs3.next()) {
									controller.getRam_value().setText(rs3.getInt("min_ram_requirement") + " GB");
									controller.getCapacity_value().setText(rs3.getInt("min_download_size") + " GB");
									controller.getCost().setText(rs3.getBigDecimal("price") + " $");
								}							
								
								
								
								while (rs2.next()) {
									Image ig = MyController.createImage(rs2.getBytes("game_cover_image"));
									ImageView image = new ImageView(ig);
									controller1.addImageGame(image);
									
									String des = rs2.getString("update_description");
									
									if (des == null) {
										controller1.setDescriptionUpdate("");
									} else {
										controller1.setDescriptionUpdate(des);
									}	
									
									String date = MyController.changeDateToString(rs2.getDate("update_date"));
									date_list.add(date);
									controller1.setUpdateDate(date);
									controller.getUpdate_list().getChildren().add(pane1);
								}
								
								if (date_list.isEmpty()) {
									controller.concat(controller.getLast_update(), " No update");
						        	 System.out.println("List is empty");
						        } else {
						        	controller.concat(controller.getLast_update(), " " + date_list.get(0));
						        	System.out.println("List is not empty");
						        }
		                     
								controller.init();
								controller1.init();
			                    homeController.setPane_tmp(pane);
			                    homeController.showGameInformation();
								

			                 	ps.close();
			                 	ps1.close();
			                 	ps2.close();
			                 	ps3.close();
			                 	ps4.close();
			                 	ps5.close();
			                 	rs.close();
			                 	rs1.close();
			                 	rs2.close();
			                 	rs3.close();
			                 	rs4.close();
			                 	rs5.close();
								con.close();
		                 	
						} catch (SQLException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}           
                    });

                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
                
            }
        }).start();
    }
    
	public void updateViewTurn() throws SQLException {
		Connection con = MyController.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) AS view_focus FROM gamestore.user_game_cart WHERE game_id = ?;");
	    ps.setInt(1, this.game_id);
	    ResultSet rs = ps.executeQuery();
	    
	    if (rs.next()) {
	    	int turn = rs.getInt("view_focus");
	    	System.out.println("new View turn " + turn);
	    	Platform.runLater(() -> {
	    		view_turns.setText(turn + "");
	    	});
	    }
	    con.close();
	    ps.close();
	    rs.close();
	}
    
	public void updateDownloadTurn() throws SQLException {
		Connection con = MyController.getConnection();
	    PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) AS download_turn FROM gamestore.game_user WHERE game_id = ?;");
	    ps.setInt(1, this.game_id);
	    ResultSet rs = ps.executeQuery();
	    
	    if (rs.next()) {
	    	int turn = rs.getInt("download_turn");
	    	Platform.runLater(() -> {
	    		download_turns.setText(turn + "");
	    	});
	    }
	    con.close();
	    ps.close();
	    rs.close();
	}
    
	public void updateViewTurn(Connection con) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) AS view_focus FROM gamestore.user_game_cart WHERE game_id = ?;");
	    ps.setInt(1, this.game_id);
	    ResultSet rs = ps.executeQuery();
	    
	    if (rs.next()) {
	    	int turn = rs.getInt("view_focus");
	    	System.out.println("new View turn " + turn);
	    	Platform.runLater(() -> {
	    		view_turns.setText(turn + "");
	    	});
	    }

	    ps.close();
	    rs.close();
	}
    
	public void updateDownloadTurn(Connection con) throws SQLException {
	    PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) AS download_turn FROM gamestore.game_user WHERE game_id = ?;");
	    ps.setInt(1, this.game_id);
	    ResultSet rs = ps.executeQuery();
	    
	    if (rs.next()) {
	    	int turn = rs.getInt("download_turn");
	    	Platform.runLater(() -> {
	    		download_turns.setText(turn + "");
	    	});
	    }
	    
	    ps.close();
	    rs.close();
	}
	
	public String changeDateToString(Date date) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
    	return dateFormat.format(date);
    }
    
	public void uploadDownloadTurns() throws SQLException {
		Connection con = MyController.getConnection();
		PreparedStatement ps = con.prepareStatement("");
	}
    
    // Add methods to modify elements as needed
    public void setGameName(String name) {
        game_name.setText(name);
    }

    public void setDownloadTurns(int turns) {
        download_turns.setText(String.valueOf(turns));
    }

    public void setViewTurns(int turns) {
        view_turns.setText(String.valueOf(turns));
    }
    
    public void addImageForImagePane(byte[] dataOfImage) throws IOException {
    	Image ig = MyController.createImage(dataOfImage);
    	ImageView image = new ImageView(ig);
    	image.fitHeightProperty().bind(image_pane.heightProperty());
    	image.fitWidthProperty().bind(image_pane.widthProperty());
    	
    	image_pane.getChildren().clear();
    	image_pane.getChildren().add(image);
    }

	public int getGame_id() {
		return game_id;
	}

	public void setGame_id(int game_id) {
		this.game_id = game_id;
	}
	
	public HomeController getHomeController() {
		return homeController;
	}

	public void setHomeController(HomeController homeController) {
		if (homeController != null) {
			this.homeController = homeController;
			System.out.println("Added home Controller to Game Controller");
		} else {
			System.out.println("Cannot add home Controller in Game Controller");
		}
	}
}