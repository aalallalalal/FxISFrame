package application.control;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import beans.ProjectBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class TabAchieveController implements Initializable
{
	@FXML
	AnchorPane root;

	ObservableList<AchieveHBox> list_achieve = FXCollections.observableArrayList();
	
	@FXML
	ListView<AchieveHBox> listView_achieve = new ListView<AchieveHBox>();
	
	Image image_succ = new Image("resources/nosucced.png");
	ImageView imageView_succed = new ImageView(image_succ);
	
	private ProcessingController processingController;
	public void init(ProcessingController controller) 
	{
		processingController = controller;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		listView_achieve.setVisible(false);
	}
	
	public void addAchieveHBox(ProjectBean project) 
	{
		AchieveHBox temp = new AchieveHBox(project);
		list_achieve.add(temp);
		listView_achieve.setItems(list_achieve);
		listView_achieve.setVisible(true);
	}
	
	public void clearItem() 
	{
		list_achieve.clear();
		listView_achieve.getItems().clear();
	}

	protected class AchieveHBox extends HBox{
		
		Label project_name = new Label();   //工程名
		
		JFXButton result_picture = new JFXButton("查看拼接结果");
		JFXButton result_mid = new JFXButton("查看中间结果");
		
		Button openinFile = new Button();
		Image image_openFile = new Image("resources/guanbi.png");
		ImageView imageView_open = new ImageView(image_openFile);
		
		public AchieveHBox(ProjectBean project)
		{
			project_name.setText(project.getProjectName());
			openinFile.setGraphic(imageView_open);
			openinFile.setOnAction(new EventHandler<ActionEvent>()
			{
				
				@Override
				public void handle(ActionEvent event)
				{
					try {
						String path = System.getProperty("user.dir");
						Desktop.getDesktop().open(new File(path + "\\Run\\Result\\" + project.getProjectName()));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			
			result_picture.setOnAction(new EventHandler<ActionEvent>()
			{
				
				@Override
				public void handle(ActionEvent event)
				{
					
				}
			});
			
			result_mid.setOnAction(new EventHandler<ActionEvent>()
			{
				
				@Override
				public void handle(ActionEvent event)
				{
					
				}
			});
			
			super.getChildren().addAll(project_name, result_picture, result_mid);
		}

		@Override
		protected void layoutChildren()
		{
			// TODO Auto-generated method stub
			super.layoutChildren();
			setSpacing(20);
		}
		
	}
}
