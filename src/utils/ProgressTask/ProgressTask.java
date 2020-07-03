package utils.ProgressTask;

import java.io.IOException;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.UIUtil;

/**
 * 异步线程类，会自己弹出加载框
 * @author DP
 *
 */
public class ProgressTask {

	private Stage dialogStage;
	private Thread inner;

	public ProgressTask(final Task<?> task, Stage primaryStage) {

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

		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			public void handle(WorkerStateEvent event) {
				if (dialogStage != null) {
					dialogStage.close();
				}
			}
		});

	}

	public void start() {
		inner.start();
		activateProgressBar();
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
}
