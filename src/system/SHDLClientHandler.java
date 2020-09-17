package system;

import infrastructure.ResourceManager;
import infrastructure.components.FTPConnector;
import infrastructure.constants.GlobaleKonstanten;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import severeLobster.backend.spiel.OnlineHighscore;
import severeLobster.backend.spiel.Spiel;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


public class SHDLClientHandler implements Runnable
{

	private static Socket clientSocket;
	private static PrintWriter out;
	private static	BufferedReader in;

	private static ResourceManager resourceManager;

	public String User;

	//private static SternenSpielApplicationBackend backend = new SternenSpielApplicationBackend();
	public SHDLClientHandler(Socket _clientSocket)
	{
		clientSocket = _clientSocket;
	}

	private  String getAction(String strInput)
	{
		if (strInput==null)
			return "Unbekannter Befehl";

		if(strInput.startsWith("UPLOADPUZZLE|"))	
			return receivePuzzle(strInput.substring(strInput.indexOf('|')+1, strInput.length()));
		else if (strInput.startsWith("KILLS|"))
			return receiveKills(strInput.substring(strInput.indexOf('|')+1, strInput.length()));
		else if (strInput.equals("GETSTATUS"))
			return sendOnlineStatus();
		else if (strInput.equals("GETPUZZLES"))
			return sendPuzzles();
		else if (strInput.startsWith("GETPUZZLE|"))
			return sendPuzzle(strInput.substring(strInput.indexOf('|')+1, strInput.length()));
		else if (strInput.startsWith("GETHIGHSCORE|"))
			return sendHighscore(strInput.substring(strInput.indexOf('|')+1, strInput.length()));
		else if (strInput.startsWith("GETRANKING"))
			return sendRanking();
		else if (strInput.startsWith("GETSTARCOUNT"))
			return sendStars();
		else if (strInput.startsWith("GETKILLCOUNT"))
			return sendKills();
		else if (strInput.startsWith("GETRANK"))
			return sendRank();


		return "Unbekannter Befehl";
	}



	private synchronized String  sendHighscore(String substring) {
		String strPuzzle= substring.substring(substring.indexOf("|")+1, substring.length())+".puz";
		String strScore=substring.substring(0, substring.indexOf("|"));
		File file= new File(GlobaleKonstanten.DEFAULT_FREIGEGEBENE_PUZZLE_SAVE_DIR, strPuzzle);
		XStream xstream = new XStream(new DomDriver());
		InputStream inputStream;
		try 
		{
			System.out.println("Lese:"+file.getPath());
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e)
		{
			return ("GETHIGHSCORE| " + e.getMessage());
		}
		Spiel spSpiel = (Spiel) xstream.fromXML(inputStream);
		try 
		{
			spSpiel.score.readObject(new FileInputStream(GlobaleKonstanten.DEFAULT_FREIGEGEBENE_PUZZLE_SAVE_DIR+File.separator+strPuzzle.substring(0,strPuzzle.length()-4)+".scr"));
		} catch (Exception e1)
		{
			System.out.println("Kein Highscore für "+strPuzzle+" vorhanden");
			spSpiel.score = new OnlineHighscore();

		}
		try {
			spSpiel.score.compare(User+"-"+strScore);
			spSpiel.score.writeObject(new FileOutputStream(GlobaleKonstanten.DEFAULT_FREIGEGEBENE_PUZZLE_SAVE_DIR+File.separator+strPuzzle.substring(0,strPuzzle.length()-4)+".scr"));
			resourceManager.setScore(strScore, strPuzzle);
			if(Integer.valueOf(strScore)>0)
				resourceManager.addStars(spSpiel.getSpielfeld().countSterne());
		} catch (Exception e) {
			return ("GETHIGHSCORE| " + e.getMessage());
		}
		file=new File(GlobaleKonstanten.DEFAULT_FREIGEGEBENE_PUZZLE_SAVE_DIR+File.separator+strPuzzle.substring(0,strPuzzle.length()-4)+".scr");

		try
		{
			String[] scoreArray=spSpiel.score.get();
			
			out.println(String.valueOf(scoreArray.length));
			for(int i =0;i<scoreArray.length;i++)
				out.println(scoreArray[i]);
			out.println("|fertig");
			out.flush();
		}
		catch (Exception e)
		{
			return ("GETHIGHSCORE| " + e.getMessage());
		}
		return file.getName()+" erfolgreich übertragen";
	}
	private synchronized String  sendRanking()
	{
		OnlineHighscore score = new OnlineHighscore(SHDLUserInterface.getUserCount());
		try 
		{
		score.readObject(new FileInputStream(GlobaleKonstanten.DEFAULT_CONF_SAVE_DIR+File.separator +"Ranking.scr"));
		} 
		catch (Exception e1)
		{
			System.out.println("Kein Ranking vorhanden");
			

		}
	
		try
		{
			String[] scoreArray=score.get();
			out.println(String.valueOf(scoreArray.length));
			for(int i =0;i<scoreArray.length;i++)
				out.println(scoreArray[i]);
			out.println("|fertig");
			out.flush();
		}
		catch (Exception e)
		{
			return ("GETHIGHSCORE| " + e.getMessage());
		}
		return "Ranking erfolgreich übertragen";
	}

