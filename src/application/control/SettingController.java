package application.control;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;

import beans.FinalDataBean;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.ResUtil;
import utils.SaveSettingsUtil;
import utils.StrUtil;
import utils.ToastUtil;
import utils.UIUtil;
import views.myTextField.DecimalField;
import views.myTextField.IntegerField;

/**
 * 设置其他参数界面controller
 * 
 * @author DP
 *
 */
public class SettingController extends BaseController implements Initializable {
	public static final String style_temp = "-fx-background-color:#5AF102;-fx-text-fill: white;-fx-padding:2.0;-fx-font-size:10.0;";
	public static final String style_unname = "-fx-background-color:#FFB731;-fx-text-fill: white;-fx-padding:2.0;-fx-font-size:10.0;";
	public static final String style_no = "-fx-background-color:#FF8282;-fx-text-fill: white;-fx-padding:2.0;-fx-font-size:10.0;";

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
	private ObservableList<SettingsBean> settingListData = FXCollections.observableArrayList();
	@FXML
	ListView<HBox> listView_projects;
	private ObservableList<HBox> listViewData_proj = FXCollections.observableArrayList();
	@FXML
	ListView<HBox> listView_settings;
	private ObservableList<HBox> listViewData_setting = FXCollections.observableArrayList();
	private ProjectBean currentProject;
	private int currentIndex;
	@FXML
	VBox setting_pane;
	@FXML
	Label label_project_name;
	private Stage stage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initCheckBox();
		initToggleGroup();
		initTextField();
		initListView();
		setSettingsListDataInfo();
	}

	private void setSettingsListDataInfo() {
		SaveSettingsUtil.getSettingsData(stage, new SaveSettingsUtil.Callback() {
			@Override
			public void onGetData(ArrayList<SettingsBean> list) {
				settingListData.clear();
				settingListData.addAll(list);
				addSettingsToList(settingListData, null);
			}
		});
	}

	private void addSettingsToList(ObservableList<SettingsBean> list, SettingsBean singleSetting) {
		if (list != null) {
			listViewData_setting.clear();
			for (int i = 0; i < list.size(); i++) {
				MyFxmlBean fxmlbean = UIUtil.loadFxml(getClass(), "/application/fxml/SettingsTempHBox.fxml");
				HBox temp = (HBox) fxmlbean.getPane();
				SettingsBean setting = list.get(i);
				setItemSettingContent(setting, temp);
				listViewData_setting.add(temp);
			}
		} else if (singleSetting != null) {
			settingListData.add(singleSetting);
			MyFxmlBean fxmlbean = UIUtil.loadFxml(getClass(), "/application/fxml/SettingsTempHBox.fxml");
			HBox temp = (HBox) fxmlbean.getPane();
			setItemSettingContent(singleSetting, temp);
			listViewData_setting.add(temp);
		}
	}

	private void setItemSettingContent(SettingsBean setting, HBox temp) {
		Label name = (Label) temp.lookup("#name");
		name.setText(setting.getName());
	}

	private void initListView() {
		listView_projects.setItems(listViewData_proj);
		listView_projects.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				currentIndex = listView_projects.getSelectionModel().getSelectedIndex();
				if (currentIndex >= 0 && currentIndex < listViewData_proj.size()) {
					currentProject = projectListData.get(currentIndex);
					setSettingViews(currentProject.getSettings(), false);
				} else {
					setting_pane.setVisible(false);
				}
			}
		});
		listView_settings.setItems(listViewData_setting);
		listView_settings.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
					// 双击
					int selectedIndex = listView_settings.getSelectionModel().getSelectedIndex();
					if (selectedIndex < 0 || selectedIndex >= settingListData.size()) {
						return;
					}
					SettingsBean settings = settingListData.get(selectedIndex);
					if (settings == null) {
						return;
					}
					MyFxmlBean settingDialogBean = UIUtil.openDialog(getClass(),
							"/application/fxml/SettingsDialog.fxml", ConstSize.Main_Frame_Width,
							ConstSize.Main_Frame_Height, settings.getName(), stage);
					SettingsDialogController settingDialogController = settingDialogBean.getFxmlLoader()
							.getController();
					settingDialogController.initExtraData(2, projectListData, settings);
					settingDialogController.setCallBack(new application.control.SettingsDialogController.CallBack() {
						@Override
						public void onReturn(SettingsBean settings) {
							refreshProjectListView();
							if(currentProject!=null) {
								setSettingViews(currentProject.getSettings(), false);
							}
							SaveSettingsUtil.changeSettingData(settings, null);
							settingDialogBean.getStage().close();
						}
					});
				}
			}
		});
	}

	/**
	 * 设置参数数据
	 * 
	 * @param settings
	 * @param showName
	 */
	protected void setSettingViews(SettingsBean settings, boolean showName) {
		label_project_name.setText(currentProject.getProjectName());
		setting_pane.setVisible(true);
		if (settings == null) {
			clearSettingViews(showName);
		} else {
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
	}

	private void clearSettingViews(boolean showName) {
		checkBox_SaveMiddle.setSelected(false);
		checkBox_preCheck.setSelected(false);
		textArea_width.setText("");
		textArea_hight.setText("");
		textArea_gsd.setText("");
		textArea_cameraSize.setText("");
		textArea_flyHeight.setText("");
		group.selectToggle(radioButton_way1);
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
		addProjectsToList(projectListData);
	}

	/**
	 * 添加项目列表数据
	 * 
	 * @param projects
	 */
	private void addProjectsToList(ObservableList<ProjectBean> projects) {
		listViewData_proj.clear();
		for (int i = 0; i < projects.size(); i++) {
			MyFxmlBean fxmlbean = UIUtil.loadFxml(getClass(), "/application/fxml/SettingsProjectHBox.fxml");
			HBox temp = (HBox) fxmlbean.getPane();
			ProjectBean project = projects.get(i);
			setItemProjectContent(project, temp);
			listViewData_proj.add(temp);
		}
	}

	/**
	 * 设置项目列表item
	 * 
	 * @param project
	 * @param temp
	 */
	private void setItemProjectContent(ProjectBean project, HBox temp) {
		Label project_name = (Label) temp.lookup("#project_name");
		project_name.setText(project.getProjectName());
		changeButtonView(temp, project.getSettings());
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

		/**
		 * 添加设置模板
		 */
		void onClickAddSettings(ObservableList<ProjectBean> projectListData);
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

	@FXML
	protected void onSaveSetting() {
		SettingsBean newSetting = checkAndSave();
		if (newSetting == null) {
			return;
		}
		currentProject.setSettings(newSetting);
		changeButtonView(listViewData_proj.get(currentIndex), newSetting);
	}

	/**
	 * 项目列表全部刷新
	 */
	private void refreshProjectListView() {
		for (int i = 0; i < projectListData.size(); i++) {
			ProjectBean itemProj = projectListData.get(i);
			if (i >= 0 && i < listViewData_proj.size()) {
				HBox hBox = listViewData_proj.get(i);
				if (itemProj != null && hBox != null) {
					changeButtonView(hBox, itemProj.getSettings());
				}
			}
		}
	}

	/**
	 * 项目列表单项刷新
	 * 
	 * @param item
	 * @param setting
	 */
	private void changeButtonView(HBox item, SettingsBean setting) {
		JFXButton settingBtn = (JFXButton) item.lookup("#setting");
		if (setting == null) {
			settingBtn.setText(ResUtil.gs("setting_name_empty"));
			settingBtn.setStyle(style_no);
		} else {
			settingBtn.setText(setting.getName());
			if (setting.getSettingType() == 1) {
				settingBtn.setStyle(style_unname);
			} else {
				settingBtn.setStyle(style_temp);
			}
		}
	}

	/**
	 * 调用新建模板后，成功回调方法。
	 * 
	 * @param settings
	 */
	public void addSettingResult(SettingsBean settings) {
		refreshProjectListView();
		if(currentProject!=null) {
			setSettingViews(currentProject.getSettings(), false);
		}
		SaveSettingsUtil.saveProject(settings, null);
		addSettingsToList(null, settings);
	}

	@FXML
	protected void onAddSetting() {
		if (listener != null) {
			listener.onClickAddSettings(projectListData);
		}
	}

	/**
	 * 校验并创建新的settingbean
	 * 
	 * @return
	 */
	private SettingsBean checkAndSave() {
		SettingsBean bean = null;
		if (StrUtil.isEmpty(textArea_width.getText()) || StrUtil.isEmpty(textArea_hight.getText())) {
			ToastUtil.toast(ResUtil.gs("setting_net_error"));
			return bean;
		}
		if (checkBox_preCheck.isSelected() && (int) group.getSelectedToggle().getUserData() == 0
				&& (StrUtil.isEmpty(textArea_flyHeight.getText()) || StrUtil.isEmpty(textArea_cameraSize.getText()))) {
			ToastUtil.toast(ResUtil.gs("setting_pre_check_error"));
			return bean;
		}
		if (checkBox_preCheck.isSelected() && (int) group.getSelectedToggle().getUserData() == 1
				&& StrUtil.isEmpty(textArea_gsd.getText())) {
			ToastUtil.toast(ResUtil.gs("setting_pre_check_error"));
			return bean;
		}

		int width, height;
		try {
			width = textArea_width.getValue();
			height = textArea_hight.getValue();
			if (width < 10 || height < 10) {
				ToastUtil.toast(ResUtil.gs("setting_net_size_error"));
				return bean;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return bean;
		}
		bean = new SettingsBean();
		bean.setSaveMiddle(checkBox_SaveMiddle.isSelected());
		bean.setNetWidth(textArea_width.getValue() + "");
		bean.setNetHeight(textArea_hight.getValue() + "");
		bean.setPreCheck(checkBox_preCheck.isSelected());
		bean.setPreCheckWay((int) group.getSelectedToggle().getUserData());
		bean.setGsd(textArea_gsd.getValue() + "");
		bean.setFlyHeight(textArea_flyHeight.getValue() + "");
		bean.setCameraSize(textArea_cameraSize.getValue() + "");
		bean.setSettingType(1);
		return bean;
	}

	@Override
	protected void onClickRightBtn() {
		boolean checkFinalData = checkFinalData();
		if (checkFinalData) {
			FinalDataBean finalDataBean = new FinalDataBean(projectListData);
			if (listener != null) {
				System.out.println(finalDataBean.toSettingParameter());
				listener.onClickStart(finalDataBean);
			}
		}
	}

	private boolean checkFinalData() {
		boolean isOk = true;
		for (ProjectBean project : projectListData) {
			if (project == null) {
				isOk = false;
				break;
			}
			if (project.getSettings() == null) {
				isOk = false;
				ToastUtil.toast(ResUtil.gs("project") + project.getProjectName() + ResUtil.gs("setting_has_no_set"));
				break;
			}
		}
		return isOk;
	}
}
