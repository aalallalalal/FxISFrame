package application.control;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

/**
 * 项目列表界面controller
 * @author DP
 *
 */
public class ProjectsController implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	public void test() {
		System.out.println("ProjectsController来自其他controller的调用");
	}
}
