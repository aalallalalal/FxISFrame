package beans;

import java.util.List;

/**
 * 通过经纬度查出的数据值
 */
public class GoogleMapGeocodingBean {

	private Plus_code plus_code;
	private List<Results> results;
	private String status;

	public void setPlus_code(Plus_code plus_code) {
		this.plus_code = plus_code;
	}

	public Plus_code getPlus_code() {
		return plus_code;
	}

	public void setResults(List<Results> results) {
		this.results = results;
	}

	public List<Results> getResults() {
		return results;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public class Plus_code {

		private String compound_code;
		private String global_code;

		public void setCompound_code(String compound_code) {
			this.compound_code = compound_code;
		}

		public String getCompound_code() {
			return compound_code;
		}

		public void setGlobal_code(String global_code) {
			this.global_code = global_code;
		}

		public String getGlobal_code() {
			return global_code;
		}

	}

	public class Results {

		private String formatted_address = "";

		public void setFormatted_address(String formatted_address) {
			this.formatted_address = formatted_address;
		}

		public String getFormatted_address() {
			if (formatted_address == null) {
				return "";
			}
			return formatted_address;
		}

	}
}