package base.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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
	@FXML
	JFXButton btn_close;
	@FXML
	JFXButton btn_hide;
	private RotateTransition rotateEnter;
	private RotateTransition rotateExit;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AnimatorUtil.fadeShow(root, 1500);
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
		btn_hide.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				AnimatorUtil.scale(btn_hide, 350, 1, 0.7);
			}
		});
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
