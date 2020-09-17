package starryheavens.visual;

import javax.swing.JPanel;

import starryheavens.SH_SYSTEM_Animation;

public class SH_R_Playeraim implements Runnable
{
	private JPanel m_oComponent;

	public SH_R_Playeraim(JPanel m_oComponent) {
		this.m_oComponent=m_oComponent;// TODO Auto-generated constructor stub
	}

	@Override
	public void run() 
	{
		SH_SYSTEM_Animation.notifyDamage(SH_SYSTEM_Animation.getHero(),m_oComponent,50, 5);	
		
		long beforeTime, timeDiff = 0;

		beforeTime = System.currentTimeMillis();
		while (timeDiff<500) 
		{
			timeDiff = System.currentTimeMillis() - beforeTime;
			SH_SYSTEM_Animation.getHero().aim();
		}
		SH_SYSTEM_Animation.notifyDamage(SH_SYSTEM_Animation.getHero(),m_oComponent,50, 5);	
	
	}

}
