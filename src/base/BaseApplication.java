package base;

import consts.ConstRes;
import consts.ConstSize;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.ResUtil;
import utils.SaveLanguageUtil;
import utils.UIUtil;

public abstract class BaseApplication extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			ResUtil.initLanguage(SaveLanguageUtil.getData());
			Pane uiPane = FXMLLoader.load(getClass().getResource(getUIPath()), ResUtil.getResource());
			Scene scene = getScene(uiPane, primaryStage);
			scene.setFill(null);
			primaryStage.setScene(scene);
			scene.getStylesheets().add(getClass().getResource(getCSSPath()).toExternalForm());
			primaryStage.initStyle(StageStyle.TRANSPARENT);// 设定窗口无边框
			primaryStage.show();
			UIUtil.centerWindow(primaryStage, 0);
			uiPane.requestFocus();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected double frameWidth() {
		return ConstSize.Main_Frame_Width;
	}

	protected double frameHeight() {
		return ConstSize.Main_Frame_Height;
	}

	protected abstract String getUIPath();

	protected abstract Scene getScene(Pane uiPane, Stage stage);

	protected String getCSSPath() {
		return ConstRes.CSS_Path;
	}

}
