package application.control;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import base.controller.ConfirmDialogController.CallBack;
import beans.DBRecordBean;
import consts.ConstSize;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import utils.DBUtil;
import utils.FileUtil;
import utils.ResUtil;
import utils.ToastUtil;
import utils.UIUtil;

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
	private TableColumn<DBRecordBean, Object> paramInfogroup;
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
	private TableColumn<DBRecordBean, Object> runinfo;
	@FXML
	private TableColumn<DBRecordBean, String> starttime;
	@FXML
	private TableColumn<DBRecordBean, String> endtime;
	@FXML
	private TableColumn<DBRecordBean, Boolean> state;
	@FXML
	private TableColumn<DBRecordBean, String> failreason;
	
	@FXML
	private Label label;
	
	@FXML
	private JFXButton clearAll;
	
	@FXML
	private JFXButton clear;
	
	ObservableList<DBRecordBean> list = FXCollections.observableArrayList();
	ArrayList<DBRecordBean> listarray = DBUtil.selectAll();

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		for(DBRecordBean temp : listarray)
			list.add(temp);
		HistoryTableView.setItems(list);
		
		initTableView();
		//鼠标双击事件
		HistoryTableView.setOnMouseClicked(new EventHandler<MouseEvent>()
		{

			@Override
			public void handle(MouseEvent event)
			{
				if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2)
				{
					System.out.println("历史记录，双击打开");
					onOpenFileSystem();
				}
			}
		});
		
		label.setText(ResUtil.gs("total") + list.size() + ResUtil.gs("item"));//初始化总共多少条历史记录，仅限于点击时刻数据库中拥有的。
	}

	/**
	 * 填充historytableview
	 */
	private void initTableView()
	{
		HistoryTableView.setTableMenuButtonVisible(true);
		HistoryTableView.setEditable(false);
		HistoryTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		HistoryTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
		
		starttime.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DBRecordBean,String>, ObservableValue<String>>()
		{
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<DBRecordBean, String> param)
			{
				SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateStr = dateformat.format(param.getValue().getProject().getLastRuntime());
				SimpleStringProperty time = new SimpleStringProperty(dateStr);
				return time;
			}
		});
		
		endtime.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DBRecordBean,String>, ObservableValue<String>>()
		{
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<DBRecordBean, String> param)
			{
				SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateStr = dateformat.format(param.getValue().getRunTime());
				SimpleStringProperty time = new SimpleStringProperty(dateStr);
				return time;
			}
		});
	
		state.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DBRecordBean,Boolean>, ObservableValue<Boolean>>()
		{
			
			@Override
			public ObservableValue<Boolean> call(CellDataFeatures<DBRecordBean, Boolean> param)
			{
				SimpleBooleanProperty flag = new SimpleBooleanProperty(param.getValue().getProject().getErroDetail().equals(null));
				return flag;
			}
		});

		failreason.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DBRecordBean,String>, ObservableValue<String>>()
		{
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<DBRecordBean, String> param)
			{
				SimpleStringProperty temp = new SimpleStringProperty(param.getValue().getProject().getErroDetail());
				return temp;
			}
		});

	}
	
	/**
	 * 清空全部历史记录
	 */
	@FXML
	void onClearAll()
	{
		UIUtil.openConfirmDialog(getClass(), ConstSize.Confirm_Dialog_Frame_Width,
				ConstSize.Confirm_Dialog_Frame_Height, ResUtil.gs("Clear_All"), ResUtil.gs("are_you_sure_to_clear_all"),
				(Stage) root.getScene().getWindow(), new CallBack() {
					@Override
					public void onCancel() {
					}

					@Override
					public void onConfirm() {
						for(DBRecordBean temp : list) {
							FileUtil.deleteDir(new File(temp.getResultPath()));
						}
						list.clear();
					}
				});
		
	}
	
	/**
	 * 删除某一项历史记录
	 */
	@FXML
	void onClear() 
	{
		DBRecordBean temp = HistoryTableView.getSelectionModel().getSelectedItem();
		FileUtil.deleteDir(new File(temp.getResultPath()));
		list.remove(temp);
	}
	
	/**
	 * 打开文件夹 所在位置
	 */
	@FXML
	void onOpenFileSystem()
	{
		DBRecordBean temp = HistoryTableView.getSelectionModel().getSelectedItem();
		try
		{
			Desktop.getDesktop().open(new File(temp.getResultPath()));
		} catch (IOException e)
		{
			e.printStackTrace();
			ToastUtil.toast(ResUtil.gs("Failed_to_open_folder"));
		}
	}
	
	
	

}
