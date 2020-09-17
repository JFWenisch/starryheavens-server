package system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import severeLobster.backend.spiel.OnlineHighscore;
import infrastructure.ResourceManager;
import infrastructure.constants.GlobaleKonstanten;

public class SHDLUserInterface 
{

	public static ArrayList<String> getUsers()
	{
		ArrayList<String> m_oUsers=new ArrayList();
		File[] files = new File(GlobaleKonstanten.DEFAULT_CONF_SAVE_DIR.toString()).listFiles();
		  if (files != null) { // Erforderliche Berechtigungen etc. sind vorhanden
		    for (int i = 0; i < files.length; i++) 
		    {
		   
		      if (files[i].isDirectory()) 
		     
		       continue;
		     
		      else 
		      {
		       String strName=files[i].getName();
		       if(!strName.endsWith(".properties"))
		    	   continue;
		       else
		       {
		    	   strName=strName.substring(0,strName.length()-11);
		    	
		    	   m_oUsers.add(strName);
		       }
		    	   
		    	
		      }
		    }
		
		  }
		return m_oUsers;
		  
	}
	public static int getUserCount()
	{
		return getUsers().size();
	}
	public static void updateRanks()
	{
		OnlineHighscore score = new OnlineHighscore(SHDLUserInterface.getUserCount());
		ArrayList<String> oList =SHDLUserInterface.getUsers();
		ResourceManager oRSM;
		int iKills;
		int iStars;
		System.out.println("USERS: "+oList.size());
		
		for(int i=0; i < oList.size(); i++)
		{
			oRSM = new ResourceManager(oList.get(i));
			if(oRSM.getStars()!=null)
			try
			{
				System.out.println(oList.get(i)+"-"+oRSM.getStars());
				score.compare(oList.get(i)+"-"+oRSM.getStars());
		
			}
			catch (Exception e)
			{
			System.err.println(e.toString());
			}
		}	
		try {
			score.writeObject(new FileOutputStream(GlobaleKonstanten.DEFAULT_CONF_SAVE_DIR+File.separator +"Ranking.scr"));
		}  catch (Exception e) {
			System.err.println(e.toString());
			
		}
		
		String[] strStore = score.get();
		System.out.println("##########  HIGHSCORE ##########");
		int iOutputCounter=1;
		for(int j=0;j <strStore.length;j++)
			{
			//Ranking in userprops aktualisieren
			oRSM=new ResourceManager(strStore[j].substring(0, strStore[j].indexOf("-")));
			oRSM.setRanking(iOutputCounter);
			System.out.println(String.valueOf(iOutputCounter)+". " + strStore[j]);
			
			iOutputCounter++;
			}
		
		

	}
	public static void main(String[] args) 
	{
		updateRanks();
	}
}
