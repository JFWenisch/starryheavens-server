package starryheavens.system;
import severeLobster.backend.spiel.Spiel;
import severeLobster.backend.spiel.SternenSpielApplicationBackend;
import severeLobster.frontend.application.MainFrame;


public class SH_R_SpielUeberpuefen implements Runnable
{

SternenSpielApplicationBackend oSpiel;
	public SH_R_SpielUeberpuefen(SternenSpielApplicationBackend sternenSpielApplicationBackend) {
	oSpiel=sternenSpielApplicationBackend;
	}

	public void run() 
	{
	
		try 

		{

			Thread.sleep(3000);
			if(oSpiel!=null)
			oSpiel.zurueckZumLetztenFehlerfreienSpielzug();
		} 
		catch (InterruptedException e) 
		{


		}	
	}

}
