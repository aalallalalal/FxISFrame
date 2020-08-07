package beans;

import java.io.Serializable;

/**
 * 参数实体类
 * 
 * @author DP
 *
 */
public class SettingsBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isSaveMiddle;
	private String netWidth, netHeight;
	private boolean isPreCheck;
	private int preCheckWay;// 0,1
	private String flyHeight; //way0
	private String cameraSize;//way0
	private String gsd; //way1
	private int language; // 0中文，1英文

	public boolean isSaveMiddle() {
		return isSaveMiddle;
	}

	public void setSaveMiddle(boolean isSaveMiddle) {
		this.isSaveMiddle = isSaveMiddle;
	}

	public String getNetWidth() {
		return netWidth;
	}

	public void setNetWidth(String netWidth) {
		this.netWidth = netWidth;
	}

	public String getNetHeight() {
		return netHeight;
	}

	public void setNetHeight(String netHeight) {
		this.netHeight = netHeight;
	}

	public boolean isPreCheck() {
		return isPreCheck;
	}

	public void setPreCheck(boolean isPreCheck) {
		this.isPreCheck = isPreCheck;
	}

	public String getFlyHeight() {
		return flyHeight;
	}

	public void setFlyHeight(String flyHeight) {
		this.flyHeight = flyHeight;
	}

	public String getCameraSize() {
		return cameraSize;
	}

	public void setCameraSize(String cameraSize) {
		this.cameraSize = cameraSize;
	}

	public int getLanguage() {
		return language;
	}

	public void setLanguage(int language) {
		this.language = language;
	}

	public int getPreCheckWay() {
		return preCheckWay;
	}

	public void setPreCheckWay(int preCheckWay) {
		this.preCheckWay = preCheckWay;
	}

	public String getGsd() {
		return gsd;
	}

	public void setGsd(String gsd) {
		this.gsd = gsd;
	}

}
