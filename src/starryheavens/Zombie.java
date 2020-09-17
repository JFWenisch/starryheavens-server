package starryheavens;

import infrastructure.graphics.GraphicUtils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Zombie extends GameObject 
{

	/**
	 * 
	 * @param Target
	 * @param oComponent
	 * @throws IOException
	 */
	public Zombie(GameObject Target, JPanel oComponent) throws IOException 
	{
	super(Target,"Zombie", oComponent);

		x=oComponent.getWidth();
		

	}
	protected void resetPosition()
	{
		super.resetPosition();
		x = m_oComponent.getWidth();
	}

	@Override
	protected void move() 
	{
		
		super.move();
		if(m_oTarget.getPosition()>x+(m_oComponent.getWidth()/600))
			x += m_oComponent.getWidth()/600;
		else if (m_oTarget.getPosition()<x-(m_oComponent.getWidth()/600))
			x-=m_oComponent.getWidth()/600;
	
		SH_SYSTEM_Animation.notifyDamage(this,m_oComponent, 1, 5);
	}


	@Override
	public ArrayList<BufferedImage> setAnimationList(ArrayList<BufferedImage> m_oAnimationList) throws IOException
	{
		 BufferedImage bigImg = ImageIO.read(resourceManager.getClass().getResourceAsStream("graphics/icons/43193.png"));

			final int width = 90;
			final int height = 90;
		
			int x = 0;
			int y = 0;
			int i=1;
			while(x+width*i < bigImg.getWidth())
			{
				try
				{
					m_oAnimationList.add(bigImg.getSubimage(x+(width*i),y,width,height));
				}
				catch(Exception e)
				{
					break;
				}
				i++;
			}
			return m_oAnimationList;
		
	}


}
