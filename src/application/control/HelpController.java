package application.control;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;

/**
 * 软件设置界面界面controller
 * 
 * @author DP
 */
public class HelpController implements Initializable {

	@FXML
	WebView webview;
	@FXML
	BorderPane root;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		WebEngine engine = webview.getEngine();
		webview.setZoom(0.9);
		String url = getClass().getResource("/html/help.html").toExternalForm();
		engine.load(url);
		webview.setOnScroll(
			        new EventHandler<ScrollEvent>() {
			            @Override
			            public void handle(ScrollEvent event) {
			                double zoomFactor = 1.05;
			                double deltaY = event.getDeltaY();

			                if (deltaY < 0){
			                    zoomFactor = 0.95;
			                }
			                double newZoom = webview.getZoom()*zoomFactor;
			                if(newZoom<=0.9||newZoom>=1.8) {
			                }else {
			                	webview.setZoom(newZoom);
			                }
			                event.consume();
			            }
			        });

	}
}
