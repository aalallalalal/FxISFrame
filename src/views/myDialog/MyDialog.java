package views.myDialog;

import base.controller.MyDialogController;
import beans.MyFxmlBean;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.DragWindowHandler;
import utils.UIUtil;

public class MyDialog<R extends Object> extends Dialog<R> {

	private BorderPane framePane;
	private MyDialogController controller;
	private String title;
	private String uiPath;
	private double frameW;
	private double frameH;

	public MyDialog(String title, String uiPath, double frameW, double frameH) {
		this.title = title;
		this.uiPath = uiPath;
		this.frameW = frameW;
		this.frameH = frameH;
		initStyle(StageStyle.TRANSPARENT);// 去掉窗口修饰同时让弹出窗口透明
	}

	public void showDialog() {
		DialogPane p = getDialogPane();
		// Get the Stage.
		Stage stage = (Stage) p.getScene().getWindow();
		p.setPrefWidth(frameW);
		p.setPrefHeight(frameH);

		MyFxmlBean loadFxml = UIUtil.loadFxml(getClass(), "/views/myDialog/MyDialog.fxml");
		framePane = (BorderPane) loadFxml.getPane();
		controller = loadFxml.getFxmlLoader().getController();
		((BorderPane) framePane.getCenter()).setCenter(UIUtil.loadFxml(getClass(), uiPath).getPane());

		Pane bar = (Pane) framePane.lookup("#bar");
		DragWindowHandler handler = new DragWindowHandler(stage); // primaryStage为start方法中的局部b
		bar.setOnMousePressed(handler);
		bar.setOnMouseDragged(handler);

		p.setContent(framePane);
		setWidth(frameW);
		setHeight(frameH);
		show();
	}

}
