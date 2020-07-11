package utils;

import java.io.BufferedReader;

import java.io.InputStreamReader;


public class WatchThread extends Thread
{
	Process p;
	boolean over;
	
	public WatchThread(Process p)
	{
		this.p = p;
		this.over = false;
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		try {
			if(p == null)
				return;
			
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			while(true)
			{
				if(p == null || over)
					break;
				
				while(br.readLine()!=null); 
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void setOver(boolean over)
	{
		this.over = over;
	}
	
}
