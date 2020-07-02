package base;

import consts.ConstRes;
import consts.ConstSize;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public abstract class BaseApplication extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Pane uiPane = FXMLLoader.load(getClass().getResource(getUIPath()));
			Scene scene = getScene(uiPane,primaryStage);
			scene.setFill(null);
			primaryStage.setScene(scene);
			scene.getStylesheets().add(getClass().getResource(getCSSPath()).toExternalForm());
			primaryStage.initStyle(StageStyle.TRANSPARENT);// 设定窗口无边框
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected int frameWidth() {
		return ConstSize.Main_Frame_Width;
	}

	protected int frameHeight() {
		return ConstSize.Main_Frame_Height;
	}

	protected abstract String getUIPath();

	protected abstract Scene getScene(Pane uiPane,Stage stage);
	
	protected String getCSSPath() {
		return ConstRes.CSS_Path;
	}

}
