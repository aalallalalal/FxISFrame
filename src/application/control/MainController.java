package application.control;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import application.Main;
import application.control.CreateProjectDialogController.CallBack;
import beans.FinalDataBean;
import beans.MyFxmlBean;
import beans.ProjectBean;
import beans.SettingsBean;
import beans.SoftwareSettingsBean;
import consts.ConstSize;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import utils.MyPlatform;
import utils.ResUtil;
import utils.ToastUtil;
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
	private BaseController currentController;

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
	private Label title;

	/**
	 * createProject小界面的操作回调
	 * 
	 * @author DP
	 *
	 */
	private class CreateProjListener implements CreateProjectController.CreateProjectListener {

		@Override
		public void onCreateProject() {
			openCreateProjectDialog(true);
		}

		@Override
		public void onClearData() {
			if (projectsController != null) {
				projectsController.clearData();
			}
		}

		@Override
		public void onOpenProject() {
			openOpenProjectDialog(true);
		}

		@Override
		public void onClickHelp() {

		}

		@Override
		public void onClickSet() {
			MyFxmlBean openDialog;
			openDialog = UIUtil.openDialog(getClass(), "/application/fxml/SoftwareSettingsDialog.fxml",
					ConstSize.Dialog_Frame_Width, ConstSize.Dialog_Frame_Height, ResUtil.gs("software_setting"),
					getStage());
			if (openDialog != null) {
				SoftwareSettingsDialogController controller = openDialog.getFxmlLoader().getController();
				controller.setCallBack(new SoftwareSettingsDialogController.CallBack() {
					@Override
					public void onDone(SoftwareSettingsBean settings, boolean isChanged) {
						openDialog.getStage().close();
						if (isChanged) {
							ToastUtil.toast(ResUtil.gs("restart"));
							MyPlatform.runLater(new Runnable() {
								@Override
								public void run() {
									getStage().close();
									try {
										new Main().start(new Stage());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}, 2000);
						}
					}
				});
			}
		}

		@Override
		public void onClickChangeLanguage(int afterLanguage) {
			String strlanguage = afterLanguage == 0 ? "简体中文" : "English";
			ToastUtil.toast(ResUtil.gs("restart_note", strlanguage) + ResUtil.gs("restart"));
			MyPlatform.runLater(new Runnable() {
				@Override
				public void run() {
					getStage().close();
					try {
						new Main().start(new Stage());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, 2000);
		}
	}

	/**
	 * Projects小界面的操作回调
	 * 
	 * @author DP
	 *
	 */
	private class ProjectsListener implements ProjectsController.ProjectsListener {
		@Override
		public void onCreateProject() {
			openCreateProjectDialog(false);
		}

		@Override
		public void onClickRightBtn(ObservableList<ProjectBean> projectListData) {
			nextPage();
			if (settingController != null) {
				settingController.setProjectsInfo(projectListData);
			}
		}

		@Override
		public void onOpenProject() {
			openOpenProjectDialog(false);
		}
	}

	/**
	 * Setting小界面的操作回调
	 * 
	 * @author DP
	 *
	 */
	private class SettingListener implements SettingController.SettingListener {

		@Override
		public void onClickStart(FinalDataBean finalData) {
			nextPage();
			if (processingController != null) {
				processingController.startExec(finalData);
			}
		}

		@Override
		public void onClickLeftBtn() {
			prePage();
		}

		@Override
		public void onClickAddSettings(ObservableList<ProjectBean> projectListData) {
			MyFxmlBean settingDialogBean = UIUtil.openDialog(getClass(), "/application/fxml/SettingsDialog.fxml",
					ConstSize.Main_Frame_Width, ConstSize.Main_Frame_Height, ResUtil.gs("setting_new_temp"),
					getStage());
			SettingsDialogController settingDialogController = settingDialogBean.getFxmlLoader().getController();
			settingDialogController.initExtraData(2, projectListData, null);
			settingDialogController.setCallBack(new application.control.SettingsDialogController.CallBack() {
				@Override
				public void onReturn(SettingsBean settings) {
					settingController.addSettingResult(settings);
					settingDialogBean.getStage().close();
				}
			});			
		}
	}

	/**
	 * Processing小界面的操作回调
	 * 
	 * @author DP
	 *
	 */
	private class ProcessingListener implements ProcessingController.ProcessingListener {

		@Override
		public void toprojects() {
			mPagination.setCurrentPageIndex(1);
			processingController.initPage();
		}

		@Override
		public void tofirstpage() {
			mPagination.setCurrentPageIndex(0);
			projectsController.clearData(); // 清空
			processingController.initPage();
		}

		@Override
		public void updateSuccBox() {
			System.out.println("更新成功界面");
			processingController.updateSucc();
			processingController.updateParam();
			processingController.nextRun();
		}

		@Override
		public void updateFailBox(String reason) {
			System.out.println("更新失败界面");
			processingController.updateFail();
			processingController.updateParam();
			processingController.nextRun();
		}

		@Override
		public void openResultFromFileSystem() {
			try {
				String path = System.getProperty("user.dir");
				Desktop.getDesktop().open(new File(path + "\\Run\\Result"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void update(String lineStr) {
			processingController.textarea.appendText(lineStr);
		}

		@Override
		public void updateFinish() {
			processingController.setState(false);
			processingController.currentProject.setText("");
			processingController.textarea.clear();
			processingController.updatecontrol();
			changeBottomBtnsView(currentController, 3);
		}
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
		fxmlLoader.setResources(ResUtil.getResource());
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
				createProjectController.setMainStage(getStage());
			}
			currentController = createProjectController;
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
				projectsController.setMainStage(getStage());
			}
			currentController = projectsController;
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
				settingController.setMainStage(getStage());
			}
			currentController = settingController;
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
				processingController.setMainStage(getStage());
			}
			currentController = processingController;
			currentPane = ProcessingPane;
			System.out.println("donknow:+" + pageIndex);
			break;
		default:
			break;
		}

		changeBottomBtnsView(currentController, pageIndex);
		return currentPane;
	}

	/**
	 * 设置界面bottom部分的显示与按钮的显示内容等。
	 * 
	 * @param currentController
	 * @param pageIndex
	 */
	private void changeBottomBtnsView(BaseController currentController, Integer pageIndex) {
		currentController.onInitBottomBtnsAndTitle(btn_pre, btn_next, title);
		switch (pageIndex.intValue()) {
		case 0:
			bottomBtnsPane.setVisible(false);
			bottomGroupPane.setVisible(true);
			break;
		case 1:
			bottomBtnsPane.setVisible(true);
			bottomGroupPane.setVisible(false);
			break;
		case 2:
			bottomBtnsPane.setVisible(true);
			bottomGroupPane.setVisible(false);
			break;
		case 3:
			bottomBtnsPane.setVisible(true);
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
		currentController.onClickLeftBtn();
	}

	@FXML
	public void rightBtn() {
		currentController.onClickRightBtn();
	}

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
						title = (Label) root.getParent().lookup("#bar_title");// 因为一开始root还没加入parent里，parent为空。写到这里已经加入parent了。
						return createPage(pageIndex);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
			}
		});
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

	/**
	 * 打开创建项目dialog
	 */
	private void openOpenProjectDialog(boolean isToNextPage) {
		MyFxmlBean openDialog;
		openDialog = UIUtil.openDialog(getClass(), "/application/fxml/OpenProjectDialog.fxml",
				ConstSize.Dialog_Frame_Width, ConstSize.Dialog_Frame_Height, ResUtil.gs("createProject_openproject"),
				getStage());
		if (openDialog != null) {
			OpenProjectDialogController controller = openDialog.getFxmlLoader().getController();
			controller.initData();
			controller.setCallBack(new OpenProjectDialogController.CallBack() {
				@Override
				public void onDone(ProjectBean project) {
					Stage dialog = openDialog.getStage();
					if (dialog != null) {
						dialog.close();
					}

					File projectFile = new File(project.getProjectDir());
					File locationFile = new File(project.getProjectLocationFile());
					if (projectFile == null || !projectFile.exists()) {
						// 项目路径不存在
						openChangeProjectDialog(project, isToNextPage);
						ToastUtil.toast(ResUtil.gs("input_error_project_path"));
						return;
					} else {
						// 项目路径存在
						if (project.getLocationFrom() == 1 && (locationFile == null || !locationFile.exists()
								|| !(project.getProjectLocationFile().endsWith(".txt")
										|| project.getProjectLocationFile().endsWith(".TXT")
										|| project.getProjectLocationFile().endsWith(".GPS")
										|| project.getProjectLocationFile().endsWith(".gps")))) {
							// 项目路径存在且是文件读取，但经纬度文件不正确
							openChangeProjectDialog(project, isToNextPage);
							ToastUtil.toast(ResUtil.gs("input_error_location_path"));
							return;
						}
					}

					// 没问题
					if (isToNextPage) {
						nextPage();
					}
					if (projectsController != null) {
						projectsController.addProject(project);
					}
				}
			});
		}
	}

	/**
	 * 更新项目信息
	 * 
	 * @param bean
	 * @param isToNextPage
	 */
	private void openChangeProjectDialog(ProjectBean bean, boolean isToNextPage) {
		MyFxmlBean changeDialog;
		changeDialog = UIUtil.openDialog(getClass(), "/application/fxml/ChangeProjectDialog.fxml",
				ConstSize.Dialog_Frame_Width, ConstSize.Dialog_Frame_Height, ResUtil.gs("createProject_changeproject"),
				getStage());
		if (changeDialog != null) {
			ChangeProjectDialogController controller = changeDialog.getFxmlLoader().getController();
			controller.setInitData(bean);
			controller.setCallBack(new ChangeProjectDialogController.CallBack() {
				@Override
				public void onDone(ProjectBean project) {
					Stage dialog = changeDialog.getStage();
					if (dialog != null) {
						dialog.close();
					}
					// 没问题
					if (isToNextPage) {
						nextPage();
					}
					if (projectsController != null) {
						projectsController.addProject(project);
					}
				}
			});
		}
	}

	/**
	 * 打开新建项目dialog
	 */
	private void openCreateProjectDialog(boolean isToNextPage) {
		MyFxmlBean createDialog;
		createDialog = UIUtil.openDialog(getClass(), "/application/fxml/CreateProjectDialog.fxml",
				ConstSize.Dialog_Frame_Width, ConstSize.Dialog_Frame_Height, ResUtil.gs("createProject_createproject"),
				getStage());
		if (createDialog != null) {
			CreateProjectDialogController controller = createDialog.getFxmlLoader().getController();
			controller.setCallBack(new CallBack() {
				@Override
				public void onDone(ProjectBean project) {
					Stage dialog = createDialog.getStage();
					if (dialog != null) {
						dialog.close();
					}
					if (isToNextPage) {
						nextPage();
					}
					if (projectsController != null) {
						projectsController.addProject(project);
					}
				}
			});
		}
	}

}
