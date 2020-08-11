package beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import utils.ResUtil;

/**
 * 项目实体类
 * 
 * @author DP
 *
 */
public class ProjectBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String projectName;
	private String projectDir;
	private String projectLocationFile;
	private String createTime;
	private long lastUsedTime;
	private String path_result; // 结果图片路径
	private int locationFrom; // 0:从图片，1：从文件
	private SettingsBean settings;
	private int info;
	private String erroDetail;

	public ProjectBean(String projectName, String projectDir, int locationFrom, String projectLocationFile) {
		super();
		this.projectName = projectName;
		this.projectDir = projectDir;
		this.setLocationFrom(locationFrom);
		this.projectLocationFile = projectLocationFile;
		this.setId(System.currentTimeMillis());
		this.lastUsedTime = getId();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		this.createTime = df.format(new Date());// new Date()为获取当前系统时间
		this.info = 0;

	}
	public ProjectBean(ProjectBean project) {
		this.projectName = project.getProjectName();
		this.projectDir = project.getProjectDir();
		this.setLocationFrom(project.getLocationFrom());
		this.projectLocationFile = project.getProjectLocationFile();
		this.setId(System.currentTimeMillis());
		this.lastUsedTime = getId();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		this.createTime = df.format(new Date());// new Date()为获取当前系统时间
		this.info = 0;
	}
	
	
	public String getErroDetail() {
		return erroDetail;
	}

	public void setErroDetail(String erroDetail) {
		this.erroDetail = erroDetail;
	}

	public int getInfo() {
		return info;
	}

	public void setInfo(int info) {
		this.info = info;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectDir() {
		return projectDir;
	}

	public void setProjectDir(String projectDir) {
		this.projectDir = projectDir;
	}

	public String getProjectLocationFile() {
		return projectLocationFile;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public void setProjectLocationFile(String projectLocationFile) {
		this.projectLocationFile = projectLocationFile;
	}

	@Override
	public String toString() {
		return "工程名：" + getProjectName() + " 工程路径：" + getProjectDir() + " 经纬度由来：" + getLocationFrom() + " 经纬度路径："
				+ getProjectLocationFile();
	}

	public int getLocationFrom() {
		return locationFrom;
	}

	public void setLocationFrom(int locationFrom) {
		this.locationFrom = locationFrom;
	}

	public long getId() {
		return id;
	}

	public String getPath_result() {
		return path_result;
	}

	public void setPath_result(String path_result) {
		this.path_result = path_result;
	}

	public long getLastUsedTime() {
		return lastUsedTime;
	}

	public void setLastUsedTime(long lastUsedTime) {
		this.lastUsedTime = lastUsedTime;
	}

	public String getParam() {
		String parameter = " \"-read_LatALon\" ";
		if (this.getLocationFrom() == 0)
			parameter += "\"_img\" ";
		else {
			parameter += "\"_file\" \"";
			parameter += this.getProjectLocationFile();
			parameter += "\" ";
		}
		parameter += '"' + this.getProjectDir() + '"';
		return parameter;
	}

	public void setId(long id) {
		this.id = id;
	}

	public SettingsBean getSettings() {
		return settings;
	}

	public void setSettings(SettingsBean settings) {
		this.settings = settings;
	}

	/**
	 * 若无参数：0 有未命名参数:1 模板参数 ：2；
	 * 
	 * @return
	 */
	public int getSettingType() {
		if (settings == null) {
			return 0;
		}
		return settings.getSettingType();
	}

	public String getSettingName() {
		if (settings == null) {
			return ResUtil.gs("setting_name_empty");
		} else {
			return settings.getName();
		}
	}

	public String toSettingParameter() {
		String settingPara = "";
		// 网格大小
		settingPara += "\"-grid_w\"" + " \"" + settings.getNetWidth();
		settingPara += "\" " + "\"-grid_h\"" + " \"" + settings.getNetHeight();

		// 是否重叠度先验
		settingPara += "\" " + "\"-try_OP\"" + " "
				+ (settings.isPreCheck()
						? ("\"yes\"" + " " + "\"-hight\"" + " \"" + settings.getFlyHeight() + "\" " + "\"-Focal\""
								+ " \"" + settings.getCameraSize() + '"')
						: "\"no\"");

		// 是否保存中间结果
		settingPara += " " + "\"-save_mid\"" + " " + (settings.isSaveMiddle() ? "\"yes\"" : "\"no\"") + " ";

		// 中英文
		settingPara += "\"-languages\" " + '"' + (settings.getLanguage() == 0 ? "Chinese" : "English") + '"';
		return settingPara;
	}

	public String transToTipStr(boolean isShowSettings) {
		StringBuffer sb = new StringBuffer(ResUtil.gs("project_name"));
		sb.append(getProjectName());
		sb.append("\n");
		sb.append(ResUtil.gs("project_create_time") + ":" + getCreateTime());
		sb.append("\n");
		sb.append(ResUtil.gs("project_path") + getProjectDir());
		sb.append("\n");
		sb.append(ResUtil.gs("project_location_way"));
		if (getLocationFrom() == 0) {
			sb.append(ResUtil.gs("project_location_way_img"));
		} else {
			sb.append(ResUtil.gs("project_location_way_file"));
			sb.append("\n");
			sb.append(ResUtil.gs("project_location_path") + getProjectLocationFile());
		}
		if (isShowSettings && getSettings() != null) {
			sb.append("\n");
			sb.append(getSettings().transToTipStr());
		}
		return sb.toString();
	}
}
