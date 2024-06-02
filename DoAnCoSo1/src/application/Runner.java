package application;

import Model.KeyDB;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Runner extends Application {
	private Stage accountStage;
    private Stage homeStage;
    private Home home;
    private Account account;
    private KeyDB keyDB = new KeyDB(-1, -1, "");

    @Override
    public void start(Stage primaryStage) {
        accountStage = primaryStage;
        home = new Home();
        account = new Account();
        System.out.println("Runner set in Runner class");
        showAccountStage();
        
        new Thread(() -> {
        	while(true) {
        		System.out.println("The amount of Thread: " + Thread.activeCount());
        		try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }).start();
        
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });
    }


    public void showAccountStage() {
    	Platform.runLater(() -> {
    		try {
                account.start(accountStage);
                if (account.getController() != null) {
                	account.getController().setRunner(this);
                } else {
                	System.out.println("Account controller is null");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    	});
    }

    public void showHomeStage() {
    	Platform.runLater(() -> {
    		try {
                if (homeStage == null) {
                    homeStage = new Stage();
                }
                home.start(homeStage, getKeyDB());
                if (home.getHomeController() != null && home.getHomeController().getKeyDB() != null) {
                	System.out.println("Added Home controller to Home and KeyDB to Home Controller");
                	System.out.println(home.getHomeController().getKeyDB().getUser_id());
                	System.out.println(home.getHomeController().getKeyDB().getUsername());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    	});
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void closeAccount() {
    	Platform.runLater(() -> {
    		if (accountStage != null) {
                accountStage.close();
            }
    	});
    }

    public Stage getAccountStage() {
        return accountStage;
    }

    public void setAccountStage(Stage accountStage) {
        this.accountStage = accountStage;
    }

    public Stage getHomeStage() {
        return homeStage;
    }

    public void setHomeStage(Stage homeStage) {
        this.homeStage = homeStage;
    }


	public Home getHome() {
		return home;
	}

	public void setHome(Home home) {
		this.home = home;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}


	public KeyDB getKeyDB() {
		return keyDB;
	}


	public void setKeyDB(KeyDB keyDB) {
		this.keyDB = keyDB;
	}
	
	
}
