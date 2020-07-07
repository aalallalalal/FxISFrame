package utils;

import java.io.File;

import javax.activation.MimetypesFileTypeMap;

public class FileUtil {

	/**
	 * 判断file是否为图片
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isImage(File file) {
		MimetypesFileTypeMap mtftp;
		mtftp = new MimetypesFileTypeMap();
		/*
		 * 不添加下面的类型会造成误判
		 * 详见：http://stackoverflow.com/questions/4855627/java-mimetypesfiletypemap-
		 * always-returning-application-octet-stream-on-android-e
		 */
		mtftp.addMimeTypes("image png tif jpg jpeg bmp");
		String mimetype = mtftp.getContentType(file);
		String type = mimetype.split("/")[0];
		return type.equals("image");
	}

	/**
	 * 不是真删除，只是把文件，放在DELETED_IMAGES文件夹中。
	 * 
	 * @param imgPath
	 */
	public static void deleteImage(String imgPath) {
		File imgFile = new File(imgPath);
		if (imgFile == null || !imgFile.exists() || imgFile.isDirectory()) {
			return;
		}
		File parentFile = imgFile.getParentFile();
		if (parentFile == null || !parentFile.exists()) {
			return;
		}
		File deleteDir = new File(parentFile, "DELETED_IMAGES");
		if (!deleteDir.exists()) {// 如果文件夹不存在
			deleteDir.mkdir();// 创建文件夹
		}
		File deletedImg = new File(deleteDir, imgFile.getName());
		imgFile.renameTo(deletedImg);
	}
}
