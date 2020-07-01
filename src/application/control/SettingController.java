package application.control;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

/**
 * 设置其他参数界面controller
 * 
 * @author DP
 *
 */
public class SettingController implements Initializable {
	private SettingListener listener;

	public interface SettingListener {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	public void setListener(SettingListener listener) {
		this.listener = listener;
	}

}