	private static synchronized String sendPuzzle(String substring) 
	{



		try
		{
			File file= new File(GlobaleKonstanten.DEFAULT_FREIGEGEBENE_PUZZLE_SAVE_DIR, substring);

			//Datei einlesen
			byte [] mybytearray  = new byte [(int)file.length()];
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			bis.read(mybytearray,0,mybytearray.length);
			bis.close();

			//Bytestream Senden
			OutputStream oos = clientSocket.getOutputStream();
			oos.write(mybytearray,0,mybytearray.length);
			oos.flush();
			out.println("|fertig");

		}
		catch (Exception e)
		{
			return ("GETPUZZLE| " + e.getMessage());
		}
		return substring+" erfolgreich übertragen";
	}
	private synchronized String sendStars()
	{
		// TODO Auto-generated method stub
		return ((ResourceManager) resourceManager).getStars();
	}
	private synchronized String sendKills()
	{
		// TODO Auto-generated method stub
		return ((ResourceManager) resourceManager).getKills();
	}
	private synchronized String sendRank()
	{
		return resourceManager.getRanking();
	}
	private  synchronized String sendOnlineStatus()
	{

	

		return SHDLFrame.oUserCount.getText()+" | "+"PUZZLE: "+ String.valueOf(PuzzleManager.getPuzzles().length)+" | "+SHDLFrame.oUpdated.getText();
	}
	private static synchronized String sendPuzzles()
	{
		File[] puzzles =PuzzleManager.getPuzzles();

		SHDLFrame.oPuzzleCount.setText("PUZZLE: "+String.valueOf(puzzles.length));
		out.println(String.valueOf(puzzles.length));
		for(int i=0;i<puzzles.length;i++)
		{
			out.println( puzzles[i].getName() );
		}

		out.flush();
		return "Puzzles:"+ String.valueOf(puzzles.length);
	}
	private  synchronized String receiveKills(String strInput)
	{
	resourceManager.addKills(Integer.valueOf(strInput));
		
		return "Upload erfolgreich";
	}
	private  synchronized String receivePuzzle(String strInput)
	{
		Spiel spSpiel=null;
		File tmpFile=null;
		String userInput;
		try
		{
			tmpFile= new File(GlobaleKonstanten.DEFAULT_FREIGEGEBENE_PUZZLE_SAVE_DIR,strInput);
			if (tmpFile.exists())
			{
				tmpFile.delete();
				System.out.println(tmpFile.getName()+" überschrieben");
			}
			FileWriter writer = new FileWriter(tmpFile ,true);

			while ((userInput = in.readLine()) != null) 
			{	  
				if(userInput.contains("|fertig"))
				{					
					String strLastLine =userInput.substring(0, userInput.indexOf('>')+1);
					writer.write(strLastLine);
					writer.flush();
					writer.close();
					break;
				}
				writer.write(userInput); 
			}
			XStream xstream = new XStream(new DomDriver());
			InputStream inputStream = new FileInputStream(tmpFile);
			spSpiel = (Spiel) xstream.fromXML(inputStream);
			inputStream.close();
			tmpFile.renameTo(new File(GlobaleKonstanten.DEFAULT_FREIGEGEBENE_PUZZLE_SAVE_DIR,strInput.substring(0,strInput.length()-4)+"-"+spSpiel.getSchwierigkeitsgrad()+"-"+(spSpiel.getSpielfeld().getBreite()*spSpiel.getSpielfeld().getHoehe())+"-"+User+"-.puz"));
			resourceManager.addStars((spSpiel.getSpielfeld().countSterne())*10);//tmpFile.delete();
		}
		catch (Exception e)
		{

			return e.toString();
		} 
	
		return "Upload erfolgreich";
	}
	private synchronized boolean initClient(String strMessage) 
	{
		if(!strMessage.startsWith("VERIFY|"))
			return false;
		try
		{
			strMessage=strMessage.substring(strMessage.indexOf("|")+1, strMessage.length());

			if(verifyVersion(strMessage.substring(0, strMessage.indexOf("|"))))
				if(verifyUser(strMessage.substring(strMessage.indexOf("|")+1, strMessage.length())))
				{
					out.println("ACK");
					out.flush();
				}
				
		}
		catch(Exception e)
		{
			return false;
		}

		return true;
	}

