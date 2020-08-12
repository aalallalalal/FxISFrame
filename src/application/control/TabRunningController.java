package application.control;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import beans.FinalDataBean;
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
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import utils.ResUtil;
import utils.UIUtil;
import views.MyToolTip;


public class TabRunningController implements Initializable
{
	@FXML
	private AnchorPane root;
	
	private ObservableList<ProjectBean> list_running = FXCollections.observableArrayList();
	@FXML
	private ListView<ProjectBean> listView_running = new ListView<ProjectBean>();
	
	private Label runInfo;
	
	public ObservableList<ProjectBean> getList_running()
	{
		return list_running;
	}

	public void setList_running(ObservableList<ProjectBean> list_running)
	{
		this.list_running = list_running;
	}

	private ProcessingController processingController;
	public void init(ProcessingController controller) 
	{
		processingController = controller;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		BackgroundImage myBI= new BackgroundImage(new Image("/resources/wushuju.png"), 
			     BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, 
			      BackgroundSize.DEFAULT); 
			//then you set to your node 
		root.setBackground(new Background(myBI)); 
		listView_running.setItems(list_running);
		listView_running.setCellFactory(new Callback<ListView<ProjectBean>, ListCell<ProjectBean>>()
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
							MyFxmlBean fxmlbean = UIUtil.loadFxml(getClass(), "/application/fxml/RunningHBox.fxml");
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
	
	/**
	 * 将即将运行的任务全部添加到list_running中
	 * 并设置成等待状态
	 */
	void addServiceToList(FinalDataBean finalData) 
	{
		for(int i = 0 ; i < finalData.getProjectListData().size() ; i ++) 
		{
			add(finalData.getProjectListData().get(i));
		}
	}
	
	/**
	 * 添加新成员，初始化成等待运行
	 * @param project
	 */
	public void add(ProjectBean project)
	{
		list_running.add(project);
		listView_running.setVisible(true);
	}
	
	/**
	 * 初始化cell中的graphic
	 * @param project
	 * @param temp
	 */
	public void setContent(ProjectBean project, HBox temp) {
		Label project_name = (Label)temp.lookup("#project_name");
		project_name.setText(project.getProjectName());
		project_name.setTooltip(new MyToolTip(project.transToTipStr(true)));
		runInfo= (Label)temp.lookup("#runningInfo");
		
		if(project.getInfo() == 0)
		{	
			runInfo.setText(ResUtil.gs("wait"));
		}
		else
		{
			runInfo.setText(ResUtil.gs("running"));
			ProgressIndicator p = (ProgressIndicator)temp.lookup("#run"); 
			p.setVisible(true);
		}
		//关闭任务
		JFXButton close = (JFXButton)temp.lookup("#close");
		close.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				listView_running.getSelectionModel().select(project);
				int select = listView_running.getSelectionModel().getSelectedIndex();
				processingController.closeService(select);
			}
		});
		//查看参数
		JFXButton projectDetail = (JFXButton)temp.lookup("#paramDetail");
		projectDetail.setOnAction(new EventHandler<ActionEvent>()
		{
			
			@Override
			public void handle(ActionEvent event)
			{
				MyFxmlBean settingDialogBean = UIUtil.openDialog(getClass(),
						"/application/fxml/SettingsDialog.fxml", ConstSize.Dialog_Frame_Width,
						ConstSize.Dialog_Frame_Height+80, project.getProjectName(), processingController.stage);
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
	}
	
	/**
	 * 清空lsit中所有内容
	 */
	public void clearItems()
	{
		list_running.clear();
		listView_running.getItems().clear();
	}
	
	/**
	 * 将listView的第一个设置为正在运行的状态
	 */
	  public void toRunning() 
	  { 
		  list_running.get(0).setInfo(1);
	  }
	 
	
	/**
	 * 更新此controller中的数据
	 * @author window's xp
	 *
	 */
	public void updateRemove(int i)
	{
		list_running.remove(i);
		listView_running.setItems(list_running);
	}

	public void updatecontrol()
	{
		listView_running.setVisible(false);
	}

	public void appointSelect(ProjectBean project)
	{
		listView_running.getSelectionModel().select(project);
	}

}
