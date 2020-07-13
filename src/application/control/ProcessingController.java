package application.control;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

import beans.FinalDataBean;
import beans.ProjectBean;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utils.ExeProcedureUtil;
import javafx.concurrent.Task;
import javafx.fxml.FXML;

/**
 * 正在计算、计算结果界面controller
 * 
 * @author DP
 *
 */
public class ProcessingController extends BaseController implements Initializable {

	private ProcessingListener listener;

	private FinalDataBean finalData;

	private boolean result;// 运行结果

	private boolean state = true;// 当前状态（是否运行完）

	Image image = new Image("resources/timg.gif");
	Image image_succ = new Image("resources/succ.png");
	Image image_failed = new Image("resources/failed.png");
	@FXML
	ImageView imageView = new ImageView(image);

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
	 * 开始执行程序，程序运行结束后改变当前状态并且设置程序运行结果
	 * 
	 * @param finalData
	 * @throws Exception
	 */
	public void startExec(FinalDataBean finalData) throws Exception {
		this.finalData = finalData;
		
		Task<String> task = new Task<String>() {
	        @Override
	        public String call() throws Exception {
	            // process long-running computation, data retrieval, etc...
	        	String path_Exe = "D:/eclipse/project/GitHub/exe/ImageStitching/ImageStitching.exe";//exe文件的结果路径
	        	String result_cmd = ExeProcedureUtil.execute(path_Exe, "D:/eclipse/project/GitHub/exe/18");//参数
				return result_cmd;
	        }
	    };
	    
	    task.setOnSucceeded(e -> {
			this.result = true;
			// update UI with result
			this.state = false;
			listener.updateSuccPage();
	    });
	    
	    task.setOnFailed(e -> {
	    	this.result = false;
	    	this.state = false;
	    	listener.updateFailPage();
	    });
	    
	    new Thread(task).start();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
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
			listener.toprojects();
		}

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
