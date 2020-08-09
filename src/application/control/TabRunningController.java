package application.control;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import beans.FinalDataBean;
import beans.MyFxmlBean;
import beans.ProjectBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utils.UIUtil;


public class TabRunningController implements Initializable
{
	@FXML
	private AnchorPane root;
	
	private ObservableList<HBox> list_running = FXCollections.observableArrayList();
	@FXML
	private ListView<HBox> listView_running = new ListView<HBox>();
	
	private List<ProjectBean> list_current = new ArrayList<ProjectBean>();
	
	private Label runInfo;
	
	Image image_running = new Image("resources/norunning.png");
	ImageView imageView_running = new ImageView(image_running);
	
	

	public List<ProjectBean> getList_current()
	{
		return list_current;
	}

	public void setList_current(List<ProjectBean> list_current)
	{
		this.list_current = list_current;
	}


	private ProcessingController processingController;
	//注入processingcontroller
	public void init(ProcessingController controller) 
	{
		processingController = controller;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("初始化running");
		listView_running.setVisible(false);
	}
	
	/**
	 * 将即将运行的任务全部添加到list_running中
	 * 并设置成等待状态
	 */
	void addServiceToList(FinalDataBean finalData) 
	{
		for(int i = 0 ; i < finalData.getProjectListData().size() ; i ++) 
		{
			ProjectBean project = finalData.getProjectListData().get(i);
			add(project);
		}
		listView_running.setItems(list_running);
		listView_running.setVisible(true);
	}
	
	/**
	 * 添加新成员，初始化成等待运行
	 * @param project
	 */
	public void add(ProjectBean project)
	{
		MyFxmlBean fxmlbean = UIUtil.loadFxml(getClass(), "/application/fxml/RunningHBox.fxml");
		HBox temp = (HBox)fxmlbean.getPane();
		setContent(project, temp);
		
		list_running.add(temp);
		list_current.add(project);
	}
	
	/**
	 * 将生成的hbox设置成等待运行状态
	 * @param project
	 * @param temp
	 */
	public void setContent(ProjectBean project, HBox temp) {
		Label project_name = (Label)temp.lookup("#project_name");
		project_name.setText(project.getProjectName());
		runInfo= (Label)temp.lookup("#runningInfo");
		runInfo.setText("等待运行...");
		JFXButton close = (JFXButton)temp.lookup("#close");
		close.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				listView_running.getSelectionModel().select(temp);
				int select = listView_running.getSelectionModel().getSelectedIndex();
				processingController.closeService(select);
			}
		});
	}
	
	/**
	 * 清空lsit中所有内容
	 */
	public void clearItem()
	{
		list_running.clear();
		listView_running.getItems().clear();
		list_current.clear();
	}
	
	/**
	 * 将listView的第一个设置为正在运行的状态
	 */
	public void toRunning()
	{
		ProgressIndicator p = (ProgressIndicator)list_running.get(0).lookup("#run");
		p.setVisible(true);
		runInfo = (Label)list_running.get(0).lookup("#runningInfo");
		runInfo.setText("正在运行...");
		/*
		 * ProgressBar progressbar =
		 * (ProgressBar)list_running.get(0).lookup("#progress");
		 * progressbar.setProgress(-1);
		 */
	}
	
	/**
	 * 更新此controller中的数据
	 * @author window's xp
	 *
	 */
	public void updateRemove(int i)
	{
		list_running.remove(i);
		list_current.remove(i);
		listView_running.setItems(list_running);
	}

	/*public void updateRunningInfo(String lineStr)
	{
		runInfo.setText(lineStr);
	}*/
}
