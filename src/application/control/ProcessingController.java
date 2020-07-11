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
import utils.WatchThread;
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
	@FXML
	ImageView imageView = new ImageView(image);

	/**
	 * 开始执行程序，程序运行结束后改变当前状态并且设置程序运行结果
	 * 
	 * @param finalData
	 * @throws Exception
	 */
	public void startExec(FinalDataBean finalData) throws Exception {
		this.finalData = finalData;
		System.out.println("ProcessingController startExec：" + finalData);
		
		Task<String> task = new Task<String>() {
	        @Override
	        public String call() throws Exception {
	            // process long-running computation, data retrieval, etc...
	        	String path_Exe = "";
	        	String result_cmd = ExeProcedureUtil.execute(path_Exe, FinalDataBean.para_Exe);
				return result_cmd;
	        }
	    };
	    task.setOnSucceeded(e -> {
	    	String l;
			l = task.getValue().toString();
			System.out.println("读取：" + l);
			if(l != null)
			{
				this.result = true;
			}else {
				this.result = false;
			}
			// update UI with result
			this.state = false;
			listener.updatePage();
	        
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
			leftBtn.setVisible(false);
			rightBtn.setVisible(true);
			// TODO 根据情况显示：重新拼接；完成
			rightBtn.setText("返回主界面");
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
		listener.toprojects();
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
		void toprojects();

		void tofirstpage();

		void updatePage();
	}

}
