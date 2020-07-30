package utils;

import javafx.animation.FadeTransition;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class AnimatorUtil {

	/**
	 * 渐渐显示动画
	 * 
	 * @param pane
	 * @param time
	 * @return
	 */
	public static FadeTransition fadeShow(Pane pane, double time) {
		FadeTransition fadeTransition = new FadeTransition(Duration.millis(time), pane);
		fadeTransition.setFromValue(0);
		fadeTransition.setToValue(1);
		fadeTransition.setCycleCount(1);
		fadeTransition.play();
		return fadeTransition;
	}

}
