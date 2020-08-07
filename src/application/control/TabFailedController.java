package application.control;

import java.net.URL;
import java.util.ResourceBundle;

import beans.MyFxmlBean;
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
	}

	public void clearItem()
	{
		list_failed.clear();
		listView_failed.getItems().clear();
	}

	

}