	private synchronized boolean verifyUser(String substring) 
	{
		User= substring;
		resourceManager = new ResourceManager(User);
		System.out.println(User + " checking password..");
		if (verifyPassword())
		{
			System.out.println(User + " eingeloggt!");
			return  true;	
		}
		return false;
		
	}

	private synchronized boolean verifyVersion(String substring) 
	{
		if(!substring.equals(SHDLServer.strClientVersion))
			out.println("UPDATE|"+SHDLServer.strUpdateUrl);
		out.flush();
		return true;
	}
	private synchronized boolean verifyPassword() 
	{
		String strPassword =null;
		try
		{
			 strPassword = resourceManager.getPassword();
		
		}
		catch(Exception euuu)
		{
			strPassword = "REGISTER";
			System.out.println("Neues Password wird vergeben");
		}
		
		String text = "";
		if(strPassword.equals("REGISTER"))
		{
			
			out.println(strPassword);
		
			try {
				 text = in.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			resourceManager.setPassword(text);
			return true;
		}
		else
		{
			out.println("REGISTER");
			
			try {
				 text = in.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			out.flush();
			if(text.equals(strPassword))
				return true;
			
			 return false;
		}
		
		
	}

	public void run()
	{
		try
		{
			
		
			System.out.println("Thread " + Thread.currentThread().getId() + " für Clientanfrage gestartet !");
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String strInput = null;
			String strOutput = null;
			if(initClient(in.readLine()))
				while(clientSocket.isConnected())
				{
					try
					{

						strInput=in.readLine();	
						System.out.println(User + ": Eingabe: " + strInput);
						strOutput=getAction(strInput);
						System.out.println(User + ": Ausgabe: " +strOutput);
					}
					catch (IOException ioEx)
					{
						System.out.println( User +" "+ ioEx.getMessage());
						break;
					}	

					if(!(strOutput.equals("Unbekannter Befehl")))
						out.println(strOutput);
					out.flush();
				}
			System.out.println(User + " ausgeloggt!");
		}
		catch (IOException ioEx)
		{
			System.out.println("Fehler beim Schreiben:" + ioEx.getMessage());
		}
	}
	
}
