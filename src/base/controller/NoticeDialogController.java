package base.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;

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

public class NoticeDialogController implements Initializable {

	@FXML
	BorderPane root;
	@FXML
	Label bar_title;
	@FXML
	JFXTextArea textArea_content;
	@FXML
	JFXButton btn_close;
	private RotateTransition rotateEnter;
	private RotateTransition rotateExit;

	private String title;
	private String content;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AnimatorUtil.fadeShow(root, ConstRes.SHOW_TIME);
		if (textArea_content != null) {
			textArea_content.setText(content);
		}
		if (bar_title != null) {
			bar_title.setText(title);
		}
		btn_close.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (rotateExit != null) {
					rotateExit.stop();
				}
				rotateEnter = AnimatorUtil.rotate(btn_close.getGraphic(), 350, 0, 90);
			}
		});
		btn_close.addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (rotateEnter != null) {
					rotateEnter.stop();
				}
				rotateExit = AnimatorUtil.rotate(btn_close.getGraphic(), 350, 90, 0);
			}
		});
		root.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.ENTER) {
					onClickClose();
				}
			}
		});
	}

	public void setContent(String content) {
		this.content = content;
		if (textArea_content != null) {
			textArea_content.setText(content);
		}
	}

	/**
	 * 设置dialog的title
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
		if (bar_title != null) {
			bar_title.setText(title);
		}
	}

	@FXML
	public void onClickClose() {
		close();
	}

	@FXML
	public void onConfirm() {
		close();
	}

	/**
	 * 关闭dialog
	 */
	public void close() {
		// 这里是取到Stage的具体代码
		Stage stage = (Stage) root.getScene().getWindow();
		stage.close();
	}
}
