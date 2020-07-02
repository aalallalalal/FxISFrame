package beans;

/**
 * 项目实体类
 * @author DP
 *
 */
public class ProjectBean {
	private String projectName;
	private String projectDir;
	private String projectLocationFile;

	
	public ProjectBean(String projectName, String projectDir, String projectLocationFile) {
		super();
		this.projectName = projectName;
		this.projectDir = projectDir;
		this.projectLocationFile = projectLocationFile;
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

	public void setProjectLocationFile(String projectLocationFile) {
		this.projectLocationFile = projectLocationFile;
	}

	@Override
	public String toString() {
		return "工程名：" + getProjectName() + " 工程路径：" + getProjectDir() + " 经纬度路径：" + getProjectLocationFile();
	}
}
