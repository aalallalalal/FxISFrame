package utils;

import java.io.File;

import javafx.scene.Node;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * 文件(夹)浏览器工具类
 * @author wxp
 *
 */

public class FileChooserUtil
{
	
	/**
	 * 文件选择器，用来选择经纬度文件
	 * @param title 	文件选择器的标题
	 * @param node		任意一个node
	 * @param callback	回调（需要实现其中的result方法）
	 */
	public static void OpenFileChooserUtil(String title, Node node, Callback callback)
	{
		FileChooser filechooser = new FileChooser();
		File selectedFile;
		
		//设置窗口的标题
		filechooser.setTitle(title);
		
		//文件类型
		filechooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt", "*.GSP"));
		
		//显示窗口,返回选择文件
		selectedFile = filechooser.showOpenDialog(node.getScene().getWindow());
		
		if (selectedFile == null) {
			System.out.println("未选择文件");
			if (callback != null) {
				callback.onResult(false, null);
			}
		} else {
			System.out.println("选择文件");
			if (callback != null) {
				callback.onResult(true, selectedFile);
			}
		}
		
	}
	/**
	 * 文件夹选择器，用来选择项目的文件夹
	 * @param title 	文件选择器的标题
	 * @param node		primaryStage即可
	 * @param callback	回调（需要实现其中的result方法）
	 */
	public static void OpenDirectoryChooserUtil(String title, Node stage, Callback callback)
	{
		DirectoryChooser directoryChooser=new DirectoryChooser();
		directoryChooser.setTitle(title);
		File selectedFile = directoryChooser.showDialog(stage.getScene().getWindow());
		if (selectedFile == null) {
			System.out.println("未选择文件");
			if (callback != null) {
				callback.onResult(false, null);
			}
		} else {
			System.out.println("选择文件");
			if (callback != null) {
				callback.onResult(true, selectedFile);
			}
		}
	}
	
	public interface Callback {
		void onResult(boolean isChoose, File file);
	}
	
}
