package base.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import consts.ConstRes;
import javafx.animation.RotateTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import utils.AnimatorUtil;

public class MyDialogController implements Initializable {
	@FXML
	Label bar_title;
	@FXML
	BorderPane root;
	@FXML
	JFXButton btn_close;
	private RotateTransition rotateEnter;
	private RotateTransition rotateExit;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AnimatorUtil.fadeShow(root,ConstRes.SHOW_TIME);
		btn_close.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (rotateExit != null) {
					rotateExit.stop();
				}
				rotateEnter = AnimatorUtil.rotate(btn_close, 350, 0, 90);
			}
		});
		btn_close.addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (rotateEnter != null) {
					rotateEnter.stop();
				}
				rotateExit = AnimatorUtil.rotate(btn_close, 350, 90, 0);
			}
		});
		root.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ESCAPE) {
					onClickClose();
				}
			}
		});
	}

	@FXML
	public void onClickClose() {
		// 这里是取到Stage的具体代码
		Stage stage = (Stage) root.getScene().getWindow();
		stage.close();
		System.out.println("仅关闭此窗口");
	}

}
