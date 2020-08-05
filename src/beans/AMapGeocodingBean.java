package beans;

/**
 * 高德api返回数据
 * @author DP
 *
 */
public class AMapGeocodingBean {

	private String status;
	private Regeocode regeocode;
	private String info;
	private String infocode;

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setRegeocode(Regeocode regeocode) {
		this.regeocode = regeocode;
	}

	public Regeocode getRegeocode() {
		return regeocode;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getInfo() {
		return info;
	}

	public void setInfocode(String infocode) {
		this.infocode = infocode;
	}

	public String getInfocode() {
		return infocode;
	}

	public class Regeocode {
		private String formatted_address;

		public void setFormatted_address(String formatted_address) {
			this.formatted_address = formatted_address;
		}

		public String getFormatted_address() {
			return formatted_address;
		}
	}
}
