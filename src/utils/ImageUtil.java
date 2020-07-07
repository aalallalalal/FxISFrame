package utils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.GpsDirectory;

import beans.ImageBean;

public class ImageUtil {

	/**
	 * 读取照片里面的信息
	 * 
	 * @throws IOException
	 * @throws ImageProcessingException
	 */
	public static void printImageTags(String imgPath, ImageBean imageBean)
			throws ImageProcessingException, IOException {
		File img = new File(imgPath);
		if (img == null || !img.exists() || img.isDirectory()) {
			return;
		}
		Metadata metadata;
		System.out.println(img.getAbsolutePath());
		metadata = ImageMetadataReader.readMetadata(img);
		Collection<GpsDirectory> gps = metadata.getDirectoriesOfType(GpsDirectory.class);
		if (gps == null || gps.size() <= 0) {
			return;
		}
		for (GpsDirectory item : gps) {
			Collection<Tag> tags = item.getTags();
			for (Tag itemTag : tags) {
				if ("GPS Latitude".equals(itemTag.getTagName())) {
					imageBean.setLatitude(pointToLatlong(itemTag.getDescription()));
				}
				if ("GPS Longitude".equals(itemTag.getTagName())) {
					imageBean.setLongitude(pointToLatlong(itemTag.getDescription()));
				}
				if ("GPS Altitude".equals(itemTag.getTagName())) {
					imageBean.setHeight(itemTag.getDescription());
				}
			}
		}
	}

	/**
	 * 经纬度格式 转换为 度分秒格式 ,如果需要的话可以调用该方法进行转换 41 *
	 * 
	 * @param point 坐标点 @return
	 */
	public static double pointToLatlong(String point) {
		Double du = Double.parseDouble(point.substring(0, point.indexOf("°")).trim());
		Double fen = Double.parseDouble(point.substring(point.indexOf("°") + 1, point.indexOf("'")).trim());
		Double miao = Double.parseDouble(point.substring(point.indexOf("'") + 1, point.indexOf("\"")).trim());
		Double duStr = du + fen / 60 + miao / 60 / 60;
		return duStr;
	}

}
