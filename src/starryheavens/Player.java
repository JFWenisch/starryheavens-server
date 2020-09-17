package starryheavens;

import infrastructure.GlobaleKonstanten;
import infrastructure.ResourceManager;
import infrastructure.graphics.GraphicUtils;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import severeLobster.frontend.application.MainFrame;
import starryheavens.visual.SH_R_Playeraim;

public class Player extends GameObject implements MouseMotionListener,MouseListener
{
	BufferedImage lampe = ImageIO.read(resourceManager.getClass().getResourceAsStream("graphics/icons/lampe.png"));
	private int mx;
	private int my;
	BufferedImage bigImg ;



	public Player(String strText, JPanel oComponent) throws IOException 
	{
		super(null, strText, oComponent);

		oComponent.addMouseListener(this);
		oComponent.addMouseMotionListener(this);		
	}

	/**
	 * @return the strName
	 */

	public void aim ()
	{
		Graphics g = m_oComponent.getGraphics();

		g.setColor(Color.WHITE);
		int basis=m_oComponent.getHeight()/5;
		if(my> m_oComponent.getHeight()-(m_oComponent.getHeight()/3))
		{
			Polygon p = new Polygon();
			if(x<mx)
			{
				p.addPoint(x,y+(m_oComponent.getHeight()/7));
				if(mx>x+m_oComponent.getWidth() /100 * 25)
				{
					p.addPoint(x+m_oComponent.getWidth() /100 * 25, my-basis/2);
					p.addPoint(x+m_oComponent.getWidth() /100 * 25,my+basis/2);
				}
				else
				{
					p.addPoint(mx, my-basis/2);
					p.addPoint(mx,my+basis/2);
				}
			}
			else
			{
				p.addPoint(x,y+(m_oComponent.getHeight()/7));
				if(mx<x-m_oComponent.getWidth() /100 * 25)
				{
					p.addPoint(x-m_oComponent.getWidth() /100 * 25, my-basis/2);
					p.addPoint(x-m_oComponent.getWidth() /100 * 25,my+basis/2);
				}
				else
				{
					p.addPoint(mx, my-basis/2);
					p.addPoint(mx,my+basis/2);
				}
			}
			Graphics2D g2 = (Graphics2D)g;
			g2.draw(p);

			final int R = 255;
			final int G = 255;
			final int B = 255;
			GradientPaint gp = 
					new GradientPaint(mx, 0, new Color(R, G, B, 0),
							x, 0, new Color(R, G, B, 255), true);
			g2.setPaint(gp);
			g2.fill(p);
			/*
			if(x<mx)
			{
			g.drawLine (x+50,y+m_oComponent.getHeight()/7,mx,my-basis/2);
			g.drawLine (x+50,y+m_oComponent.getHeight()/7,mx,my+basis/2);
			g.drawLine (mx,my+basis/2,mx,my-basis/2);
			}
			else
			{
				g.drawLine (x+35,y+m_oComponent.getHeight()/7,mx,my-basis/2);
				g.drawLine (x+35,y+m_oComponent.getHeight()/7,mx,my+basis/2);
				g.drawLine (mx,my+basis/2,mx,my-basis/2);
			}
			 */
			if(x<mx)
				g.drawImage(SH_SYSTEM_Animation.horizontalflip(lampe), x-(lampe.getWidth()/2),  y+(m_oComponent.getHeight()/10), null, m_oComponent);
			else
				g.drawImage(lampe, x-(lampe.getWidth()/2),  y+(m_oComponent.getHeight()/10), null, m_oComponent);


		}
	}
	private void shoot ()
	{


	}
	protected void resetPosition()
	{
		super.resetPosition();
		y= m_oComponent.getHeight()/10;
	}
	protected void move() 
	{

		super.move();
		if(SH_SYSTEM_Animation.getHero().getHealth()>0)
		{
			if(mx>x)
				x += m_oComponent.getWidth()/500;
			else if (mx<x)
				x-=m_oComponent.getWidth()/500;
		}

	}
	public void receiveDamage(int iDamage, Graphics g)
	{
		if( getHealth()>0)
		{
		
	
		}


	}
	public void drawSprite(Graphics g) throws IOException
	{

		int iheightScale=m_oComponent.getHeight()/5;
		int iwidthScale=m_oComponent.getWidth()/10;
		long actualTime=System.currentTimeMillis();
		g.setColor(Color.YELLOW);
		g.setFont(GlobaleKonstanten.GAMEFONT.deriveFont((float) 16));
		g.drawString(getName()+" ("+String.valueOf(getHealth()+")"), x, y+iheightScale);

		if(getAnimationList()!=null && SH_SYSTEM_Animation.getHero().getHealth()>0)
		{
			if(iAnimationCounter>= getAnimationList().size())
				iAnimationCounter=0;
			if(x<mx)
				g.drawImage(GraphicUtils.getScaledIconImage(SH_SYSTEM_Animation.horizontalflip((BufferedImage) getAnimationList().get(iAnimationCounter)),iwidthScale,iheightScale), (x-getAnimationList().get(iAnimationCounter).getWidth()), y, null);
			else
				g.drawImage( GraphicUtils.getScaledIconImage(getAnimationList().get(iAnimationCounter),iwidthScale,iheightScale), x-getAnimationList().get(iAnimationCounter).getWidth(), y, null);
			if(actualTime > (iAnimationDate + iAnimationDelay))
			{
				if(iAnimationCounter>= getAnimationList().size())
					iAnimationCounter=0;
				else
					iAnimationCounter++;
				iAnimationDate=actualTime;
			}
		}

	}


	public void mouseEntered( MouseEvent e ) {
		// called when the pointer enters the applet's rectangular area
	}
	public void mouseExited( MouseEvent e ) {
		// called when the pointer leaves the applet's rectangular area
	}
	public void mouseClicked( MouseEvent e ) {
		// called after a press and release of a mouse button
		// with no motion in between
		// (If the user presses, drags, and then releases, there will be
		// no click event generated.)
		shoot();

		if(e.getButton()==MouseEvent.BUTTON2)
			try {
				SH_SYSTEM_Animation.add(new Zombie(this, m_oComponent));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}


	}
	public void mousePressed( MouseEvent e ) {  // called after a button is pressed down
		shoot();


	}
	public void mouseReleased( MouseEvent e ) {  // called after a button is released


	}
	public void mouseMoved( MouseEvent e ) {  // called during motion when no buttons are down
		mx = e.getX();
		my = e.getY();

	}
	public void mouseDragged( MouseEvent e ) {  // called during motion with buttons down
		mx = e.getX();
		my = e.getY();


	}

	@Override
	public ArrayList<BufferedImage> setAnimationList(ArrayList<BufferedImage> m_oAnimationList)throws IOException
	{

		bigImg = ImageIO.read(resourceManager.getClass().getResourceAsStream("graphics/icons/walk.png"));
		//bigImg = ImageIO.read(new File("C:\\temp\\walk.png"));
		final int width = 32;
		final int height = 52;

		int x = 32;
		int y =52;
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
