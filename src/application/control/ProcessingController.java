package application.control;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

/**
 * 正在计算、计算结果界面controller
 * @author DP
 *
 */
public class ProcessingController implements Initializable {

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
}
