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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import utils.ResUtil;
import utils.SaveProjectsUtil;
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
	private TableColumn<ProjectBean, String> location_path_projects;
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
	ImageView imageView = new ImageView(new Image("/resources/wushuju.png"));

	/**
	 * 添加项目
	 */
	public void addProject(ProjectBean project) {
		if (!checkDuplicates(project)) {
			project.setInfo(0);
			projectListData.add(project);
			String bottomtext = ResUtil.gs("projectList_num", projectListData.size() + "");
			bottomLabel.setText(bottomtext);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initTableView();
		 initDragFile() ;
		projectTableView.setRowFactory(new Callback<TableView<ProjectBean>, TableRow<ProjectBean>>() {
			@Override
			public TableRow<ProjectBean> call(TableView<ProjectBean> param) {
				{
					TableRow<ProjectBean> row = new TableRow<ProjectBean>();
					row.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {
							if (event.getClickCount() == 2 && (!row.isEmpty())) {
								onDetailProject();
							}
						}
					});
					return row;
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void initTableView() {
//		projectTableView.setEditable(true);// 表格设置为可编辑
		name_projects = new TableColumn<ProjectBean, String>(ResUtil.gs("project_name_simple"));
//		name_projects.setEditable(true);
//		name_projects.setCellFactory(TextFieldTableCell.forTableColumn());

		path_projects = new TableColumn<ProjectBean, String>(ResUtil.gs("project_path_simple"));
		location_path_projects = new TableColumn<ProjectBean, String>(ResUtil.gs("locationpath"));
		time_createProject = new TableColumn<ProjectBean, String>(ResUtil.gs("project_create_time"));
		projectTableView.getColumns().addAll(name_projects, path_projects, time_createProject,location_path_projects);
		projectTableView.setItems(projectListData);
		
		name_projects.setPrefWidth(100);
		path_projects.setPrefWidth(310);
		location_path_projects.setPrefWidth(145);
		time_createProject.setPrefWidth(140);
		path_projects.setSortable(false);

		name_projects.setCellValueFactory(new PropertyValueFactory<ProjectBean, String>("projectName"));
		location_path_projects.setCellValueFactory(new PropertyValueFactory<ProjectBean, String>("projectLocationFile"));
		time_createProject.setCellValueFactory(new PropertyValueFactory<ProjectBean, String>("createTime"));
		path_projects.setCellValueFactory(new PropertyValueFactory<ProjectBean, String>("projectDir"));
		time_createProject
				.setCellFactory(new Callback<TableColumn<ProjectBean, String>, TableCell<ProjectBean, String>>() {
					@Override
					public TableCell<ProjectBean, String> call(TableColumn<ProjectBean, String> param) {
						return new ToolTipTableCell<ProjectBean>();
					}
				});
		name_projects.setCellFactory(new Callback<TableColumn<ProjectBean, String>, TableCell<ProjectBean, String>>() {
			@Override
			public TableCell<ProjectBean, String> call(TableColumn<ProjectBean, String> param) {
				return new ToolTipTableCell<ProjectBean>();
			}
		});
		location_path_projects.setCellFactory(new Callback<TableColumn<ProjectBean, String>, TableCell<ProjectBean, String>>() {
			@Override
			public TableCell<ProjectBean, String> call(TableColumn<ProjectBean, String> param) {
				return new ToolTipTableCell<ProjectBean>();
			}
		});
		path_projects.setCellFactory(new Callback<TableColumn<ProjectBean, String>, TableCell<ProjectBean, String>>() {
			@Override
			public TableCell<ProjectBean, String> call(TableColumn<ProjectBean, String> param) {
				return new ToolTipTableCell<ProjectBean>();
			}
		});

		name_projects.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ProjectBean, String>>() {
			@Override
			public void handle(CellEditEvent<ProjectBean, String> event) {
				ProjectBean bean = ((ProjectBean) event.getTableView().getItems()
						.get(event.getTablePosition().getRow()));
				bean.setProjectName(event.getNewValue());
				SaveProjectsUtil.changeProjectData(bean, null);
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
		projectTableView.setPlaceholder(imageView);
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
		if (index < 0 || index >= projectListData.size()) {
			return;
		}
		ProjectBean project = projectListData.get(index);
		MyFxmlBean openFrame = UIUtil.openFrame(getClass(), "/application/fxml/ImageList.fxml",
				ConstSize.Main_Frame_Width-80, ConstSize.Main_Frame_Height,
				ResUtil.gs("project") + project.getProjectName());
		ImageListController controller = openFrame.getFxmlLoader().getController();
		controller.setProjectInfo(project);
		controller.setCallBack(new ImageListController.Callback() {
			@Override
			public void onProjectChange(ProjectBean project) {
				projectListData.set(index, project);
			}
		});
	}

	@FXML
	void onRemove() {
		ProjectBean selectedItem = projectTableView.getSelectionModel().getSelectedItem();
		if (selectedItem == null) {
			return;
		}
		UIUtil.openConfirmDialog(getClass(), ConstSize.Confirm_Dialog_Frame_Width,
				ConstSize.Confirm_Dialog_Frame_Height, ResUtil.gs("projectList_remove"),
				ResUtil.gs("projectList_remove_confirm", selectedItem.getProjectName()),
				(Stage) root.getScene().getWindow(), new CallBack() {
					@Override
					public void onCancel() {
					}

					@Override
					public void onConfirm() {
						projectListData.remove(selectedItem);
						String bottomtext = ResUtil.gs("projectList_num", projectListData.size() + "");
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

		void onOpenProject(String absolutePath);

	}

	@Override
	protected void onSetBottomBtnsAndTitle() {
		leftBtn.setVisible(false);
		rightBtn.setVisible(true);
		rightBtn.setText(ResUtil.gs("next_step"));
		title.setText(ResUtil.gs("project"));
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

	/**
	 * 拖拽文件夹打开创建项目功能
	 */
	private void initDragFile() {
		root.addEventHandler(DragEvent.DRAG_OVER, new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				if (event.getGestureSource() != root && event.getDragboard().hasFiles()) {
					event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
				}
				event.consume();
			}
		});
		root.addEventHandler(DragEvent.DRAG_DROPPED, new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard();
				boolean success = false;
				if (db.hasFiles()) {
					if (listener != null && db.getFiles().size() >= 0) {
						listener.onOpenProject(db.getFiles().get(0).getAbsolutePath());
					}
					success = true;
				}
				event.setDropCompleted(success);
				event.consume();
			}
		});
	}
	
}
