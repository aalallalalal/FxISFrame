package application.control;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import utils.FileUtil;
import utils.ResUtil;
import utils.ToastUtil;
import utils.UIUtil;

public class TabAchieveController implements Initializable
{
	@FXML
	AnchorPane root;

	ObservableList<ProjectBean> list_achieve = FXCollections.observableArrayList();
	
	@FXML
	ListView<ProjectBean> listView_achieve = new ListView<ProjectBean>();
	
	private ProcessingController processingController;
	public void init(ProcessingController controller) 
	{
		processingController = controller;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		BackgroundImage myBI= new BackgroundImage(new Image("resources/wushuju.png"), 
			     BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, 
			      BackgroundSize.DEFAULT); 
			//then you set to your node 
		root.setBackground(new Background(myBI)); 
		listView_achieve.setVisible(false);
		listView_achieve.setItems(list_achieve);
		listView_achieve.setCellFactory(new Callback<ListView<ProjectBean>, ListCell<ProjectBean>>()
		{
			
			@Override
			public ListCell<ProjectBean> call(ListView<ProjectBean> param)
			{
				ListCell<ProjectBean> cell = new ListCell<ProjectBean>() {

					@Override
					protected void updateItem(ProjectBean item, boolean empty)
					{
						// TODO Auto-generated method stub
						super.updateItem(item, empty);
						if(empty == false)
						{
							MyFxmlBean fxmlbean = UIUtil.loadFxml(getClass(), "/application/fxml/AchieveHBox.fxml");
							HBox temp = (HBox)fxmlbean.getPane();
							setContent(item, temp);
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
	
	public void addAchieveHBox(ProjectBean project) 
	{
		list_achieve.add(project);
		listView_achieve.setVisible(true);
	}
	
	/**
	 * 删除此任务同时删除结果目录
	 * @param i
	 */
	public void remove(int i)
	{
		File file = new File(System.getProperty("user.dir") + "\\Run\\" + list_achieve.get(i).getProjectName());
		FileUtil.deleteRunDir(file);
		list_achieve.remove(i);
		if(list_achieve.isEmpty())
			listView_achieve.setVisible(false);
	}
	
	/**
	 * 设置新添加的cell的内容
	 * @param project
	 * @param temp
	 */
	private void setContent(ProjectBean project, HBox temp)
	{
		Label project_name = (Label)temp.lookup("#project_name");
		project_name.setText(project.getProjectName());
		Label achieve_time = (Label)temp.lookup("#achieve_time");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String achieveTime = df.format(new Date());// new Date()为获取当前系统时间
		achieve_time.setText(achieveTime);
		
		//打开文件夹所在位置
		JFXButton openfile = (JFXButton)temp.lookup("#openfile");
		openfile.setGraphic(new ImageView(new Image("resources/wenjianjia.png")));
		openfile.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				listView_achieve.getSelectionModel().select(project);
				try {
					String path = System.getProperty("user.dir");
					Desktop.getDesktop().open(new File(path + "\\Run\\" + project.getProjectName() + "\\Result"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		//查看参数
		JFXButton paramDetail = (JFXButton)temp.lookup("#paramDetail");
		paramDetail.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				listView_achieve.getSelectionModel().select(project);
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
		
		//查看结果
		JFXButton result = (JFXButton)temp.lookup("#view_result");
		result.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				listView_achieve.getSelectionModel().select(project);
				System.out.println(listView_achieve.getSelectionModel().getSelectedIndex());
				Runtime runtime = Runtime.getRuntime();
				try {
					String path = System.getProperty("user.dir");
					int i = project.getProjectDir().lastIndexOf("/");
					String name_dir = project.getProjectDir().substring(i + 1);
					path = path + "\\Run\\" + project.getProjectName() + "\\Result\\0_results\\" + name_dir + "-result\\" + name_dir + "-[TIRS].png";
					runtime.exec("cmd /c " + path);
				} catch (IOException e) {
					ToastUtil.toast(ResUtil.gs("open_image_error"));
					e.printStackTrace();
				}
			}
		});
		
		//中间结果
		JFXButton midResult = (JFXButton)temp.lookup("#midresult");
		midResult.setOnAction(new EventHandler<ActionEvent>()
		{
			
			@Override
			public void handle(ActionEvent event)
			{
				listView_achieve.getSelectionModel().select(project);
				if(list_achieve.get(listView_achieve.getSelectionModel().getSelectedIndex()).getSettings().isSaveMiddle()){
					try {
						String path = System.getProperty("user.dir");
						int i = project.getProjectDir().lastIndexOf("/");
						String name_dir = project.getProjectDir().substring(i + 1);
						Desktop.getDesktop().open(new File(path + "\\Run\\" + project.getProjectName() + "\\Result\\1_debugs\\" + name_dir + "-debug"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else
				{
					ToastUtil.toast(ResUtil.gs("no-midResult"));
				}
			}	
		});
		
		//重新运行
		JFXButton restart = (JFXButton)temp.lookup("#restart");
		restart.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				listView_achieve.getSelectionModel().select(project);
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
						remove(listView_achieve.getSelectionModel().getSelectedIndex());
						processingController.addNewService(project);
					}
				});
			}
		});
		
	}

	/**
	 * 清空list信息
	 */
	public void clearItem() 
	{
		list_achieve.clear();
		listView_achieve.getItems().clear();
		listView_achieve.setVisible(false);
	}
}
