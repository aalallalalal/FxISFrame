package application;

import java.io.IOException;

import base.BaseApplication;
import consts.ConstRes;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utils.ResUtil;
import utils.UIUtil;

public class Main extends BaseApplication {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	protected String getUIPath() {
		return "/application/fxml/Main.fxml";
	}

	@Override
	protected Scene getScene(Pane uiPane, Stage stage) {
		Scene barScene = null;
		try {
			// 获取带bar的框架界面
			BorderPane framePane = (BorderPane) FXMLLoader.load(getClass().getResource(ConstRes.UI_Bar_Path),ResUtil.getResource());
			UIUtil.setFrameCanDrag(framePane, stage);
			// 将子界面加入框架中
			((BorderPane) (framePane.getCenter())).setCenter(uiPane);
			barScene = new Scene(framePane, frameWidth(), frameHeight());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return barScene;
	}

}
