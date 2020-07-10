package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;

import beans.ProjectBean;

/**
 * 保存、读取存储在本地的项目数据
 * 
 * @author DP
 *
 */
public class SaveUtil {
	public static void changeProjectData(ProjectBean bean) {
		ArrayList<ProjectBean> projectsData = getProjectsData();
		for (ProjectBean item : projectsData) {
			if (item.getId() == bean.getId()) {
				item.setProjectName(bean.getProjectName());
				break;
			}
		}
		saveProjectsData(projectsData);
	}
	
	public static void saveProject(ProjectBean proj) {
		ArrayList<ProjectBean> list = getProjectsData();
		if (list == null) {
			list = new ArrayList<ProjectBean>();
		}
		list.add(proj);
		saveProjectsData(list);
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

	@SuppressWarnings("unchecked")
	public static ArrayList<ProjectBean> getProjectsData() {
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

}
