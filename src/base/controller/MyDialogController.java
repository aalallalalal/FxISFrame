package base.controller;

import java.net.URL;
import java.util.ResourceBundle;

import consts.ConstRes;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import utils.AnimatorUtil;

public class MyDialogController implements Initializable {
	@FXML
	Label bar_title;
	@FXML
	BorderPane root;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AnimatorUtil.fadeShow(root,ConstRes.SHOW_TIME);
	}

	@FXML
	public void onClickClose() {
		// 这里是取到Stage的具体代码
		Stage stage = (Stage) root.getScene().getWindow();
		stage.close();
		System.out.println("仅关闭此窗口");
	}

}
