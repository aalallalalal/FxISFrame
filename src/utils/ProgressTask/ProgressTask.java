package utils.ProgressTask;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.UIUtil;

/**
 * 异步线程类，会自己弹出加载框
 * 
 * @author DP
 *
 */
public class ProgressTask {

	private Stage dialogStage;
	private Thread inner;
	private Timer time;

	public ProgressTask(final MyTask task, Stage primaryStage) {

		dialogStage = new Stage();

		// 窗口父子关系
		dialogStage.initOwner(primaryStage);
		dialogStage.setWidth(180);
		dialogStage.setHeight(160);
		dialogStage.initStyle(StageStyle.UNDECORATED);
		dialogStage.initStyle(StageStyle.TRANSPARENT);
		dialogStage.initModality(Modality.APPLICATION_MODAL);

		Pane uiPane;
		try {
			uiPane = FXMLLoader.load(getClass().getResource("/utils/ProgressTask/ProgressTask.fxml"));
			Scene scene = new Scene(uiPane);
			UIUtil.setFrameCss(getClass(), scene);
			scene.setFill(null);
			dialogStage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
		inner = new Thread(task);

		// 根据实际需要处理消息值
		task.messageProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if ("DONE".equals(newValue)) {
					if (dialogStage != null) {
						dialogStage.close();
					}
				}
			}
		});

		time = new Timer();
	}

	public void start() {
		activateProgressBar();
		inner.start();
		time.schedule(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if (dialogStage != null) {
							dialogStage.close();
						}
					}
				});
			}
		}, 10000);
	}

	public void activateProgressBar() {
		dialogStage.show();
	}

	public Stage getDialogStage() {
		return dialogStage;
	}

	public void cancelProgressBar() {
		dialogStage.close();
	}

	public static abstract class MyTask extends Task<Integer> {
		@Override
		protected void succeeded() {
			super.succeeded();
			updateMessage("DONE");
		}

		@Override
		protected void failed() {
			super.failed();
			updateMessage("DONE");
		}
	}
}
