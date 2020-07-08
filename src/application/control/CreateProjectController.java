package application.control;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * 创建项目界面controller
 * 
 * @author DP
 *
 */
public class CreateProjectController extends BaseController implements Initializable {
	private CreateProjectListener listener;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void test() {
	}

	@FXML
	public void help() {
		System.out.println("帮助界面");
	}

	@FXML
	public void createProject() {
		if (listener != null) {
			listener.onCreateProject();
		}
	}

	public void setListener(CreateProjectListener listener) {
		this.listener = listener;
	}

	public interface CreateProjectListener {
		void onCreateProject();
	}

	@Override
	protected void onClickLeftBtn() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onClickRightBtn() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onSetBottomBtnsAndTitle() {
		if(title!=null) {
			title.setText("图片拼接");
		}
	}
}
