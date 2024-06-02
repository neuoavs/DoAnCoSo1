package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;

import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import javafx.scene.control.DatePicker;

public class ChangeInforController {

    @FXML
    private AnchorPane change_container;

    @FXML
    private Label confirm_change_infor;

    @FXML
    private Label confirm_change_pass;

    @FXML
    private PasswordField confirm_pass_value;

    @FXML
    private DatePicker date_value;

    @FXML
    private TextField email_value;

    @FXML
    private AnchorPane infor_container;

    @FXML
    private ComboBox<String> nationality_value;

    @FXML
    private PasswordField new_pass_value;

    @FXML
    private PasswordField old_pass_value;

    @FXML
    private AnchorPane pass_container;

    @FXML
    private TextField username_value;

	private HomeController homeController;
	private Alert alert;
	
	@FXML
    void change_information(MouseEvent event) {
		new Thread(() -> {
			String user = username_value.getText().trim();
			String email = email_value.getText().trim();
			String birth = date_value.getValue().toString();
			String nat = nationality_value.getValue();
			
			if (user.isEmpty() || email.isEmpty()) {
				showError(alert, getErrorText(user, email), "Error enter");
			} else if (user.indexOf(' ') != -1) {
				showError(alert, "The user must not contain spaces. Please try Again!", "Error Enter");
			} else if (!MyController.checkFormatEmail(email)) {
				showError(alert, "The email is not valid. Please try again!", "Error enter");
			} else
				try {
					if(MyController.checkUsername(user)) {
						showError(alert, "The username readly exists. Please try Again!", "Error account");
					} else {
						inputOldPassAndCheck(alert, user, email, birth, nat);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		}).start();
    }

    public void changeInfor(String user, String email, String birth, String nationality) throws SQLException {
    	new Thread(() -> {
			try {
				Connection con = MyController.getConnection();
				PreparedStatement ps = con.prepareStatement("UPDATE users SET username = ?, email = ?, date_of_birth = ?, nationality = ? WHERE (user_id = ?);");
				ps.setString(1, user);
	        	ps.setString(2, email);
	        	ps.setDate(3, Date.valueOf(birth));
	        	ps.setString(4, nationality);
	        	ps.setInt(5, homeController.getKeyDB().getUser_id());
	        	
	        	Platform.runLater(() -> {
	        		try {
	    				if (ps.executeUpdate() == 1) {
	    					showError(alert, "Congratulate! You have successfully changed the information of your account.", "Congratulate");
	    					new Thread(() -> {
	    						homeController.getKeyDB().setUsername(user);
	    						try {
	    							homeController.getPersonalController().updateInformation();
	    						} catch (SQLException | IOException e) {
	    							// TODO Auto-generated catch block
	    							e.printStackTrace();
	    						}
	    						
	    						if(homeController.getFundsController() != null) {
	    							homeController.getFundsController().updateContentPay(homeController.getFundsController().getContent_pay(), homeController.getFundsController().getListCheck());
	    						}
	    					}).start();
	    					
	    					
	    				} else {
	    					
	    					showError(alert, "Have some errors. Cannot change the information", "Error");
	    				}
	    				
	    				con.close();
	    				ps.close();
	    			} catch (SQLException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
	        	});
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
    	}).start();
    }
    
   
	public void inputOldPassAndCheck(Alert alert, String username, String email, String birthday, String nationality) {
		Platform.runLater(() -> {
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Enter password");
			dialog.setHeaderText("Please enter your password!");
			dialog.setContentText("Password:");
			Optional<String> result = dialog.showAndWait();
			result.ifPresent(passInput -> {
				try {
					if (MyController.checkPassword(homeController.getKeyDB().getUsername(), passInput)) {
						changeInfor(username, email, birthday, nationality);
					} else {
						showError(alert, "The password is not correct", "Error account");
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		});

	}
    
    

    @FXML
    void change_password(MouseEvent event) {
    	new Thread(() -> {
			String oldPass = old_pass_value.getText().trim();
			String newPass = new_pass_value.getText().trim();
			String reNewPass = confirm_pass_value.getText().trim();
			
			if (oldPass.isEmpty() || newPass.isEmpty() || reNewPass.isEmpty()) {
				showError(alert, getErrorText(oldPass, newPass, reNewPass), "Error enter");
			} else if (oldPass.length() < 8 || newPass.length() < 8 || reNewPass.length() < 8) {
				showError(alert, "The passwords must not less than 8 letters. Please try again!", "Error enter");
			} else {
				try {
					if (!newPass.equals(reNewPass)) {
						showError(alert, "The confirm password is not matched with the new password. Please try Again!", "Error Enter");
					} else if (oldPass.equals(newPass)) {
						showError(alert, "The new password must not match with the old password. Please try Again!", "Error Enter");
					}
					
					else if (!MyController.checkPassword(homeController.getKeyDB().getUsername(), oldPass)) {
						showError(alert, "The old password is not correct. Please try again!", "Error enter");
					} else {
						changePass(newPass);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}		

		}).start();
    }
    
    
    public void changePass(String password) throws SQLException {
    	Connection con = MyController.getConnection();
    	PreparedStatement ps = con.prepareStatement("UPDATE users SET password = ? WHERE user_id = ?;");
    	ps.setString(1, password);
    	ps.setInt(2, homeController.getKeyDB().getUser_id());
    	
    	Platform.runLater(() -> {
    		try {
				if (ps.executeUpdate() == 1) {
					showError(alert, "Congratulate! You have successfully changed the password of your account.", "Congratulate");
				} else {
					showError(alert, "Have some errors. Cannot change the password", "Error");
				}
				
				con.close();
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	});
    }
    
	
	public void init() throws SQLException {
		Platform.runLater(() -> {
			alert = new Alert(Alert.AlertType.ERROR);
			MyController.addEffectButton(confirm_change_pass, "#00b4d8", "#0077b6");
			MyController.addEffectButton(confirm_change_infor, "#00b4d8", "#0077b6");
			infor_container.prefWidthProperty().bind(change_container.widthProperty().divide(2.0).subtract(7.5));
			pass_container.prefWidthProperty().bind(change_container.widthProperty().divide(2.0).subtract(7.5));
		});
		formatDateForBirthday();
		addListValueForNationality();
		updateDefaultInformation();
	}


	public HomeController getHomeController() {
		return homeController;
	}


	public void setHomeController(HomeController homeController) {
		this.homeController = homeController;
	}

	public void addListValueForNationality() {
		Platform.runLater(() -> {
			nationality_value.getItems().addAll(
					"Afghanistan",
					"Albania",
					"Algeria",
					"Andorra",
					"Angola",
					"Anguilla",
					"Antigua and Barbuda",
					"Argentina",
					"Armenia",
					"Aruba",
					"Australia",
					"Austria",
					"Azerbaijan",
					"Bahamas",
					"Bahrain",
					"Bangladesh",
					"Barbados",
					"Belarus",
					"Belgium",
					"Belize",
					"Benin",
					"Bermuda",
					"Bhutan",
					"Bolivia",
					"Bosnia and Herzegovina",
					"Botswana",
					"Brazil",
					"British Virgin Islands",
					"Brunei",
					"Bulgaria",
					"Burkina Faso",
					"Burundi",
					"Cabo Verde",
					"Cambodia",
					"Cameroon",
					"Canada",
					"Cayman Islands",
					"Central African Republic",
					"Chad",
					"Chile",
					"Colombia",
					"Comoros",
					"Congo",
					"Costa Rica",
					"Côte d'Ivoire",
					"Croatia",
					"Cuba",
					"Cyprus",
					"Czech Republic",
					"Democratic Republic of the Congo",
					"Denmark",
					"Djibouti",
					"Dominica",
					"Dominican Republic",
					"East Timor",
					"Ecuador",
					"Egypt",
					"El Salvador",
					"Equatorial Guinea",
					"Eritrea",
					"Estonia",
					"Eswatini",
					"Ethiopia",
					"Falkland Islands",
					"Faroe Islands",
					"Fiji",
					"Finland",
					"France",
					"French Polynesia",
					"Gabon",
					"Gambia",
					"Georgia",
					"Germany",
					"Ghana",
					"Gibraltar",
					"Greece",
					"Greenland",
					"Grenada",
					"Guam",
					"Guatemala",
					"Guinea",
					"Guinea-Bissau",
					"Guyana",
					"Haiti",
					"Honduras",
					"Hong Kong",
					"Hungary",
					"Iceland",
					"India",
					"Indonesia",
					"Iran",
					"Iraq",
					"Ireland",
					"Israel",
					"Italy",
					"Jamaica",
					"Japan",
					"Jersey",
					"Jordan",
					"Kazakhstan",
					"Kenya",
					"Kiribati",
					"Kuwait",
					"Kyrgyzstan",
					"Laos",
					"Latvia",
					"Lebanon",
					"Lesotho",
					"Liberia",
					"Libya",
					"Liechtenstein",
					"Lithuania",
					"Luxembourg",
					"Macau, China",
					"Madagascar",
					"Malawi",
					"Malaysia",
					"Maldives",
					"Mali",
					"Malta",
					"Marshall Islands",
					"Mauritania",
					"Mauritius",
					"Mexico",
					"Micronesia",
					"Moldova",
					"Monaco",
					"Mongolia",
					"Montenegro",
					"Montserrat",
					"Morocco",
					"Mozambique",
					"Myanmar",
					"Namibia",
					"Nauru",
					"Nepal",
					"Netherlands",
					"New Caledonia",
					"New Zealand",
					"Nicaragua",
					"Niger",
					"Nigeria",
					"Niue",
					"North Macedonia",
					"Norway",
					"Oman",
					"Pakistan",
					"Palestine",
					"Panama",
					"Papua New Guinea",
					"Paraguay",
					"Peru",
					"Philippines",
					"Poland",
					"Portugal",
					"Puerto Rico",
					"Qatar",
					"Romania",
					"Russia",
					"Rwanda",
					"Saint Helena, Ascension and Tristan da Cunha",
					"Saint Kitts and Nevis",
					"Saint Lucia",
					"Saint Vincent and the Grenadines",
					"Samoa",
					"San Marino",
					"São Tomé and Príncipe",
					"Saudi Arabia",
					"Senegal",
					"Serbia",
					"Seychelles",
					"Sierra Leone",
					"Singapore",
					"Slovakia",
					"Slovenia",
					"Solomon Islands",
					"Somalia",
					"South Africa",
					"South Korea",
					"South Sudan",
					"Spain",
					"Sri Lanka",
					"Sudan",
					"Suriname",
					"Sweden",
					"Switzerland",
					"Syria",
					"Taiwan",
					"Tajikistan",
					"Tanzania",
					"hailand",
					"Togo",
					"Tonga",
					"Trinidad and Tobago",
					"Tunisia",
					"Turkey",
					"Turkmenistan",
					"Tuvalu",
					"U.S. Virgin Islands",
					"Uganda",
					"Ukraine",
					"United Arab Emirates",
					"United Kingdom",
					"United States",
					"Uruguay",
					"Uzbekistan",
					"Vanuatu",
					"Venezuela",
					"Vietnam",
					"Wallis and Futuna",
					"Yemen",
					"Zambia",
					"Zimbabwe"
					);
		});
	}
	

	
	public void formatDateForBirthday() {
		Platform.runLater(() -> {
			date_value.setConverter(new StringConverter<LocalDate>() {
				 String pattern = "yyyy-MM-dd";
				 DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

				 {
					 date_value.setPromptText(pattern.toLowerCase());
				 }

				 @Override
				 public String toString(LocalDate date) {
				     if (date != null) {
				         return dateFormatter.format(date);
				     } else {
				         return "";
				     }
				 }

				 @Override 
				 public LocalDate fromString(String string) {
				     if (string != null && !string.isEmpty()) {
				         return LocalDate.parse(string, dateFormatter);
				     } else {
				         return null;
				     }
				 }
			});
		});
	}
		
	public void showError(Alert a, String content, String title) {
    	Platform.runLater(() -> {
        	a.setTitle(title);
        	a.setContentText(content);
        	a.setHeaderText(null);
        	a.setGraphic(null);
    		
    		Optional<ButtonType> result = a.showAndWait();
    		if (result.isEmpty()) {
    			try {
					updateDefaultInformation();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
        	} else if (result.get() == ButtonType.OK) {
        		try {
					updateDefaultInformation();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
    	});
    	
    }
	
	
	public void updateDefaultInformation() throws SQLException {
		new Thread(() -> {
			try {
				Connection con = MyController.getConnection();
				PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE user_id = ?;");
				ps.setInt(1, homeController.getKeyDB().getUser_id());
				ResultSet rs = ps.executeQuery();
				Platform.runLater(() -> {
					try {
						if (rs.next()) {
							username_value.setText("");
							username_value.setPromptText(rs.getString("username"));
							email_value.setText("");
							email_value.setPromptText(rs.getString("email"));
							date_value.setValue(rs.getDate("date_of_birth").toLocalDate());
							nationality_value.setValue(rs.getString("nationality"));
							username_value.setText("");
							old_pass_value.setText("");
							new_pass_value.setText("");
							confirm_pass_value.setText("");
						}
						
						con.close();
						ps.close();
						rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
		
	}
	
	public String getErrorText(String user, String email) {
    	String nofi = "";
    	int count = 0;
        StringBuilder addNofi = new StringBuilder();

        if (user.isEmpty()) {
        	addNofi.append("the username and ");
        	count++;
        }
        
        if (email.isEmpty()) {
        	addNofi.append("the email and ");
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
    
	public String getErrorText(String oldPass, String newPass, String reNewPass) {
    	String nofi = "";
    	int count = 0;
        StringBuilder addNofi = new StringBuilder();

        if (oldPass.isEmpty()) {
        	addNofi.append("the old password and ");
        	count++;
        }
        
        if (newPass.isEmpty()) {
        	addNofi.append("the new password and ");
        	count++;
        }
        
        if (reNewPass.isEmpty()) {
        	addNofi.append("the cofirm password and ");
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
	
}
