package application.control;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

/**
 * 创建项目界面controller
 * 
 * @author DP
 *
 */
public class CreateProjectController implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void test() {
		System.out.println("CreateProjectController来自其他controller的调用");
	}
}
