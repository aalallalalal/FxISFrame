package beans;

import java.io.Serializable;

import utils.ResUtil;

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
	private int settingType = 1;// 1：未命名参数 2：模板参数
	private long id = 0;
	private String name = "";
	private static final long serialVersionUID = 1L;
	private boolean isSaveMiddle;
	private String netWidth, netHeight;
	private boolean isPreCheck;
	private int preCheckWay;// 0,1
	private String flyHeight; // way0
	private String cameraSize;// way0
	private String gsd; // way1
	private long lastUserTime = 0;

	public SettingsBean() {
		this.id = System.currentTimeMillis();
		this.setLastUsedTime(this.id);
	}

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

	public String getName() {
		if (getSettingType() == 1) {
			return ResUtil.gs("setting_name_not");
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
		setSettingType(2);
	}

	public int getSettingType() {
		return settingType;
	}

	public void setSettingType(int settingType) {
		this.settingType = settingType;
	}

	public long getId() {
		return id;
	}

	/**
	 * 一样的数据new个settingsbean出来
	 * 
	 * @return
	 */
	public SettingsBean copyToANew(String name) {
		SettingsBean temp = new SettingsBean();
		temp.setCameraSize(getCameraSize());
		temp.setSettingType(getSettingType());
		temp.setFlyHeight(getFlyHeight());
		temp.setGsd(getGsd());
		temp.setName(name);
		temp.setNetHeight(getNetHeight());
		temp.setNetWidth(getNetWidth());
		temp.setPreCheck(isPreCheck());
		temp.setPreCheckWay(getPreCheckWay());
		temp.setSaveMiddle(isSaveMiddle());
		return temp;
	}

	/**
	 * 模板转为临时
	 * 
	 * @return
	 */
	public SettingsBean changeTempToCustom() {
		SettingsBean custom = new SettingsBean();
		custom.setCameraSize(getCameraSize());
		custom.setSettingType(1);
		custom.setFlyHeight(getFlyHeight());
		custom.setGsd(getGsd());
		custom.setName("");
		custom.setNetHeight(getNetHeight());
		custom.setNetWidth(getNetWidth());
		custom.setPreCheck(isPreCheck());
		custom.setPreCheckWay(getPreCheckWay());
		custom.setSaveMiddle(isSaveMiddle());
		return custom;
	}

	public int getLanguage() {
		return 0;
	}

	public long getLastUserTime() {
		return lastUserTime;
	}

	public void setLastUsedTime(long lastUserTime) {
		this.lastUserTime = lastUserTime;
	}
	
	/**
	 * 将settingsBean 转化为tip可用字符串
	 * @param setting
	 * @return
	 */
	public String transToTipStr() {
		StringBuffer sb = new StringBuffer(ResUtil.gs("setting_name"));
		sb.append(getName());
		sb.append("\n");
		sb.append(ResUtil.gs("setting_tips_netsize", getNetWidth(), getNetHeight()));
		sb.append("\n");
		sb.append(ResUtil.gs("setting_tips_pre_check"));
		if (!isPreCheck()) {
			sb.append(ResUtil.gs("setting_tips_no"));
		} else {
			sb.append(ResUtil.gs("setting_tips_yes"));
			sb.append("\n");
			if (getPreCheckWay() == 0) {
				sb.append(ResUtil.gs("setting_tips_way1", getFlyHeight(), getCameraSize()));
			} else {
				sb.append(ResUtil.gs("setting_tips_way2", getGsd()));
			}
		}
		sb.append("\n");
		sb.append(ResUtil.gs("setting_tips_save_middle",
				isSaveMiddle() ? ResUtil.gs("setting_tips_yes") : ResUtil.gs("setting_tips_no")));
		return sb.toString();
	}
}
