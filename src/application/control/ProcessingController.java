package application.control;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utils.ProgressTask.ExeTask;
import javafx.concurrent.Task;
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
	
	Task<Boolean> task;
	Thread mythread;

	Image image = new Image("resources/timg.gif");
	Image image_succ = new Image("resources/succ.png");
	Image image_failed = new Image("resources/failed.png");
	@FXML
	ImageView imageView = new ImageView(image);
	
	@FXML
	Label DetailInfo;

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
		task = new ExeTask(this.listener);
	    mythread = new Thread(task,"拼接线程");
	    mythread.setDaemon(true);
	    mythread.start();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	//取消进程，返回主界面之后初始化进程界面,
	//在listener中的toprojects，tofirstpage方法中调用
	public void initPage()
	{
		this.setState(true);
		this.setResult(true);
		this.imageView.setImage(image);
		this.DetailInfo.setText("拼接中...");
	}

	public void test() {
		System.out.println("ProcessingController来自其他controller的调用");
	}

	public void setListener(ProcessingListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onSetBottomBtnsAndTitle() {
		if (state) {
			title.setText("拼接中");
			// TODO 根据情况显示：上一步；或不显示
			leftBtn.setVisible(false);
			rightBtn.setVisible(true);
			// TODO 根据情况显示：重新拼接；完成
			rightBtn.setText("  取 消  ");
		} else if (result) {
			title.setText("拼接成功");
			// TODO 根据情况显示：上一步；或不显示
			leftBtn.setVisible(true);
			leftBtn.setText("查看结果");
			rightBtn.setVisible(true);
			// TODO 根据情况显示：重新拼接；完成
			rightBtn.setText("返回首页");
		} else {
			title.setText("拼接失败");
			// TODO 根据情况显示：上一步；或不显示
			leftBtn.setVisible(true);
			leftBtn.setText("项目列表");
			rightBtn.setVisible(true);
			// TODO 根据情况显示：重新拼接；完成
			rightBtn.setText("返回主界面");
		}

	}

	@Override
	protected void onClickLeftBtn() {
		// TODO Auto-generated method stub
		if(result)
		{
			listener.openResultFromFileSystem();
		}
		else
		{
			listener.toprojects();
		}
		
	}

	@Override
	protected void onClickRightBtn() {
		// TODO Auto-generated method stub
		if (!state) {
			listener.tofirstpage();
		} else {
			task.cancel(true);
			System.out.println(task.isCancelled());
			
		}
		mythread.interrupt();
		System.out.println("线程：" + mythread.isAlive());

	}
	
	public interface ProcessingListener {
		//转到项目列表界面
		void toprojects();
		//转到首页
		void tofirstpage();
		//更新成功界面
		void updateSuccPage();
		//更新失败界面
		void updateFailPage();
		//打开文件系统的结果目录
		void openResultFromFileSystem();
	}

}
