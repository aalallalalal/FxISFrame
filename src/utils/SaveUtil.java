package utils;

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
public class SaveUtil {
	/**
	 * 改变项目数据
	 * 
	 * @param bean
	 * @param stage
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
		if (!writeName.exists()) {
			return new ArrayList<ProjectBean>();
		}
		FileInputStream writer;
		ArrayList<ProjectBean> data = null;
		try {
			writer = new FileInputStream(writeName);
			ObjectInputStream objectinputStream = new ObjectInputStream(writer);
			data = (ArrayList<ProjectBean>) objectinputStream.readObject();
			objectinputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		data.sort(new Comparator<ProjectBean>() {
			@Override
			public int compare(ProjectBean o1, ProjectBean o2) {
				long dist = o2.getId() - o1.getId();
				if (dist > 0) {
					return 1;
				} else if (dist == 0) {
					return 0;
				} else {
					return -1;
				}
			}
		});
		return data;
	}

	private static void saveProjectsData(ArrayList<ProjectBean> list) {
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

	public interface Callback {
		void onGetData(ArrayList<ProjectBean> list);
	}

}
