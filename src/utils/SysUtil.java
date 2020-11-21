package utils;

import java.io.File;
import java.io.IOException;

public class SysUtil {

	public static boolean exeOpenFile(String filePath) {
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec("cmd /c start " + "\"\" \"" + filePath + "\"");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据中英文获取不同的help版本
	 * @return
	 */
	public static File openHelpFile() {
		File file;
		if (SaveLanguageUtil.getData() == 0) {
			file = new File(System.getProperty("user.dir") + "\\help\\help.html");
		} else {
			file = new File(System.getProperty("user.dir") + "\\help\\help_E.html");
		}
		if (file == null || !file.exists() || file.isDirectory()) {
			file = new File(System.getProperty("user.dir") + "\\help\\help.html");
		}
		return file;
	}
}
