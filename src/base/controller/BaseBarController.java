package base.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import beans.MyFxmlBean;
import consts.ConstSize;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utils.AnimatorUtil;
import utils.ResUtil;
import utils.SysUtil;
import utils.UIUtil;

/**
 * 主界面的controller
 * 
 * @author DP
 *
 */
public class BaseBarController implements Initializable {
	@FXML
	private HBox bar;
	@FXML
	BorderPane root;
	@FXML
	Label bar_title;
	@FXML
	JFXButton btn_close;
	@FXML
	JFXButton btn_hide;
	@FXML
	JFXButton btn_history;
	@FXML
	JFXButton btn_help;
	private RotateTransition rotateEnter;
	private RotateTransition rotateExit;
	private RotateTransition rotateEnter2;
	private RotateTransition rotateExit2;
	protected ScaleTransition scaleExit;
	protected TranslateTransition translateExit;
	private ScaleTransition scaleEnter;
	private TranslateTransition translateEnter;
	private ScaleTransition scaleExitHelp;
	private ScaleTransition scaleEnterHelp;
	@FXML
	ImageView img_hide;
	@FXML
	ImageView img_history;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AnimatorUtil.fadeShow(root, 1500);
		btn_help.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (scaleExitHelp != null) {
					scaleExitHelp.stop();
				}
				scaleEnterHelp = AnimatorUtil.scale(btn_help, 200, 1, 1.2);
			}
		});
		btn_help.addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (scaleEnterHelp != null) {
					scaleEnterHelp.stop();
				}
				scaleExitHelp = AnimatorUtil.scale(btn_help, 200, 1.2, 1);
			}
		});
		btn_close.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (rotateExit != null) {
					rotateExit.stop();
				}
				rotateEnter = AnimatorUtil.rotate(btn_close.getGraphic(), 350, 0, 90);
			}
		});
		btn_close.addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (rotateEnter != null) {
					rotateEnter.stop();
				}
				rotateExit = AnimatorUtil.rotate(btn_close.getGraphic(), 350, 90, 0);
			}
		});
		btn_history.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (rotateExit2 != null) {
					rotateExit2.stop();
				}
				rotateEnter2 = AnimatorUtil.rotate(btn_history.getGraphic(), 350, 0, 45);
			}
		});
		btn_history.addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (rotateEnter2 != null) {
					rotateEnter2.stop();
				}
				rotateExit2 = AnimatorUtil.rotate(btn_history.getGraphic(), 350,45, 0);
			}
		});
		btn_hide.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (scaleExit != null) {
					scaleExit.stop();
				}
				if (translateExit != null) {
					translateExit.stop();
				}
				scaleEnter = AnimatorUtil.scale(img_hide, 350, 1, 0.7);
				translateEnter = AnimatorUtil.translate(img_hide, 350, 0, 3);
			}
		});
		btn_hide.addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (scaleEnter != null) {
					scaleEnter.stop();
				}
				if (translateEnter != null) {
					translateEnter.stop();
				}
				scaleExit = AnimatorUtil.scale(img_hide, 350, 0.7, 1);
				translateExit = AnimatorUtil.translate(img_hide, 350, 3, 0);
			}
		});
		root.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ESCAPE) {
					onClickClose(null);
				}
			}
		});
	}

	public BaseBarController() {
	}

	@FXML
	public void onClickHide(ActionEvent event) {
		Stage stage = (Stage) root.getScene().getWindow();
		stage.setIconified(true);
	}
	
	@FXML
	public void onClickHelp(ActionEvent event) {
		File file = SysUtil.openHelpFile();
		SysUtil.exeOpenFile(file.getAbsolutePath());
	}

	@FXML
	public void onClickClose(ActionEvent event) {
		// 这里是取到Stage的具体代码
		Stage stage = (Stage) root.getScene().getWindow();
		stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}

	@FXML
	public void onClickHistory(ActionEvent event) {
		MyFxmlBean history = UIUtil.openDialog(getClass(), "/application/fxml/History.fxml",
				ConstSize.Main_Frame_Width + 420, ConstSize.Main_Frame_Height + 50, ResUtil.gs("stitching_history"),
				(Stage) root.getScene().getWindow());
	}

}
