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

import beans.ProjectBean;
import javafx.stage.Stage;
import utils.ProgressTask.ProgressTask;

/**
 * 保存、读取存储在本地的项目数据
 * 
 * @author DP
 *
 */
public class SaveProjectsUtil {
	private static final int MAX_SIZE = 50;// 最大保存项目数量50
	private static final long MAX_TIME = 1000 * 60 * 60 * 24 * 30 * 2; // 两个月

	private static ProjectBeanComparator comparator = new ProjectBeanComparator();

	/**
	 * 更新最近修改，访问时间
	 * 
	 * @param bean
	 */
	public static void upDataProjectData(ProjectBean bean) {
		ProgressTask task = new ProgressTask(new ProgressTask.MyTask<Void>() {
			@Override
			protected void succeeded() {
				super.succeeded();
			}

			@Override
			protected Void call() {
				ArrayList<ProjectBean> projectsData = getProjectsDataNoInThread();
				for (ProjectBean item : projectsData) {
					if (item.getId() == bean.getId()) {
						item.setLastUsedTime(System.currentTimeMillis());
						break;
					}
				}
				saveProjectsData(projectsData);
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
	public static void changeProjectData(ProjectBean bean, Stage stage) {
		ProgressTask task = new ProgressTask(new ProgressTask.MyTask<Void>() {
			@Override
			protected void succeeded() {
				super.succeeded();
			}

			@Override
			protected Void call() {
				ArrayList<ProjectBean> projectsData = getProjectsDataNoInThread();
				for (ProjectBean item : projectsData) {
					if (item.getId() == bean.getId()) {
						item.setProjectName(bean.getProjectName());
						item.setLastUsedTime(System.currentTimeMillis());
						break;
					}
				}
				saveProjectsData(projectsData);
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
	public static void saveProject(ProjectBean proj, Stage stage) {
		ProgressTask task = new ProgressTask(new ProgressTask.MyTask<Void>() {
			@Override
			protected void succeeded() {
				super.succeeded();
			}

			@Override
			protected Void call() {
				ArrayList<ProjectBean> list = getProjectsDataNoInThread();
				if (list == null) {
					list = new ArrayList<ProjectBean>();
				}
				list.add(proj);
				saveProjectsData(list);
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
	public static void getProjectsData(Stage stage, Callback callback) {
		ProgressTask task = new ProgressTask(new ProgressTask.MyTask<ArrayList<ProjectBean>>() {
			@Override
			protected void succeeded() {
				super.succeeded();
				try {
					if (callback != null) {
						ArrayList<ProjectBean> arrayList = get();
						callback.onGetData(arrayList);
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}

			@Override
			protected ArrayList<ProjectBean> call() {
				ArrayList<ProjectBean> projectsDataNoInThread = getProjectsDataNoInThread();
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
	private static ArrayList<ProjectBean> getProjectsDataNoInThread() {
		String path = System.getProperty("user.dir");
		File writeName = new File(path, "datas"); // 相对路径，如果没有则要建立一个新的output.txt文件
		if (!writeName.exists() || writeName.length() == 0) {
			return new ArrayList<ProjectBean>();
		}
		FileInputStream writer;
		ArrayList<ProjectBean> data = null;
		try {
			writer = new FileInputStream(writeName);
			ObjectInputStream objectinputStream = new ObjectInputStream(writer);
			data = (ArrayList<ProjectBean>) objectinputStream.readObject();
			objectinputStream.close();
		} catch (EOFException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		data.sort(comparator);
		return data;
	}

	private static void saveProjectsData(ArrayList<ProjectBean> list) {
		controlListSize(list);

		try {
			String path = System.getProperty("user.dir");
			File writeName = new File(path, "datas"); // 相对路径，如果没有则要建立一个新的output.txt文件
			if (!writeName.exists()) {
				writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
			}
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
	private static void controlListSize(ArrayList<ProjectBean> list) {
		if (list == null || list.size() == 0) {
			return;
		}
		if (list.size() <= MAX_SIZE) {
			return;
		}
		list.sort(comparator);
		ProjectBean projectBean = list.get(MAX_SIZE);
		long dist = System.currentTimeMillis() - projectBean.getLastUsedTime();
		if (dist >= MAX_TIME) {
			ArrayList<ProjectBean> sublist = (ArrayList<ProjectBean>) list.subList(MAX_SIZE, list.size());
			list.removeAll(sublist);
		}
	}

	public interface Callback {
		void onGetData(ArrayList<ProjectBean> list);
	}

	private static class ProjectBeanComparator implements Comparator<ProjectBean> {
		@Override
		public int compare(ProjectBean o1, ProjectBean o2) {
			long dist = o2.getLastUsedTime() - o1.getLastUsedTime();
			if (dist > 0) {
				return 1;
			} else if (dist == 0) {
				return 0;
			} else {
				return -1;
			}
		}
	}

}
