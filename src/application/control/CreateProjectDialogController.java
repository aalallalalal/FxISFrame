package application.control;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * 创建项目dialog界面controller
 * 
 * @author DP
 *
 */
public class CreateProjectDialogController implements Initializable {
	private CallBack callBack;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void test() {
	}

	@FXML
	public void done() {
		if (callBack != null) {
			callBack.onDone();
		}
	}

	public void setCallBack(CallBack callBack) {
		this.callBack = callBack;
	}

	public interface CallBack {
		void onDone();
	}
}
