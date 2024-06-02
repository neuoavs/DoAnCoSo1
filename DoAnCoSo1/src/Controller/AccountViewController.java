package Controller;

import javafx.fxml.FXML;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.regex.Pattern;

import Model.KeyDB;
import application.Home;
import application.Runner;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class AccountViewController {

    @FXML
    private TextField emailSignUp;
	@FXML
	private Label slideLeft;
	@FXML
	private Label slideRight;
	@FXML
	private AnchorPane slidePane;
	@FXML
	private TextField usernameLogin;
	@FXML
	private TextField usernameSignUp;
	@FXML
	private PasswordField passwordLogin;
	@FXML
	private PasswordField passwordSignUp;
	@FXML
	private PasswordField rePasswordSignUp;
	
	private TranslateTransition slideTransition;
	
	private boolean inLogin = true;
	private boolean inSignUp = false;
	private Alert alert;
	private Runner runner = null;
	
	@FXML
	public void initialize() {
        Platform.runLater(() -> {
        	init();
        });
    }

	public void init() {
		slideTransition = new TranslateTransition(Duration.millis(300), slidePane);
        slideTransition.setFromX(0);
        slideTransition.setToX(300);
        alert = new Alert(Alert.AlertType.ERROR);
	}
	
	@FXML
	void slideLeftClicked(MouseEvent event) {
		Platform.runLater(() -> {
			slideTransition.setRate(-1);
			slideTransition.play();
			usernameSignUp.setText("");
			passwordSignUp.setText("");
			rePasswordSignUp.setText("");
			emailSignUp.setText("");
			usernameLogin.setText("");
			passwordLogin.setText("");
			inLogin = true;
			inSignUp = false;
		});
	}

	@FXML
	void slideRightClicked(MouseEvent event) {
		Platform.runLater(() -> {
			slideTransition.setRate(1);
			slideTransition.play();
			usernameSignUp.setText("");
			passwordSignUp.setText("");
			rePasswordSignUp.setText("");
			emailSignUp.setText("");
			usernameLogin.setText("");
			passwordLogin.setText("");
			
			inLogin = false;
			inSignUp = true;
		});
	}
	
    @FXML
    void login(MouseEvent event) throws SQLException {
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				String user = usernameLogin.getText().trim();
		        String pass = passwordLogin.getText();
		     
		        if (user.isEmpty() || pass.isEmpty()) {
		        	showError(alert, getErrorTextSignUp(user, pass), "Error enter");
		        } else if (user.contains(" ")) {
		            showError(alert, "The username must not contain spaces. Please try Again!", "Error Enter");
		        } else if(user.length() < 6 || pass.length() < 8) {

		    		if (user.length() < 6 && pass.length() >= 8) {
		    			showError(alert, "The username must not under 6 letters. Please try Again!", "Error Enter");
		    		} else if (user.length() >= 6 && pass.length() < 8) {
		    			showError(alert, "The password must not under 8 letters. Please try Again!", "Error Enter");
		    		} else {
		    			showError(alert, "The username must not under 6 letters and the password must not under 8 letters. Please try Again!", "Error Enter");
		    		}
		        } else {
		            if (inLogin) {
		            	try {
							if (checkAccount(user, pass)) {
								if (runner != null) {
									 runner.closeAccount();
							         runner.showHomeStage();
								} else {
									System.out.println("Runner is null in Account Controller");
								}
							   
							} else {
							    showError(alert, "The username or the password is not correct. Please try Again!", "Error account");
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            } 
		        }
			}
		}).start();
    	
    }

    @FXML
    void sign_up(MouseEvent event) throws SQLException {
    	new Thread(new Runnable() {
			@Override
			public void run() {
				String user = usernameSignUp.getText().trim();
		    	String pass = passwordSignUp.getText();
		    	String rePass = rePasswordSignUp.getText();
		    	String email = emailSignUp.getText().trim();
		    	
		    	if (user.isEmpty() || pass.isEmpty() || rePass.isEmpty() || email.isEmpty()) {
		    		showError(alert, getErrorTextSignUp(user, email, pass, rePass), "Error enter");
		    	} else if (user.indexOf(' ') != -1) {
		    		showError(alert, "The user must not contain spaces. Please try Again!", "Error Enter");
		    	} else if (!MyController.checkFormatEmail(email)) {
		    		showError(alert, "The email is not valid. Please try again!", "Error enter");
		    	} else if (user.length() < 6 || pass.length() < 8) {
		    		if (user.length() < 6 && pass.length() >= 8) {
		    			showError(alert, "The username must not under 6 letters. Please try Again!", "Error Enter");
		    		} else if (user.length() >= 6 && pass.length() < 8) {
		    			showError(alert, "The password must not under 8 letters. Please try Again!", "Error Enter");
		    		} else {
		    			showError(alert, "The username must not under 6 letters and the password must not under 8 letters. Please try Again!", "Error Enter");
		    		}
		    	} else if (!pass.equals(rePass)) {
		    		showError(alert, "The re-password is not matched with the password. Please try Again!", "Error Enter");
		    	} else {
		    		if (inSignUp) {
		    			try {
							if (MyController.checkUsername(user)) {
								if (MyController.checkEmail(email)) {
									showError(alert, "The username and the email readly exists. Please try Again!", "Error account");
								} else {
									showError(alert, "The username readly exists. Please try Again!", "Error account");
								}
							} else if (MyController.checkEmail(email)) {
								showError(alert, "The email readly exists. Please try Again!", "Error account");
							} else {
								addNewAccount(user, pass, email);
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    		}
		    	}
			}
		}).start();
    	
    } 	
	
    public String getErrorTextSignUp(String username, String email, String password, String rePassword) {
    	String nofi = "";
    	int count = 0;
        StringBuilder addNofi = new StringBuilder();

        if (username.isEmpty()) {
        	addNofi.append("the username and ");
        	count++;
        }
        
        if (email.isEmpty()) {
        	addNofi.append("the email and ");
        	count++;
        }
        
        if (password.isEmpty()) {
        	addNofi.append("the password and ");
        	count++;
        }
        
        if (rePassword.isEmpty()) {
        	addNofi.append("the re-password and ");
        	count++;
        }

        if (addNofi.length() > 0) {
        	addNofi.setLength(addNofi.length() - 5);
        	
        	if (count == 1) {
        		 nofi += addNofi.toString() + " is empty. Please try again!";
        	} else {
        		nofi += addNofi.toString() + " are empty. Please try again!";
        	}
           
        }
        
        return Character.toUpperCase(nofi.charAt(0)) + nofi.substring(1);
    }
    
    public String getErrorTextSignUp(String username, String password) {
    	String nofi = "";
    	int count = 0;
        StringBuilder addNofi = new StringBuilder();

        if (username.isEmpty()) {
        	addNofi.append("the username and ");
        	count++;
        }
        
        if (password.isEmpty()) {
        	addNofi.append("the password and ");
        	count++;
        }
        
        if (addNofi.length() > 0) {
        	addNofi.setLength(addNofi.length() - 5);
        	if (count == 1) {
        		nofi += addNofi.toString() + " is empty. Please try again!";
	       	} else {
	       		nofi += addNofi.toString() + " are empty. Please try again!";
	       	}
        }
        
        return Character.toUpperCase(nofi.charAt(0)) + nofi.substring(1);
    }
    
    
    
    public boolean checkAccount(String username, String password) throws SQLException {
    	Connection con = MyController.getConnection();
    	PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "';");
    	ResultSet rs = ps.executeQuery();
    	
    	if (rs.next()) {
    		if (runner.getKeyDB() != null) {
    			runner.getKeyDB().setUser_id(rs.getInt("user_id"));
    			runner.getKeyDB().setUsername(rs.getString("username"));
    			System.out.println("Updated Key DB for Account Controller");
    		}
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

    
    public void addNewAccount(String user, String pass, String email) throws SQLException {
    	
    	
    	new Thread (new Runnable() {
			
			@Override
			public void run() {
				try {
					Connection con = MyController.getConnection();
					PreparedStatement ps = con.prepareStatement("INSERT INTO users (username, password, email) VALUES ('" + user +"', '" + pass +"', '" + email + "');");
					
					if (ps.executeUpdate() == 1) {
						getAlert().setAlertType(Alert.AlertType.INFORMATION);
			    		showError(alert, "Congratulate! You have successfully created your account. Please log in!", "Congratulate");
			    		getAlert().setAlertType(Alert.AlertType.ERROR);
					} else {
			    		showError(alert, "Account creation failed. Please try agian!", "Error account");
			    	}
			    	
			    	con.close();
					ps.close();
					Thread.interrupted();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();;
    }
    
    public void showError(Alert a, String content, String title) {

    	Platform.runLater(() -> {
        	a.setTitle(title);
        	a.setContentText(content);
        	a.setHeaderText(null);
        	a.setGraphic(null);
        	a.setHeight(200.0);
    		Optional<ButtonType> result = a.showAndWait();
    		if (result.isEmpty()) {
        		usernameSignUp.setText("");
        		passwordSignUp.setText("");
        		rePasswordSignUp.setText("");
        		emailSignUp.setText("");
        		usernameLogin.setText("");
        		passwordLogin.setText("");
        	} else if (result.get() == ButtonType.OK) {
        		usernameSignUp.setText("");
        		passwordSignUp.setText("");
        		rePasswordSignUp.setText("");
        		emailSignUp.setText("");
        		usernameLogin.setText("");
        		passwordLogin.setText("");
        	}
    	});
    	
    }
    
	public boolean isInLogin() {
		return inLogin;
	}

	public void setInLogin(boolean inLogin) {
		this.inLogin = inLogin;
	}

	public boolean isInSignUp() {
		return inSignUp;
	}

	public void setInSignUp(boolean inSignUp) {
		this.inSignUp = inSignUp;
	}
	
	public Alert getAlert() {
		return alert;
	}

	public void setAlert(Alert alert) {
		this.alert = alert;
	}


	public Runner getRunner() {
		return runner;
	}

	public void setRunner(Runner runner) {
		if (runner != null) {
			this.runner = runner;
			System.out.println("Added to Account View Controller");
		} else {
			System.out.println("Can not set runner for Account Controller");
		}
		
	}
	
	
}
