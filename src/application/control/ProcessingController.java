package application.control;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

/**
 * 正在计算、计算结果界面controller
 * 
 * @author DP
 *
 */
public class ProcessingController extends BaseController implements Initializable {

	private ProcessingListener listener;

	public interface ProcessingListener {

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
		title.setText("拼接中");
		// TODO 根据情况显示：上一步；或不显示
		leftBtn.setVisible(false);
		rightBtn.setVisible(true);
		// TODO 根据情况显示：重新拼接；完成
		rightBtn.setText("  取 消  ");
	}

	@Override
	protected void onClickLeftBtn() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onClickRightBtn() {
		// TODO Auto-generated method stub

	}
}
