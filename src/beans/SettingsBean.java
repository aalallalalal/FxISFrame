package beans;

/**
 * 参数实体类
 * 
 * @author DP
 *
 */
public class SettingsBean {

	private boolean isSaveMiddle;
	private String netWidth, netHeight;
	private boolean isPreCheck;
	private String flyHeight;
	private String cameraSize;

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

}
