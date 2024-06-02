package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import Model.CheckBoxAddFunds;
import application.Home;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class AddFundsController {
    @FXML
    private AnchorPane add_fund;

    @FXML
    private AnchorPane add_fund_container;

    @FXML
    private AnchorPane bank_pane;

    @FXML
    private Circle check_10;

    @FXML
    private Circle check_100;

    @FXML
    private Circle check_1000;

    @FXML
    private Circle check_200;

    @FXML
    private Circle check_50;

    @FXML
    private Circle check_500;

    @FXML
    private Button check_button;

    @FXML
    private Circle check_epay;

    @FXML
    private Circle check_momo;

    @FXML
    private Label content_pay;

    @FXML
    private Line line;

    @FXML
    private Label money;

    @FXML
    private ImageView qr_bank;

    private List<CheckBoxAddFunds> listCheck = new ArrayList<>();
    
    private boolean checkContentPay = false;
    
    private String balance;
    
    private HomeController homeController = null;
    
    @FXML
    void checkAndAddFund(MouseEvent event) {
    	if (checkContentPay) {
    		System.out.println(balance);
    		addFunds(balance);
    	} else {
    		if (!listCheck.get(6).isChecked() && !listCheck.get(7).isChecked()) {
    			boolean otherNo = true;
    			
    			for (CheckBoxAddFunds check : listCheck) {
					if (check.isChecked()) {
						otherNo = false;
						break;
					}
				}
    			if (otherNo) {
    				showError("You didn't tick the method pay and the value of money. Please try again!", "Error tick", true);
    			} else {
    				showError("You didn't tick the method pay. Please try again!", "Error tick", true);
    			}
    			
    		} else {
    			showError("You didn't tick the value of money. Please try again!", "Error tick", true);
    		}
    	}
    }
    
    public void init() throws SQLException {
    	new Thread(() -> {
    		Platform.runLater(() -> {
        		content_pay.setText(homeController.getKeyDB().getUsername());
            	MyController.addEffectButton(check_button, "#90e0ef", "#00b4d8");
            	MyController.radiusForPane(add_fund);
                check_button.prefWidthProperty().bind(add_fund.widthProperty().divide(2).subtract(15));
                
                add_fund.widthProperty().addListener((observable, oldValue, newValue) -> {
                	Platform.runLater(() -> {
                		if (newValue.doubleValue() > 0.0) {
                            updateLayout(newValue.doubleValue());
                        }
                	});
                });

                add_fund.heightProperty().addListener((observable, oldValue, newValue) -> {
                	Platform.runLater(() -> {
                		if (newValue.doubleValue() > 0.0) {
                            if (add_fund.getWidth() > 0.0) {
                                updateLayout(add_fund.getWidth());
                            }

                            if (add_fund.getPrefWidth() > 0.0) {
                                updateLayout(add_fund.getPrefWidth());
                            }
                        }
                	}); 
                });
        	});
    		
    		
    		try {
				updateMoney();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		createListCheckBox(listCheck);
    		addActionCheck(listCheck);
    	}).start();
    	
    }
    
    public void addActionCheck(List<CheckBoxAddFunds> list) {
        for (CheckBoxAddFunds check : list) {
            check.getCheckBox().setOnMouseClicked(event -> {
                if (check.getValue().matches("momo") || check.getValue().matches("epay")) {
                	if (check.isChecked() == true) {
                		resetCheckMethodPay(list);
                	} else {
                		resetCheckMethodPay(list);
                		check.setChecked(true);;
                	}
                	 checkActionColor(check.getCheckBox(), check.isChecked());
                } else {
                	if (check.isChecked() == true) {
                        resetCheckMoney(list);
                	} else {
                		resetCheckMoney(list);
                		check.setChecked(true);
                		balance = check.getValue();
                	}
                	 checkActionColor(check.getCheckBox(), check.isChecked());
                }
                updateContentPay(content_pay, list);
            });
        }
    }
    
    public void updateContentPay(Label content, List<CheckBoxAddFunds> list) {
    	String bf = "";
    	String af = "";
    	for (CheckBoxAddFunds check : list) {
			if ((check.getValue().matches("momo") || check.getValue().matches("epay")) && check.isChecked()) {
				bf = check.getValue();
			} else {
				if (check.isChecked()) {
					af = check.getValue();
				}
			}
		}
    	
    	if (bf.isEmpty() || af.isEmpty() || homeController.getKeyDB().getUsername().isEmpty()) {
    		checkContentPay = false;
    	} else {
    		checkContentPay = true;
    	}
		content.setText(homeController.getKeyDB().getUsername() + " " + bf + " " + af);
    }
    
    public void resetCheckMoney(List<CheckBoxAddFunds> list) {
        for (CheckBoxAddFunds check : list) {
            if (!check.getValue().matches("momo") && !check.getValue().matches("epay")) {
                check.setChecked(false);
                checkActionColor(check.getCheckBox(), false);
            }
        }
    }

    public void resetCheckMethodPay(List<CheckBoxAddFunds> list) {
        for (CheckBoxAddFunds check : list) {
            if (check.getValue().matches("momo") || check.getValue().matches("epay")) {
                check.setChecked(false);
                checkActionColor(check.getCheckBox(), false);
            }
        }
    }

    public void checkActionColor(Circle check, boolean isCheck) {
    	Platform.runLater(() -> {
    		if (isCheck) {
                check.setFill(Paint.valueOf("#00b4d8"));
            } else {
                check.setFill(Paint.valueOf("#caf0f8"));
            }
    	});
    }

    public void createListCheckBox(List<CheckBoxAddFunds> list) {
        list.add(new CheckBoxAddFunds(check_10, false, "10"));
        list.add(new CheckBoxAddFunds(check_50, false, "50"));
        list.add(new CheckBoxAddFunds(check_100, false, "100"));
        list.add(new CheckBoxAddFunds(check_200, false, "200"));
        list.add(new CheckBoxAddFunds(check_500, false, "500"));
        list.add(new CheckBoxAddFunds(check_1000, false, "1000"));
        list.add(new CheckBoxAddFunds(check_momo, false, "momo"));
        list.add(new CheckBoxAddFunds(check_epay, false, "epay"));
    }

    public void updateLayout(double width_value) {
        double right = width_value / 2.0;
        check_button.prefWidthProperty().bind(add_fund.widthProperty().divide(2).subtract(15));
        bank_pane.prefWidthProperty().bind(add_fund.widthProperty().divide(2));
        MyController.updateLinePosition(line, add_fund, 65.0, 15.0, right);
        double left_qr = (bank_pane.getPrefWidth() - qr_bank.getFitWidth()) / 2.0;
        MyController.setPaddingTopLeft(qr_bank, 25.0, left_qr);
    }

    public void addFunds(String balance) {
    	new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Connection con = MyController.getConnection();
					PreparedStatement ps = con.prepareStatement("UPDATE users SET balance = balance + " + balance + " WHERE username = '" + homeController.getKeyDB().getUsername() + "';");
					if (ps != null) {
						Platform.runLater(() -> {
							try {
								if (ps.executeUpdate() == 1) {
									updateMoney();
									homeController.getAddCartController().updateMoney(homeController.getKeyDB().getUser_id());
									showError("Congratulate! You have successfully added money to your account!", "Congratulate", false);
								} else {
									
									System.out.println("Add fund is not sucessed");
									
								}
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						});
					}
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
    }
    
    
    public void updateMoney() throws SQLException {
    	Connection con = MyController.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username = '" + homeController.getKeyDB().getUsername() + "';");
		ResultSet rs = ps.executeQuery();
		
		Platform.runLater(() -> {
			try {
				if (rs.next()) {
					money.setText(rs.getBigDecimal("balance") + "$");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
    }
    
    public void showError(String content, String title, boolean graphic) {
    	Alert a = new Alert(Alert.AlertType.ERROR);
    	a.setTitle(title);
    	a.setContentText(content);
    	a.setHeaderText(null);
    	if(!graphic) {
    		a.setGraphic(null);
    	}
    	Optional<ButtonType> result = a.showAndWait();
    	
    	if (result.isEmpty()) {
    		System.out.println("Add fund alert is died");
    	} else if (result.get() == ButtonType.OK) {
    		System.out.println("Add fund alert is died");
    	}
    }
    

    public List<CheckBoxAddFunds> getListCheck() {
        return listCheck;
    }

    public void setListCheck(List<CheckBoxAddFunds> listCheck) {
        this.listCheck = listCheck;
    }

	public boolean isCheckContentPay() {
		return checkContentPay;
	}

	public void setCheckContentPay(boolean checkContentPay) {
		this.checkContentPay = checkContentPay;
	}

	public HomeController getHomeController() {
		return homeController;
	}

	public void setHomeController(HomeController homeController) {
		if (homeController != null) {
			this.homeController = homeController;
			System.out.println("Added home Controller to Add Fund Controller");
		} else {
			System.out.println("Cannot add home Controller in Add Fund Controller");
		}
	}

	public Label getContent_pay() {
		return content_pay;
	}

	public void setContent_pay(Label content_pay) {
		this.content_pay = content_pay;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}
	
	
	
}
