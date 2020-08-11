package application.control;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import beans.ProjectBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import utils.ResUtil;
import utils.SaveProjectsUtil;
import utils.SaveProjectsUtil.Callback;

/**
 * 创建项目dialog界面controller
 * 
 * @author DP
 */
public class OpenProjectDialogController implements Initializable {
	@FXML
	BorderPane root1;
	@FXML
	private JFXButton btnDone;
	@FXML
	private TableView<ProjectBean> projectTableView;

	private TableColumn<ProjectBean, String> name_projects;
	private TableColumn<ProjectBean, String> path_projects;
	private TableColumn<ProjectBean, String> time_createProject;
	private ObservableList<ProjectBean> projectListData = FXCollections.observableArrayList();

	private CallBack callBack;

	public void initData() {
		SaveProjectsUtil.getProjectsData((Stage) root1.getScene().getWindow(), new Callback() {
			@Override
			public void onGetData(ArrayList<ProjectBean> list) {
				projectListData.addAll(list);
			}
		});
		initTableView();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@SuppressWarnings("unchecked")
	private void initTableView() {
		name_projects = new TableColumn<ProjectBean, String>(ResUtil.gs("project_name_simple"));
		path_projects = new TableColumn<ProjectBean, String>(ResUtil.gs("project_path_simple"));
		time_createProject = new TableColumn<ProjectBean, String>(ResUtil.gs("project_create_time"));
		projectTableView.getColumns().addAll(name_projects, path_projects, time_createProject);
		projectTableView.setItems(projectListData);

		name_projects.setPrefWidth(80);
		path_projects.setPrefWidth(243);
		time_createProject.setPrefWidth(150);
		path_projects.setSortable(false);

		name_projects.setCellValueFactory(new PropertyValueFactory<ProjectBean, String>("projectName"));
		time_createProject.setCellValueFactory(new PropertyValueFactory<ProjectBean, String>("createTime"));
		path_projects.setCellValueFactory(new PropertyValueFactory<ProjectBean, String>("projectDir"));
		name_projects.setCellFactory(
				new javafx.util.Callback<TableColumn<ProjectBean, String>, TableCell<ProjectBean, String>>() {
					@Override
					public TableCell<ProjectBean, String> call(TableColumn<ProjectBean, String> param) {
						ToolTipTableCell<ProjectBean> toolTipTableCell = new ToolTipTableCell<ProjectBean>();
						return toolTipTableCell ;
					}
				});
		path_projects.setCellFactory(
				new javafx.util.Callback<TableColumn<ProjectBean, String>, TableCell<ProjectBean, String>>() {
					@Override
					public TableCell<ProjectBean, String> call(TableColumn<ProjectBean, String> param) {
						ToolTipTableCell<ProjectBean> toolTipTableCell = new ToolTipTableCell<ProjectBean>();
						return toolTipTableCell ;
					}
				});
		time_createProject.setCellFactory(
				new javafx.util.Callback<TableColumn<ProjectBean, String>, TableCell<ProjectBean, String>>() {
					@Override
					public TableCell<ProjectBean, String> call(TableColumn<ProjectBean, String> param) {
						ToolTipTableCell<ProjectBean> toolTipTableCell = new ToolTipTableCell<ProjectBean>();
						return toolTipTableCell ;
					}
				});
		projectTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				onTestMouse(event);
			}
		});
	}

	protected void onTestMouse(MouseEvent event) {
		if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
			done();
		}
	}

	@FXML
	public void done() {
		ProjectBean bean = (ProjectBean) projectTableView.getSelectionModel().getSelectedItem();
		if (bean == null) {
			return;
		}
		if (callBack != null) {
			SaveProjectsUtil.upDataProjectData(bean);
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
