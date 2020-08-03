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
	@FXML
	JFXButton button_help;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (listener != null) {
			listener.onClearData();
		}
//				ResUtil.initLanguage(combox_language.getSelectionModel().getSelectedIndex());
//				SaveLanguageUtil.saveData(combox_language.getSelectionModel().getSelectedIndex());
	}

	public void test() {
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

		void onOpenProject();

		void onClearData();

		void onClickHelp();

		void onClickSet();
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
		if (title != null) {
			title.setText("图片拼接");
		}
	}

	@FXML
	public void openProject() {
		if (listener != null) {
			listener.onOpenProject();
		}
	}

	@FXML
	public void onClickHelp() {
		System.out.println("帮助界面");
		if (listener != null) {
			listener.onClickHelp();
		}
	}

	@FXML
	public void onClickSetting() {
		if (listener != null) {
			listener.onClickSet();
		}
	}
}
