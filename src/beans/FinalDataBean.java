package beans;

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

	public FinalDataBean(ObservableList<ProjectBean> projectListData, SettingsBean settings) {
		super();
		this.projectListData = projectListData;
		this.settings = settings;
		for(ProjectBean project : this.projectListData)
		{
			project.setSettings(this.settings);
		}
	}

	public FinalDataBean(ObservableList<ProjectBean> projectListData) {
		super();
		this.projectListData = projectListData;
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
