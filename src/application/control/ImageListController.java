package application.control;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXRadioButton;

import beans.ImageBean;
import beans.ProjectBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.FileUtil;
import utils.ProgressTask.ProgressTask;
import javafx.scene.layout.BorderPane;

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

	@Override
	public void initialize(URL location, ResourceBundle resources) {

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
		ProgressTask task = new ProgressTask(new Task<Integer>() {
			@Override
			protected void succeeded() {
				super.succeeded();

				// TODO 更新列表
			}

			@Override
			protected Integer call() throws Exception {
				File file = new File(project.getProjectDir());
				if (file != null && file.exists()) {
					File[] itemFiles = file.listFiles();
					for (File item : itemFiles) {
						if (!item.isDirectory() && FileUtil.isImage(item)) {
							// 不是文件夹，并且是图片
							// TODO ,解析图片经纬度等。
							listData.add(new ImageBean(item.getAbsolutePath(), item.getName()));
						}
					}
				}
				return null;
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
