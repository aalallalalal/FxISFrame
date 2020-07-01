package beans;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public class MyFxmlBean {

	private FXMLLoader mFxmlLoader;
	private Pane mPane;

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
}
