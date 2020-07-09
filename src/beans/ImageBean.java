package beans;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class ImageBean {
	private SimpleStringProperty path;
	private SimpleStringProperty name;
	private String longitudeRef ;
	private String latitudeRef ;
	private SimpleDoubleProperty longitude;// 经度
	private SimpleDoubleProperty latitude;// 维度
	private SimpleStringProperty height;// 高度
	private boolean selected;

	public ImageBean(String path, String name) {
		this.path = new SimpleStringProperty(path);
		this.name = new SimpleStringProperty(name);
		this.longitude = new SimpleDoubleProperty();
		this.latitude = new SimpleDoubleProperty();
		this.height = new SimpleStringProperty();
	}

	public String getPath() {
		return path.get();
	}

	public void setPath(String path) {
		this.path.set(path);
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public double getLongitude() {
		return longitude.get();
	}

	public void setLongitude(double longitude) {
		this.longitude.set(longitude);
	}

	public double getLatitude() {
		return latitude.get();
	}

	public void setLatitude(double latitude) {
		this.latitude.set(latitude);
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getHeight() {
		return height.get();
	}

	public void setHeight(String height) {
		this.height.set(height);
	}

	public String getLongitudeRef() {
		return longitudeRef;
	}

	public void setLongitudeRef(String longitudeRef) {
		this.longitudeRef = longitudeRef;
	}

	public String getLatitudeRef() {
		return latitudeRef;
	}

	public void setLatitudeRef(String latitudeRef) {
		this.latitudeRef = latitudeRef;
	}

}
