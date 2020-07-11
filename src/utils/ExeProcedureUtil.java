package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 执行外部exe程序工具类
 * @author wxp
 *
 */

public class ExeProcedureUtil
{	
	/**
	 * 执行外部exe程序
	 * @param path_Exe  .exe文件的路径
	 * @param para_Exe  .exe程序的参数
	 * @return			 执行程序后cmd显示的结果
	 * @throws Exception
	 */
	public static String execute(String path_Exe, String para_Exe) throws Exception {
		String[] cmds = {path_Exe, para_Exe};
		final Process proc = new ProcessBuilder(cmds).start();
		WatchThread wt = new WatchThread(proc);
    	wt.start();
    	proc.waitFor();
        wt.setOver(true);
        BufferedReader stdout = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        return stdout.toString();
	}

}
