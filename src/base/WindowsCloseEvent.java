package base;

import base.controller.ConfirmDialogController.CallBack;
import consts.ConstSize;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utils.ResUtil;
import utils.UIUtil;

/**
 * 主框架关闭监听
 * 
 * @author DP
 *
 */
public class WindowsCloseEvent implements EventHandler<WindowEvent> {

	private Stage stage;

	public WindowsCloseEvent(Stage stage) {
		this.stage = stage;
	}

	public void handle(WindowEvent event) {
		event.consume();
		UIUtil.openConfirmDialog(getClass(), ConstSize.Confirm_Dialog_Frame_Width,
				ConstSize.Confirm_Dialog_Frame_Height, ResUtil.gs("exit"), ResUtil.gs("sure_exit"), stage,
				new CallBack() {
					@Override
					public void onConfirm() {
						Platform.exit();
					}

					@Override
					public void onCancel() {
					}
				});
	}
}
