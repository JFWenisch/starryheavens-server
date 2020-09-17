package system;


import infrastructure.components.FTPConnector;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;


public class SHDLServer
{
	public static String strClientVersion = "0.9a";
	public static String strUpdateUrl = "http://www.nirako-it.de/STARRYHEAVENS";
	private static boolean headless=false;
	public static  FTPConnector ftpConnector;
	public static void main(String[] args)
	{
		if(args.length == 0)
		{
			System.out.println("No run arguments found");
		}
		else
		{
			if(Arrays.asList(args).contains("headless"))
				headless=true;
		}
		if(!headless)
		{
			try
			{
				JFrame oFrame = new SHDLFrame();
				oFrame.setVisible(true);
			}
			catch(Exception eNowWindow)
			{
				System.out.println("Running in cmdmode");
			}
		}

		ServerSocket serverSocket = null;

		try
		{
			serverSocket = new ServerSocket(13000);
		}
		catch (IOException e)
		{
			System.out.println("Binden an Port  13000 schlug fehl: " + e.getMessage());
			System.exit(-1);
		}
		try
		{
			ftpConnector = new FTPConnector("ftp.strato.de","starryheavens@extranet.nirako.de", "heavens0815!", 21);
			ftpConnector.connect();

		}
		catch(Exception ex)
		{
			System.out.println("FTP-Verbindung konnte nicht hergestellt werden:"+ex.getStackTrace());
		}

		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(new SHDLUpdateService());

		/**
		 * Endlosschleife
		 */
		while (true)
		{
			try
			{

				System.out.println ("ServerSocket - accepting");
				Socket clientSocket = serverSocket.accept();

				//Wenn die Anfrage da ist, dann wird ein Thread gestartet, der 
				//die weitere Verarbeitung übernimmt.
				System.out.println ("ServerSocket - accept done");
				executor.execute(new SHDLClientHandler(clientSocket));
				System.out.println ("ServerSocket - Thread started, next client please...");
			}
			catch (IOException e)
			{
				System.out.println (e.toString());
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				main(new String[]{e.toString()});
			}

		}
	}
} 