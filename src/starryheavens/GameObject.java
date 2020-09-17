package starryheavens;

import infrastructure.GlobaleKonstanten;
import infrastructure.ResourceManager;
import infrastructure.graphics.GraphicUtils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

public abstract class GameObject implements Runnable
{
	protected int  x ;
	protected int  y;
	private int m_oHealth=100;
	private String m_oName;
	protected JPanel m_oComponent = null;
	private  int DELAY = 16; //60fps
	protected GameObject m_oTarget;
	private ArrayList<BufferedImage> m_oAnimationList;
	protected int iAnimationCounter=0;
	protected long iAnimationDate=System.currentTimeMillis();
	protected long iAnimationDelay=100;

	protected  final ResourceManager resourceManager = ResourceManager.get("user");
	protected GameObject(GameObject oTarget,String strText, JPanel oComponent) throws IOException 
	{
		m_oName=strText;
		m_oComponent=oComponent;
		m_oTarget=oTarget;


		createSprite();
		resetPosition();
	}
	protected void move()
	{
	//	y =  m_oComponent.getHeight() -100;
		y = m_oComponent.getHeight() - (m_oComponent.getHeight()/5);
		if (y > m_oComponent.getHeight() | x > m_oComponent.getWidth()) 
		{
			
		resetPosition();
		}
	}
	protected  void createSprite() throws IOException
	{
		m_oAnimationList=setAnimationList(new ArrayList());
		 if(m_oAnimationList.size()<1)
			 m_oAnimationList=null;
	
	}
	public void drawSprite(Graphics g) throws IOException
	{
		long actualTime=System.currentTimeMillis();
		int iheightScale=m_oComponent.getHeight()/5;
		int iwidthScale=m_oComponent.getWidth()/5;
		if(m_oAnimationList!=null)
		{
			if(iAnimationCounter>= m_oAnimationList.size())
				iAnimationCounter=0;
			if(x>m_oTarget.getPosition())
				g.drawImage(GraphicUtils.getScaledIconImage(SH_SYSTEM_Animation.horizontalflip((BufferedImage) m_oAnimationList.get(iAnimationCounter)),iwidthScale,iheightScale), x, y, null);
			else
				g.drawImage( GraphicUtils.getScaledIconImage(m_oAnimationList.get(iAnimationCounter),iwidthScale,iheightScale), x, y, null);
			if(actualTime > (iAnimationDate + iAnimationDelay))
			{
				if(iAnimationCounter>= m_oAnimationList.size())
					iAnimationCounter=0;
				else
					iAnimationCounter++;
				iAnimationDate=actualTime;
			}
		}

	}
	/**
	 * @return the m_oAnimationList
	 */
	public ArrayList<BufferedImage> getAnimationList() {
		return m_oAnimationList;
	}
	/**
	 * @param m_oAnimationList the m_oAnimationList to set
	 * @return 
	 * @throws IOException 
	 */
	public abstract ArrayList<BufferedImage> setAnimationList(ArrayList<BufferedImage> m_oAnimationList) throws IOException ;



	public String getName() 
	{
		return m_oName;
	}
	protected void resetPosition()
	{
		if(m_oHealth>0)
		{
		x = m_oComponent.getWidth()/2;
		y = m_oComponent.getHeight() -  m_oComponent.getHeight() /10;
		}
	}
	public int getPosition()
	{
		return x;
	}
	public void refresh() 
	{

		try
		{
			
			move();

		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Toolkit.getDefaultToolkit().sync();


		//	g.dispose();

	}
	@Override
	public String toString()
	{
		return "GameObject [strName=" + m_oName + "]";
	}
	public void run() 
	{
		long beforeTime, timeDiff, sleep;

		beforeTime = System.currentTimeMillis();

		while (m_oHealth>0) 
		{


			refresh();

			timeDiff = System.currentTimeMillis() - beforeTime;
			sleep = DELAY - timeDiff;

			if (sleep < 0)
				sleep = 2;
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				System.out.println("interrupted");
			}

			beforeTime = System.currentTimeMillis();
		}
	}
	public void receiveDamage(int iDamage, Graphics g)
	{
		g.setColor(Color.RED);
		g.setFont(GlobaleKonstanten.GAMEFONT);
		g.drawString("- "+String.valueOf(iDamage), x, y);
	

		if(m_oHealth>iDamage)
			m_oHealth=m_oHealth-iDamage;
		else 
			m_oHealth=0;
		if(m_oHealth==0)
		{
			System.out.println(this.toString()+" got killed!");
			SH_SYSTEM_Animation.remove( this);
		}
	}
	protected int getHealth()
	{
		return m_oHealth;
	}
	protected void setHealth(int i)
	{
		m_oHealth=i;
	}
	protected void resurrect()
	{
		if(m_oHealth==0)
			System.out.println(this.toString()+" got resurrected!");
		m_oHealth=100;
		
	}

}
