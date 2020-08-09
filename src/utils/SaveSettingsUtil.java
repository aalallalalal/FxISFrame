package utils;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

import beans.SettingsBean;
import javafx.stage.Stage;
import utils.ProgressTask.ProgressTask;

/**
 * 保存、读取存储在本地的参数模板数据
 * 
 * @author DP
 *
 */
public class SaveSettingsUtil {
	private static String path = System.getProperty("user.dir") + "/Datas";
	private static String fileName = "Settings_Data";

	private static final int MAX_SIZE = 100;// 最大保存项目数量100
	private static final long MAX_TIME = 1000 * 60 * 60 * 24 * 30 * 2; // 两个月

	private static SettingsBeanComparator comparator = new SettingsBeanComparator();

	static {
		checkFile();
	}

	/**
	 * 更新最近修改，访问时间
	 * 
	 * @param bean
	 */
	public static void upDataSettingsData(ArrayList<SettingsBean> beans) {
		ProgressTask task = new ProgressTask(new ProgressTask.MyTask<Void>() {
			@Override
			protected void succeeded() {
				super.succeeded();
			}

			@Override
			protected Void call() {
				ArrayList<SettingsBean> projectsData = getProjectsDataNoInThread();
				for(SettingsBean itemSelected:beans) {
					for (SettingsBean item : projectsData) {
						if (item.getId() == itemSelected.getId()) {
							item.setLastUsedTime(System.currentTimeMillis());
							break;
						}
					}
				}
				saveSettingsData(projectsData);
				return null;
			}
		});
		task.start();
	}

	/**
	 * 改变项目名
	 * 
	 * @param bean
	 * @param stage null：不显示加载弹出框
	 */
	public static void changeSettingData(SettingsBean bean, Stage stage) {
		ProgressTask task = new ProgressTask(new ProgressTask.MyTask<Void>() {
			@Override
			protected void succeeded() {
				super.succeeded();
			}

			@Override
			protected Void call() {
				ArrayList<SettingsBean> projectsData = getProjectsDataNoInThread();
				for (SettingsBean item : projectsData) {
					if (item.getId() == bean.getId()) {
						item.setCameraSize(bean.getCameraSize());
						item.setSettingType(bean.getSettingType());
						item.setFlyHeight(bean.getFlyHeight());
						item.setGsd(bean.getGsd());
						item.setName(bean.getName());
						item.setNetHeight(bean.getNetHeight());
						item.setNetWidth(bean.getNetWidth());
						item.setPreCheck(bean.isPreCheck());
						item.setPreCheckWay(bean.getPreCheckWay());
						item.setSaveMiddle(bean.isSaveMiddle());
						break;
					}
				}
				saveSettingsData(projectsData);
				return null;
			}
		}, stage);
		task.start();
	}

	/**
	 * 保存项目到文件
	 * 
	 * @param proj
	 * @param stage
	 */
	public static void saveProject(SettingsBean proj, Stage stage) {
		ProgressTask task = new ProgressTask(new ProgressTask.MyTask<Void>() {
			@Override
			protected void succeeded() {
				super.succeeded();
			}

			@Override
			protected Void call() {
				ArrayList<SettingsBean> list = getProjectsDataNoInThread();
				if (list == null) {
					list = new ArrayList<SettingsBean>();
				}
				list.add(proj);
				saveSettingsData(list);
				return null;
			}
		}, stage);
		task.start();
	}

	/**
	 * 获取项目列表
	 * 
	 * @param stage
	 * @param callback
	 */
	public static void getSettingsData(Stage stage, Callback callback) {
		ProgressTask task = new ProgressTask(new ProgressTask.MyTask<ArrayList<SettingsBean>>() {
			@Override
			protected void succeeded() {
				super.succeeded();
				try {
					if (callback != null) {
						ArrayList<SettingsBean> arrayList = get();
						callback.onGetData(arrayList);
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}

			@Override
			protected ArrayList<SettingsBean> call() {
				ArrayList<SettingsBean> projectsDataNoInThread = getProjectsDataNoInThread();
				return projectsDataNoInThread;
			}
		}, stage);
		task.start();
	}

	/**
	 * 同步文件中读取list数据
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static ArrayList<SettingsBean> getProjectsDataNoInThread() {

		File writeName = checkFile(); // 相对路径，如果没有则要建立一个新的output.txt文件
		if (!writeName.exists() || writeName.length() == 0) {
			return new ArrayList<SettingsBean>();
		}
		FileInputStream writer;
		ArrayList<SettingsBean> data = null;
		try {
			writer = new FileInputStream(writeName);
			ObjectInputStream objectinputStream = new ObjectInputStream(writer);
			data = (ArrayList<SettingsBean>) objectinputStream.readObject();
			objectinputStream.close();
		} catch (EOFException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		data.sort(comparator);
		return data;
	}

	private static void saveSettingsData(ArrayList<SettingsBean> list) {
		controlListSize(list);

		try {
			File writeName = checkFile(); // 相对路径，如果没有则要建立一个新的output.txt文件
			FileOutputStream writer = new FileOutputStream(writeName);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(writer);
			objectOutputStream.writeObject(list);
			objectOutputStream.flush(); // 把缓存区内容压入文件
			objectOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 控制文件最大项目数量。 如果数量小于MAX_SIZE那么不做控制。
	 * 如果数量大于MAX_SIZE，那么检查MAX_SIZE处的最近修改时间距离现在的时间间隔。
	 * 如果时间间隔大于MAX_TIME，那么把之前的数据都删掉，如果时间间隔小于MAX_TIME，那么不做控制。
	 * 
	 * @param list
	 */
	private static void controlListSize(ArrayList<SettingsBean> list) {
		if (list == null || list.size() == 0) {
			return;
		}
		if (list.size() <= MAX_SIZE) {
			return;
		}
		list.sort(comparator);
		SettingsBean settingsBean = list.get(MAX_SIZE);
		long dist = System.currentTimeMillis() - settingsBean.getLastUserTime();
		if (dist >= MAX_TIME) {
			ArrayList<SettingsBean> sublist = (ArrayList<SettingsBean>) list.subList(MAX_SIZE, list.size());
			list.removeAll(sublist);
		}
	}

	public interface Callback {
		void onGetData(ArrayList<SettingsBean> list);
	}

	private static class SettingsBeanComparator implements Comparator<SettingsBean> {
		@Override
		public int compare(SettingsBean o1, SettingsBean o2) {
			long dist = o2.getLastUserTime() - o1.getLastUserTime();
			if (dist > 0) {
				return 1;
			} else if (dist == 0) {
				return 0;
			} else {
				return -1;
			}
		}
	}

	private static File checkFile() {
		File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File writeName = new File(folder, fileName); // 相对路径，如果没有则要建立一个新的output.txt文件
		if (!writeName.exists()) {
			try {
				writeName.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		File file = new File(path, fileName);
		return file;
	}
}
