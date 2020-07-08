package application.control;

import com.jfoenix.controls.JFXButton;

import javafx.scene.control.Label;

public abstract class BaseController {
	protected JFXButton leftBtn, rightBtn;
	protected Label title;

	public final void onInitBottomBtnsAndTitle(JFXButton leftBtn, JFXButton rightBtn, Label title) {
		this.leftBtn = leftBtn;
		this.rightBtn = rightBtn;
		this.title = title;
		onSetBottomBtnsAndTitle();
	}

	protected abstract void onSetBottomBtnsAndTitle();

	protected abstract void onClickLeftBtn();

	protected abstract void onClickRightBtn();
}
