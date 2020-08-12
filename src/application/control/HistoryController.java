package application.control;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import beans.DBRecordBean;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import utils.DBUtil;

public class HistoryController implements Initializable
{
	@FXML
	private BorderPane root;
	
	@FXML
	private TableView<DBRecordBean> HistoryTableView;
	@FXML
	private TableColumn<DBRecordBean, String> project_name;
	@FXML
	private TableColumn<DBRecordBean, String> pictures_dir;
	@FXML
	private TableColumn<DBRecordBean, Number> inputway;
	@FXML
	private TableColumn<DBRecordBean, String> location_dir;
	@FXML
	private TableColumn<DBRecordBean, String> height_net;
	@FXML
	private TableColumn<DBRecordBean, String> width_net;
	@FXML
	private TableColumn<DBRecordBean, Boolean> isPreCheck;
	@FXML
	private TableColumn<DBRecordBean, String> flyHeight;
	@FXML
	private TableColumn<DBRecordBean, String> CameraSize;
	@FXML
	private TableColumn<DBRecordBean, String> Gsd;
	@FXML
	private TableColumn<DBRecordBean, Boolean> isSave_middle;
	@FXML
	private TableColumn<DBRecordBean, String> runntime;
	
	@FXML
	private Label label;
	
	ObservableList<DBRecordBean> list = FXCollections.observableArrayList();
	ArrayList<DBRecordBean> listarray = DBUtil.selectAll();

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		HistoryTableView.setItems(list);
		for(DBRecordBean temp : listarray)
		{
			list.add(temp);
		}
		initTableView();
	}

	
	private void initTableView()
	{
		project_name.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DBRecordBean,String>, ObservableValue<String>>()
		{
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<DBRecordBean, String> param)
			{
				SimpleStringProperty name = new SimpleStringProperty(param.getValue().getProject().getProjectName());
				return name;
			}
		});
		
		pictures_dir.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DBRecordBean,String>, ObservableValue<String>>()
		{
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<DBRecordBean, String> param)
			{
				SimpleStringProperty picture_dir = new SimpleStringProperty(param.getValue().getProject().getProjectDir());
				return picture_dir;
			}
		});
		
		inputway.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DBRecordBean,Number>, ObservableValue<Number>>()
		{
			
			@Override
			public ObservableValue<Number> call(CellDataFeatures<DBRecordBean, Number> param)
			{
				SimpleIntegerProperty way = new SimpleIntegerProperty(param.getValue().getProject().getLocationFrom());
				return way;
			}
		});

		location_dir.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DBRecordBean,String>, ObservableValue<String>>()
		{
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<DBRecordBean, String> param)
			{
				SimpleStringProperty dir = new SimpleStringProperty(param.getValue().getProject().getProjectLocationFile());
				return dir;
			}
		});

		height_net.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DBRecordBean,String>, ObservableValue<String>>()
		{
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<DBRecordBean, String> param)
			{
				SimpleStringProperty str = new SimpleStringProperty(param.getValue().getProject().getSettings().getNetHeight());
				return str;
			}
		});
		
		width_net.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DBRecordBean,String>, ObservableValue<String>>()
		{
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<DBRecordBean, String> param)
			{
				SimpleStringProperty width = new SimpleStringProperty(param.getValue().getProject().getSettings().getNetWidth());
				return width;
			}
		});

		isPreCheck.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DBRecordBean,Boolean>, ObservableValue<Boolean>>()
		{
			
			@Override
			public ObservableValue<Boolean> call(CellDataFeatures<DBRecordBean, Boolean> param)
			{
				SimpleBooleanProperty pre = new SimpleBooleanProperty(param.getValue().getProject().getSettings().isPreCheck());
				return pre;
			}
		});
		
		flyHeight.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DBRecordBean,String>, ObservableValue<String>>()
		{
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<DBRecordBean, String> param)
			{
				SimpleStringProperty fly = new SimpleStringProperty(param.getValue().getProject().getSettings().getFlyHeight());
				return fly;
			}
		});

		CameraSize.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DBRecordBean,String>, ObservableValue<String>>()
		{
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<DBRecordBean, String> param)
			{
				SimpleStringProperty size = new SimpleStringProperty(param.getValue().getProject().getSettings().getCameraSize());
				return size;
			}
		});
		
		isSave_middle.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DBRecordBean,Boolean>, ObservableValue<Boolean>>()
		{
			
			@Override
			public ObservableValue<Boolean> call(CellDataFeatures<DBRecordBean, Boolean> param)
			{
				SimpleBooleanProperty flag = new SimpleBooleanProperty(param.getValue().getProject().getSettings().isSaveMiddle());
				return flag;
			}
		});
		
		runntime.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DBRecordBean,String>, ObservableValue<String>>()
		{
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<DBRecordBean, String> param)
			{
				SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateStr = dateformat.format(param.getValue().getRunTime());
				SimpleStringProperty size = new SimpleStringProperty(dateStr);
				return size;
			}
		});
	}
	

}
