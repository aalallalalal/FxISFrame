package application.control;

import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import beans.ImageBean;
import beans.MyFxmlBean;
import beans.ProjectBean;
import consts.ConstSize;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
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
	VBox Lvbox = new VBox();
	@FXML
	Label Label;
	@FXML
	private TableView<ProjectBean> projectTableView;
	private TableColumn<ProjectBean, String> name_projects;
	private TableColumn<ProjectBean, String> path_projects;
	private TableColumn<ProjectBean, String> time_createProject;
	@FXML
	VBox Rvbox = new VBox();
	@FXML
	private JFXButton addProject;
	@FXML
	private JFXButton seeProject;
	@FXML
	private JFXButton removeProject;

	@FXML
	private Label bottomLabel;

	private ObservableList<ProjectBean> projectListData = FXCollections.observableArrayList();

	public void addProject(ProjectBean project) {
		projectListData.add(project);
		String bottomtext = "共有" + projectListData.size() + "个项目";
		bottomLabel.setText(bottomtext);
		Iterator<ProjectBean> iter = projectListData.iterator();
		while (iter.hasNext()) {

			System.out.println("列表控件刷新：" + iter.next());
		}

		// TODO 刷新项目列表控件，显示出来
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initTableView();

		projectTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				onTestMouse(event);
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void initTableView()
	{
		// TODO Auto-generated method stub
		name_projects = new TableColumn<ProjectBean, String>("工程名称");
		path_projects = new TableColumn<ProjectBean, String>("路径");
		time_createProject = new TableColumn<ProjectBean, String>("创建时间");
		projectTableView.getColumns().addAll(name_projects, path_projects, time_createProject);
		projectTableView.setItems(projectListData);
		
		name_projects.setPrefWidth(150);
		path_projects.setPrefWidth(250);
		time_createProject.setPrefWidth(140);
		path_projects.setSortable(false);
		
		name_projects.setCellValueFactory(new PropertyValueFactory<ProjectBean, String>("projectName"));
		time_createProject.setCellValueFactory(new PropertyValueFactory<ProjectBean, String>("createTime"));
		path_projects.setCellValueFactory(new PropertyValueFactory<ProjectBean, String>("projectDir"));
		
		
		
	}

	public void test() {
		System.out.println("ProjectsController来自其他controller的调用");
	}

	// 列表双击事件
	protected void onTestMouse(MouseEvent event) {
		// TODO Auto-generated method stub
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
		int index = projectTableView.getSelectionModel().getSelectedIndex();
		projectListData.remove(index);
		String bottomtext = "共有" + projectListData.size() + "个项目";
		bottomLabel.setText(bottomtext);

	}

	public void setListener(ProjectsListener listener) {
		this.listener = listener;
	}

	public interface ProjectsListener {
		void onCreateProject();

		/**
		 * 点击下一步按钮
		 */
		void onClickRightBtn(ObservableList<ProjectBean> projectListData);

	}

	// 单元格显示内容
	private class MyListCell extends ListCell<ProjectBean> {

		@Override
		protected void updateItem(ProjectBean item, boolean empty) {
			// TODO Auto-generated method stub
			super.updateItem(item, empty);

			if (item == null) {
				this.setText("");
			} else {
				this.setText(item.getProjectName());
			}
		}

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

	public void clearData()
	{
		// TODO Auto-generated method stub
		
	}

}
