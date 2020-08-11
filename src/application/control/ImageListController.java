package application.control;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.drew.imaging.ImageProcessingException;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import base.controller.ConfirmDialogController.CallBack;
import beans.ImageBean;
import beans.MyFxmlBean;
import beans.ProjectBean;
import consts.ConstSize;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.FileChooserUtil;
import utils.FileUtil;
import utils.GpsUtil;
import utils.ImageUtil;
import utils.ResUtil;
import utils.SaveProjectsUtil;
import utils.ToastUtil;
import utils.UIUtil;
import utils.ProgressTask.ProgressTask;
import views.MyToolTip;

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
	@FXML
	JFXTextField textField_projectName;

	private ObservableList<ImageBean> listData = FXCollections.observableArrayList();
	@FXML
	TableView<ImageBean> tableView;
	private ToggleGroup group;

	private ProjectBean project;
	private ProgressTask task;
	private TableColumn<ImageBean, String> longtitudeCol;
	private TableColumn<ImageBean, String> latitudeCol;
	private TableColumn<ImageBean, String> heightCol;
	@FXML
	ImageView imageview;

	private ArrayList<HashMap<String, String>> analysingGps;

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

		Node close = root.getParent().lookup("#close");
		close.addEventFilter(MouseDragEvent.MOUSE_PRESSED, new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				int locationFrom = project.getLocationFrom();
				if (locationFrom == 1) {
					String locationPath = project.getProjectLocationFile();
					File locationFile = new File(locationPath);
					if (locationPath == null || "".equals(locationPath) || locationFile == null
							|| !locationFile.exists()) {
						ToastUtil.toast(ResUtil.gs("input_error_location_path"));
						event.consume();
						return;
					}
				}
				project.setProjectName(textField_projectName.getText());
				SaveProjectsUtil.changeProjectData(project, null);
				if (callBack != null) {
					callBack.onProjectChange(project);
				}
			}
		});
	}

	/**
	 * 解析location文件
	 * 
	 * @param proj
	 */
	private void analysingGps(ProjectBean proj) {
		if (proj != null) {
			analysingGps = GpsUtil.analysingGps(proj);
		}
	}

	/**
	 * 刷新图片数据。
	 */
	private void refreshListData() {
		try {
			listData.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
		task = new ProgressTask(new ProgressTask.MyTask<Integer>() {
			@Override
			protected void succeeded() {
				super.succeeded();
			}

			@Override
			protected Integer call() {
				File file = new File(project.getProjectDir());
				if (file != null && file.exists()) {
					ArrayList<ImageBean> processList = new ArrayList<ImageBean>();
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
							processList.add(imageBean);
						}
					}
					if (project.getLocationFrom() == 1) {
						analysingGps(project);
						processList = setImageDataFromFile(processList);
					}
					listData.addAll(processList);
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
				labelLocation.setText(project.getProjectLocationFile());
				labelLocation.setTooltip(new MyToolTip(project.getProjectLocationFile()));
			} else {
				// 文件读入经纬度
				group.selectToggle(radioButton_file);
				hbox_location.setDisable(false);
				labelLocation.setText(project.getProjectLocationFile());
				labelLocation.setTooltip(new MyToolTip(project.getProjectLocationFile()));
			}

			textField_projectName.setText(project.getProjectName());
		}
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
				}
			}
		});
	}

	@FXML
	public void onClickSelectLocation() {
		FileChooserUtil.OpenFileChooserUtil(ResUtil.gs("choose_location_file"), labelLocation,
				new FileChooserUtil.Callback() {
					@Override
					public void onResult(boolean isChoose, File file) {
						if (isChoose) {
							String path = file.getAbsolutePath();
							path = path.replaceAll("\\\\", "/");
							labelLocation.setText(path);
							labelLocation.setTooltip(new MyToolTip(file.getAbsolutePath()));
							if (project != null) {
								project.setLocationFrom(1);
								project.setProjectLocationFile(path);
								refreshListData();
							}
						}
					}
				});
	}

	@FXML
	public void onClickHelp() {
		UIUtil.openNoticeDialog(getClass(), ConstSize.Notice_Dialog_Frame_Width, ConstSize.Notice_Dialog_Frame_Height,
				ResUtil.gs("tips"), ResUtil.gs("Text_LocationFile_Notice"), (Stage) root.getScene().getWindow());
	}

	@FXML
	public void onDeleteImg() {
		ImageBean selectedItem = tableView.getSelectionModel().getSelectedItem();
		if (selectedItem == null) {
			return;
		}
		UIUtil.openConfirmDialog(getClass(), ConstSize.Confirm_Dialog_Frame_Width,
				ConstSize.Confirm_Dialog_Frame_Height, ResUtil.gs("imageList_remove_image"),
				ResUtil.gs("imageList_remove_image_confirm", selectedItem.getName()),
				(Stage) root.getScene().getWindow(), new CallBack() {
					@Override
					public void onCancel() {
					}

					@Override
					public void onConfirm() {
						listData.remove(selectedItem);
						FileUtil.deleteImage(selectedItem.getPath());
						FileUtil.deleteTxt(project.getProjectDir());
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
			ToastUtil.toast(ResUtil.gs("open_image_error"));
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void initTableView() {
		TableColumn<ImageBean, String> path = new TableColumn<ImageBean, String>(ResUtil.gs("imageList_image_name"));
		longtitudeCol = new TableColumn<ImageBean, String>(ResUtil.gs("imageList_image_long"));
		latitudeCol = new TableColumn<ImageBean, String>(ResUtil.gs("imageList_image_lat"));
		heightCol = new TableColumn<ImageBean, String>(ResUtil.gs("imageList_image_height"));
		tableView.getColumns().addAll(path, latitudeCol, longtitudeCol, heightCol);
		path.setPrefWidth(120);
		path.setMinWidth(120);
		longtitudeCol.setPrefWidth(110);
		latitudeCol.setPrefWidth(110);
		heightCol.setPrefWidth(100);

		path.setSortable(false);
		longtitudeCol.setSortable(false);
		latitudeCol.setSortable(false);
		heightCol.setSortable(false);
		path.setCellValueFactory(new PropertyValueFactory<ImageBean, String>("name"));
		path.setCellFactory(new javafx.util.Callback<TableColumn<ImageBean, String>, TableCell<ImageBean, String>>() {
			@Override
			public TableCell<ImageBean, String> call(TableColumn<ImageBean, String> param) {
				return new ToolTipTableCell<ImageBean>();
			}
		});
		longtitudeCol.setCellValueFactory(
				new javafx.util.Callback<TableColumn.CellDataFeatures<ImageBean, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<ImageBean, String> arg0) {
						SimpleStringProperty re = null;
						try {
							re = new SimpleStringProperty();
							String set;
							if ("".equals(arg0.getValue().getLongitudeRef())) {
								set = arg0.getValue().getLongitude() + "";
							} else {
								set = arg0.getValue().getLongitudeRef() + ":" + arg0.getValue().getLongitude();
							}
							re.set(set);
						} catch (Exception e) {
							re.set("");
						}
						return re;
					}
				});
		longtitudeCol.setCellFactory(
				new javafx.util.Callback<TableColumn<ImageBean, String>, TableCell<ImageBean, String>>() {
					@Override
					public TableCell<ImageBean, String> call(TableColumn<ImageBean, String> param) {
						return new ToolTipTableCell<ImageBean>();
					}
				});
		latitudeCol.setCellValueFactory(
				new javafx.util.Callback<TableColumn.CellDataFeatures<ImageBean, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<ImageBean, String> arg0) {
						SimpleStringProperty re = null;
						try {
							re = new SimpleStringProperty();
							if ("".equals(arg0.getValue().getLatitudeRef())) {
								re.set(arg0.getValue().getLatitude() + "");
							} else {
								re.set(arg0.getValue().getLatitudeRef() + ":" + arg0.getValue().getLatitude());
							}
						} catch (Exception e) {
							re.set("");
						}
						return re;
					}
				});
		latitudeCol.setCellFactory(
				new javafx.util.Callback<TableColumn<ImageBean, String>, TableCell<ImageBean, String>>() {
					@Override
					public TableCell<ImageBean, String> call(TableColumn<ImageBean, String> param) {
						return new ToolTipTableCell<ImageBean>();
					}
				});
		heightCol.setCellValueFactory(new PropertyValueFactory<ImageBean, String>("height"));
		heightCol.setCellFactory(
				new javafx.util.Callback<TableColumn<ImageBean, String>, TableCell<ImageBean, String>>() {
					@Override
					public TableCell<ImageBean, String> call(TableColumn<ImageBean, String> param) {
						return new ToolTipTableCell<ImageBean>();
					}
				});
		tableView.setItems(listData);

		tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ImageBean>() {
			@Override
			public void changed(ObservableValue<? extends ImageBean> observable, ImageBean oldValue,
					ImageBean newValue) {
				if (newValue != null) {
					vbox_rightButtons.setDisable(false);
					initImageView(newValue);
				} else {
					vbox_rightButtons.setDisable(true);
				}
			}
		});
		tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				onTestMouse(event);
			}
		});
	}

	/**
	 * 初始化图片显示
	 * 
	 * @param newValue
	 */
	private void initImageView(ImageBean newValue) {
		String path = "file:" + newValue.getPath();
		Image image = new Image(path);
		imageview.setImage(image);
		imageview.setFitWidth(140);
		imageview.setSmooth(false);
		imageview.setCache(false);
	}

	/**
	 * 列表双击事件
	 * 
	 * @param event
	 */
	protected void onTestMouse(MouseEvent event) {
		if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
			onSeeImg();
		}
	}

	private void initRadioView() {
		group = new ToggleGroup();
		radioButton_img.setToggleGroup(group);
		radioButton_img.setUserData(0);
		radioButton_file.setToggleGroup(group);
		radioButton_file.setUserData(1);
	}

	/**
	 * 解析图片数据
	 * 
	 * @param listData2
	 */
	private ArrayList<ImageBean> setImageDataFromFile(ArrayList<ImageBean> listData2) {
		return listData2 = GpsUtil.setImageDataFromFile(analysingGps, listData2);
	}

	@FXML
	public void onSeeLine() {
//		MyFxmlBean openFrame = UIUtil.openFrame(getClass(), "/application/fxml/FlightLine.fxml",
//				ConstSize.Second_Frame_Width, ConstSize.Second_Frame_Height, project.getProjectName() + "飞行路径");
//		FlightLineController controller = openFrame.getFxmlLoader().getController();
//		controller.setData(listData);

		MyFxmlBean openFrame = UIUtil.openFrame(getClass(), "/application/fxml/GoogleMapFlightLine.fxml",
				ConstSize.Flight_Width, ConstSize.Flight_Height,
				project.getProjectName() + " " + ResUtil.gs("imageList_flight"));
		GoogleMapFlightLineController controller = openFrame.getFxmlLoader().getController();
		controller.setData(listData);
	}

	private Callback callBack;

	public void setCallBack(Callback callBack) {
		this.callBack = callBack;
	}

	public interface Callback {
		void onProjectChange(ProjectBean project);
	}

}
