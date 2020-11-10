package application.control;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import beans.MyFxmlBean;
import beans.ProjectBean;
import beans.SettingsBean;
import consts.ConstSize;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.ResUtil;
import utils.StrUtil;
import utils.ToastUtil;
import utils.UIUtil;
import views.MyToolTip;
import views.myTextField.DecimalField;
import views.myTextField.IntegerField;

/**
 * 参数dialog界面controller
 * 
 * @author DP
 */
public class SettingsDialogController implements Initializable {
	@FXML
	private JFXButton btnDone;
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
	@FXML
	JFXTextField text_setting_name;
	@FXML
	JFXCheckBox checkBox_selectAll;
	@FXML
	ListView<HBox> listView_projects;
	private ObservableList<HBox> listView_proj = FXCollections.observableArrayList();

	private CallBack callBack;
	private int type; // 2:模板来，1：单独修改来
	@FXML
	VBox vbox_projectlist;
	private ObservableList<ProjectBean> projectListData;
	private SettingsBean returnSettings;
	@FXML HBox hbox_name;
	
	ImageView imageView = new ImageView(new Image("/resources/wushuju.png"));

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initToggleGroup();
		initCheckBox();
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
		checkBox_selectAll.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
					for (HBox item : listView_proj) {
						JFXCheckBox checkBox_select = (JFXCheckBox) item.lookup("#checkBox_select");
						checkBox_select.setSelected(checkBox_selectAll.isSelected());
					}
				}
			}
		});
	}

	@FXML
	private void onClickHelpCamera() {
		ToastUtil.toast(ResUtil.gs("setting_camera_tip"));
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

	@FXML
	public void done() {
		if (type == 0) {
			if (callBack != null) {
				callBack.onReturn(null);
			}
			return;
		}

		boolean isDone = checkAndNew();
		if (isDone) {

			if (type == 2) {
				checkChangeProjectData();
			}

			if (callBack != null) {
				callBack.onReturn(returnSettings);
			}
		}
	}

	/**
	 * 最后了，把项目数据改了。
	 */
	private void checkChangeProjectData() {
		for (int i = 0; i < projectListData.size(); i++) {
			if (i >= 0 && i < listView_proj.size()) {
				JFXCheckBox checkBox_select = (JFXCheckBox) listView_proj.get(i).lookup("#checkBox_select");
				boolean ischecked = checkBox_select.isSelected();
				ProjectBean item = projectListData.get(i);
				SettingsBean settings = item.getSettings();
				if (ischecked) {
					// 选择了
					if (settings == null) {
						// ,没有setting,设置上去.
						item.setSettings(returnSettings);
					} else {
						long id = settings.getId();
						if (id != returnSettings.getId()) {
							// id不一样，设置上去
							item.setSettings(returnSettings);
						} else {
							// id一样，没必要设置
						}
					}
				} else {
					// 没有选择
					if (settings == null) {
						// ,没有setting,不管
					} else {
						long id = settings.getId();
						if (id != returnSettings.getId()) {
							// id不一样，不管
						} else {
							// id一样，则清空它的setting
							item.setSettings(null);
						}
					}
				}
			}
		}
	}

	public void setCallBack(CallBack callBack) {
		this.callBack = callBack;
	}

	public interface CallBack {
		void onReturn(SettingsBean settings);
	}

	/**
	 * 
	 * @param type            2:显示project列表数据，需要project列表数据，但settings可空，空的时候表示是新建模板，回调会返回一个新的settings。
	 *                        非空表示修改模板，只会 修改传入的settings，返回的不是new 的settings。
	 *                        1：不显示project列表数据，projectlistdata可以为空，settings可空，返回的是一个new
	 *                        settings 0:只是查看，不可编辑
	 * @param projectListData
	 * @param settings
	 */
	public void initExtraData(int type, ObservableList<ProjectBean> projectListData, SettingsBean settings) {
		this.type = type;
		this.projectListData = projectListData;
		if (type == 0) {
			// 仅仅是查看参数，不可修改，不可操作
			justSeeSettings(settings);
			return;
		}

		if (settings == null) {
			this.returnSettings = new SettingsBean();
			if (type == 2) {
				returnSettings.setSettingType(2);
			} else if (type == 1) {
				returnSettings.setSettingType(1);
			}
		} else {
			if (type == 2) {
				this.returnSettings = settings;
				this.returnSettings.setSettingType(2);
			} else if (type == 1) {
				this.returnSettings = settings.copyToANew("");
				this.returnSettings.setSettingType(1);
			}
		}

		if (type == 1) {
			vbox_projectlist.setVisible(false);
			root.setRight(null);
		} else if (type == 2) {
			vbox_projectlist.setVisible(true);
			initListView();
		}

		if(type==1||type==0) {
			//隐藏名字
			hbox_name.setVisible(false);
		}
		
		returnSettingsView();
	}

	private void justSeeSettings(SettingsBean settings) {
		vbox_projectlist.setVisible(false);
		root.setRight(null);
		text_setting_name.setVisible(true);
		text_setting_name.setEditable(false);
		checkBox_SaveMiddle.setDisable(true);
		checkBox_preCheck.setDisable(true);
		textArea_width.setEditable(false);
		textArea_hight.setEditable(false);
		textArea_gsd.setEditable(false);
		textArea_cameraSize.setEditable(false);
		textArea_flyHeight.setEditable(false);
		radioButton_way1.setDisable(true);
		radioButton_way2.setDisable(true);
		if (settings == null) {
			return;
		}
		text_setting_name.setText(settings.getName());
		checkBox_SaveMiddle.setSelected(settings.isSaveMiddle());
		checkBox_preCheck.setSelected(settings.isPreCheck());
		textArea_width.setText(settings.getNetWidth());
		textArea_hight.setText(settings.getNetHeight());
		textArea_gsd.setText(settings.getGsd());
		textArea_cameraSize.setText(settings.getCameraSize());
		textArea_flyHeight.setText(settings.getFlyHeight());
		if (settings.getPreCheckWay() == 0) {
			group.selectToggle(radioButton_way1);
		} else {
			group.selectToggle(radioButton_way2);
		}
	}

	private void returnSettingsView() {
		if (type == 2) {
			text_setting_name.setVisible(true);
		} else {
			text_setting_name.setVisible(false);
		}
		text_setting_name.setText(returnSettings.getName());
		checkBox_SaveMiddle.setSelected(returnSettings.isSaveMiddle());
		checkBox_preCheck.setSelected(returnSettings.isPreCheck());
		textArea_width.setText(returnSettings.getNetWidth());
		textArea_hight.setText(returnSettings.getNetHeight());
		textArea_gsd.setText(returnSettings.getGsd());
		textArea_cameraSize.setText(returnSettings.getCameraSize());
		textArea_flyHeight.setText(returnSettings.getFlyHeight());
		if (returnSettings.getPreCheckWay() == 0) {
			group.selectToggle(radioButton_way1);
		} else {
			group.selectToggle(radioButton_way2);
		}
	}

	private void initListView() {
		listView_projects.setPlaceholder(imageView);
		listView_projects.setItems(listView_proj);
		listView_proj.clear();
		int selectCount = 0;
		for (int i = 0; i < projectListData.size(); i++) {
			MyFxmlBean fxmlbean = UIUtil.loadFxml(getClass(), "/application/fxml/SettingsDialogProjectHBox.fxml");
			HBox temp = (HBox) fxmlbean.getPane();
			ProjectBean project = projectListData.get(i);
			if (setItemProjectContent(project, temp)) {
				selectCount++;
			}
			listView_proj.add(temp);
		}
		if (selectCount == projectListData.size()) {
			checkBox_selectAll.setSelected(true);
		} else {
			checkBox_selectAll.setSelected(false);
		}
	}

	/**
	 * 设置项目列表item
	 * 
	 * @param project
	 * @param temp
	 */
	private boolean setItemProjectContent(ProjectBean project, HBox temp) {
		Label project_name = (Label) temp.lookup("#project_name");
		project_name.setText(project.getProjectName());
		project_name.setTooltip(new MyToolTip(project.transToTipStr(false)));
		JFXCheckBox checkBox_select = (JFXCheckBox) temp.lookup("#checkBox_select");
		checkBox_select.setVisible(true);
		if (project.getSettings() != null && project.getSettings().getId() == returnSettings.getId()) {
			checkBox_select.setSelected(true);
		} else {
			checkBox_select.setSelected(false);
		}
		checkBox_select.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
					if (checkBox_select.isSelected()) {
						boolean isAll = true;
						for (HBox item : listView_proj) {
							JFXCheckBox checkBox_select = (JFXCheckBox) item.lookup("#checkBox_select");
							if (!checkBox_select.isSelected()) {
								isAll = false;
								break;
							}
						}
						if (isAll) {
							checkBox_selectAll.setSelected(true);
						} else {
							checkBox_selectAll.setSelected(false);
						}
					} else {
						checkBox_selectAll.setSelected(false);
					}
				}
			}
		});
		changeButtonView(temp, project.getSettings());
		return checkBox_select.isSelected();
	}

	/**
	 * 项目列表单项刷新
	 * 
	 * @param item
	 * @param setting
	 */
	private void changeButtonView(HBox item, SettingsBean setting) {
		JFXButton settingBtn = (JFXButton) item.lookup("#setting");
		MyToolTip toolTip = new MyToolTip();
		if (setting == null) {
			settingBtn.setText(ResUtil.gs("setting_name_empty"));
			settingBtn.setStyle(SettingController.style_no);
			toolTip.setText(ResUtil.gs("setting_has_no_set"));
		} else {
			settingBtn.setText(setting.getName());
			if (setting.getSettingType() == 1) {
				settingBtn.setStyle(SettingController.style_unname);
			} else if(setting.getSettingType()==2){
				settingBtn.setStyle(SettingController.style_temp);
			}
			toolTip.setText(setting.transToTipStr());
		}
		settingBtn.setTooltip(toolTip);
	}

	/**
	 * 校验并创建新的settingbean、修改settingbean、保存
	 * 
	 * @return
	 */
	private boolean checkAndNew() {
		if (StrUtil.isEmpty(textArea_width.getText()) || StrUtil.isEmpty(textArea_hight.getText())) {
			ToastUtil.toast(ResUtil.gs("setting_net_error"));
			return false;
		}
		if (checkBox_preCheck.isSelected() && (int) group.getSelectedToggle().getUserData() == 0
				&& (StrUtil.isEmpty(textArea_flyHeight.getText()) || StrUtil.isEmpty(textArea_cameraSize.getText()))) {
			ToastUtil.toast(ResUtil.gs("setting_pre_check_error"));
			return false;
		}
		if (checkBox_preCheck.isSelected() && (int) group.getSelectedToggle().getUserData() == 1
				&& StrUtil.isEmpty(textArea_gsd.getText())) {
			ToastUtil.toast(ResUtil.gs("setting_pre_check_error"));
			return false;
		}
		if (type == 2 && StrUtil.isEmpty(text_setting_name.getText())) {
			ToastUtil.toast(ResUtil.gs("setting_name_error"));
			return false;
		}

		int width, height;
		try {
			width = textArea_width.getValue();
			height = textArea_hight.getValue();
			if (width < 10 || height < 10) {
				ToastUtil.toast(ResUtil.gs("setting_net_size_error"));
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		returnSettings.setName(text_setting_name.getText());
		returnSettings.setSaveMiddle(checkBox_SaveMiddle.isSelected());
		returnSettings.setNetWidth(textArea_width.getValue() + "");
		returnSettings.setNetHeight(textArea_hight.getValue() + "");
		returnSettings.setPreCheck(checkBox_preCheck.isSelected());
		returnSettings.setPreCheckWay((int) group.getSelectedToggle().getUserData());
		returnSettings.setGsd(textArea_gsd.getValue() + "");
		returnSettings.setFlyHeight(textArea_flyHeight.getValue() + "");
		returnSettings.setCameraSize(textArea_cameraSize.getValue() + "");

		return true;
	}

}
