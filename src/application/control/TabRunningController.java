package application.control;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import base.controller.ConfirmDialogController.CallBack;
import beans.FinalDataBean;
import beans.ProjectBean;
import consts.ConstSize;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.UIUtil;

public class TabRunningController implements Initializable
{
	@FXML
	AnchorPane root;
	
	ObservableList<RunningHBox> list_running = FXCollections.observableArrayList();
	@FXML
	ListView<RunningHBox> listView_running = new ListView<RunningHBox>();
	
	Image image_running = new Image("resources/norunning.png");
	ImageView imageView_running = new ImageView(image_running);
	
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
	
	//向列表中添加待运行的任务
	void addServiceToList() 
	{
		System.out.println("加入数据");
		for(int i = 0 ; i < FinalDataBean.pathList.size() ; i ++) 
		{
			RunningHBox temp = new RunningHBox(FinalDataBean.pathList.get(i));
			list_running.add(temp);
		}
		listView_running.setItems(list_running);
		listView_running.setVisible(true);
		listView_running.refresh();
	}
	
	public void clearItem()
	{
		list_running.clear();
		listView_running.getItems().clear();
	}
	
	protected class RunningHBox extends HBox{
		
		Label project_name = new Label();   //工程名
		
		VBox vbox = new VBox();
		ProgressBar p = new ProgressBar(0);//bar
		Label currentState = new Label("等待运行");
		
		JFXButton cancel = new JFXButton(); //取消进程按钮
		Image image_cancel = new Image("resources/guanbi.png");
		ImageView imageView_cancel = new ImageView(image_cancel);
		
		public RunningHBox(ProjectBean project) 
		{
			project_name.setText(project.getProjectName());
			System.out.println(project.getProjectName());
			vbox.getChildren().addAll(p, currentState);
			
			cancel.setGraphic(imageView_cancel);
			cancel.setOnAction(new EventHandler<ActionEvent>()
			{
				@Override
				public void handle(ActionEvent event)
				{
					UIUtil.openConfirmDialog(getClass(), ConstSize.Confirm_Dialog_Frame_Width,
							ConstSize.Confirm_Dialog_Frame_Height, "取消", "等待拼接中，确定取消该拼接任务？",
							(Stage) root.getScene().getWindow(), new CallBack() {
								@Override
								public void onCancel() {
								}

								@Override
								public void onConfirm() {
									System.out.println("取消该拼接任务！");
									int i = listView_running.getSelectionModel().getSelectedIndex();
									list_running.remove(i);
									listView_running.refresh();
									FinalDataBean.pathList.remove(i - 1); 
								}
							});
				}
			});
			
			super.getChildren().addAll(project_name);
			
		}
		
		@Override
		protected void layoutChildren()
		{
			super.layoutChildren();
			setSpacing(50);
		}
		
		void toRunning()
		{
			currentState.setText("正在运行，请稍候...");
			vbox.getChildren().remove(0);
		}
	}

	
	
}
