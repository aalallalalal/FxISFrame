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
		int i = this.projectListData.size();
		while(iter.hasNext())
		{
			if(i != 1)
			{
				ProjectBean temp = iter.next();
				projectPath += temp.getProjectDir();
				projectPath += " ";
			}
			else
			{
				ProjectBean temp = iter.next();
				projectPath += temp.getProjectDir();
			}
			i --;
		}
		System.out.println("路径：");
		System.out.println(projectPath);
		return projectPath;
	}
}
