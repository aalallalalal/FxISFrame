package application.control;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
public class SettingController  extends BaseController  implements Initializable {
	private SettingListener listener;
	@FXML
	JFXCheckBox checkBox_SaveMiddle;
	@FXML
	JFXCheckBox checkBox_preCheck;
	@FXML
	JFXTextArea textArea_width;
	@FXML
	JFXTextArea textArea_hight;
	@FXML
	JFXTextArea textArea_flyHeight;
	@FXML
	JFXTextArea textArea_cameraSize;
	@FXML
	HBox hbox_preCheckDetail;
	@FXML
	BorderPane root;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initCheckBox();
	}
	
	private void initCheckBox() {
		checkBox_preCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					hbox_preCheckDetail.setDisable(false);
				}else {
					hbox_preCheckDetail.setDisable(true);
				}
			}
		});;
		checkBox_SaveMiddle.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
				}else {
				}
			}
		});;
	}

	public interface SettingListener {

	}


	public void setListener(SettingListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onSetBottomBtnsAndTitle() {
		leftBtn.setVisible(true);
		rightBtn.setVisible(true);
		rightBtn.setText(" 开 始 ");		
		title.setText("参数配置");
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
