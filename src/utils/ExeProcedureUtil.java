package utils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

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
	public static String execute(String path_Exe, String para_Exe) 
	{
		String[] cmds = {path_Exe, para_Exe};
		BufferedReader inBr = null;
		try {
            Runtime runtime = Runtime.getRuntime();
            process = runtime.exec(cmds);
            InputStreamReader in=new InputStreamReader(process.getInputStream());
            inBr=new BufferedReader(in);
            String lineStr;

            OutputStreamWriter os = new OutputStreamWriter(process.getOutputStream());
            BufferedWriter bw = new BufferedWriter(os);
            bw.write("\n"); 
            bw.write("\n");

            while((lineStr=inBr.readLine())!=null){
                System.out.println(lineStr);
            }

            //process.waitFor();
            process.getInputStream().close();
            process.getOutputStream().close();
            inBr.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return inBr.toString();
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
	
	//清空eclipse的控制台
	public static void clearConsole()
	{
		
		try
		{
			Robot r;
			r = new Robot();
			r.mousePress(InputEvent.BUTTON3_MASK);       // 按下鼠标右键
		    r.mouseRelease(InputEvent.BUTTON3_MASK);    // 释放鼠标右键
		    r.keyPress(KeyEvent.VK_CONTROL);             // 按下Ctrl键
		    r.keyPress(KeyEvent.VK_R);                    // 按下R键
		    r.keyRelease(KeyEvent.VK_R);                  // 释放R键
		    r.keyRelease(KeyEvent.VK_CONTROL);            // 释放Ctrl键
		    r.delay(100); 
		} catch (AWTException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
	  


}
