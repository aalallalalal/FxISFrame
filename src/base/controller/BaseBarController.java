package base.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import utils.AnimatorUtil;

/**
 * 主界面的controller
 * 
 * @author DP
 *
 */
public class BaseBarController implements Initializable {
	@FXML
	private HBox bar;
	@FXML
	BorderPane root;
	@FXML
	Label bar_title;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AnimatorUtil.fadeShow(root, 1500);
	}

	public BaseBarController() {
	}

	@FXML
	public void onClickHide(ActionEvent event) {
		System.out.println("缩小");
		Stage stage = (Stage) root.getScene().getWindow();
		stage.setIconified(true);
	}

	@FXML
	public void onClickClose(ActionEvent event) {
		System.out.println("关闭整个程序");
		// 这里是取到Stage的具体代码
		Stage stage = (Stage) root.getScene().getWindow();
		stage.close();
		System.exit(0);
	}

}
