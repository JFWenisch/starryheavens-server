package severeLobster.backend.spiel;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class OnlineHighscore implements Serializable
{
	String[] elementData;
	public OnlineHighscore()
	{
		elementData = new String[10];
	}
	public OnlineHighscore(int iCount)
	{
		elementData = new String[iCount];
	}
	public void writeObject( FileOutputStream f ) throws IOException
	{ 
		ObjectOutputStream s = new ObjectOutputStream(f);
		for ( int i = 0; i < elementData.length; i++ ) 
			s.writeObject( elementData[ i ] ); 
	} 

	public void readObject( FileInputStream f ) throws IOException,ClassNotFoundException { 
		ObjectInputStream s = new ObjectInputStream(f);
		//s.defaultReadObject(); 

		for ( int i = 0; i < elementData.length; i++ ) 
			elementData[ i ] = (String) s.readObject(); 
	}
	public void initObject( String[] strScore ) throws Exception { 

		for ( int i = 0; i < strScore.length; i++ ) 
			elementData[ i ] = strScore[i]; 
	}
	public void compare(String strScore)
	{
		int iScore = Integer.parseInt(strScore.substring(strScore.indexOf('-')+1));
		String strName =strScore.substring(0, strScore.indexOf('-'));
		/**
		 * Refactoring false Score nicht initialisiert wurde
		 * @author Jean 03.03.2013
		 */
		for (int i=0;i<elementData.length;i++)
		{
			if(elementData[i]==null)
				elementData[i]="Anonym-0";
		}

		ArrayList <OnlineHighscoreComparable> oList = new ArrayList();
		boolean bFoundEntry = false;

		/**
		 *  Alle Objekte in Comparable umbenennen
		 *  @author fwenisch 04.01.2015
		 */
		for (int i=0;i<elementData.length;i++)
		{
			String userName= elementData[i].substring(0, elementData[i].indexOf('-'));
			int userScore = Integer.parseInt(elementData[i].substring(elementData[i].indexOf('-')+1));
			if(userName.equals(strName))
			{
				if(iScore<userScore)
				{
					oList.add(new OnlineHighscoreComparable(userName,userScore));
					bFoundEntry=true;

				}
				else
				{
				}
			}
			else
			{
				oList.add(new OnlineHighscoreComparable(userName,userScore));


			}
		}
		if(!bFoundEntry)
		{
			oList.add(new OnlineHighscoreComparable(strName,iScore));
		

		}
		//sortieren

		Collections.sort(oList);
		elementData= new String[oList.size()];
		/**
		 * Array aktualisieren
		 * @author Jean 04.01.2015
		 */
		for (int i=0;i<elementData.length;i++)
		{
			elementData[i]=oList.get(i).strUserName+"-"+oList.get(i).iScore;
		}
		//		
		//		String strSmall=elementData[elementData.length-1].substring(elementData[elementData.length-1].indexOf('-')-1);
		//		while(strSmall.startsWith(" ")|strSmall.contains("-"))
		//			strSmall= strSmall.substring(1, strSmall.length());	
		//		
		//		if(strSmall.equals(""))
		//			strSmall="0";
		//		int iSmall = Integer.parseInt(strSmall);
		//		String[] strTemp = new String[elementData.length];
		//		/**
		//		 * Ist unter den Top 10?
		//		 */
		//		if(iScore>=iSmall)
		//		{
		//			int iIndex=elementData.length;
		//			/**
		//			 * An welcher Stelle in der Liste?
		//			 */
		//			while(iScore>=iSmall && iIndex>0)
		//			{
		//				iIndex--;
		//				String strElement=elementData[iIndex].substring(elementData[iIndex].indexOf('-')+1);
		//				while(strElement.startsWith(" "))
		//					strElement= strElement.substring(1, strElement.length());	
		//				if(strElement.equals(""))
		//					strElement="0";
		//				iSmall=Integer.parseInt(strElement);
		//			}
		//			for (int i=0; i<elementData.length; i++)
		//			{
		//				if(iIndex>i)
		//				{
		//					strTemp[i]=elementData[i];
		//				}
		//				else if(iIndex==i)
		//				{
		//					strTemp[i]=	strScore;
		//				}
		//				else if(iIndex<i)
		//				{
		//					strTemp[i]=	elementData[i-1];
		//				}
		//			}
		//			elementData=strTemp;
		//		}
	}
	public String[] get()
	{
		if(elementData==null)
			elementData = new String[elementData.length];
		return elementData;
	}
}
