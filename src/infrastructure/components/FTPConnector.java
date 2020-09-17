package infrastructure.components;

import infrastructure.ResourceManager;
import infrastructure.constants.GlobaleKonstanten;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import severeLobster.frontend.application.MainFrame;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Util zum Arbeiten mit dem FTP auf dem die Puzzles gespeichert werden
 *
 * @author JFW
 * @date 17.11.2012
 */
public class FTPConnector {

	private FTPClient ftp;
	private String server, username, password;
	private int port;
	public File[] files;
	private final ResourceManager resourceManager = ResourceManager.get();

	public FTPConnector(String server, String username, String password, int port) {
		ftp = new FTPClient();
		this.server = server;
		this.username = username;
		this.password = password;
		this.port = port;
		connect();
		updateFiles();
	}

	public void connect() {
		if(!ftp.isConnected())
		{
			try {

				ftp.connect(server, port);

				ftp.login(username, password);

				ftp.enterLocalPassiveMode();
			} catch (Exception e) 
			{
				System.out.println("Es konnte keine Verbindung zum FTP herstellt werden");
			}
		}
	}

	public void disconnect() {
		try {
			boolean logout = ftp.logout();
			if (logout) {
				System.out.println("Logout from FTP server...");
			}
			ftp.disconnect();
			System.out.println("Disconnected from " + server);
		} catch (IOException ignored) {
		}
	}

	/**
	 * Aktualisiert das interne Array in dem alle verfügbaren Puzzles im Online Archiv gespeichert sind
	 *
	 * @author JFW
	 * @date 16.11.2012
	 */
	public void updateFiles() {

		File dir = GlobaleKonstanten.DEFAULT_FREIGEGEBENE_PUZZLE_SAVE_DIR;
		File[] ftemp=dir.listFiles();
		ArrayList oPuzzles = new ArrayList();
		for(int i=0; i < ftemp.length;i++)
			if(ftemp[i].toString().endsWith(".puz"))
				oPuzzles.add(ftemp[i]);

		files = new File[oPuzzles.size()];
		for(int j=0; j < oPuzzles.size();j++)
			files[j]=(File) oPuzzles.get(j);
		
		if (files.length > 0)
			MainFrame.jlOnlineSpiele.setText("| " + files.length + " " +
					resourceManager.getText("online.archive.count.text"));

	}
	public File[] getFiles()
	{
	
		files =GlobaleKonstanten.DEFAULT_FREIGEGEBENE_PUZZLE_SAVE_DIR.listFiles();

		return files;
	}

	/**
	 * Methode zum Download eines einzelnen Puzzles
	 *
	 * @param strPuzzleName - <String> PuzzleName auf dem FTP
	 */
	public File getFile(Component owner, String strPuzzleName) {
		connect();
		File f;
		File file = null;
		try {

			boolean success = false;
			for (File file1 : files) {
				if (file1.getName().equals(strPuzzleName)) {
					f = file1;
					file = new File(GlobaleKonstanten.DEFAULT_FREIGEGEBENE_PUZZLE_SAVE_DIR, f.getName());
					/**
					 * Hier wird radikal alles überschrieben, was bei 3 nicht im Papierkorb ist
					 * -> Unnötig da anderes Konzept für OnlineSpiele
					 * @author fwenisch
					if (file.exists()) {
						JOptionPane.showMessageDialog(owner,
								resourceManager.getText("online.archive.already.there.title"),
								resourceManager.getText("online.archive.already.there.body"),
								JOptionPane.ERROR_MESSAGE);
							} 
					 */

					FileOutputStream fos = new FileOutputStream(file);
					success = ftp.retrieveFile(file1.getName(), fos);
					fos.close();

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * Methode zum Upload der Puzzles
	 *
	 * @param localSourceFile  - <String> Lokale Datei
	 * @param remoteResultFile - <String> Datei auf dem FTP-Server
	 * @author JFW
	 * @date 17.11.2012
	 */
	public void upload(String localSourceFile, String remoteResultFile) {

		connect();

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(GlobaleKonstanten.DEFAULT_FREIGEGEBENE_PUZZLE_SAVE_DIR+File.separator+localSourceFile);
			ftp.storeFile(remoteResultFile, fis);
		} catch (Exception e) {
			System.out.println("Fehler beim Uploaden von " + localSourceFile);

		} finally {
			try {
				if (fis != null) {
					fis.close();
				}

			} catch (IOException e) {
				System.out.println("Fehler beim Schließen der FTP-Verbindung aufgetreten");
			}
		}
	}

	/**
	 * @return Wurde eine Verbindung hergestellt?
	 */
	public boolean isOnline() {
		return ftp.isConnected();
	}
}
