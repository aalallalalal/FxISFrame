package utils;

import consts.ConstRes;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class UIUtil {

	/**
	 * 打开一个子界面。该子界面只有关闭，没有最小化。
	 * @param clz ： getClass()
	 * @param uiPath 界面fxml路径
	 * @param frameW 界面宽  
	 * @param frameH 界面高
	 * @param title 界面右上角显示title
	 */
	public static void openFrame(Class<?> clz, String uiPath, double frameW, double frameH, String title) {
		try {
			BorderPane framePane = (BorderPane) FXMLLoader.load(clz.getResource(ConstRes.UI_Bar_No_Path));
			Pane anotherRoot = FXMLLoader.load(clz.getResource(uiPath));
			// 将子界面加入框架中
			((BorderPane) (framePane.getCenter())).setCenter(anotherRoot);
			try {
				Label titleLabel = (Label) framePane.lookup("#bar_title");
				if (titleLabel != null) {
					titleLabel.setText(title);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Scene scene = new Scene(framePane, frameW, frameH);
			Stage anotherStage = new Stage();
			anotherStage.setScene(scene);
			anotherStage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
