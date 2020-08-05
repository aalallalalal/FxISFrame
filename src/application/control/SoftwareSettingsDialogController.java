package application.control;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;

import beans.SoftwareSettingsBean;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import utils.SaveLanguageUtil;

/**
 * 创建项目dialog界面controller
 * 
 * @author DP
 */
public class SoftwareSettingsDialogController implements Initializable {
	@FXML
	private JFXButton btnDone;

	private CallBack callBack;

	private ToggleGroup group;

	@FXML
	JFXRadioButton radioButton_ch;

	@FXML
	JFXRadioButton radioButton_eng;

	private int initSelected;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		group = new ToggleGroup();
		radioButton_ch.setToggleGroup(group);
		radioButton_ch.setUserData(0);
		radioButton_eng.setToggleGroup(group);
		radioButton_eng.setUserData(1);
		initSelected = SaveLanguageUtil.getData();
		if (initSelected == 0) {
			group.selectToggle(radioButton_ch);
		} else {
			group.selectToggle(radioButton_eng);
		}
	}

	@FXML
	public void done() {
		if (callBack != null) {
			SoftwareSettingsBean bean = new SoftwareSettingsBean();
			bean.setLanguage((int) group.getSelectedToggle().getUserData());
			
			if (initSelected != bean.getLanguage()) {
				SaveLanguageUtil.saveData(bean.getLanguage());
				callBack.onDone(bean,true);
			} else {
				callBack.onDone(bean,false);
			}
		}
	}

	public void setCallBack(CallBack callBack) {
		this.callBack = callBack;
	}

	public interface CallBack {
		void onDone(SoftwareSettingsBean settings,boolean isChanged);
	}
}
