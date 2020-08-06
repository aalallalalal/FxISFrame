package application.control;

import java.net.URL;
import java.util.ResourceBundle;

import beans.SettingsBean;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;


public class TabParamController implements Initializable
{
	@FXML
	BorderPane root;
	
	@FXML
	private Label height_net = new Label();
	
	@FXML
	private Label width_net = new Label();
	
	@FXML
	private Label height_fly = new Label();
	
	@FXML
	private Label camera_size = new Label();
	
	@FXML
	private Label value_height_fly = new Label();
	
	@FXML
	private Label value_camera_size = new Label();
	
	@FXML
	private Label saveMid = new Label();
	
	@FXML
	private Label priorrate = new Label();
	
	
	private ProcessingController processingController;
	public void init(ProcessingController controller, SettingsBean settings) 
	{
		processingController = controller;
		height_fly.setVisible(true);
		camera_size.setVisible(true);
		height_net.setText(settings.getNetHeight() + "px");
		width_net.setText(settings.getNetWidth() + "px");
		if(settings.isPreCheck())
		{
			priorrate.setText("是");
			value_height_fly.setText(settings.getFlyHeight() + "m");
			value_camera_size.setText(settings.getCameraSize() + "mm");
		}
		else
		{
			priorrate.setText("否");
			height_fly.setVisible(false);
			camera_size.setVisible(false);
			
		}
		if(settings.isSaveMiddle())
			saveMid.setText("是");
		else
			saveMid.setText("否");
		
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
	
	}

}
