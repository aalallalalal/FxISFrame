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
	
	public static void deleteTxt(String path) {
		String parentdirname = path.substring(path.lastIndexOf("/"));
		FileUtil.deleteFile(path + parentdirname + "-STITCH-GRAPH.txt");
		FileUtil.deleteFile(path + parentdirname + "-STITCH-GRAPH-adjust.txt");
		FileUtil.deleteFile(path + parentdirname + "-STITCH-GRAPH-adjust_opGSP.txt");
	}
	
	public static void deleteFile(String txtPath) {
		File txtFile = new File(txtPath);
		if (txtFile == null || !txtFile.exists()) {
			System.out.println("返回");
			return;
		}
		else
		{
			System.out.println("删除：" + txtFile.delete());
		}
	}
	
	public static void deleteRunDir(File file)
	{
		if(file.exists()) {//判断路径是否存在
			if(file.isFile()){//boolean isFile():测试此抽象路径名表示的文件是否是一个标准文件。 

				file.delete();

			}else{//不是文件，对于文件夹的操作

				//保存 路径D:/1/新建文件夹2  下的所有的文件和文件夹到listFiles数组中

				File[] listFiles = file.listFiles();//listFiles方法：返回file路径下所有文件和文件夹的绝对路径

				for (File file2 : listFiles) {
					deleteRunDir(file2);
				}
			}
			file.delete();
		}else {
			System.out.println("我在FileUtil类中,该file路径不存在！！");
		}
	}
	
}
