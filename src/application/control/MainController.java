package application.control;

import java.net.URL;
import java.util.ResourceBundle;

import application.control.CreateProjectDialogController.CallBack;
import consts.ConstSize;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import utils.UIUtil;

/**
 * 主界面的controller
 * 
 * @author DP
 *
 */
public class MainController implements Initializable {
	@FXML
	private Pagination mPagination;
	@FXML
	BorderPane root;

	// 各个界面的controller，传递数据等操作，通过get()得到后，调用其中的方法。
	private CreateProjectController createProjectController;
	private ProjectsController projectsController;
	private SettingController settingController;
	private ProcessingController processingController;

	private final int MAX_PAGE_SIZE = 4;

	private BorderPane createProjPane, projectsPane, settingPane, ProcessingPane;
	private BorderPane currentPane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initPagination();
	}

	/**
	 * 初始化分页控件
	 */
	private void initPagination() {

		mPagination.setPageCount(MAX_PAGE_SIZE);
		mPagination.getStylesheets().add(getClass().getResource("/application/css/application.css").toExternalForm());
		mPagination.setPageFactory(new Callback<Integer, Node>() {
			@Override
			public Node call(Integer pageIndex) {
				if (pageIndex >= MAX_PAGE_SIZE) {
					return null;
				} else {
					try {
						return createPage(pageIndex);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
			}
		});
	}

	public MainController() {
	}

	/**
	 * createProject小界面的操作回调
	 * 
	 * @author DP
	 *
	 */
	private class CreateProjListener implements CreateProjectController.CreateProjectListener {
		@Override
		public void onCreateProject() {
			FXMLLoader openDialog = UIUtil.openDialog(getClass(), "/application/fxml/CreateProjectDialog.fxml",
					ConstSize.Dialog_Frame_Width, ConstSize.Dialog_Frame_Height, "创建项目", getStage());
			if (openDialog != null) {
				CreateProjectDialogController controller = openDialog.getController();
				controller.setCallBack(new CallBack() {
					public void onDone() {
						//弹出框点击了完成！
						System.out.println("接收到了完成！");
					}
				});
			}
		}
		
	}

	/**
	 * Projects小界面的操作回调
	 * 
	 * @author DP
	 *
	 */
	private class ProjectsListener implements ProjectsController.ProjectsListener {
	}

	/**
	 * Setting小界面的操作回调
	 * 
	 * @author DP
	 *
	 */
	private class SettingListener implements SettingController.SettingListener {
	}

	/**
	 * Processing小界面的操作回调
	 * 
	 * @author DP
	 *
	 */
	private class ProcessingListener implements ProcessingController.ProcessingListener {
	}

	/**
	 * 创建子界面
	 * 
	 * @param pageIndex
	 * @return
	 * @throws Exception
	 */
	protected Node createPage(Integer pageIndex) throws Exception {
		currentPane = null;
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
		URL location;
		switch (pageIndex.intValue()) {
		case 0:// 创建工程界面
			if (createProjPane == null) {
				location = getClass().getResource("/application/fxml/CreateProject.fxml");
				fxmlLoader.setLocation(location);
				createProjPane = (BorderPane) fxmlLoader.load(location.openStream());
				System.out.println("create:+" + pageIndex);
			}
			if (createProjectController == null) {
				createProjectController = fxmlLoader.getController();
				createProjectController.setListener(new CreateProjListener());
			}
			currentPane = createProjPane;
			System.out.println("donknow:+" + pageIndex);
			break;
		case 1:// 工程列表界面
			if (projectsPane == null) {
				location = getClass().getResource("/application/fxml/Projects.fxml");
				fxmlLoader.setLocation(location);
				projectsPane = (BorderPane) fxmlLoader.load(location.openStream());
			}
			if (projectsController == null) {
				projectsController = fxmlLoader.getController();
				projectsController.setListener(new ProjectsListener());
			}
			currentPane = projectsPane;
			System.out.println("donknow:+" + pageIndex);
			break;
		case 2:// 其他参数配置界面
			if (settingPane == null) {
				location = getClass().getResource("/application/fxml/Setting.fxml");
				fxmlLoader.setLocation(location);
				settingPane = (BorderPane) fxmlLoader.load(location.openStream());
			}
			if (settingController == null) {
				settingController = fxmlLoader.getController();
				settingController.setListener(new SettingListener());
			}
			currentPane = settingPane;
			System.out.println("donknow:+" + pageIndex);
			break;
		case 3:// 计算中、计算结果界面
			if (ProcessingPane == null) {
				location = getClass().getResource("/application/fxml/Processing.fxml");
				fxmlLoader.setLocation(location);
				ProcessingPane = (BorderPane) fxmlLoader.load(location.openStream());
			}
			if (processingController == null) {
				processingController = fxmlLoader.getController();
				processingController.setListener(new ProcessingListener());
			}
			currentPane = ProcessingPane;
			System.out.println("donknow:+" + pageIndex);
			break;
		default:
			break;
		}
		return currentPane;
	}

	@FXML
	public void prePage() {
		System.out.println("上一页");
		int nextIndex = mPagination.getCurrentPageIndex() - 1;
		if (nextIndex < 0) {
			nextIndex = 0;
		}
		mPagination.setCurrentPageIndex(nextIndex);
	}

	@FXML
	public void nextPage() {
		System.out.println("下一页");
		int nextIndex = mPagination.getCurrentPageIndex() + 1;
		if (nextIndex >= MAX_PAGE_SIZE) {
			nextIndex = 0;
		}
		mPagination.setCurrentPageIndex(nextIndex);
	}

	public CreateProjectController getCreateProjectController() {
		return createProjectController;
	}

	public ProjectsController getProjectsController() {
		return projectsController;
	}

	public SettingController getSettingController() {
		return settingController;
	}

	public ProcessingController getProcessingController() {
		return processingController;
	}

	/**
	 * 返回主界面框架的stage，可能为空
	 * 
	 * @return
	 */
	public Stage getStage() {
		Stage stage = null;
		if (root == null) {
			return stage;
		}
		try {
			stage = (Stage) root.getParent().getParent().getScene().getWindow();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stage;
	}
}
