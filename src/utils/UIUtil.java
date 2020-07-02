package utils;

import java.io.IOException;
import java.net.URL;

import beans.MyFxmlBean;
import consts.ConstRes;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UIUtil {

	/**
	 * 用来加载fxml文件的工具，返回根布局和对应的FXMLoader.
	 * 
	 * @param clz
	 * @param uiPath
	 * @return
	 */
	public static MyFxmlBean loadFxml(Class<?> clz, String uiPath) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
		URL location = clz.getResource(uiPath);
		fxmlLoader.setLocation(location);

		MyFxmlBean myFxml = new MyFxmlBean();
		try {
			myFxml.setPane(fxmlLoader.load(location.openStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		myFxml.setFxmlLoader(fxmlLoader);
		return myFxml;
	}

	/**
	 * 打开一个子界面。该子界面只有关闭，没有最小化。
	 * 
	 * @param clz    ： getClass()
	 * @param uiPath 界面fxml路径
	 * @param frameW 界面宽
	 * @param frameH 界面高
	 * @param title  界面右上角显示title
	 */
	public static MyFxmlBean openFrame(Class<?> clz, String uiPath, double frameW, double frameH, String title,
			boolean isWait) {
		try {
			BorderPane framePane = (BorderPane) FXMLLoader.load(clz.getResource(ConstRes.UI_Bar_No_Path));
			MyFxmlBean loadFxml = loadFxml(clz, uiPath);
			Pane anotherRoot = loadFxml.getPane();
			// 将子界面加入框架中
			((BorderPane) (framePane.getCenter())).setCenter(anotherRoot);
			Label titleLabel = (Label) framePane.lookup("#bar_title");
			if (titleLabel != null) {
				titleLabel.setText(title);
			}
			Scene scene = new Scene(framePane, frameW, frameH);
			Stage anotherStage = new Stage();
			anotherStage.setScene(scene);
			setFrameCanDrag(framePane, anotherStage);
			transparentFrame(anotherStage, scene);
			setFrameCss(clz, scene);
			anotherStage.show();
			loadFxml.setScene(scene);
			loadFxml.setStage(anotherStage);
			return loadFxml;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 打开一个子界面。该子界面只有关闭，没有最小化。
	 * 
	 * @param clz    ： getClass()
	 * @param uiPath 界面fxml路径
	 * @param frameW 界面宽
	 * @param frameH 界面高
	 * @param title  界面右上角显示title
	 */
	public static MyFxmlBean openDialog(Class<?> clz, String uiPath, double frameW, double frameH, String title,
			Stage ownStage) {
		try {
			BorderPane framePane = (BorderPane) FXMLLoader.load(clz.getResource(ConstRes.UI_Bar_Dialog_Path));
			MyFxmlBean loadFxml = loadFxml(clz, uiPath);
			Pane anotherRoot = loadFxml.getPane();
			// 将子界面加入框架中
			((BorderPane) (framePane.getCenter())).setCenter(anotherRoot);
			Label titleLabel = (Label) framePane.lookup("#bar_title");
			if (titleLabel != null) {
				titleLabel.setText(title);
			}
			Scene scene = new Scene(framePane, frameW, frameH);
			Stage anotherStage = new Stage();
			setFrameIsModal(anotherStage, ownStage);

			anotherStage.setScene(scene);
			transparentFrame(anotherStage, scene);
			setFrameCanDrag(framePane, anotherStage);
			setFrameCss(clz, scene);
			anotherStage.show();
			loadFxml.setScene(scene);
			loadFxml.setStage(anotherStage);
			return loadFxml;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 设置窗口为独占式（不可操作其他窗口）
	 * 
	 * @param stage
	 * @param ownStage
	 */
	private static void setFrameIsModal(Stage stage, Stage ownStage) {
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(ownStage);
	}

	/**
	 * 设置该界面可以移动
	 * 
	 * @param framePane
	 * @param stage
	 */
	public static void setFrameCanDrag(Pane framePane, Stage stage) {
		Pane bar = (Pane) framePane.lookup("#bar");
		if (bar == null) {
			return;
		}
		DragWindowHandler handler = new DragWindowHandler(stage); // primaryStage为start方法中的局部b
		bar.setOnMousePressed(handler);
		bar.setOnMouseDragged(handler);
	}

	private static void transparentFrame(Stage stage, Scene scene) {
		scene.setFill(null);
		stage.initStyle(StageStyle.TRANSPARENT);// 设定窗口无边框
	}

	private static void setFrameCss(Class<?> clz, Scene scene) {
		scene.getStylesheets().add(clz.getResource(ConstRes.CSS_Path).toExternalForm());
	}

}
