package beans;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 项目实体类
 * 
 * @author DP
 *
 */
public class ProjectBean {
	private String projectName;
	private String projectDir;
	private String projectLocationFile;
	private String createTime;
	
	private int locationFrom; // 0:从图片，1：从文件

	public ProjectBean(String projectName, String projectDir, int locationFrom, String projectLocationFile) {
		super();
		this.projectName = projectName;
		this.projectDir = projectDir;
		this.setLocationFrom(locationFrom);
		this.projectLocationFile = projectLocationFile;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
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
	
	public String getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(String createTime)
	{
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
}
