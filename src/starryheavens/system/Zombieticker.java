package starryheavens.system;

import java.io.IOException;

import severeLobster.frontend.application.MainFrame;
import starryheavens.SH_SYSTEM_Animation;
import starryheavens.Zombie;

public class Zombieticker implements Runnable
{


	private boolean running=true;
	public Zombieticker()
	{
		running=true;
	
	}
	public void run() 
	{
		// TODO Auto-generated method stub
		while(running)
		{
			try 

			{
				SH_SYSTEM_Animation.add(new Zombie(SH_SYSTEM_Animation.getHero(),MainFrame.mainPanel));
				Thread.sleep(20000);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) 
			{
				running=false;
		
			}	
		}}
	public void stop()
	{
	
		running=false;
		
	}

}
