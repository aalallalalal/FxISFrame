package utils;

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
	/**
	 * 执行外部exe程序
	 * @param path_Exe  .exe文件的路径
	 * @param para_Exe  .exe程序的参数
	 * @return			 执行程序后cmd显示的结果
	 * @throws Exception
	 */
	public static String execute(String path_Exe, String para_Exe) {
		String[] cmds = {path_Exe, para_Exe};
		Process process;
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

            process.waitFor();
            process.getInputStream().close();
            process.getOutputStream().close();
            inBr.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return inBr.toString();
        
	}

}
