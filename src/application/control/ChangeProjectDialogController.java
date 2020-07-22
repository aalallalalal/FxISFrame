package application.control;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import beans.ProjectBean;
import consts.ConstRes;
import consts.ConstSize;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import utils.FileChooserUtil;
import utils.FileChooserUtil.Callback;
import views.MyToolTip;
import utils.SaveProjectsUtil;
import utils.ToastUtil;
import utils.UIUtil;

/**
 * 创建项目dialog界面controller
 * 
 * @author DP
 */
public class ChangeProjectDialogController implements Initializable {
	@FXML
	BorderPane root;
	@FXML
	private JFXTextField textField;
	@FXML
	private Label labelProject;
	@FXML
	private Label labelLocation;
	@FXML
	private JFXButton btnDone;
	@FXML
	private HBox hbox_location;
	@FXML
	private JFXRadioButton radioButton_img;
	@FXML
	private JFXRadioButton radioButton_file;

	private CallBack callBack;

	private ToggleGroup group;

	private ProjectBean bean;

	public void setInitData(ProjectBean bean) {
		this.bean = bean;
		initData();
	}

	private void initData() {
		if (bean == null) {
			return;
		}
		textField.setText(bean.getProjectName());
		labelProject.setText(bean.getProjectDir());
		labelLocation.setText(bean.getProjectLocationFile());
		if(bean.getLocationFrom()==0) {
			group.selectToggle(radioButton_img);
		}else {
			group.selectToggle(radioButton_file);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		group = new ToggleGroup();
		radioButton_img.setToggleGroup(group);
		radioButton_img.setUserData(0);
		radioButton_file.setToggleGroup(group);
		radioButton_file.setUserData(1);
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
				if (group.getSelectedToggle() != null) {
					int data = (int) group.getSelectedToggle().getUserData();
					if (data == 0) {
						// 选择图片录入
						hbox_location.setDisable(true);
					} else {
						// 选择文件录入
						hbox_location.setDisable(false);
					}
				}
			}
		});
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
					String path = file.getAbsolutePath();
					path = path.replaceAll("\\\\", "/");
					labelLocation.setText(path);
					labelLocation.setTooltip(new MyToolTip(path));
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
					String path = file.getAbsolutePath();
					path = path.replaceAll("\\\\", "/");
					labelProject.setText(path);
					labelProject.setTooltip(new MyToolTip(path));
				}
			}
		});
	}

	@FXML
	public void onClickHelp() {
		UIUtil.openNoticeDialog(getClass(), ConstSize.Notice_Dialog_Frame_Width, ConstSize.Notice_Dialog_Frame_Height,
				"提示", ConstRes.Text_LocationFile_Notice, (Stage) root.getScene().getWindow());
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
		if (radioButton_file.isSelected() && "".equals(labelLocation.getText())) {
			ToastUtil.toast("请选择经纬度文件路径");
			return;
		}
		File projectFile = new File(labelProject.getText());
		if (projectFile == null || !projectFile.exists()) {
			// 项目路径不存在
			ToastUtil.toast("项目路径失效,请重新设置");
			return;
		}
		File locationFile = new File(labelLocation.getText());
		if (radioButton_file.isSelected() && (locationFile == null || !locationFile.exists())) {
			ToastUtil.toast("经纬度文件路径失效,请重新设置");
			return;
		}
		int locationFrom = (int) group.getSelectedToggle().getUserData();
		bean.setProjectName(textField.getText());
		bean.setProjectDir(labelProject.getText());
		bean.setLocationFrom(locationFrom);
		bean.setProjectLocationFile(labelLocation.getText());
		SaveProjectsUtil.changeProjectData(bean, null);
		if (callBack != null) {
			callBack.onDone(bean);
		}
	}

	public void setCallBack(CallBack callBack) {
		this.callBack = callBack;
	}

	public interface CallBack {
		void onDone(ProjectBean project);
	}

}
