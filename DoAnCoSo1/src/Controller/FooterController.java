package Controller;

import java.awt.Desktop;
import java.net.URI;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class FooterController {
    @FXML
    void facebook(MouseEvent event) {
    	try {
    		Desktop.getDesktop().browse(new URI("https://www.facebook.com/nguyens.tungs.921/"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @FXML
    void gmail(MouseEvent event) {
    	try {
    		Desktop.getDesktop().browse(new URI("mailto:tungntt.23itb@vku.udn.vn"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @FXML
    void instagram(MouseEvent event) {
    	try {
    		Desktop.getDesktop().browse(new URI("https://www.instagram.com/neuo.nttt/"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
