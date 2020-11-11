package utils;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class AnimatorUtil {

	/**
	 * 渐渐显示动画
	 * 
	 * @param pane
	 * @param time
	 * @return
	 */
	public static FadeTransition fadeShow(Node pane, double time) {
		FadeTransition fadeTransition = new FadeTransition(Duration.millis(time), pane);
		fadeTransition.setFromValue(0);
		fadeTransition.setToValue(1);
		fadeTransition.setCycleCount(1);
		fadeTransition.play();
		return fadeTransition;
	}

	public static RotateTransition rotate(Node pane, double time, double from, double to) {
		RotateTransition rt = new RotateTransition(Duration.millis(time), pane);
		rt.setFromAngle(from);
		rt.setToAngle(to);
		rt.setCycleCount(1);
		rt.setAutoReverse(false);
		rt.play();
		return rt;
	}

	public static ScaleTransition scale(Node pane, double time, double fromX, double toX) {
		ScaleTransition st = new ScaleTransition(Duration.millis(time), pane);
		st.setFromX(fromX);
		st.setToX(toX);
		st.setCycleCount(1);
		st.play();
		return st;
	}

	public static TranslateTransition translate(Node pane, double time, double fromY, double toY) {
		TranslateTransition tt = new TranslateTransition(Duration.millis(time), pane);
		tt.setFromY(fromY);
		tt.setToY(toY);
		tt.setCycleCount(1);
		tt.play();
		return tt;
	}

	public static FadeTransition fadeHide(Node pane, double time) {
		FadeTransition fadeTransition = new FadeTransition(Duration.millis(time), pane);
		fadeTransition.setFromValue(1);
		fadeTransition.setToValue(0);
		fadeTransition.setCycleCount(1);
		fadeTransition.play();
		return fadeTransition;
	}
}
