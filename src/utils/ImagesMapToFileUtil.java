package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import beans.ProjectBean;
import javafx.stage.Stage;
import utils.ProgressTask.ProgressTask;

/**
 * 项目删除图片文件与map的互关工具
 * 
 * @author DP
 *
 */
public class ImagesMapToFileUtil {
	/**
	 * 保存map到文件。异步
	 * 
	 * @param proj
	 * @param stage
	 */
	public static void saveMap(ProjectBean project, HashMap<String, Boolean> map, Stage stage) {
		if (project == null || map == null) {
			return;
		}
		ProgressTask task = new ProgressTask(new ProgressTask.MyTask<Void>() {
			@Override
			protected void succeeded() {
				super.succeeded();
			}

			@Override
			protected Void call() {
				mapToFile(project, map);
				return null;
			}
		}, stage);
		task.start();
	}

	/**
	 * 读取文件为map。异步
	 * 
	 * @param proj
	 * @param stage
	 */
	public static void getMap(ProjectBean project, Stage stage, Callback callback) {
		ProgressTask task = new ProgressTask(new ProgressTask.MyTask<HashMap<String, Boolean>>() {
			@Override
			protected void succeeded() {
				super.succeeded();
				HashMap<String, Boolean> map;
				try {
					map = get();
					if (map != null && callback != null) {
						callback.onGetData(map);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}

			@Override
			protected HashMap<String, Boolean> call() {
				String deletedFilePath = getDeletedFilePath(project);
				HashMap<String, Boolean> fileToMap = fileToMap(deletedFilePath, project);
				return fileToMap;
			}
		}, stage);
		task.start();
	}

	/**
	 * 将label文件转为map
	 * 
	 * @param filePath
	 * @param project
	 * @return
	 */
	public static HashMap<String, Boolean> fileToMap(String filePath, ProjectBean project) {
		HashMap<String, Boolean> map = new HashMap<String, Boolean>();
		File file = new File(filePath);
		if (file == null || !file.exists() || file.isDirectory()) {
			System.out.println("initMaptofile!!!");
			return initMapToFile(file, project);
		}
		InputStreamReader isr = null;
		FileInputStream fis = null;
		BufferedReader bf = null;
		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, "UTF-8");
			bf = new BufferedReader(isr);
			String str;
			// 按行读取字符串
			String isNotDelete = "";
			String imageName = "";
			boolean isND = true;
			while ((str = bf.readLine()) != null) {
				int length = str.length();
				isNotDelete = str.substring(length - 1);
				imageName = str.substring(0, length - 1).trim();
				isND = isNotDelete.equals("1") ? true : false;
				map.put(imageName, isND);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bf.close();
				isr.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	/**
	 * 初始化label文件，默认图片全部存在
	 * 
	 * @param file
	 * @param project
	 * @return
	 */
	public static HashMap<String, Boolean> initMapToFile(File file, ProjectBean project) {
		HashMap<String, Boolean> map = new HashMap<String, Boolean>();
		if (file == null) {
			return map;
		}
		if (!file.exists()) {
			try {
				boolean createNewFile = file.createNewFile();
				if (!createNewFile) {
					return map;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (file.isDirectory()) {
			boolean delete = file.delete();
			boolean createNewFile;
			try {
				createNewFile = file.createNewFile();
				if (!createNewFile || !delete) {
					return map;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		File imagesfile = new File(project.getProjectDir());
		if (imagesfile != null && imagesfile.exists()) {
			File[] itemFiles = imagesfile.listFiles();
			for (File item : itemFiles) {
				if (!item.isDirectory() && FileUtil.isImage(item)) {
					// 不是文件夹，并且是图片
					map.put(item.getName(), true);
				}
			}
		}
		System.out.println("kaishi maptofiel" + map.size());
		mapToFile(project, map);
		return map;
	}

	/**
	 * 将map文件转为label文件
	 * 
	 * @param project
	 * @param map
	 * @return
	 */
	private static File mapToFile(ProjectBean project, HashMap<String, Boolean> map) {
		File fileDelete = new File(getDeletedFilePath(project));
		if (!fileDelete.exists()) {
			try {
				fileDelete.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try {
			fos = new FileOutputStream(fileDelete);
			osw = new OutputStreamWriter(fos, "UTF-8");
			bw = new BufferedWriter(osw);
			for (Entry<String, Boolean> entry : map.entrySet()) {
				String mapKey = entry.getKey();
				Boolean mapValue = entry.getValue();
				bw.write(mapKey + " " + (mapValue.booleanValue() ? "1" : "0") + "\n");
			}
			bw.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
				osw.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileDelete;
	}

	public static String getDeletedFilePath(ProjectBean project) {
		if (project == null || StrUtil.isEmpty(project.getProjectDir())) {
			return "";
		}
		String filePath = project.getProjectDir() + "/" + "ISTIRS_" + project.getId() + "";
		return filePath;
	}

	public interface Callback {
		void onGetData(HashMap<String, Boolean> map);
	}

}
