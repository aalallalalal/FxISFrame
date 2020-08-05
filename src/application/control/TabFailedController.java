package application.control;

import java.net.URL;
import java.util.ResourceBundle;

import beans.ProjectBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class TabFailedController implements Initializable
{
	@FXML
	AnchorPane root;
	
	ObservableList<FailedHBox> list_failed = FXCollections.observableArrayList();
	
	@FXML
	ListView<FailedHBox> listView_failed = new ListView<FailedHBox>();

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
		// TODO Auto-generated method stub
		
	}
	
	public void addFailedHBox(String name, String reason)
	{
		list_failed.add(new FailedHBox(name, reason));
		listView_failed.setItems(list_failed);
	}

	private class FailedHBox extends HBox{
		
		Label project_name = new Label();   //工程名
		Label FailDetailInfo = new Label();
		
		public FailedHBox(String project_name, String reason) 
		{
			this.project_name.setText(project_name);
			FailDetailInfo.setText(reason);
			super.getChildren().addAll(this.project_name, FailDetailInfo);
		}

		@Override
		protected void layoutChildren()
		{
			// TODO Auto-generated method stub
			super.layoutChildren();
			setSpacing(50);
		}
	}

	
}
