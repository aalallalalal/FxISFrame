package application;

import java.io.IOException;

import base.BaseApplication;
import consts.ConstRes;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
			BorderPane framePane = (BorderPane) FXMLLoader.load(getClass().getResource(ConstRes.UI_Bar_Path));
			Pane bar = (Pane) framePane.lookup("#bar");
			DragWindowHandler handler = new DragWindowHandler(stage); // primaryStage为start方法中的局部b
			bar.setOnMousePressed(handler);
			bar.setOnMouseDragged(handler);
			// 将子界面加入框架中
			((BorderPane) (framePane.getCenter())).setCenter(uiPane);
			barScene = new Scene(framePane, frameWidth(), frameHeight());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return barScene;
	}

	/**
	 * 拖动移动窗体监听器
	 * @author DP
	 *
	 */
	class DragWindowHandler implements EventHandler<MouseEvent> {

		private Stage primaryStage; // primaryStage为start方法头中的Stage
		private double oldStageX;
		private double oldStageY;
		private double oldScreenX;
		private double oldScreenY;

		public DragWindowHandler(Stage primaryStage) { // 构造器
			this.primaryStage = primaryStage;
		}

		@Override
		public void handle(MouseEvent e) {
			if (e.getEventType() == MouseEvent.MOUSE_PRESSED) { // 鼠标按下的事件
				this.oldStageX = this.primaryStage.getX();
				this.oldStageY = this.primaryStage.getY();
				this.oldScreenX = e.getScreenX();
				this.oldScreenY = e.getScreenY();

			} else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) { // 鼠标拖动的事件
				this.primaryStage.setX(e.getScreenX() - this.oldScreenX + this.oldStageX);
				this.primaryStage.setY(e.getScreenY() - this.oldScreenY + this.oldStageY);
			}
		}
	}
}
