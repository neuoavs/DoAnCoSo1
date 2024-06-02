package Controller;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.converter.LocalDateTimeStringConverter;


public class MyController {
	public static Connection getConnection() {
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gamestore", "root", "171410Tn");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	
	public static void setPadding(Node node, double top, double bottom, double left, double right) {
    	AnchorPane.setTopAnchor(node, top);
    	AnchorPane.setBottomAnchor(node, bottom);
    	AnchorPane.setLeftAnchor(node, left);
    	AnchorPane.setRightAnchor(node, right);
    }
	
	public static void setPadding(Node node, double topAndBottom, double leftAndRight) {
		AnchorPane.setTopAnchor(node, topAndBottom);
    	AnchorPane.setBottomAnchor(node, topAndBottom);
    	AnchorPane.setLeftAnchor(node, leftAndRight);
    	AnchorPane.setRightAnchor(node, leftAndRight);
	}

	
	public static void setPaddingTopLeft(Node node, double top, double left) {
		AnchorPane.setTopAnchor(node, top);
		AnchorPane.setLeftAnchor(node, left);
	}
	
	public static void setPaddingTopRight(Node node, double top, double right) {
		AnchorPane.setTopAnchor(node, top);
		AnchorPane.setRightAnchor(node, right);
	}
	
	
	public static void setPaddingBottomLeft(Node node, double bottom, double left) {
		AnchorPane.setBottomAnchor(node, bottom);
		AnchorPane.setLeftAnchor(node, left);
	}
	
	public static void setPaddingBottomRight(Node node, double bottom, double right) {
		AnchorPane.setBottomAnchor(node, bottom);
		AnchorPane.setRightAnchor(node, right);
	}
	
	
	public static void setPaddingBottomLeftRight(Node node, double bottom, double left, double right) {
		AnchorPane.setBottomAnchor(node, bottom);
		AnchorPane.setLeftAnchor(node, left);
		AnchorPane.setRightAnchor(node, right);
	}
	
	
	public static void setPaddingTopLeftRight(Node node, double top, double left, double right) {
		AnchorPane.setTopAnchor(node, top);
		AnchorPane.setLeftAnchor(node, left);
		AnchorPane.setRightAnchor(node, right);
	}
	
	public static void addEffectButton(Node node, String beforColor, String afterColor) {
		String defautStyle = node.getStyle();
		node.setOnMousePressed(event -> {
			
			node.setStyle(defautStyle + "-fx-background-color: " + afterColor +  ";");
		});
		
		node.setOnMouseReleased(event -> {
			node.setStyle(defautStyle + "-fx-background-color: " + beforColor + ";");
		});
	}
	
	
	public static Image createImage(byte[] data) {
		return new Image(new ByteArrayInputStream(data));
	}
	
	public static String changeDateToString(Date date) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
    	return dateFormat.format(date);
    }
	
	public static void updateLinePosition(Line line, AnchorPane container, double top, double left, double right) {
		double w;
	    double h;
	    
	    if (container.getWidth() > 0.0) {
	        w = container.getWidth();
	    } else {
	        w = container.getPrefWidth();
	    }
	    
	    if (container.getHeight() > 0.0) {
	        h = container.getHeight();
	    } else {
	        h = container.getPrefHeight();
	    }
	    
	    
	    Platform.runLater(() -> {
	    	line.setLayoutX(0.0);
	        line.setLayoutY(top);
	        line.setStartX(left);
	        line.setEndX(w - right);
	    });
	}
	
	public static void radiusForPane(Pane pane) {
		Rectangle clip = new Rectangle(); 
    	clip.setArcWidth(10);
    	clip.setArcHeight(10);
    	
    	pane.setClip(clip);
    	clip.widthProperty().bind(pane.widthProperty());
    	clip.heightProperty().bind(pane.heightProperty());
	}
	
	public static void radiusForPane(ScrollPane pane) {
		Rectangle clip = new Rectangle(); 
    	clip.setArcWidth(10);
    	clip.setArcHeight(10);
    	
    	pane.setClip(clip);
    	clip.widthProperty().bind(pane.widthProperty());
    	clip.heightProperty().bind(pane.heightProperty());
	}
	
	public static boolean checkFormatEmail(String email) { 
        String format = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                            "[a-zA-Z0-9_+&*-]+)*@" + 
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                            "A-Z]{2,7}$"; 
                              
        Pattern pattern = Pattern.compile(format); 
        if (email == null) 
            return false; 
        return pattern.matcher(email).matches(); 
    }
	
	
	public static boolean checkUsername(String username) throws SQLException{
    	Connection con = MyController.getConnection();
    	PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username = '" + username + "';");
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
	
	public static boolean checkPassword(String username, String password) throws SQLException {
		Connection con = MyController.getConnection();
    	PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?;");
    	ps.setString(1, username);
    	ps.setString(2, password);
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
	
	public static boolean checkEmail(String email) throws SQLException {
    	Connection con = MyController.getConnection();
    	PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE email = '" + email + "';");
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
	
	
	public static void addImage(Image image, Pane pane) {
			ImageView imageView = new ImageView(image);
			imageView.fitWidthProperty().bind(pane.widthProperty());
			imageView.fitHeightProperty().bind(pane.heightProperty());
			pane.getChildren().clear();
			pane.getChildren().add(imageView);
	}
} 
