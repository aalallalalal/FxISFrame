package utils.ProgressTask;

import application.control.ProcessingController.ProcessingListener;
import beans.FinalDataBean;
import javafx.concurrent.Task;
import utils.ExeProcedureUtil;

public class ExeTask extends Task<Boolean> 
{
	public ProcessingListener listener;
	public ExeTask(ProcessingListener listener)
	{
		this.listener = listener;
	}
	
	@Override
    public Boolean call() {
        // process long-running computation, data retrieval, etc...
    	String path = System.getProperty("user.dir");
    	String path_Exe = path + "\\ExeProcedure\\ImageStitching.exe";//exe文件的结果路径
    	String flag = "Stitch Finished!";
    	boolean Falg = ExeProcedureUtil.execute(path_Exe, FinalDataBean.para_Exe, flag, this);//参数
		return Falg;
    }
	
    @Override
	protected void succeeded()
	{
    	if(getValue() == true)
    		listener.updateSuccPage();
    	else
    	{
    		listener.updateFailPage();
    	}
    		
	}

	@Override
	public void updateMessage(String message)
	{
		// TODO Auto-generated method stub
		super.updateMessage(message);
	}

	@Override
	protected void cancelled()
	{
		listener.toprojects();
    	System.out.println("关闭exe进程：" + ExeProcedureUtil.closeExe());
	}

	@Override
	protected void failed()
	{
		listener.updateFailPage();
		System.out.println("程序异常");
	}
	
	
}
