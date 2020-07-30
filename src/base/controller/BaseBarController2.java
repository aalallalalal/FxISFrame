package base.controller;

import java.net.URL;
import java.util.ResourceBundle;

import consts.ConstRes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import utils.AnimatorUtil;

/**
 * 子界面的controller
 * 
 * @author DP
 *
 */
public class BaseBarController2 implements Initializable {
	@FXML
	private HBox bar;
	@FXML
	BorderPane root;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AnimatorUtil.fadeShow(root, ConstRes.SHOW_TIME);
	}

	public BaseBarController2() {

	}

	@FXML
	public void onClickClose(ActionEvent event) {
		// 这里是取到Stage的具体代码
		Stage stage = (Stage) root.getScene().getWindow();
		stage.close();
		System.out.println("仅关闭此窗口");
	}

}
