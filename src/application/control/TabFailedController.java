package application.control;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import utils.UIUtil;

public class TabFailedController implements Initializable
{
	@FXML
	AnchorPane root;
	
	ObservableList<HBox> list_failed = FXCollections.observableArrayList();
	
	@FXML
	ListView<HBox> listView_failed = new ListView<HBox>();

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
	}
	
	public void addFailedHBox(ProjectBean project, String reason)
	{
		MyFxmlBean fxmlbean = UIUtil.loadFxml(getClass(), "/application/fxml/FailedHBox.fxml");
		HBox temp = (HBox)fxmlbean.getPane();
		setContent(temp, project, reason);
		list_failed.add(temp);
		listView_failed.setItems(list_failed);
		listView_failed.setVisible(true);
	}
	
	/**
	 * 设置新添加的运行失败的item
	 * @param name
	 * @param reason
	 */
	private void setContent(HBox temp, ProjectBean project, String reason)
	{
		Label project_name = (Label)temp.lookup("#project_name");
		project_name.setText(project.getProjectName());
		Label faileddetail = (Label)temp.lookup("#faildetailinfo");
		faileddetail.setText(reason);
		Label currenttime = (Label)temp.lookup("#currenttime");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String time = df.format(new Date());// new Date()为获取当前系统时间
		currenttime.setText(time);
		
		JFXButton paramDetail = (JFXButton)temp.lookup("#paramDetail");
		paramDetail.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				
			}
		});
		
		JFXButton restart = (JFXButton)temp.lookup("#restart");
		restart.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				processingController.addnewservice(project);
			}
		});
	}

	/**
	 * 清空fail tab中的数据
	 */
	public void clearItem()
	{
		list_failed.clear();
		listView_failed.getItems().clear();
	}

	

}
