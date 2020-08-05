package application.control;

import java.net.URL;
import java.util.ResourceBundle;


import com.jfoenix.controls.JFXTabPane;

import base.controller.ConfirmDialogController.CallBack;
import beans.FinalDataBean;
import beans.ProjectBean;
import consts.ConstSize;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
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

	private boolean state = true;// 当前状态（是否运行完）
	
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
	
	@FXML
	Tab tab_param = new Tab();
	@FXML
	TabParamController tabParamController;

	public boolean isState()
	{
		return state;
	}

	public void setState(boolean state)
	{
		this.state = state;
	}

	/**
	 * 开始执行程序，程序运行结束后改变页面
	 * 
	 * @param finalData
	 * @throws Exception
	 */
	public void startExec()
	{
		service = new ExeService(this.listener);
		 //异常监听 监听现在状态是否有异常并打印
        service.exceptionProperty().addListener(new ChangeListener<Throwable>() {
            @Override
            public void changed(ObservableValue<? extends Throwable> observable, Throwable oldValue, Throwable newValue) {
                listener.updateFailBox(newValue.toString());
            }
        });
        System.out.println("开始");
        tabRunningController.addServiceToList();
        updateParam();
        nextRun();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("初始化");
		tabRunningController.init(this);
		tabAchieveController.init(this);
		tabFailedController.init(this);
		tabParamController.init(this);
		tabPane.getSelectionModel().select(tab_running);
		tab_running.setGraphic(new Label("正在拼接"));
		tab_achieve.setGraphic(new Label("拼接成功"));
		tab_failed.setGraphic(new Label("拼接失败"));
		tab_param.setGraphic(new Label("当前参数"));
	}
	
	//取消进程，返回主界面之后初始化进程界面,
	//在listener中的toprojects，tofirstpage方法中调用
	public void initPage()
	{
		this.setState(true);
		this.textarea.clear();
		tabRunningController.clearItem();
		tabAchieveController.clearItem();
		tabFailedController.clearItem();
		FinalDataBean.pathList.clear();
	}
	
	public void setListener(ProcessingListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onSetBottomBtnsAndTitle() {
		if (state) 
		{
			title.setText("拼接中");
			// TODO 根据情况显示：上一步；或不显示
			leftBtn.setVisible(false);
			rightBtn.setVisible(true);
			// TODO 根据情况显示：重新拼接；完成
			rightBtn.setText("  取 消  ");
		} 
		else 
		{
			title.setText("拼接完成");
			// TODO 根据情况显示：上一步；或不显示
			leftBtn.setVisible(true);
			leftBtn.setText("返回首页");
			rightBtn.setVisible(true);
			// TODO 根据情况显示：重新拼接；完成
			rightBtn.setText("项目列表");
		}

	}

	@Override
	protected void onClickLeftBtn() {
		listener.tofirstpage();
	}

	@Override
	protected void onClickRightBtn() {
		// TODO Auto-generated method stub
		if (!state) {
			listener.toprojects();
		} else {
			UIUtil.openConfirmDialog(getClass(), ConstSize.Confirm_Dialog_Frame_Width,
					ConstSize.Confirm_Dialog_Frame_Height, "取消拼接", "拼接运行中，确定取消所有任务？",
					(Stage) root.getScene().getWindow(), new CallBack() {
						@Override
						public void onCancel() {
						}

						@Override
						public void onConfirm() {
							System.out.println("取消进程！");
							service.cancel();
							System.out.println(service.isRunning());
						}
					});
		}
	}
	
	public void updateParam() {
		ProjectBean next;
 		if(!FinalDataBean.pathList.isEmpty())
 		{
 			next = FinalDataBean.pathList.get(0);
 			FinalDataBean.para_Exe = FinalDataBean.setting + next.getParam() + "%" + next.getProjectName();
 			currentProject.setText(next.getProjectName() + " . . .");
 			System.out.println(currentProject.getText());
 		}
	}
	
	public void nextRun()
	{
		if(!FinalDataBean.pathList.isEmpty())
		{
			tabRunningController.list_running.get(0).toRunning();
			service.reset();
			service.start();
		}else {
			listener.updateFinish();
		}
	}
	
	public void updateSucc()
	{
		tabAchieveController.addAchieveHBox(FinalDataBean.pathList.get(0));
		tabRunningController.list_running.remove(0);
		FinalDataBean.pathList.remove(0);
	}
	
	public void updateFail()
	{
		tabFailedController.addFailedHBox(FinalDataBean.pathList.get(0).getProjectName(), service.getValue());
		tabRunningController.list_running.remove(0);
		FinalDataBean.pathList.remove(0);
	}
	
	public interface ProcessingListener {
		//转到项目列表界面
		void toprojects();
		//转到首页
		void tofirstpage();
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

	public void updatecontrol()
	{
		tabRunningController.listView_running.setVisible(false);
	}

}
