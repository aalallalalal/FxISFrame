package application.control;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTabPane;

import base.controller.ConfirmDialogController.CallBack;
import beans.DBRecordBean;
import beans.FinalDataBean;
import beans.ProjectBean;
import consts.ConstSize;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import utils.DBUtil;
import utils.ResUtil;
import utils.ToastUtil;
import utils.UIUtil;
import utils.ProgressTask.ExeService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.fxml.FXML;

/**
 * 正在计算、计算结果界面controller
 * 
 * @author DP
 *
 */
public class ProcessingController extends BaseController implements Initializable {

	public ProcessingListener listener;
	
	private boolean state = true;
	
	Service<String> service;
	
	@FXML
	Label currentProject = new Label();
	
	@FXML
	TextArea textarea = new TextArea();
	
	@FXML
	BorderPane root;
	
	@FXML
	JFXTabPane tabPane = new JFXTabPane();
	
	@FXML
	Tab tab_running = new Tab();
	@FXML
	TabRunningController tabRunningController;
	
	@FXML
	Tab tab_achieve = new Tab();
	@FXML
	TabAchieveController tabAchieveController;
	
	@FXML
	Tab tab_failed = new Tab();
	@FXML
	TabFailedController tabFailedController;
	
	DBRecordBean dbRecord = new DBRecordBean();

	/**
	 * 开始执行程序，程序运行结束后改变页面
	 * 
	 * @param finalData
	 * @throws Exception
	 */
	public void startExec(FinalDataBean finalData)
	{
		service = new ExeService(this.listener);
		 //异常监听 监听现在状态是否有异常并打印
        service.exceptionProperty().addListener(new ChangeListener<Throwable>() {
            @Override
            public void changed(ObservableValue<? extends Throwable> observable, Throwable oldValue, Throwable newValue) {
                listener.updateFailBox(newValue.toString());
            }
        });
        tabRunningController.addServiceToList(finalData);
        updateParam();
        nextRun();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tabRunningController.init(this);
		tabAchieveController.init(this);
		tabFailedController.init(this);
		tabPane.getSelectionModel().select(tab_running);
	}
	
	//取消进程，返回主界面之后初始化进程界面,
	//在listener中的toprojects，tofirstpage方法中调用
	public void initPage()
	{
		this.textarea.clear();
		tabAchieveController.clearItem();
		tabRunningController.clearItems();
		tabFailedController.clearItems();
		this.setState(true);
		tabPane.getSelectionModel().select(tab_running);
	}
	
	public void setListener(ProcessingListener listener) {
		this.listener = listener;
	}

	
	public boolean isState()
	{
		return state;
	}

	public void setState(boolean state)
	{
		this.state = state;
	}

	@Override
	protected void onSetBottomBtnsAndTitle() {
		title.setText(ResUtil.gs("splicing-service"));
		rightBtn.setVisible(true);
		rightBtn.setText(ResUtil.gs("all_close"));
		if(state)
			leftBtn.setVisible(false);
		else
		{
			leftBtn.setVisible(true);
			leftBtn.setText(ResUtil.gs("project-list"));
		}
	}

	@Override
	protected void onClickLeftBtn() {
		listener.toprojects();
	}

	@Override
	protected void onClickRightBtn() {
		if (tabRunningController.getList_running().isEmpty()) {
			ToastUtil.toast(ResUtil.gs("Don't-have-running-service"));
		} else {
			UIUtil.openConfirmDialog(getClass(), ConstSize.Confirm_Dialog_Frame_Width,
					ConstSize.Confirm_Dialog_Frame_Height, ResUtil.gs("all_close"), ResUtil.gs("is_running_are_you_sure"),
					(Stage) root.getScene().getWindow(), new CallBack() {
						@Override
						public void onCancel() {
						}

						@Override
						public void onConfirm() {
							service.cancel();
							for(ProjectBean project : tabRunningController.getList_running())
								tabFailedController.addFailedHBox(project, ResUtil.gs("this-service-have-cancelled"));
							tabRunningController.clearItems();
							tabRunningController.updatecontrol();
							listener.updateFinish();
							tabPane.getSelectionModel().select(tab_failed);
						}
					});
		}
	}
	
