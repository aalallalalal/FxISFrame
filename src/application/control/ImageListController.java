package application.control;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.drew.imaging.ImageProcessingException;
import com.jfoenix.controls.JFXRadioButton;

import beans.ImageBean;
import beans.ProjectBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.FileUtil;
import utils.ImageUtil;
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

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		TableColumn<ImageBean, String> path = new TableColumn<ImageBean, String>("名称");
		TableColumn<ImageBean, Double> longtitude = new TableColumn<ImageBean, Double>("经度");
		TableColumn<ImageBean, Double> latitude = new TableColumn<ImageBean, Double>("纬度");
		TableColumn<ImageBean, String> height = new TableColumn<ImageBean, String>("高度");
		tableView.getColumns().addAll(path, latitude, longtitude, height);
		path.setPrefWidth(120);
		path.setSortable(false);
		longtitude.setSortable(false);
		latitude.setSortable(false);
		height.setSortable(false);
		path.setCellValueFactory(new PropertyValueFactory<ImageBean, String>("name"));
		longtitude.setCellValueFactory(new PropertyValueFactory<ImageBean, Double>("longitude"));
		latitude.setCellValueFactory(new PropertyValueFactory<ImageBean, Double>("latitude"));
		height.setCellValueFactory(new PropertyValueFactory<ImageBean, String>("height"));
		tableView.setItems(listData);
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
		ProgressTask task = new ProgressTask(new ProgressTask.MyTask() {
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
							try {
								ImageUtil.printImageTags(item, imageBean);
							} catch (ImageProcessingException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
							listData.add(imageBean);
						} else {
						}
					}
				}
				return 1;
			}
		}, (Stage) root.getScene().getWindow());
		task.start();
	}

	@FXML
	public void onClickSelectLocation() {
	}

	@FXML
	public void onClickHelp() {
	}

	@FXML
	public void onDeleteImg() {
	}

	@FXML
	public void onSeeImg() {
	}

}
