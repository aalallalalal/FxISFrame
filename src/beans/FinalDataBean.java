package beans;

import java.util.Iterator;

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
	public String toParameter()
	{
		String projectPath = "";
		Iterator<ProjectBean> iter =  this.projectListData.iterator();
		while(iter.hasNext())
		{
			ProjectBean temp = iter.next();
			projectPath += temp.getProjectDir();
		}
		return projectPath;
	}
}
