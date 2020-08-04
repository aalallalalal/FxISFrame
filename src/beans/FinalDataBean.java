package beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.collections.ObservableList;

/**
 * 最后的数据
 * 
 * @author DP
 *
 */
public class FinalDataBean {
	private ObservableList<ProjectBean> projectListData;
	private SettingsBean settings;
	public static String para_Exe;
	public static String setting;
	public static List<ProjectBean> pathList = new ArrayList<ProjectBean>();

	public FinalDataBean(ObservableList<ProjectBean> projectListData, SettingsBean settings) {
		super();
		this.projectListData = projectListData;
		this.settings = settings;
	}

	public ObservableList<ProjectBean> getProjectListData() {
		return projectListData;
	}

	public void setProjectListData(ObservableList<ProjectBean> projectListData) {
		this.projectListData = projectListData;
	}

	public SettingsBean getSettings() {
		return settings;
	}

	public void setSettings(SettingsBean settings) {
		this.settings = settings;
	}

	
	/**
	 * 将项目列表的路径参数和其他参数转换成String形式
	 * @return
	 */
	public void toPathParameter()
	{
		Iterator<ProjectBean> iter =  this.projectListData.iterator();
		while(iter.hasNext())
		{
			ProjectBean temp = iter.next();
			pathList.add(temp);
		}
	}
	
	public String toSettingParameter() {
		String settingPara = "";
		//网格大小
		settingPara += "\"-grid_w\"" + " \"" + settings.getNetWidth();
		settingPara += "\" " + "\"-grid_h\"" + " \"" + settings.getNetHeight();
		
		//是否重叠度先验
		settingPara += "\" " + "\"-try_OP\"" + " " + (settings.isPreCheck() ? 
						("\"yes\"" + " " + "\"-hight\"" + " \"" + settings.getFlyHeight() + "\" " + "\"-Focal\"" + " \"" + settings.getCameraSize() + '"') 
						:"\"no\"");
		
		//是否保存中间结果
		settingPara += " " + "\"-save_mid\"" + " " + (settings.isSaveMiddle() ? "\"yes\"" : "\"no\"") + " ";
		
		//中英文
		settingPara += "\"-languages\" " +  '"' + (settings.getLanguage() == 0 ? "Chinese" : "English") + '"';
		return settingPara;
	}
}
