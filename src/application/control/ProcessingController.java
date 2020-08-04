package application.control;


import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;

import base.controller.ConfirmDialogController.CallBack;
import beans.FinalDataBean;
import beans.ProjectBean;
import consts.ConstSize;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.UIUtil;
import utils.ProgressTask.ExeService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

	private boolean result;// 运行结果

	private boolean state = true;// 当前状态（是否运行完）
	
	Service<String> service;

	Image image_running = new Image("resources/norunning.png");
	Image image_succ = new Image("resources/nosucced.png");
	Image image_failed = new Image("resources/nofailed.png");
	
	ImageView imageView_running = new ImageView(image_running);
	ImageView imageView_succed = new ImageView(image_succ);
	ImageView imageView_failed = new ImageView(image_failed);
	
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
	Tab tab_achieve = new Tab();
	
	@FXML
	Tab tab_failed = new Tab();
	
	VBox vbox_running = new VBox(); 
	VBox vbox_achieve = new VBox();
	VBox vbox_failed = new VBox();
	
	ObservableList<MyHBox> list_running = FXCollections.observableArrayList();
	ObservableList<MyHBox> list_achieve = FXCollections.observableArrayList();
	ObservableList<MyHBox> list_failed = FXCollections.observableArrayList();
	
	@FXML
	ListView<MyHBox> listView_running = new ListView<MyHBox>(list_running);
	@FXML
	ListView<MyHBox> listView_achieve = new ListView<MyHBox>(list_achieve);
	@FXML
	ListView<MyHBox> listView_failed = new ListView<MyHBox>(list_failed);
	
	public boolean isResult()
	{
		return result;
	}

	public void setResult(boolean result)
	{
		this.result = result;
	}

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
        
        
        System.out.println(FinalDataBean.pathList.size());
        for(int i = 0 ; i < FinalDataBean.pathList.size() ; i ++)
        {
			/*
			 * HBox hbox = new HBox(); 
			 * Label l = new Label(FinalDataBean.pathList.get(i).getProjectName()); 
			 * Label s = new Label("等待执行"); 
			 * ProgressBar p = new ProgressBar(-1);
			 * hbox.getChildren().addAll(l, p, s); 
			 * vbox_running.getChildren().add(hbox);
			 */
        	MyHBox hbox = new MyHBox(FinalDataBean.pathList.get(i)); 
        	list_running.add(hbox);
        }
        updateParam();
        nextRun();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tabPane.getSelectionModel().select(tab_running);
		tabPane.setRotateGraphic(false);
		tab_running.setGraphic(new Label("正在拼接"));
		tab_achieve.setGraphic(new Label("拼接成功"));
		tab_failed.setGraphic(new Label("拼接失败"));
		tab_running.setContent(listView_running);
		tab_achieve.setContent(imageView_succed);
		tab_failed.setContent(imageView_failed);
		listView_running.setStyle("-fx-background-insets: 0 ;");
		listView_achieve.setStyle("-fx-background-insets: 0 ;");
		listView_failed.setStyle("-fx-background-insets: 0 ;");
	}
	
	//取消进程，返回主界面之后初始化进程界面,
	//在listener中的toprojects，tofirstpage方法中调用
	public void initPage()
	{
		this.setState(true);
		this.setResult(true);
		this.textarea.clear();
		list_running.clear();
		list_achieve.clear();
		list_failed.clear();
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
 			FinalDataBean.para_Exe = FinalDataBean.setting + next.getParam();
 			currentProject.setText(next.getProjectName() + " . . .");
 			System.out.println(currentProject.getText());
 			System.out.println(FinalDataBean.para_Exe);
 		}
	}
	
	public void nextRun()
	{
		if(!FinalDataBean.pathList.isEmpty())
		{
			FinalDataBean.pathList.remove(0);
			service.reset();
			service.start();
		}else {
			tab_running.setContent(imageView_running);
			listener.updateFinish();
		}
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

	protected class MyHBox extends HBox
	{
		
		Label project_name = new Label();
		Label FailDetailInfo = new Label();
		ProgressBar p = new ProgressBar(-1);
		JFXButton result_picture = new JFXButton("查看拼接结果");
		JFXButton result_mid = new JFXButton("查看中间结果");
		//关闭选中的任务图片按钮
		//打开文件夹所在位置图片按钮
		
		public MyHBox(ProjectBean project)
		{
			project_name.setText(project.getProjectName());
			
			super.getChildren().add(project_name);
			
		}
		
		@Override
		protected void layoutChildren()
		{
			super.layoutChildren();
			setSpacing(50);
			
		}

		public void toRunning()
		{
			
		}
		
		public void toSucc()
		{
			super.getChildren().addAll(result_picture, result_mid);
		}
		
		public void toFailed(String reason)
		{
			FailDetailInfo.setText(reason);
			super.getChildren().add(FailDetailInfo);
		}
	}
}
