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

}
