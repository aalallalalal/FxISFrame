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
	private static Scene scene;
	private boolean isDone = false;

	static {
		Pane uiPane = null;
		try {
			uiPane = FXMLLoader.load(ProgressTask.class.getResource("/utils/ProgressTask/ProgressTask.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		scene = new Scene(uiPane);
		UIUtil.setFrameCss(ProgressTask.class, scene);
		scene.setFill(null);
	}

	public ProgressTask(final MyTask<?> task, Stage primaryStage) {
		// 窗口父子关系
		dialogStage = new Stage();
		if (primaryStage != null) {
			dialogStage.initOwner(primaryStage);
		}
		dialogStage.setWidth(180);
		dialogStage.setHeight(160);
		dialogStage.initStyle(StageStyle.UNDECORATED);
		dialogStage.initStyle(StageStyle.TRANSPARENT);
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.setScene(scene);

		inner = new Thread(task);
		// 根据实际需要处理消息值
		task.messageProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// 执行异步任务完毕，不管是否成功。
				if ("DONE".equals(newValue)) {
					isDone = true;
					if (dialogStage != null) {
						dialogStage.close();
					}
				}
			}
		});
	}

	/**
	 * 启动异步线程
	 */
	public void start() {
		inner.start();

		// 开启dialog显示延迟，如果异步任务在500ms内执行完毕，则不显示dialog
		Timer openDialogTime = new Timer();
		openDialogTime.schedule(new TimerTask() {
			@Override
			public void run() {
				// 500ms过去了,还没执行完,那开启dialog
				if (!isDone) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							activateProgressBar();
							// 开启保证dialog消失的延迟执行.10s内，还没执行完，就认定为dialog消失出现bug。主动关掉dialog
							Timer closeTimer = new Timer();
							closeTimer.schedule(new TimerTask() {
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
					});
				}
			}
		}, 200);

	}

	private void activateProgressBar() {
		dialogStage.show();
	}

	public Stage getDialogStage() {
		return dialogStage;
	}

	public void cancelProgressBar() {
		dialogStage.close();
	}

	public static abstract class MyTask<T> extends Task<T> {
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

		@Override
		protected void cancelled() {
			super.cancelled();
			updateMessage("DONE");
		}
	}
}
