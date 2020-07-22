package utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javafx.application.Platform;
import utils.ProgressTask.ExeTask;

/**
 * 执行外部exe程序工具类
 * @author wxp
 *
 */

public class ExeProcedureUtil
{	
	static Process process;
	/**
	 * 执行外部exe程序
	 * @param path_Exe  .exe文件的路径
	 * @param para_Exe  .exe程序的参数
	 * @return			 执行程序后cmd显示的结果
	 * @throws Exception
	 */
	public static boolean execute(String path_Exe, String para_Exe, String flag, ExeTask task) 
	{
		String[] cmds = {path_Exe, para_Exe};
		BufferedReader inBr = null;
		String lineStr = "";
		String oldLine = "";
		try {
            Runtime runtime = Runtime.getRuntime();
            process = runtime.exec(cmds);
            InputStreamReader in=new InputStreamReader(process.getInputStream(), Charset.forName("GBK"));
            inBr = new BufferedReader(in);
            
            FileOutputStream fos = new FileOutputStream("RuntimeDetialInfo.txt");
			BufferedOutputStream bos  = new BufferedOutputStream(fos);
			
            while((lineStr=inBr.readLine())!=null){
                System.out.println(lineStr);
                oldLine = lineStr;
                bos.write((lineStr + "\n").getBytes("UTF-8"));
                //task.updateMessage(lineStr);
                final String newStr = lineStr;
				
				  Platform.runLater(new Runnable() {
				  
					  @Override public void run() { //更新JavaFX的主线程的代码放在此处
					 		task.listener.update("\n" + newStr);
					  } 
				  });
            }
                
            bos.flush();
			bos.close();
            process.getInputStream().close();
            process.getOutputStream().close();
            inBr.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
		if(oldLine.equals(flag))
			return true;
		else
		{
			task.updateMessage(oldLine);
			return false;
		}
	}
	
	//关闭进程
	public static boolean closeExe()
	{
		String command = "taskkill /f /im ImageStitching.exe";
		try
		{
			Runtime.getRuntime().exec(command);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		process.destroy();
		if(process.isAlive())
			return false;
		return true;
	}
}
