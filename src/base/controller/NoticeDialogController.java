package base.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextArea;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class NoticeDialogController implements Initializable {

	@FXML
	BorderPane root;
	@FXML
	Label bar_title;
	@FXML
	JFXTextArea textArea_content;

	private String title;
	private String content;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (textArea_content != null) {
			textArea_content.setText(content);
		}
		if (bar_title != null) {
			bar_title.setText(title);
		}
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
