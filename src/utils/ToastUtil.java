package utils;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * 弹出框，用于简单提示
 */
public class ToastUtil {
	private static Stage stage = new Stage();

	static {
		stage.initStyle(StageStyle.TRANSPARENT);// 舞台透明
	}

	// 默认3秒
	public static void toast(String msg) {
		toast(msg, 2000);
	}

	/**
	 * 指定时间消失
	 * 
	 * @param msg
	 * @param time
	 */
	public static void toast(String msg, int time) {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> stage.close());
			}
		};
		init(msg);
		Timer timer = new Timer();
		timer.schedule(task, time);
		stage.show();
	}

	// 设置消息
	private static void init(String msg) {
		Label label = new Label(msg);// 默认信息
		label.setText(msg);
		label.setPrefWidth(150);
		label.setStyle(
				"-fx-text-fill: #fff;-fx-background: rgba(56,56,56,0.4);-fx-border-radius: 25;-fx-background-radius: 25");// label透明,圆角
		label.setPrefHeight(50);
		label.setMaxWidth(800);
		label.setPadding(new Insets(15));
		label.setAlignment(Pos.CENTER);// 居中
		label.setFont(new Font(12));// 字体大小
		Scene scene = new Scene(label,160,50);
		scene.setFill(null);// 场景透明
		stage.setScene(scene);
	}
}
