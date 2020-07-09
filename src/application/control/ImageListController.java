package application.control;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.drew.imaging.ImageProcessingException;
import com.jfoenix.controls.JFXRadioButton;

import base.controller.ConfirmDialogController.CallBack;
import beans.ImageBean;
import beans.ProjectBean;
import consts.ConstRes;
import consts.ConstSize;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.FileChooserUtil;
import utils.FileChooserUtil.Callback;
import utils.FileUtil;
import utils.ImageUtil;
import utils.ToastUtil;
import utils.UIUtil;
import utils.ProgressTask.ProgressTask;

/**
 * 点击项目进入，图片列表界面controller
 * 
 * @author DP
 *
 */
public class ImageListController implements Initializable {

	@FXML
	HBox hbox_location;
	@FXML
	JFXRadioButton radioButton_file;
	@FXML
	JFXRadioButton radioButton_img;
	@FXML
	Label labelLocation;
	@FXML
	VBox vbox_rightButtons;
	@FXML
	BorderPane root;

	private ObservableList<ImageBean> listData = FXCollections.observableArrayList();
	@FXML
	TableView<ImageBean> tableView;
	private ToggleGroup group;

	private ProjectBean project;
	private ProgressTask task;
	private TableColumn<ImageBean, Double> longtitudeCol;
	private TableColumn<ImageBean, Double> latitudeCol;
	private TableColumn<ImageBean, String> heightCol;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initTableView();
		initRadioView();
	}

	/**
	 * 获取到controller后，调用此方法来初始化显示数据。
	 * 
	 * @param project
	 */
	public void setProjectInfo(ProjectBean project) {
		if (project == null) {
			return;
		}
		this.project = project;
		initDataView();
		refreshListData();
		refreshTabViewColumn();
	}

	/**
	 * 刷新图片数据。 TODO 读取文件数据
	 */
	private void refreshListData() {
		try {
			listData.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
		task = new ProgressTask(new ProgressTask.MyTask() {
			@Override
			protected void succeeded() {
				super.succeeded();
			}

			@Override
			protected Integer call() {
				File file = new File(project.getProjectDir());
				if (file != null && file.exists()) {
					File[] itemFiles = file.listFiles();
					for (File item : itemFiles) {
						if (!item.isDirectory() && FileUtil.isImage(item)) {
							// 不是文件夹，并且是图片
							ImageBean imageBean = new ImageBean(item.getAbsolutePath(), item.getName());
							if (project.getLocationFrom() == 0) {
								// 从图片中读取经纬度才解析图片数据
								try {
									ImageUtil.printImageTags(item.getAbsolutePath(), imageBean);
								} catch (ImageProcessingException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							listData.add(imageBean);
						}
					}
					if (project.getLocationFrom() == 1) {
						setImageDataFromFile();
					}
				}
				return 1;
			}
		}, (Stage) root.getScene().getWindow());
		task.start();
	}

	private void initDataView() {
		if (project != null) {
			int from = project.getLocationFrom();
			if (from == 0) {
				// 图片读入经纬度
				group.selectToggle(radioButton_img);
			} else {
				// 文件读入经纬度
				group.selectToggle(radioButton_file);
				labelLocation.setText(project.getProjectLocationFile());
			}
		}
	}

	@FXML
	public void onClickSelectLocation() {
		FileChooserUtil.OpenFileChooserUtil("选择经纬度文件", labelLocation, new Callback() {
			@Override
			public void onResult(boolean isChoose, File file) {
				if (isChoose) {
					labelLocation.setText(file.getAbsolutePath());
					if (project != null) {
						project.setProjectLocationFile(file.getAbsolutePath());
					}
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
	public void onDeleteImg() {
		ImageBean selectedItem = tableView.getSelectionModel().getSelectedItem();
		if (selectedItem == null) {
			return;
		}
		UIUtil.openConfirmDialog(getClass(), ConstSize.Confirm_Dialog_Frame_Width,
				ConstSize.Confirm_Dialog_Frame_Height, "移除图片",
				"确认将图片:" + selectedItem.getName() + "移出项目?(将移动图片至" + "\"项目路径下/DELETED_IMAGES\"文件夹中)",
				(Stage) root.getScene().getWindow(), new CallBack() {
					@Override
					public void onCancel() {
					}

					@Override
					public void onConfirm() {
						listData.remove(selectedItem);
						FileUtil.deleteImage(selectedItem.getPath());
					}
				});

	}

	@FXML
	public void onSeeImg() {
		ImageBean selectedItem = tableView.getSelectionModel().getSelectedItem();
		if (selectedItem == null) {
			return;
		}
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec("cmd /c " + selectedItem.getPath());
		} catch (IOException e) {
			ToastUtil.toast("打开图片失败！");
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void initTableView() {
		TableColumn<ImageBean, String> path = new TableColumn<ImageBean, String>("名称");
		longtitudeCol = new TableColumn<ImageBean, Double>("经度");
		latitudeCol = new TableColumn<ImageBean, Double>("纬度");
		heightCol = new TableColumn<ImageBean, String>("高度");
		tableView.getColumns().addAll(path, latitudeCol, longtitudeCol, heightCol);
		path.setPrefWidth(120);
		path.setMinWidth(120);
		longtitudeCol.setPrefWidth(100);
		longtitudeCol.setMinWidth(100);
		latitudeCol.setPrefWidth(100);
		latitudeCol.setMinWidth(100);
		heightCol.setPrefWidth(100);
		heightCol.setMinWidth(100);

		path.setSortable(false);
		longtitudeCol.setSortable(false);
		latitudeCol.setSortable(false);
		heightCol.setSortable(false);
		path.setCellValueFactory(new PropertyValueFactory<ImageBean, String>("name"));
		longtitudeCol.setCellValueFactory(new PropertyValueFactory<ImageBean, Double>("longitude"));
		latitudeCol.setCellValueFactory(new PropertyValueFactory<ImageBean, Double>("latitude"));
		heightCol.setCellValueFactory(new PropertyValueFactory<ImageBean, String>("height"));
		tableView.setItems(listData);

		tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ImageBean>() {
			@Override
			public void changed(ObservableValue<? extends ImageBean> observable, ImageBean oldValue,
					ImageBean newValue) {
				if (newValue != null) {
					vbox_rightButtons.setDisable(false);
				} else {
					vbox_rightButtons.setDisable(true);
				}
			}
		});
		refreshTabViewColumn();
	}

	/**
	 * 从图片中读取才显示经纬度信息。从文件中读取不显示。 TODO 读取文件经纬度功能。
	 */
	private void refreshTabViewColumn() {
		if (project != null) {
			int locationFrom = project.getLocationFrom();
			if (locationFrom == 0) {
				longtitudeCol.setVisible(true);
				latitudeCol.setVisible(true);
				heightCol.setVisible(true);
			} else {
				longtitudeCol.setVisible(false);
				latitudeCol.setVisible(false);
				heightCol.setVisible(false);
			}
		}
	}

	private void initRadioView() {
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
						if (project != null) {
							project.setLocationFrom(0);
						}
					} else {
						// 选择文件录入
						hbox_location.setDisable(false);
						if (project != null) {
							project.setLocationFrom(1);
						}
					}
					refreshListData();
					refreshTabViewColumn();
				}
			}
		});
	}

	/**
	 * 解析图片数据 TODO:文件经纬度解析
	 */
	private void setImageDataFromFile() {
		// TODO 文件中读取。先不做
	}
}
