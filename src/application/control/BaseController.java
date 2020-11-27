package application.control;

import com.jfoenix.controls.JFXButton;

import javafx.scene.control.Label;
import javafx.stage.Stage;
import utils.ToastUtil;
import utils.UIUtil;

public abstract class BaseController {
	protected JFXButton leftBtn, rightBtn;
	protected Label title;
	protected Stage stage;

	public final void onInitBottomBtnsAndTitle(JFXButton leftBtn, JFXButton rightBtn, Label title) {
		this.leftBtn = leftBtn;
		this.rightBtn = rightBtn;
		this.title = title;
		onSetBottomBtnsAndTitle();
	}

	public void setMainStage(Stage stage) {
		this.stage = stage;
		ToastUtil.setOwnerStage(stage);
	}

	protected abstract void onSetBottomBtnsAndTitle();

	protected abstract void onClickLeftBtn();

	protected abstract void onClickRightBtn();
}
