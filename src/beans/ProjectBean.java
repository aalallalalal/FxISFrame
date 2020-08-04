package beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

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

	public ProjectBean(String projectName, String projectDir, int locationFrom, String projectLocationFile) {
		super();
		this.projectName = projectName;
		this.projectDir = projectDir;
		this.setLocationFrom(locationFrom);
		this.projectLocationFile = projectLocationFile;
		this.id = System.currentTimeMillis();
		this.lastUsedTime = id;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		this.createTime = df.format(new Date());// new Date()为获取当前系统时间
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
	
	public String getParam()
	{
		String parameter = " \"-read_LatALon\" ";
		if(this.getLocationFrom() == 0)
			parameter += "\"_img\" ";
		else
		{
			parameter += "\"_file \"";
			parameter += this.getProjectLocationFile();
			parameter += " ";
		}
		parameter += '"' + this.getProjectDir() + '"';
		return parameter;
	}
}
