package starryheavens;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

import severeLobster.frontend.application.MainFrame;

public class SH_SYSTEM_Animation 
{
	private static ArrayList<GameObject> oList = new ArrayList();
	private static Player oHero;
	public static void setHero(Player oPlayer)
	{
		oHero=oPlayer;
		add(oHero);
	}
	public static Player getHero()
	{
		return oHero;
	}
	/**
	 * This method flips the image horizontally
	 * @param img --> BufferedImage Object to be flipped horizontally
	 * @return
	 */
	public static BufferedImage horizontalflip(BufferedImage img) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage dimg = new BufferedImage(w, h, img.getType());
		Graphics2D g = dimg.createGraphics();
		/**
		 * img - the specified image to be drawn. This method does nothing if
		 * img is null. dx1 - the x coordinate of the first corner of the
		 * destination rectangle. dy1 - the y coordinate of the first corner of
		 * the destination rectangle. dx2 - the x coordinate of the second
		 * corner of the destination rectangle. dy2 - the y coordinate of the
		 * second corner of the destination rectangle. sx1 - the x coordinate of
		 * the first corner of the source rectangle. sy1 - the y coordinate of
		 * the first corner of the source rectangle. sx2 - the x coordinate of
		 * the second corner of the source rectangle. sy2 - the y coordinate of
		 * the second corner of the source rectangle. observer - object to be
		 * notified as more of the image is scaled and converted.
		 *
		 */
		g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null);
		g.dispose();
		return dimg;
	}

	/**
	 * This method flips the image vertically
	 * @param img --> BufferedImage object to be flipped
	 * @return
	 */
	public static BufferedImage verticalflip(BufferedImage img) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage dimg = new BufferedImage(w, h, img.getColorModel()
				.getTransparency());
		Graphics2D g = dimg.createGraphics();
		g.drawImage(img, 0, 0, w, h, 0, h, w, 0, null);
		g.dispose();
		return dimg;
	}
	public static void add(GameObject go)
	{

		//MainFrame.executor.execute(go);
		oList.add(go);
	}
	public static void repaint(Graphics g)
	{
		for(int i=0; i <oList.size();i++)
			try 
		{
				oList.get(i).move();
				oList.get(i).drawSprite(g);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			System.err.println(oList.get(i)+" konnte nicht gezeichnet werden. "+ e.getMessage());
		}
	}

	public static void notifyDamage(GameObject oDamageDealer, JPanel m_oComponent, int iDamage , int iSpread) 
	{

		int oPlayerPosition= oDamageDealer.getPosition();
		iSpread=(m_oComponent.getWidth() /100 * iSpread);
		Graphics componentGraphics =m_oComponent.getGraphics();
		for(int i = 0;  i< oList.size(); i++)
		{
			GameObject oCurObject = oList.get(i);
			if (oCurObject == oDamageDealer | oCurObject.getClass().equals(oDamageDealer.getClass()) )
				continue;

			if((oPlayerPosition-oCurObject.getPosition() < iSpread )&& (oPlayerPosition-oCurObject.getPosition()> (iSpread*-1))) 
			{
				oCurObject.receiveDamage(iDamage,componentGraphics);
				if(oPlayerPosition-oCurObject.getPosition() < iSpread )
					oCurObject.x=oCurObject.x-(m_oComponent.getWidth()/20);
				 if(oPlayerPosition-oCurObject.getPosition()> (iSpread*-1))
					oCurObject.x=oCurObject.x+(m_oComponent.getWidth()/20);

			}

		
		}


	}
	public static void reset()
	{
		for(int i = 0;  i< oList.size(); i++)
		{
			if(oList.get(i)instanceof Player)
				continue;
			oList.get(i).setHealth(0);
			oList.remove(i);
			
		}
		if(oHero!=null)
		{
			setHero(oHero);
		}
		
	}

	public static void remove(GameObject gameObject) 
	{
		try
		{
			oList.remove(gameObject);
		}
		catch(Exception e)
		{
			System.out.println(gameObject.toString()+ " is already dead");

		}

	}
	public static void resurrectHero()
	{

		if(oHero!=null)
		{
			oHero.resurrect();
			setHero(oHero);
		}
	}

}
