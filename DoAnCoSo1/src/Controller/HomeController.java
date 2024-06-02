package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import Model.KeyDB;
import Model.PageView;
import application.Home;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class HomeController {

    @FXML
    private VBox content_container;

    @FXML
    private AnchorPane main;

    @FXML
    private ScrollPane scroll_pane_home;
    
    private List<Pane> list_pane;
    private List<String> list_path;
    private List<PageView> list_page;
    private List<GameController> listGame;
    private String sql = "SELECT * FROM games;";
    private Pane pane_tmp = null;
    private KeyDB keyDB = null;
    private boolean addAddFund = false;
    private boolean addSeeAll = false;
    private boolean addFAQPage = false;
    private boolean addFilter = false;
    private boolean addPersonal = false;
    private boolean addChange = false;

    private SeeAllController controller;
    private AddFundsController fundsController;
    private PersonalController personalController;
    private AddCartController addCartController;

    @FXML
    void show_add_funds(MouseEvent event) throws IOException, SQLException {
    	if (addAddFund) {
    		showAddFund();
    	} else {
    		
    		list_pane.add(createPane("/View/AddFunds.fxml"));
    		list_page.add(new PageView("/View/AddFunds.fxml", list_pane.size() - 1));
    		MyController.setPadding(list_pane.get(findIndexBySrc("/View/AddFunds.fxml")), 0.0, 192.0);
    		addAddFund = true;
    		showAddFund();
    	}
    }
    
   
    public void showAddFund() {
    	Platform.runLater(() -> {
    		fundsController.updateContentPay(fundsController.getContent_pay(), fundsController.getListCheck());
    		main.getChildren().clear();
        	main.getChildren().add(list_pane.get(findIndexBySrc("/View/AddFunds.fxml")));
    	});
    }

    @FXML
    void show_personal(MouseEvent event) throws IOException , SQLException{
    	if (addPersonal) {
        	showPersonalPage();
    	} else {
    		list_pane.add(createPane("/View/Personal.fxml"));
    		list_page.add(new PageView("/View/Personal.fxml", list_pane.size() - 1));
    		addPersonal = true;
    		showPersonalPage();
    	}
    	

    }
    
    public void showPersonalPage() throws SQLException, IOException {
    	Platform.runLater(() -> {
    		content_container.getChildren().clear();
        	content_container.getChildren().add(list_pane.get(findIndexBySrc("/View/Personal.fxml")));
        	main.getChildren().clear();
            main.getChildren().add(scroll_pane_home);
    	});
    }
    
    
    @FXML
    void show_home(MouseEvent event) {
    	showHomeView();
    }
    
    public void showHomeView() {
    	Platform.runLater(() -> {
    		content_container.getChildren().clear();
        	addElementsHomePage();
        	main.getChildren().clear();
            main.getChildren().add(scroll_pane_home);
    	});
    }
    
    
    @FXML
    void show_faq_page(MouseEvent event) throws IOException , SQLException{
    	if (addFAQPage) {
    		showFAQPage();
    	} else {
    		list_pane.add(createPane("/View/FAQPage.fxml"));
    		list_page.add(new PageView("/View/FAQPage.fxml", list_pane.size() - 1));
    		addFAQPage = true;
    		showFAQPage();
    	}
    }

    public void showFAQPage() {
    	Platform.runLater(() -> {
    		content_container.getChildren().clear();
        	content_container.getChildren().add(list_pane.get(findIndexBySrc("/View/FAQPage.fxml")));
        	main.getChildren().clear();
        	
            main.getChildren().add(scroll_pane_home);
    	});
    }
    
    @FXML
    void show_filter_page(MouseEvent event) throws IOException, SQLException {
    	if (addFilter) {
        	showFilterPage();
    	} else {
    		list_pane.add(createPane("/View/Filter.fxml"));
    		list_page.add(new PageView("/View/Filter.fxml", list_pane.size() - 1));
    		addFilter = true;
    		showFilterPage();
    	}
    	

    }
    
    public void showFilterPage() {
    	Platform.runLater(() -> {
    		content_container.getChildren().clear();
        	content_container.getChildren().add(list_pane.get(findIndexBySrc("/View/Filter.fxml")));
        	main.getChildren().clear();
            main.getChildren().add(scroll_pane_home);
    	});
    }
    
    public void showSeeAllGame(){
    	new Thread(() -> {
    		if (addSeeAll) {
        		this.controller.setSql(sql);
        		try {
					this.controller.addGamesFromDataBase(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		Platform.runLater(() -> {
            		content_container.getChildren().clear();
                	content_container.getChildren().add(list_pane.get(findIndexBySrc("/View/SeeAll.fxml")));
                	main.getChildren().clear();
                    main.getChildren().add(scroll_pane_home);
            	});
        	} else {
        		try {
					try {
						list_pane.add(createPane("/View/SeeAll.fxml"));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		list_page.add(new PageView("/View/SeeAll.fxml", list_pane.size() - 1));
        		addSeeAll = true;
        		showSeeAllGame();
        	}
    	}).start();
    }
    
    public void showChangePage() throws IOException, SQLException {
    	if (addChange) {
    		Platform.runLater(() -> {
    			main.getChildren().clear();
            	main.getChildren().add(list_pane.get(findIndexBySrc("/View/ChangeInfor.fxml")));
        	});
    	} else {
    		list_pane.add(createPane("/View/ChangeInfor.fxml"));
    		list_page.add(new PageView("/View/ChangeInfor.fxml", list_pane.size() - 1));
    		MyController.setPadding(list_pane.get(findIndexBySrc("/View/ChangeInfor.fxml")), 0.0, 192.0);
    		addChange = true;
    		showChangePage();
    	}
    }
    
    @FXML
    void show_cart(MouseEvent event) {
    	showCart();
    }
    
    public void showCart() {
    	Platform.runLater(() -> {
    		main.getChildren().clear();
        	main.getChildren().add(list_pane.get(findIndexBySrc("/View/AddCart.fxml")));
    	});
    }
    
    public void init() throws IOException, SQLException {
        list_pane = new ArrayList<>();
        listGame = new ArrayList<>();
        list_page = new ArrayList<>();
        list_path = new ArrayList<>(Arrays.asList(
            "/View/SliderImage.fxml",
            "/View/NewGame.fxml",
            "/View/NewUpdated.fxml",
            "/View/Footer.fxml",
            "/View/AddCart.fxml"));

        for (String path : list_path) {
            list_pane.add(createPane(path));
            list_page.add(new PageView(path, list_pane.size() - 1));
        }
        
        addElementsHomePage();

        Platform.runLater(() -> {
        	MyController.setPadding(scroll_pane_home, 0.0, 192.0);
        	MyController.setPadding(list_pane.get(findIndexBySrc("/View/AddCart.fxml")), 0.0, 192.0);
            content_container.prefWidthProperty().bind(scroll_pane_home.widthProperty());
            main.getChildren().clear();
            
        });
        
        showHomeView();
    }
  
    public void addElementsHomePage() {
    	Platform.runLater(() -> {
    		for (int i = 0; i < list_pane.size(); i++) {
        		if (i < 4) {
        			content_container.getChildren().add(list_pane.get(i));  
        		}
        	}
    	});
    }
    
    public void showGameInformation() {
    	Platform.runLater(() -> {
    		content_container.getChildren().clear();
        	content_container.getChildren().add(pane_tmp);
        	pane_tmp.prefWidthProperty().bind(content_container.prefWidthProperty());
        	main.getChildren().clear();
            main.getChildren().add(scroll_pane_home);
    	});
    }

    
    public void updateDownloadTurn(int gameID){
    	for (GameController gameController : listGame) {
			if (gameController.getGame_id() == gameID) {
				try {
					gameController.updateDownloadTurn();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}
		}
    }
    
    public void updateViewTurn(int gameID) {
    	for (GameController gameController : listGame) {
			if (gameController.getGame_id() == gameID) {
				try {
					gameController.updateViewTurn();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}
		}
    }
    
    
    
    public int findIndexBySrc(String src) {
    	for (PageView pageView : list_page) {
			if (pageView.getSrc().equals(src)) {
				return pageView.getIndex();
			}
		}
    	return -1;
    }
    
    public Pane createPane(String path) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        Pane pane = loader.load();
        processController(path, loader);
        pane.setMaxWidth(Double.MAX_VALUE);
        System.out.println("create Pane is sucessed");
        return pane;
    }

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Pane getPane_tmp() {
		return pane_tmp;
	}

	public void setPane_tmp(Pane pane_tmp) {
		this.pane_tmp = pane_tmp;
	}
	
	public void processController(String path, FXMLLoader loader) throws SQLException, IOException {
        switch (path) {
            case "/View/SeeAll.fxml": {
                this.controller = loader.getController();
                this.controller.setHomeController(this);
                this.controller.setSql(sql);
                this.controller.init();
                break;
            }
            case "/View/SliderImage.fxml": {
                SliderImageController controller = loader.getController();
                controller.setHomeController(this);
                controller.init();
                break;
            }
            case "/View/NewGame.fxml": {
                NewGameController controller = loader.getController();
                controller.setHomeController(this);
                controller.init("SELECT * FROM games ORDER BY release_date DESC LIMIT 10;");
                break;
            }
            case "/View/NewUpdated.fxml": {
                NewUpdatedController controller = loader.getController();
                controller.setHomeController(this);
                controller.init("SELECT g.game_id, g.game_name, g.game_cover_image "
						+ "FROM games g "
						+ "INNER JOIN updates u ON g.game_id = u.game_id "
						+ "ORDER BY u.update_date DESC "
						+ "LIMIT 10;");
                break;
            }
            case "/View/AddFunds.fxml": {
            	fundsController = loader.getController();
            	fundsController.setHomeController(this);
            	fundsController.init();
                break;
            }
            case "/View/Personal.fxml": {
                personalController = loader.getController();
                personalController.setHomeController(this);
                personalController.init();
                break;
            }
            case "/View/ChangeInfor.fxml": {
            	ChangeInforController controller = loader.getController();
            	controller.setHomeController(this);
            	controller.init();
            	break;
            }
            case "/View/Filter.fxml": {
            	FilterController controller = loader.getController();
            	controller.setHomeController(this);
            	controller.init();
            	break;
            }
            
            case "/View/AddCart.fxml": { 
            	addCartController = loader.getController();
            	addCartController.setHomeController(this);
            	addCartController.init(this.getKeyDB().getUser_id());
            	break;
            }
            default: {
                break;
            }
        }
    }
	
	public KeyDB getKeyDB() {
		return keyDB;
	}

	public void setKeyDB(KeyDB keyDB) {
		this.keyDB = keyDB;
		if (this.keyDB == null) {
			System.out.println("Cannot add KeyDB to HomeController");
		}
	}

	public boolean isAddAddFund() {
		return addAddFund;
	}

	public void setAddAddFund(boolean addAddFund) {
		this.addAddFund = addAddFund;
	}

	public boolean isAddSeeAll() {
		return addSeeAll;
	}

	public void setAddSeeAll(boolean addSeeAll) {
		this.addSeeAll = addSeeAll;
	}

	public boolean isAddFAQPage() {
		return addFAQPage;
	}

	public void setAddFAQPage(boolean addFAQPage) {
		this.addFAQPage = addFAQPage;
	}

	public boolean isAddFilter() {
		return addFilter;
	}

	public void setAddFilter(boolean addFilter) {
		this.addFilter = addFilter;
	}

	public boolean isAddPersonal() {
		return addPersonal;
	}

	public void setAddPersonal(boolean addPersonal) {
		this.addPersonal = addPersonal;
	}


	public SeeAllController getController() {
		return controller;
	}


	public void setController(SeeAllController controller) {
		this.controller = controller;
	}


	public AddFundsController getFundsController() {
		return fundsController;
	}


	public void setFundsController(AddFundsController fundsController) {
		this.fundsController = fundsController;
	}


	public PersonalController getPersonalController() {
		return personalController;
	}


	public void setPersonalController(PersonalController personalController) {
		this.personalController = personalController;
	}


	public AddCartController getAddCartController() {
		return addCartController;
	}


	public void setAddCartController(AddCartController addCartController) {
		this.addCartController = addCartController;
	}


	public List<Pane> getList_pane() {
		return list_pane;
	}


	public void setList_pane(List<Pane> list_pane) {
		this.list_pane = list_pane;
	}


	public List<String> getList_path() {
		return list_path;
	}


	public void setList_path(List<String> list_path) {
		this.list_path = list_path;
	}


	public List<PageView> getList_page() {
		return list_page;
	}


	public void setList_page(List<PageView> list_page) {
		this.list_page = list_page;
	}


	public List<GameController> getListGame() {
		return listGame;
	}


	public void setListGame(List<GameController> listGame) {
		this.listGame = listGame;
	}
	

	
}