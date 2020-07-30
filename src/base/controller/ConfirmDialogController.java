package base.controller;

import java.net.URL;
import java.util.ResourceBundle;

import consts.ConstRes;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import utils.AnimatorUtil;
import javafx.scene.control.Label;

public class ConfirmDialogController implements Initializable {

	@FXML
	BorderPane root;
	@FXML
	Label bar_title;
	@FXML
	Label label_content;

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
