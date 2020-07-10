package application.control;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import base.controller.ConfirmDialogController.CallBack;
import beans.MyFxmlBean;
import beans.ProjectBean;
import consts.ConstSize;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.SaveUtil;
import utils.UIUtil;

/**
 * 项目列表界面controller
 * 
 * @author DP
 *
 */
public class ProjectsController extends BaseController implements Initializable {
	private ProjectsListener listener;
	@FXML
	Label Label;
	@FXML
	private TableView<ProjectBean> projectTableView;
	private TableColumn<ProjectBean, String> name_projects;
	private TableColumn<ProjectBean, String> path_projects;
	private TableColumn<ProjectBean, String> time_createProject;
	@FXML
	VBox Rvbox;
	@FXML
	private JFXButton addProject;
	@FXML
	private JFXButton seeProject;
	@FXML
	private JFXButton removeProject;

	@FXML
	private Label bottomLabel;

	private ObservableList<ProjectBean> projectListData = FXCollections.observableArrayList();
	@FXML
	BorderPane root;

	/**
	 * 添加项目
	 */
	public void addProject(ProjectBean project) {
		if (!checkDuplicates(project)) {
			projectListData.add(project);
			String bottomtext = "共有" + projectListData.size() + "个项目";
			bottomLabel.setText(bottomtext);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initTableView();
		projectTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				onTestMouse(event);
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void initTableView() {
		projectTableView.setEditable(true);// 表格设置为可编辑
		name_projects = new TableColumn<ProjectBean, String>("工程名称");
		name_projects.setEditable(true);
		name_projects.setCellFactory(TextFieldTableCell.forTableColumn());

		path_projects = new TableColumn<ProjectBean, String>("路径");
		time_createProject = new TableColumn<ProjectBean, String>("创建时间");
		projectTableView.getColumns().addAll(name_projects, path_projects, time_createProject);
		projectTableView.setItems(projectListData);

		name_projects.setPrefWidth(150);
		path_projects.setPrefWidth(270);
		time_createProject.setPrefWidth(120);
		path_projects.setSortable(false);

		name_projects.setCellValueFactory(new PropertyValueFactory<ProjectBean, String>("projectName"));
		time_createProject.setCellValueFactory(new PropertyValueFactory<ProjectBean, String>("createTime"));
		path_projects.setCellValueFactory(new PropertyValueFactory<ProjectBean, String>("projectDir"));

		name_projects.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ProjectBean, String>>() {
			@Override
			public void handle(CellEditEvent<ProjectBean, String> event) {
				ProjectBean bean = ((ProjectBean) event.getTableView().getItems()
						.get(event.getTablePosition().getRow()));
				bean.setProjectName(event.getNewValue());
				SaveUtil.changeProjectData(bean);
			}
		});

		projectTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ProjectBean>() {
			@Override
			public void changed(ObservableValue<? extends ProjectBean> observable, ProjectBean oldValue,
					ProjectBean newValue) {
				if (newValue != null) {
					Rvbox.setDisable(false);
				} else {
					Rvbox.setDisable(true);
				}
			}
		});
	}

	// 列表双击事件
	protected void onTestMouse(MouseEvent event) {
		if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
			onDetailProject();
		}
	}

	// 添加工程的事件响应
	@FXML
	void createProject() {
		if (listener != null)
			listener.onCreateProject();
	}

	// 查看工程的事件响应
	@FXML
	void onDetailProject() {
		int index = projectTableView.getSelectionModel().getSelectedIndex();
		ProjectBean project = projectListData.get(index);
		MyFxmlBean openFrame = UIUtil.openFrame(getClass(), "/application/fxml/ImageList.fxml",
				ConstSize.Second_Frame_Width, ConstSize.Second_Frame_Height, "项目" + project.getProjectName());
		ImageListController controller = openFrame.getFxmlLoader().getController();
		controller.setProjectInfo(project);
	}

	@FXML
	void onRemove() {
		ProjectBean selectedItem = projectTableView.getSelectionModel().getSelectedItem();
		if (selectedItem == null) {
			return;
		}
		UIUtil.openConfirmDialog(getClass(), ConstSize.Confirm_Dialog_Frame_Width,
				ConstSize.Confirm_Dialog_Frame_Height, "移除项目", "确认将项目:" + selectedItem.getProjectName() + "移出?",
				(Stage) root.getScene().getWindow(), new CallBack() {
					@Override
					public void onCancel() {
					}

					@Override
					public void onConfirm() {
						projectListData.remove(selectedItem);
						String bottomtext = "共有" + projectListData.size() + "个项目";
						bottomLabel.setText(bottomtext);
					}
				});
	}

	public void setListener(ProjectsListener listener) {
		this.listener = listener;
	}

	public interface ProjectsListener {
		void onCreateProject();

		void onOpenProject();

		/**
		 * 点击下一步按钮
		 */
		void onClickRightBtn(ObservableList<ProjectBean> projectListData);

	}

	@Override
	protected void onSetBottomBtnsAndTitle() {
		leftBtn.setVisible(false);
		rightBtn.setVisible(true);
		rightBtn.setText("下一步");
		title.setText("项目");
	}

	@Override
	protected void onClickRightBtn() {
		if (listener != null) {
			listener.onClickRightBtn(projectListData);
		}
	}

	@Override
	protected void onClickLeftBtn() {
	}

	public void clearData() {
		projectListData.clear();
	}

	/**
	 * 打开项目
	 */
	@FXML
	public void openProject() {
		if (listener != null) {
			listener.onOpenProject();
		}
	}

	/**
	 * 防止多次加入同一项目
	 * 
	 * @param project
	 * @return
	 */
	private boolean checkDuplicates(ProjectBean project) {
		for (ProjectBean item : projectListData) {
			if (item.getId() == project.getId()) {
				return true;
			}
		}
		return false;
	}
}
