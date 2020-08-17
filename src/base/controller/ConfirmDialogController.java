package base.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import consts.ConstRes;
import javafx.fxml.Initializable;
import javafx.animation.RotateTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import utils.AnimatorUtil;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class ConfirmDialogController implements Initializable {

	@FXML
	BorderPane root;
	@FXML
	Label bar_title;
	@FXML
	Label label_content;
	@FXML
	JFXButton btn_close;
	private RotateTransition rotateEnter;
	private RotateTransition rotateExit;

	private CallBack callback;
	private String title;
	private String content;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AnimatorUtil.fadeShow(root, ConstRes.SHOW_TIME);
		if (label_content != null) {
			label_content.setText(content);
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
				if (event.getCode() == KeyCode.ESCAPE) {
					onClickClose();
				}
			}
		});
	}

	public void setContent(String content) {
		this.content = content;
		if (label_content != null) {
			label_content.setText(content);
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
		if (callback != null) {
			callback.onCancel();
		}
		close();
	}

	@FXML
	public void onCancel() {
		if (callback != null) {
			callback.onCancel();
		}
		close();
	}

	@FXML
	public void onConfirm() {
		if (callback != null) {
			callback.onConfirm();
		}
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

	public void setCallback(CallBack callback) {
		this.callback = callback;
	}

	public interface CallBack {
		void onCancel();

		void onConfirm();
	}
}
