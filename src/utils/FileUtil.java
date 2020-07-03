package utils;

import java.io.File;

import javax.activation.MimetypesFileTypeMap;

public class FileUtil {

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
}
