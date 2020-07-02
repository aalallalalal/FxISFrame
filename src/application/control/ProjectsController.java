package application.control;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import beans.ProjectBean;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * 项目列表界面controller
 * 
 * @author DP
 *
 */
public class ProjectsController implements Initializable {
	private ProjectsListener listener;
	@FXML
	Label label;
	private ArrayList<ProjectBean> list = new ArrayList<ProjectBean>();

	public interface ProjectsListener {

	}

	public void addProject(ProjectBean project) {
		list.add(project);
		refreshList();
		// TODO 刷新项目列表控件，显示出来
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	private void refreshList() {
		// TODO 刷新列表控件
		System.out.println("列表控件刷新：" + list.get(0));
	}

	public void test() {
		System.out.println("ProjectsController来自其他controller的调用");
	}

	public void setListener(ProjectsListener listener) {
		this.listener = listener;
	}
}
