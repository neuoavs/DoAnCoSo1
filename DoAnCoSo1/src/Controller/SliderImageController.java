package Controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.Home;
import application.Runner;

public class SliderImageController {
	
	@FXML
	private Pane image_container;
	@FXML
	private Circle image_1;
	@FXML
	private Circle image_2;
	@FXML
	private Circle image_3;
	@FXML
	private Circle image_4;
	@FXML
	private Circle image_5;
	@FXML
	private Label next;
	@FXML
	private Label back;

	private List<Image> images = new ArrayList<>();
	private List<Circle> circles = new ArrayList<>();
	private int currentImageIndex = 0;
	private Timeline timeline;
	private Color light;
	private Color dark;
	private List<Integer> IDs = new ArrayList<>();

	private HomeController homeController = null;
	
	public void init(){
		new Thread(() -> {
			Platform.runLater(() -> {
				MyController.radiusForPane(image_container);
				dark = Color.rgb(0, 180, 216);
				light = Color.rgb(202, 240, 248);
				MyController.addEffectButton(next, "#90e0ef", "#00b4d8");
				MyController.addEffectButton(back, "#90e0ef", "#00b4d8");
			});
			
			try {
				addImages();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			showImage(0);
			startAutomaticImageChange();
		}).start();
	}

	@FXML
	public void back_images(MouseEvent event) {
		changeImage(-1);
		resetTimeline();
	}

	@FXML
	public void next_images(MouseEvent event) {
		changeImage(1);
		resetTimeline();
	}

	@FXML
	void show_game(MouseEvent event) throws IOException, SQLException {
		Connection con = MyController.getConnection();
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/View/GameInformation.fxml"));
		Pane pane = loader.load();
		GameInformationController controller = loader.getController();

		int game_id = IDs.get(currentImageIndex);

		PreparedStatement ps = con.prepareStatement("SELECT * FROM games WHERE game_id = " + game_id + ";");
		PreparedStatement ps1 = con.prepareStatement(
				"SELECT g.genre_name " + "FROM games ga " + "INNER JOIN game_genre gg ON ga.game_id = gg.game_id "
						+ "INNER JOIN genres g ON gg.genre_id = g.genre_id " + "WHERE ga.game_id = " + game_id + ";");

		
		ResultSet rs = ps.executeQuery();
		ResultSet rs1 = ps1.executeQuery();
		Platform.runLater(() -> {

			try {
				if (rs.next()) {
					controller.setDescription(rs.getString("game_description"));
					controller.setGameName(rs.getString("game_name"));
					controller.setReleaseDate(MyController.changeDateToString(rs.getDate("release_date")));
					controller.addImage(rs.getBytes("game_cover_image"));
					while (rs1.next()) {
						controller.addTagGenre(rs1.getString("genre_name"));
					}
					homeController.setPane_tmp(pane);
					homeController.showGameInformation();

					con.close();
					ps.close();
					ps1.close();
					rs.close();
					rs1.close();
				}
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});		
	}

	public void addImages() throws SQLException {
		Connection con = MyController.getConnection();

		PreparedStatement ps = con
				.prepareStatement("SELECT * FROM games " + "ORDER BY release_date DESC " + "LIMIT 5;");

		ResultSet rs = ps.executeQuery();

		Platform.runLater(() -> {
			try {
				while (rs.next()) {
					
					images.add(MyController.createImage(rs.getBytes("game_cover_image")));
					IDs.add(rs.getInt("game_id"));
				}
				circles.add(image_1);
				circles.add(image_2);
				circles.add(image_3);
				circles.add(image_4);
				circles.add(image_5);

				for (int i = 0; i < circles.size(); i++) {
					final int imageIndex = i;
					circles.get(i).setOnMouseClicked(event -> {
						currentImageIndex = imageIndex;
						showImage(currentImageIndex);
						updateCircleColors();
						resetTimeline();
					});
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

	private void changeImage(int step) {
		currentImageIndex = (currentImageIndex + step + images.size()) % images.size();
		showImage(currentImageIndex);
		updateCircleColors();
	}

	private void showImage(int index) {
		Platform.runLater(() -> {
			Image image = images.get(index);
			ImageView imageView = new ImageView(image);
			
			imageView.fitWidthProperty().bind(image_container.widthProperty());
			imageView.fitHeightProperty().bind(image_container.heightProperty());
			image_container.getChildren().clear();
			image_container.getChildren().add(imageView);
		});
	}

	private void updateCircleColors() {
		Platform.runLater(() -> {
			for (int i = 0; i < circles.size(); i++) {
				if (i == currentImageIndex) {
					circles.get(i).setFill(dark);
				} else {
					circles.get(i).setFill(light);
				}
			}
		});
	}

	private void startAutomaticImageChange() {
		timeline = new Timeline(new KeyFrame(Duration.seconds(3), this::nextImage));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	private void resetTimeline() {
		timeline.stop();
		timeline.playFromStart();
	}

	private void nextImage(ActionEvent event) {
		changeImage(1);
	}
	
	public HomeController getHomeController() {
		return homeController;
	}

	public void setHomeController(HomeController homeController) {
		if (homeController != null) {
			this.homeController = homeController;
			System.out.println("Added home Controller to Slider Image Controller");
		} else {
			System.out.println("Cannot add home Controller in Slider Image Controller");
		}
	}
}
