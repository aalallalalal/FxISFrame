package application.control;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import beans.MyFxmlBean;
import beans.ProjectBean;
import beans.SettingsBean;
import consts.ConstSize;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import utils.FileUtil;
import utils.UIUtil;

public class TabFailedController implements Initializable
{
	@FXML
	AnchorPane root;
	
	ObservableList<ProjectBean> list_failed = FXCollections.observableArrayList();
	
	@FXML
	ListView<ProjectBean> listView_failed = new ListView<ProjectBean>();

	Image image_failed = new Image("resources/nofailed.png");
	ImageView imageView_failed = new ImageView(image_failed);
	
	private ProcessingController processingController;
	public void init(ProcessingController controller) 
	{
		processingController = controller;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		listView_failed.setVisible(false);
		listView_failed.setCellFactory(new Callback<ListView<ProjectBean>, ListCell<ProjectBean>>()
		{
			
			@Override
			public ListCell<ProjectBean> call(ListView<ProjectBean> param)
			{
				ListCell<ProjectBean> cell = new ListCell<ProjectBean>() {

					@Override
					protected void updateItem(ProjectBean item, boolean empty)
					{
						super.updateItem(item, empty);
						if(empty == false)
						{
							MyFxmlBean fxmlbean = UIUtil.loadFxml(getClass(), "/application/fxml/FailedHBox.fxml");
							HBox temp = (HBox)fxmlbean.getPane();
							setContent(temp, item);
							this.setGraphic(temp);
						}
						else
						{
							this.setGraphic(null);
						}
					}
					
				};
				return cell;
			}
		});
	}
	
	public void addFailedHBox(ProjectBean project, String reason)
	{
		project.setErroDetail(reason);
		list_failed.add(project);
		listView_failed.setItems(list_failed);
		listView_failed.setVisible(true);
	}
	
	/**
	 * 设置添加的运行失败的item
	 * @param name
	 * @param reason
	 */
	private void setContent(HBox temp, ProjectBean project)
	{
		Label project_name = (Label)temp.lookup("#project_name");
		project_name.setText(project.getProjectName());
		Label faileddetail = (Label)temp.lookup("#faildetailinfo");
		faileddetail.setText(project.getErroDetail());
		Label currenttime = (Label)temp.lookup("#currenttime");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String time = df.format(new Date());// new Date()为获取当前系统时间
		currenttime.setText(time);
		
		//查看参数
		JFXButton paramDetail = (JFXButton)temp.lookup("#paramDetail");
		paramDetail.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				listView_failed.getSelectionModel().select(project);
				MyFxmlBean settingDialogBean = UIUtil.openDialog(getClass(),
						"/application/fxml/SettingsDialog.fxml", ConstSize.Main_Frame_Width,
						ConstSize.Main_Frame_Height, project.getProjectName(), processingController.stage);
				SettingsDialogController settingDialogController = settingDialogBean.getFxmlLoader().getController();
				settingDialogController.initExtraData(0, null, project.getSettings());
				settingDialogController.setCallBack(new application.control.SettingsDialogController.CallBack() {
					@Override
					public void onReturn(SettingsBean settings) {
						settingDialogBean.getStage().close();
					}
				});
			}
		});
		
		//修改参数&重新运行
		JFXButton restart = (JFXButton)temp.lookup("#restart");
		restart.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				listView_failed.getSelectionModel().select(project);
				MyFxmlBean settingDialogBean = UIUtil.openDialog(getClass(),
						"/application/fxml/SettingsDialog.fxml", ConstSize.Main_Frame_Width,
						ConstSize.Main_Frame_Height, project.getProjectName(), processingController.stage);
				SettingsDialogController settingDialogController = settingDialogBean.getFxmlLoader().getController();				
				settingDialogController.initExtraData(1, null, project.getSettings());
				settingDialogController.setCallBack(new application.control.SettingsDialogController.CallBack() {
					@Override
					public void onReturn(SettingsBean settings) {
						project.setSettings(settings);
						settingDialogBean.getStage().close();
						remove(listView_failed.getSelectionModel().getSelectedIndex());
						processingController.addNewService(project);
					}
				});
			}
		});
		
	}

	public void remove(int selectedIndex)
	{
		File file = new File(System.getProperty("user.dir") + "\\Run\\" + list_failed.get(selectedIndex).getProjectName());
		FileUtil.deleteRunDir(file);
		list_failed.remove(selectedIndex);
		if(list_failed.isEmpty())
			listView_failed.setVisible(false);
	}
	/**
	 * 清空fail tab中的数据
	 */
	public void clearItems()
	{
		list_failed.clear();
		listView_failed.getItems().clear();
		listView_failed.setVisible(false);
	}

	

}
