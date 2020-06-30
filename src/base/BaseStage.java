package base;

import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * 无边框，圆角卡片 界面
 * @author DP
 *
 */
public class BaseStage extends Stage{
	public BaseStage() {
		initStyle(StageStyle.TRANSPARENT);// 设定窗口无边框
	}
}
