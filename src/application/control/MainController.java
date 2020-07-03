package application.control;

import java.net.URL;
import java.util.ResourceBundle;

import application.control.CreateProjectDialogController.CallBack;
import beans.MyFxmlBean;
import beans.ProjectBean;
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
import javafx.scene.layout.FlowPane;
import com.jfoenix.controls.JFXButton;

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
	@FXML
	FlowPane bottomGroupPane;
	@FXML
	BorderPane bottomBtnsPane;
	@FXML
	JFXButton btn_pre;
	@FXML
	JFXButton btn_next;

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
		private MyFxmlBean openDialog;

		@Override
		public void onCreateProject() {
			openDialog = UIUtil.openDialog(getClass(), "/application/fxml/CreateProjectDialog.fxml",
					ConstSize.Dialog_Frame_Width, ConstSize.Dialog_Frame_Height, "创建项目", getStage());
			if (openDialog != null) {
				CreateProjectDialogController controller = openDialog.getFxmlLoader().getController();
				controller.setCallBack(new CallBack() {
					@Override
					public void onDone(ProjectBean project) {
						System.out.println("接收到了完成！");
						Stage dialog = openDialog.getStage();
						if (dialog != null) {
							dialog.close();
						}
						nextPage();
						if (projectsController != null) {
							projectsController.addProject(project);
						}

						// TODO TEST
						MyFxmlBean openFrame = UIUtil.openFrame(getClass(), "/application/fxml/ImageList.fxml", ConstSize.Second_Frame_Width,
								ConstSize.Second_Frame_Height, "项目" + project.getProjectName());
						ImageListController controller = openFrame.getFxmlLoader().getController();
						controller.setProjectInfo(project);
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

		changeBottomView(pageIndex);
		return currentPane;
	}

	private void changeBottomView(Integer pageIndex) {
		switch (pageIndex.intValue()) {
		case 0:
			bottomBtnsPane.setVisible(false);
			bottomGroupPane.setVisible(true);
			break;
		case 1:
			bottomBtnsPane.setVisible(true);
			btn_pre.setVisible(false);
			btn_next.setVisible(true);
			btn_next.setText("下一步");
			bottomGroupPane.setVisible(false);
			break;
		case 2:
			bottomBtnsPane.setVisible(true);
			btn_pre.setVisible(true);
			btn_next.setVisible(true);
			btn_next.setText(" 开 始 ");
			bottomGroupPane.setVisible(false);
			break;
		case 3:
			bottomBtnsPane.setVisible(true);
			// TODO 根据情况显示：上一步；或不显示
			btn_pre.setVisible(false);
			btn_next.setVisible(true);
			// TODO 根据情况显示：重新拼接；完成
			btn_next.setText("  取 消  ");
			bottomGroupPane.setVisible(false);
			break;
		default:
			break;
		}
	}

	private void nextPage() {
		System.out.println("下一页");
		int nextIndex = mPagination.getCurrentPageIndex() + 1;
		if (nextIndex >= MAX_PAGE_SIZE) {
			nextIndex = 0;
		}
		mPagination.setCurrentPageIndex(nextIndex);
	}

	private void prePage() {
		System.out.println("上一页");
		int nextIndex = mPagination.getCurrentPageIndex() - 1;
		if (nextIndex < 0) {
			nextIndex = 0;
		}
		mPagination.setCurrentPageIndex(nextIndex);
	}

	@FXML
	public void leftBtn() {
		// TODO
		prePage();
	}

	@FXML
	public void rightBtn() {
		// TODO
		nextPage();
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
