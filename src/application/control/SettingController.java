package application.control;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;

import beans.FinalDataBean;
import beans.ProjectBean;
import beans.SettingsBean;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utils.ResUtil;
import utils.SaveLanguageUtil;
import utils.StrUtil;
import utils.ToastUtil;
import views.myTextField.DecimalField;
import views.myTextField.IntegerField;

/**
 * 设置其他参数界面controller
 * 
 * @author DP
 *
 */
public class SettingController extends BaseController implements Initializable {
	private SettingListener listener;
	@FXML
	JFXCheckBox checkBox_SaveMiddle;
	@FXML
	JFXCheckBox checkBox_preCheck;
	@FXML
	IntegerField textArea_width;
	@FXML
	IntegerField textArea_hight;
	@FXML
	DecimalField textArea_flyHeight;
	@FXML
	DecimalField textArea_cameraSize;
	@FXML
	DecimalField textArea_gsd;
	@FXML
	VBox Vbox_prechecks;
	@FXML
	BorderPane root;
	@FXML
	private JFXRadioButton radioButton_way1;
	@FXML
	private JFXRadioButton radioButton_way2;
	@FXML
	private HBox hbox_way1;
	@FXML
	private HBox hbox_way2;

	private ToggleGroup group;

	private ObservableList<ProjectBean> projectListData;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initCheckBox();
		initToggleGroup();
		initTextField();
	}

	private void initTextField() {
	}

	private void initToggleGroup() {
		group = new ToggleGroup();
		radioButton_way1.setToggleGroup(group);
		radioButton_way1.setUserData(0);
		radioButton_way2.setToggleGroup(group);
		radioButton_way2.setUserData(1);
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				if ((int) group.getSelectedToggle().getUserData() == 0) {
					hbox_way1.setDisable(false);
					hbox_way2.setDisable(true);
				}
				if ((int) group.getSelectedToggle().getUserData() == 1) {
					hbox_way1.setDisable(true);
					hbox_way2.setDisable(false);
				}
			}
		});
	}

	private void initCheckBox() {
		checkBox_preCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					Vbox_prechecks.setDisable(false);
				} else {
					Vbox_prechecks.setDisable(true);
				}
			}
		});
		checkBox_SaveMiddle.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
				} else {
				}
			}
		});
	}

	/**
	 * 传递项目列表数据
	 * 
	 * @param projectListData
	 */
	public void setProjectsInfo(ObservableList<ProjectBean> projectListData) {
		this.projectListData = projectListData;
	}

	public interface SettingListener {
		/**
		 * 点击开始拼接按钮
		 * 
		 * @param finalData
		 */
		void onClickStart(FinalDataBean finalData);

		/**
		 * 点击上一页按钮
		 */
		void onClickLeftBtn();
	}

	public void setListener(SettingListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onSetBottomBtnsAndTitle() {
		leftBtn.setVisible(true);
		rightBtn.setVisible(true);
		rightBtn.setText(ResUtil.gs("start"));
		leftBtn.setText(ResUtil.gs("project"));
		title.setText(ResUtil.gs("settings"));
	}

	@Override
	protected void onClickLeftBtn() {
		if (listener != null) {
			listener.onClickLeftBtn();
		}
	}

	@Override
	protected void onClickRightBtn() {
		if (StrUtil.isEmpty(textArea_width.getText()) || StrUtil.isEmpty(textArea_hight.getText())) {
			ToastUtil.toast(ResUtil.gs("setting_net_error"));
			return;
		}
		if (checkBox_preCheck.isSelected() && (int) group.getSelectedToggle().getUserData() == 0
				&& (StrUtil.isEmpty(textArea_flyHeight.getText()) || StrUtil.isEmpty(textArea_cameraSize.getText()))) {
			ToastUtil.toast(ResUtil.gs("setting_pre_check_error"));
			return;
		}
		if (checkBox_preCheck.isSelected() && (int) group.getSelectedToggle().getUserData() == 1
				&& StrUtil.isEmpty(textArea_gsd.getText())) {
			ToastUtil.toast(ResUtil.gs("setting_pre_check_error"));
			return;
		}

		int width, height;
		try {
			width = Integer.parseInt(textArea_width.getText());
			height = Integer.parseInt(textArea_hight.getText());
			if (width < 10 || height < 10) {
				ToastUtil.toast(ResUtil.gs("setting_net_size_error"));
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		SettingsBean setting = new SettingsBean();
		setting.setSaveMiddle(checkBox_SaveMiddle.isSelected());
		setting.setNetWidth(textArea_width.getText());
		setting.setNetHeight(textArea_hight.getText());
		setting.setPreCheck(checkBox_preCheck.isSelected());
		setting.setPreCheckWay((int) group.getSelectedToggle().getUserData());
		setting.setGsd(textArea_gsd.getText());
		setting.setFlyHeight(textArea_flyHeight.getText());
		setting.setCameraSize(textArea_cameraSize.getText());
		setting.setLanguage(SaveLanguageUtil.getData());
		FinalDataBean finalDataBean = new FinalDataBean(projectListData, setting);
		if (listener != null) {
			listener.onClickStart(finalDataBean);
		}
	}
}
