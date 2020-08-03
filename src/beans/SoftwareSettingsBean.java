package beans;

/**
 * 软件设置实体类
 * 
 * @author DP
 *
 */
public class SoftwareSettingsBean {
	private int language = 0; // 0中文，1英文

	public int getLanguage() {
		return language;
	}

	public void setLanguage(int language) {
		this.language = language;
	}

}
