package application.control;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import beans.ProjectBean;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import utils.FileChooserUtil;
import utils.FileChooserUtil.Callback;
import utils.ToastUtil;

/**
 * 创建项目dialog界面controller
 * 
 * @author DP
 */
public class CreateProjectDialogController implements Initializable {
	@FXML
	private JFXTextField textField;
	@FXML
	private Label labelProject;
	@FXML
	private Label labelLocation;
	@FXML
	private JFXButton btnDone;

	private CallBack callBack;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void test() {
	}

	@FXML
	public void onClickSelectLocation() {
		// TODO
		FileChooserUtil.OpenFileChooserUtil("选择经纬度文件", labelLocation, new Callback() {
			@Override
			public void onResult(boolean isChoose, File file) {
				if (isChoose) {
					labelLocation.setText(file.getAbsolutePath());
				}
			}
		});
	}

	@FXML
	public void onClickSelectProject() {
		FileChooserUtil.OpenDirectoryChooserUtil("选择项目文件路径", labelProject, new Callback() {
			@Override
			public void onResult(boolean isChoose, File file) {
				if (isChoose) {
					labelProject.setText(file.getAbsolutePath());
				}
			}
		});
	}

	@FXML
	public void onClickHelp() {
		// TODO
	}

	@FXML
	public void done() {
		if ("".equals(textField.getText())) {
			ToastUtil.toast("请输入项目名");
			return;
		}
		if ("".equals(labelProject.getText())) {
			ToastUtil.toast("请选择项目路径");
			return;
		}
		ProjectBean project = new ProjectBean(textField.getText(), labelProject.getText(), labelLocation.getText());
		if (callBack != null) {
			callBack.onDone(project);
		}
	}

	public void setCallBack(CallBack callBack) {
		this.callBack = callBack;
	}

	public interface CallBack {
		void onDone(ProjectBean project);
	}
}