	/**
	 * 更新下一个任务的参数
	 */
	public void updateParam() {
		ProjectBean next;
 		if(!tabRunningController.getList_running().isEmpty())
 		{
 			next = tabRunningController.getList_running().get(0);
 			dbRecord.setProject(next);
 			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");// 设置日期格式
 			next.setLastRuntime(System.currentTimeMillis());
 			String time = df.format(next.getLastRuntime());
 			String path = System.getProperty("user.dir");
 			dbRecord.setResultPath(path + "\\logs\\" + next.getProjectName() + next.getId() + "\\" + time);
 			FinalDataBean.para_Exe = next.toSettingParameter() + next.getParam() + "%" + next.getProjectName() + next.getId() + "\\" + time;
 			System.out.println(FinalDataBean.para_Exe);
 			currentProject.setText(next.getProjectName() + " . . .");
 		}
	}
	/**
	 * 判断是否还有任务及对应操作
	 */
	public void nextRun()
	{
		if(!tabRunningController.getList_running().isEmpty())
		{
			tabRunningController.toRunning();
			service.reset();
			service.start();
		}else {
			listener.updateFinish();
		}
	}
	
	/*
	 * 向数据库中写入数据
	 */
	public void writeToDB()
	{
		dbRecord.setRunTime(System.currentTimeMillis());
		DBUtil.insert(dbRecord);
		ArrayList<DBRecordBean> listarray = DBUtil.selectAll();
	}
	
	/**
	 * 运行成功后更新tab页面
	 */
	public void updateSucc()
	{
		tabAchieveController.addAchieveHBox(tabRunningController.getList_running().get(0));
		tabRunningController.updateRemove(0);
	}
	/**
	 * 运行失败后更新tab页面
	 */
	public void updateFail(String reason)
	{
		tabFailedController.addFailedHBox(tabRunningController.getList_running().get(0), reason);
		listener.update("\n" + reason);
		tabRunningController.updateRemove(0);
	}
	
	public void updatecontrol()
	{
		tabRunningController.updatecontrol();
		tabPane.getSelectionModel().select(tab_achieve);
	}
	
	/**
	 * 子页面中重新运行时调用
	 * @param project
	 */
	public void addNewService(ProjectBean project)
	{
		if(tabRunningController.getList_running().isEmpty())
		{
			project.setInfo(1);
			tabRunningController.add(project);
			updateParam();
			nextRun();
			setState(true);
			leftBtn.setVisible(false);
			tabRunningController.appointSelect(project);
			tabPane.getSelectionModel().select(tab_running);
		}
		else if(service.getState().toString() != "SUCCEEDED" || service.getState().toString() != "FAILED")
		{
			project.setInfo(0);
			tabRunningController.add(project);
			tabRunningController.appointSelect(project);
			tabPane.getSelectionModel().select(tab_running);
		}
		else 
		{
			ToastUtil.toast(ResUtil.gs("restart_failed_please_retry"));
		}
	}

	public void closeService(int select)
	{
		ProjectBean project = tabRunningController.getList_running().get(select);
		if(select == 0)
		{
			UIUtil.openConfirmDialog(getClass(), ConstSize.Confirm_Dialog_Frame_Width,
					ConstSize.Confirm_Dialog_Frame_Height, ResUtil.gs("cancel_service"),ResUtil.gs("is_running_are_you_sure"),
					(Stage) root.getScene().getWindow(), new CallBack() {
						@Override
						public void onCancel() {
						}

						@Override
						public void onConfirm() {
							if(tabRunningController.getList_running().contains(project)) {
								service.cancel();
								listener.updateFailBox(ResUtil.gs("this-service-have-cancelled"));
							}else
							{
								ToastUtil.toast(ResUtil.gs("the_service_have_achieved"));
							}
						}
					});
		}
		else
		{
			UIUtil.openConfirmDialog(getClass(), ConstSize.Confirm_Dialog_Frame_Width,
					ConstSize.Confirm_Dialog_Frame_Height, ResUtil.gs("cancel_service"),ResUtil.gs("cancel_sure"),
					(Stage) root.getScene().getWindow(), new CallBack() {
						@Override
						public void onCancel() {
						}

						@Override
						public void onConfirm() {
							if(tabRunningController.getList_running().contains(project)) {
								if(project.equals(tabRunningController.getList_running().get(0))) {
									service.cancel();
									listener.updateFailBox(ResUtil.gs("this-service-have-cancelled"));
								}else
								{
									tabFailedController.addFailedHBox(project, ResUtil.gs("this-service-have-cancelled"));
									tabRunningController.updateRemove(project);
								}
							}else {
								ToastUtil.toast(ResUtil.gs("the_service_have_achieved"));
							}
							
						}
					});
			
		}
		
	}
	public interface ProcessingListener {
		//转到项目列表界面
		void toprojects();
		//更新成功界面
		void updateSuccBox();
		//更新失败界面
		void updateFailBox(String result);
		//更新拼接完成界面
		void updateFinish();
		//打开文件系统的结果目录
		void openResultFromFileSystem();
		//更新显示的运行信息
		void update(String lineStr);
	}
}
