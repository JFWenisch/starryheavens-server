package system;



import infrastructure.ResourceManager;
import infrastructure.constants.GlobaleKonstanten;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JPanel;

import severeLobster.backend.spiel.OnlineHighscore;
import severeLobster.backend.spiel.SternenSpielApplicationBackend;


public class SHDLUpdateService implements Runnable
{

	public void run()
	{

	
		while (true) 
		{


			//MainFrame.mainPanel.repaint();
			//	SH_SYSTEM_Animation.repaint(g);
			SHDLUserInterface.updateRanks();
			SHDLFrame.oUserCount.setText("USER: "+SHDLUserInterface.getUserCount());
			SHDLFrame.oUpdated.setText("Last update: "+new Date().toString());
		
			try 
			{
				Thread.sleep(300000);
		//S		System.out.println("UPDATEService goes sleeping...");
			} catch (InterruptedException e) {
			
			}

		}
	}

	
	
}

