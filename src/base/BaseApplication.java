package base;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import consts.ConstRes;
import consts.ConstSize;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import utils.DBUtil;
import utils.ResUtil;
import utils.SaveLanguageUtil;
import utils.UIUtil;

public abstract class BaseApplication extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
//		checkIfRunning();
		try {
			DBUtil.initDB();
			ResUtil.initLanguage(SaveLanguageUtil.getData());
			Pane uiPane = FXMLLoader.load(getClass().getResource(getUIPath()), ResUtil.getResource());
			Scene scene = getScene(uiPane, primaryStage);
			scene.setFill(null);
			primaryStage.setScene(scene);
			scene.getStylesheets().add(getClass().getResource(getCSSPath()).toExternalForm());
			primaryStage.initStyle(StageStyle.TRANSPARENT);// 设定窗口无边框
			primaryStage.setOnCloseRequest(new WindowsCloseEvent(primaryStage));// 注册主框架关闭监听
			primaryStage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST,new WindowsCloseEvent(primaryStage));
			primaryStage.show();
			UIUtil.centerWindow(primaryStage, 0);
			uiPane.requestFocus();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 程序退出走此方法
	 */
	@Override
	public void stop() throws Exception {
		super.stop();
		DBUtil.close();
		System.exit(0);
	}

	protected double frameWidth() {
		return ConstSize.Main_Frame_Width + 40;
	}

	protected double frameHeight() {
		return ConstSize.Main_Frame_Height;
	}

	protected abstract String getUIPath();

	protected abstract Scene getScene(Pane uiPane, Stage stage);

	protected String getCSSPath() {
		return ConstRes.CSS_Path;
	}

	/**
	 * 保证软件只有一个在执行。
	 */
	@SuppressWarnings({ "resource" })
	private static void checkIfRunning() {
		File flagFile = new File(System.getProperty("user.dir") + "/datas");
		if (!flagFile.exists()) {
			flagFile.mkdirs();
		}
		File flagOpen = new File(flagFile, "running");
		FileChannel channel = null;
		try {
			if (!flagOpen.exists()) {
				flagOpen.createNewFile();
			}
			if (flagOpen.exists()) {
				channel = new RandomAccessFile(flagOpen.getAbsolutePath(), "rw").getChannel();
				if (channel.tryLock() == null) {
					// 已经有了进程
					channel.close();
					System.out.println("程序已启动，此次启动取消");
					System.exit(0);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			if (channel != null) {
				try {
					channel.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
