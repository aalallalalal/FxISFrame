package utils;

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
}
