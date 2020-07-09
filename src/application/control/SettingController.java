package application.control;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;

import beans.FinalDataBean;
import beans.ProjectBean;
import beans.SettingsBean;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * 设置其他参数界面controller
 * 
 * @author DP
 *
 */
public class SettingController extends BaseController implements Initializable {
	private SettingListener listener;
	@FXML
	JFXCheckBox checkBox_SaveMiddle;
	@FXML
	JFXCheckBox checkBox_preCheck;
	@FXML
	JFXTextField textArea_width;
	@FXML
	JFXTextField textArea_hight;
	@FXML
	JFXTextField textArea_flyHeight;
	@FXML
	JFXTextField textArea_cameraSize;
	@FXML
	HBox hbox_preCheckDetail;
	@FXML
	BorderPane root;
	private ObservableList<ProjectBean> projectListData;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initCheckBox();
	}

	private void initCheckBox() {
		checkBox_preCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					hbox_preCheckDetail.setDisable(false);
				} else {
					hbox_preCheckDetail.setDisable(true);
				}
			}
		});
		checkBox_SaveMiddle.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
				} else {
				}
			}
		});
	}

	/**
	 * 传递项目列表数据
	 * 
	 * @param projectListData
	 */
	public void setProjectsInfo(ObservableList<ProjectBean> projectListData) {
		this.projectListData = projectListData;
	}

	public interface SettingListener {
		/**
		 * 点击开始拼接按钮
		 * 
		 * @param finalData
		 */
		void onClickStart(FinalDataBean finalData);

		/**
		 * 点击上一页按钮
		 */
		void onClickLeftBtn();
	}

	public void setListener(SettingListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onSetBottomBtnsAndTitle() {
		leftBtn.setVisible(true);
		rightBtn.setVisible(true);
		rightBtn.setText(" 开 始 ");
		leftBtn.setText("项目");
		title.setText("参数设置");
	}

	@Override
	protected void onClickLeftBtn() {
		if (listener != null) {
			listener.onClickLeftBtn();
		}
	}

	@Override
	protected void onClickRightBtn() {
		SettingsBean setting = new SettingsBean();
		setting.setSaveMiddle(checkBox_SaveMiddle.isSelected());
		setting.setNetWidth(textArea_width.getText());
		setting.setNetHeight(textArea_hight.getText());
		setting.setPreCheck(checkBox_preCheck.isSelected());
		setting.setFlyHeight(textArea_flyHeight.getText());
		setting.setCameraSize(textArea_cameraSize.getText());
		FinalDataBean finalDataBean = new FinalDataBean(projectListData, setting);
		if (listener != null) {
			listener.onClickStart(finalDataBean);
		}
	}
}
