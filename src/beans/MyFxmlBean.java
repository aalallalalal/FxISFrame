package beans;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * 加载fxml后的实体类
 * @author DP
 *
 */
public class MyFxmlBean {

	private FXMLLoader mFxmlLoader;
	private Pane mPane;
	private Stage mStage;
	private Scene mScene;

	public FXMLLoader getFxmlLoader() {
		return mFxmlLoader;
	}

	public void setFxmlLoader(FXMLLoader mFxmlLoader) {
		this.mFxmlLoader = mFxmlLoader;
	}

	public Pane getPane() {
		return mPane;
	}

	public void setPane(Pane mPane) {
		this.mPane = mPane;
	}

	public Stage getStage() {
		return mStage;
	}

	public void setStage(Stage mStage) {
		this.mStage = mStage;
	}

	public Scene getScene() {
		return mScene;
	}

	public void setScene(Scene mScene) {
		this.mScene = mScene;
	}
}
