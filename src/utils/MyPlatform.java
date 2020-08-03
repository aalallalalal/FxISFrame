package utils;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;

/**
 * 自定义延迟 主线程执行工具类
 * 
 * @author DP
 *
 */
public class MyPlatform {
	public static void runLater(Runnable runnable, long delay) {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(runnable);
			}
		};
		Timer timer = new Timer();
		timer.schedule(task, delay);
	}
}
