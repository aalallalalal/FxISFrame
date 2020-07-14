package utils.ProgressTask;

import application.control.ProcessingController;
import beans.FinalDataBean;
import javafx.concurrent.Task;
import utils.ExeProcedureUtil;

public class ExeTask extends Task<Boolean> 
{
	ProcessingController proc;
	public ExeTask(ProcessingController proc)
	{
		this.proc = proc;
	}
	
	@Override
    public Boolean call() {
        // process long-running computation, data retrieval, etc...
    	String path = System.getProperty("user.dir");
    	String path_Exe = path + "\\ExeProcedure\\ImageStitching.exe";//exe文件的结果路径
    	ExeProcedureUtil.execute(path_Exe, FinalDataBean.para_Exe);//参数
		return true;
    }
	
    @Override
	protected void succeeded()
	{
		proc.listener.updateSuccPage();
	}

	@Override
	protected void cancelled()
	{
		proc.listener.toprojects();
    	System.out.println("关闭exe进程：" + ExeProcedureUtil.closeExe());
    	ExeProcedureUtil.clearConsole();
	}

	@Override
	protected void failed()
	{
		proc.listener.updateFailPage();
	}
}
