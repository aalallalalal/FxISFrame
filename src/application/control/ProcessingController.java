package application.control;

import java.net.URL;
import java.util.ResourceBundle;

import beans.FinalDataBean;
import beans.ProjectBean;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	
	private boolean result;//运行结果
	
	private boolean state = true;//当前状态（是否运行完）
	
	Image image = new Image("resources/timg.gif");
	@FXML
	ImageView imageView = new ImageView(image);

	
	/**
	 * 开始执行程序，程序运行结束后改变当前状态并且设置程序运行结果
	 * @param finalData
	 * @throws Exception 
	 */
	public void startExec(FinalDataBean finalData) {
		this.finalData = finalData;
		System.out.println("ProcessingController startExec：" + finalData);
		Task<Boolean> task = new Task<Boolean>() {
	        @Override
	        public Boolean call() throws Exception {
	            // process long-running computation, data retrieval, etc...
	        	Thread t = new Thread();
	        	t.sleep(5000);
	            Boolean result = true; // result of computation
	            return true;
	        }
	    };
	    task.setOnSucceeded(e -> {
	        this.result = task.getValue();
	        // update UI with result
	        this.result = getresult();
			this.state = false;
	        listener.updatePage();
	    });
	    
	    new Thread(task).start();
	}

	/**
	 * 获取程序运行结果
	 * @return
	 * @throws InterruptedException 
	 */
	public boolean getresult() 
	{
		return true;
	}
	
	public boolean isEnd() throws InterruptedException
	{
		Thread t = new Thread();
		t.sleep(5000);
		return true;
	}
	
	/**
	 * 更新属性
	 * @throws Exception
	 */
	public void updateAttributes() throws Exception
	{
		while(true) {
			Thread temp = new Thread();
			temp.sleep(5000);
			System.out.println("5秒");
			if(isEnd())
			{
				System.out.println("更新属性");
				this.result = getresult();
				this.state = false;
				break;
			}
		}
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
		if(state)
		{
			title.setText("拼接中");
			// TODO 根据情况显示：上一步；或不显示
			leftBtn.setVisible(false);
			rightBtn.setVisible(true);
			// TODO 根据情况显示：重新拼接；完成
			rightBtn.setText("  取 消  ");
		}
		else if(result)
		{
			title.setText("拼接成功");
			// TODO 根据情况显示：上一步；或不显示
			leftBtn.setVisible(false);
			rightBtn.setVisible(true);
			// TODO 根据情况显示：重新拼接；完成
			rightBtn.setText("返回主界面");
		}
		else
		{
			title.setText("拼接失败");
			// TODO 根据情况显示：上一步；或不显示
			leftBtn.setVisible(true);
			rightBtn.setVisible(true);
			// TODO 根据情况显示：重新拼接；完成
			rightBtn.setText("返回主界面");
		}
		
	}

	@Override
	protected void onClickLeftBtn() {
		// TODO Auto-generated method stub
		listener.uplevel();
	}

	@Override
	protected void onClickRightBtn()
	{
		// TODO Auto-generated method stub
		if(!state)
		{
			listener.tofirstpage();
		}
		else
		{
			listener.uplevel();
		}
		
	}

	public interface ProcessingListener {
		void uplevel();
		void tofirstpage();
		void updatePage();
	}
	
}
