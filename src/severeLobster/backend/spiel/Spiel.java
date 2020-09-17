package severeLobster.backend.spiel;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import infrastructure.GlobaleKonstanten;
import infrastructure.ResourceManager;
import infrastructure.components.StoppUhr;
import infrastructure.constants.enums.SchwierigkeitsgradEnumeration;
import infrastructure.constants.enums.SpielmodusEnumeration;
import infrastructure.exceptions.LoesungswegNichtEindeutigException;
import infrastructure.sound.AudioPlayer;
import severeLobster.frontend.application.MainFrame;
import severeLobster.frontend.dialogs.GewonnenDialog;
import severeLobster.frontend.dialogs.TutorialDialog;
import starryheavens.system.Zombieticker;

import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Stack;

/**
 * Spiel - Besteht aus einem Spielfeld von Spielsteinen. Stellt ein laufendes
 * Spiel dar und beinhaltet einen aktuellen Spielstand, der gespeichert und
 * geladen werden kann. Instanzen dieser Klasse sind in ihrem Zustand komplett
 * unabhaengig voneinander.
 *
 * @author Lars Schlegelmilch, Lutz Kleiber, Paul Bruell
 */
public class Spiel  implements Serializable{
	private final ResourceManager resourceManager = ResourceManager.get();
	private final EventListenerList listeners = new EventListenerList();
	public OnlineHighscore score= new OnlineHighscore();

	/**
	 * Spielfeld wird vom Spiel erstellt oder geladen.
	 */
	private Spielfeld currentSpielfeld;
	private final ISpielfeldListener innerSpielfeldListener = new InnerSpielfeldListener();
	private String saveName;

	/**
	 * Tracking:
	 */
	private ActionHistory spielZuege;
	private Stack<ActionHistoryObject> trackingPunkte;
	private int anzahlZuege = 0;
	private int iHighscore=0;

	private final StoppUhr spielStoppUhr;
	private String spielZeit = "--";
	private boolean isSolo;

	/**
	 * Spielfeld wird mit Standardfeld initialisiert. Nach der Erstellung ist
	 * man im Spielmodus Editieren.
	 */
	public Spiel() {
		this.spielStoppUhr = new StoppUhr();
		this.currentSpielfeld = new Spielfeld(10, 10);
		currentSpielfeld.addSpielfeldListener(innerSpielfeldListener);
		if (getSpielmodus().equals(SpielmodusEnumeration.SPIELEN)) {
			spielZuege = new ActionHistory();
		}
		trackingPunkte = new Stack<ActionHistoryObject>();
	}

	private void replay()
	{
		
	}
	/**
	 * Spielstein setzen.
	 *
	 * @param x          X-Achsenwert
	 * @param y          Y-Achsenwert
	 * @param spielstein zu setzender Spielstein
	 * @return ob spielfeld fehler enthaelt.
	 */
	public boolean setSpielstein(int x, int y, Spielstein spielstein) {
		currentSpielfeld.setSpielstein(x, y, spielstein);

	
		
		return hasErrors();
	}

	public Spielstein getSpielstein(int x, int y) {
		return currentSpielfeld.getSpielstein(x, y);
	}

	public StoppUhr getSpielStoppUhr() {

		/*
		 * SpielstoppUhr wird bei dieser Version als Member beim Erstellen
		 * gesetzt und ist dann konstant. Wenn StopUhr null ist, ist die Instanz
		 * eine alte Version der Klasse.
		 */
		if (null == spielStoppUhr) {
			/* Exception Block temporaer, bis alles angepasst ist. */
			try {
				/* Braucht net uebesetzt zu werden */
				throw new NullPointerException(
						"Spiel Klasse hat sich geaendert und ist mit geladenem Spiel nicht mehr kompatibel. Gespeichertes Spiel muss mit neuer Klassenversion neu erstellt werden.");
			} catch (NullPointerException e) {
				e.printStackTrace();
				throw e;
			}
		}
		return spielStoppUhr;
	}

	/**
	 * Gibt den Schwierigkeitsgrad des Spielfeldes zurueck
	 *
	 * @return Schwierigkeitsgrad
	 */
	public SchwierigkeitsgradEnumeration getSchwierigkeitsgrad() {
		return getSpielfeld().getSchwierigkeitsgrad();
	}

	public Spielfeld getSpielfeld() {
		return currentSpielfeld;
	}

	/**
	 * Setzt ein neues, leeres Spielfeld fuer dieses Spiel. Benachrichtigt
	 * listener dieser Instanz ueber spielfeldChanged().
	 *
	 * @param x Laenge der x-Achse
	 * @param y Laenge der y-Achse
	 */
	public void initializeNewSpielfeld(final int x, final int y) {

		final Spielfeld newSpielfeld = new Spielfeld(x, y);
		setSpielfeld(newSpielfeld);
	}

	public void aendereSpielfeldGroesse(final int x, final int y) {
		final Spielfeld neuesSpielfeld = new Spielfeld(getSpielfeld(), x, y);
		setSpielfeld(neuesSpielfeld);
	}

	private void setSpielfeld(final Spielfeld newSpielfeld) {
		final SpielmodusEnumeration oldSpielmodus = getSpielmodus();
		final Spielfeld oldSpielfeld = getSpielfeld();
		if (null != oldSpielfeld) {
			oldSpielfeld.removeSpielfeldListener(innerSpielfeldListener);
		}
		newSpielfeld.addSpielfeldListener(innerSpielfeldListener);
		this.currentSpielfeld = newSpielfeld;
		fireSpielfeldChanged(currentSpielfeld);
		if (oldSpielmodus != getSpielmodus()) {
			fireSpielmodusChanged(getSpielmodus());
		}
	}

	public SpielmodusEnumeration getSpielmodus() {
		return getSpielfeld().getSpielmodus();
	}

	/**
	 * Gibt die Highscore anhand der Anzahl der Zuege, der Zeit und des
	 * Spielfeldes zurueck Ist der Spieler so schlecht das er keine positive
	 * High- score erreicht, so wird eine Score von "1" zurueckgegeben.
	 *
	 * @return Highscore des Spiels
	 */
	public int getHighscore() {
		if (spielStoppUhr != null)
		{
			int fehlversuche = getAnzahlZuege()
						- currentSpielfeld.countSterne();
				int groesse = currentSpielfeld.getBreite()
						* currentSpielfeld.getHoehe();
				long faktor = (long) (groesse / (fehlversuche + 1));
				int sekunden = (int) spielStoppUhr.getSekunden();
				int score = (int) (faktor) * 1000 - sekunden;
				iHighscore=score;
				try {
					this.score.readObject(new FileInputStream(GlobaleKonstanten.DEFAULT_FREIGEGEBENE_PUZZLE_SAVE_DIR+"\\"+getSaveName()+".scr"));
				} catch (Exception e1) {
					// Kein Highscore vorhanden!
					
				}
				

				this.score.compare(System.getProperty("user.name")+"-"+iHighscore);
				try {
					this.score.writeObject(new FileOutputStream(GlobaleKonstanten.DEFAULT_FREIGEGEBENE_PUZZLE_SAVE_DIR+"\\"+getSaveName()+".scr"));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
        
        
		return iHighscore;
	}
	/**
	 * Speichert das aktuelle Spiel
	 *
	 * @param spielname Name der Datei (ohne Dateiendung)
	 * @return 
	 * @throws IOException Exception falls Datei nicht speicherbar
	 */
	/**
	 * Speichert das aktuelle Spiel
	 *
	 * @param spielname Name der Datei (ohne Dateiendung)
	 * @return 
	 * @throws IOException Exception falls Datei nicht speicherbar
	 */
	public XStream saveSpiel(String spielname) throws IOException {
		if (getSpielmodus().equals(SpielmodusEnumeration.SPIELEN)) {
			getSpielStoppUhr().stop();
		}
		XStream xstream = new XStream(new DomDriver());
		String dateiendung = "." + getDateiendung(getSpielmodus());
		File verzeichnis = new File(getVerzeichnis(getSpielmodus()), spielname
				+ dateiendung);
		OutputStream outputStream = new FileOutputStream(verzeichnis);
		xstream.toXML(this, outputStream);
		outputStream.close();
		if (getSpielmodus().equals(SpielmodusEnumeration.SPIELEN)) {
			getSpielStoppUhr().start();
		}
		return xstream;
	}

	/**
	 * Laed ein Spiel aus .sav / .puz - Dateien
	 *
	 * @param spielname Name der Datei (ohne Dateiendung)
	 * @throws IOException Exception falls Datei nicht lesbar
	 */
	public static Spiel loadSpiel(String spielname,
			SpielmodusEnumeration spielmodus) throws IOException {
		XStream xstream = new XStream(new DomDriver());
		String dateiendung = "." + getDateiendung(spielmodus);
		File verzeichnis = new File(getVerzeichnis(spielmodus), spielname
				+ dateiendung);
		InputStream inputStream = new FileInputStream(verzeichnis);
		Spiel spiel = (Spiel) xstream.fromXML(inputStream);
		inputStream.close();
		if (SpielmodusEnumeration.SPIELEN.equals(spiel.getSpielmodus())) {
			spiel.getSpielStoppUhr().start();
		}
		return spiel;
	}

	public static Spiel newSpiel(String strPuzzleName, boolean b) throws IOException, LoesungswegNichtEindeutigException {
		XStream xstream = new XStream(new DomDriver());
		String dateiendung = "." + GlobaleKonstanten.PUZZLE_DATEITYP;
		File verzeichnis = new File(
				GlobaleKonstanten.DEFAULT_FREIGEGEBENE_PUZZLE_SAVE_DIR,
				strPuzzleName + dateiendung);
		InputStream inputStream = new FileInputStream(verzeichnis);
		Spiel neuesSpiel = (Spiel) xstream.fromXML(inputStream);
		inputStream.close();
		neuesSpiel.isSolo=b;
		neuesSpiel.setSpielmodus(SpielmodusEnumeration.SPIELEN);
		neuesSpiel.getSpielStoppUhr().start();
		return neuesSpiel;
	}
/**
	 * Laed ein erstelles Puzzle und aendert den Modus in den Spielmodus.
	 *
	 * @param spielname Name der Datei (ohne Dateiendung)
	 * @return Erstelltes Spiel im Spielmodus
	 * @throws IOException Wirft Exception, wenn Datei nicht vorhanden
	 * @throws LoesungswegNichtEindeutigException
	 *
	 */
	public static Spiel newSpiel(String spielname) throws IOException,
	LoesungswegNichtEindeutigException {
		XStream xstream = new XStream(new DomDriver());
		String dateiendung = "." + GlobaleKonstanten.PUZZLE_DATEITYP;
		File verzeichnis = new File(
				GlobaleKonstanten.DEFAULT_FREIGEGEBENE_PUZZLE_SAVE_DIR,
				spielname + dateiendung);
		InputStream inputStream = new FileInputStream(verzeichnis);
		Spiel neuesSpiel = (Spiel) xstream.fromXML(inputStream);
		inputStream.close();
		neuesSpiel.isSolo=false;
		neuesSpiel.setSpielmodus(SpielmodusEnumeration.SPIELEN);
		neuesSpiel.getSpielStoppUhr().start();
		return neuesSpiel;
	}

	/**
	 * Ueberprueft ob das Spielfeld geloest wurde (Sieg).
	 *
	 * @return sieg
	 */
	public boolean isSolved() {
		/***
		 * Methode in Spielfeld verschoben, um Spielfeld besser kapseln zu
		 * koennen.
		 */
		if (spielStoppUhr != null && currentSpielfeld.isSolved()) {
			spielStoppUhr.stop();
		}
		return currentSpielfeld.isSolved();
	}

	/**
	 * Ueberprueft ob Fehler in einem Spielfeld vorhanden sind, d.h. Tipps
	 * abgegeben wurden, die nicht der Loesung entsprechen
	 *
	 * @return fehler vorhanden
	 */
	public boolean hasErrors() {
		/**
		 * Methode in Spielfeld verschoben, um Spielfeld besser kapseln zu
		 * koennen.
		 */
		// TODO checken ob es einen unterschied macht ob man im edit oder
		// spielmodus ist wenn man nach fehlern prueft
		return currentSpielfeld.hasErrors();
	}

	/**
	 * Setzt den Spielmodus des aktuellen Spiels
	 *
	 * @param neuerSpielmodus Spielmodus des Spiels
	 * @throws LoesungswegNichtEindeutigException
	 *
	 */
	public void setSpielmodus(final SpielmodusEnumeration neuerSpielmodus)
			throws LoesungswegNichtEindeutigException {

		/* Aenderung feuert durch Kopplung auch die Listener von Spiel */
		getSpielfeld().setSpielmodus(neuerSpielmodus);
		if (neuerSpielmodus.equals(SpielmodusEnumeration.SPIELEN)) {
			if (spielZuege == null) {
				spielZuege = new ActionHistory();
			}
			/*
			 * Uhr wird nur gestartet, wenn auch wirklich auf Spielmodus SPIELEN
			 * gesetzt werden kann. Andernfalls wurde die Methode zuvor bereits
			 * durch die Exception verlassen
			 */
			getSpielStoppUhr().start();
		} else // neuer spielmodus ist EDITIEREN
		{
			getSpielStoppUhr().stop();
		}

	}

	private void fireSpielsteinChanged(final Spielfeld spielfeld, final int x,
			final int y, Spielstein newStein) {

		/** Gibt ein Array zurueck, das nicht null ist */
		final Object[] currentListeners = listeners.getListenerList();
		for (int i = currentListeners.length - 2; i >= 0; i -= 2) {
			if (currentListeners[i] == ISpielListener.class) {
				((ISpielListener) currentListeners[i + 1]).spielsteinChanged(
						this, spielfeld, x, y, newStein);
			}
		}
	}

	/**
	 * Benachrichtigt alle Listener dieses Spiel ueber einen neuen Wert an den
	 * uebergeben Koordinaten. Implementation ist glaube ich aus JComponent oder
	 * Component kopiert.
	 *
	 * @param newSpielfeld - Der neue Status, der an die Listener mitgeteilt wird.
	 */
	private void fireSpielfeldChanged(Spielfeld newSpielfeld) {

		/** Gibt ein Array zurueck, das nicht null ist */
		final Object[] currentListeners = listeners.getListenerList();
		for (int i = currentListeners.length - 2; i >= 0; i -= 2) {
			if (currentListeners[i] == ISpielListener.class) {
				((ISpielListener) currentListeners[i + 1]).spielfeldChanged(
						this, newSpielfeld);
			}
		}
	}

	private void fireSpielmodusChanged(final SpielmodusEnumeration newSpielmodus) {

		/** Gibt ein Array zurueck, das nicht null ist */
		final Object[] currentListeners = listeners.getListenerList();
		for (int i = currentListeners.length - 2; i >= 0; i -= 2) {
			if (currentListeners[i] == ISpielListener.class) {
				((ISpielListener) currentListeners[i + 1]).spielmodusChanged(
						this, newSpielmodus);
			}
		}
	}

	/**
	 * Fuegt listener zu der Liste hinzu.
	 *
	 * @param listener ISpielfeldListener
	 */
	public void addSpielListener(final ISpielListener listener) {
		listeners.add(ISpielListener.class, listener);
	}

	/**
	 * Entfernt listener von der Liste.
	 *
	 * @param listener ISpielsteinListener
	 */
	public void removeSpielListener(final ISpielListener listener) {
		listeners.remove(ISpielListener.class, listener);
	}

	/**
	 * Gibt die Dateiendung eines zu ladenen oder zu sichernden Spiels bzw.
	 * Puzzles anhand des Spielmodus zurück
	 *
	 * @param spielmodus Spielmodus des Spiels
	 * @return Dateiendung (.puz oder .sav)
	 */
	private static String getDateiendung(SpielmodusEnumeration spielmodus) {
		// Preconditions.checkNotNull(spielmodus);
		switch (spielmodus) {
		case SPIELEN:
			return GlobaleKonstanten.SPIELSTAND_DATEITYP;
		case EDITIEREN:
			return GlobaleKonstanten.PUZZLE_DATEITYP;
		}
		return null;
	}

	/**
	 * Gibt das Verzeichnis der puz/sav Dateien je nach Spielmodus zurueck
	 *
	 * @param spielmodus Spielmodus des Spiels
	 * @return Verzeichnis fuer puz/sav Dateien
	 */
	private static File getVerzeichnis(SpielmodusEnumeration spielmodus) {
		switch (spielmodus) {
		case SPIELEN:
			return GlobaleKonstanten.DEFAULT_SPIEL_SAVE_DIR;
		case EDITIEREN:
			return GlobaleKonstanten.DEFAULT_PUZZLE_SAVE_DIR;
		}
		return null;
	}

	/**
	 * Gibt den Namen des SaveGames zum Spiel zurueck
	 *
	 * @return Spielname
	 */
	public String getSaveName() {
		return saveName;
	}

	/**
	 * Setzt den Names des SaveGames
	 *
	 * @param saveName Name des SaveGames
	 */
	public void setSaveName(String saveName) {
		this.saveName = saveName;
	}

	public ActionHistory getSpielZuege() {
		return spielZuege;
	}

	public Stack<ActionHistoryObject> getTrackingPunkte() {
		return trackingPunkte;
	}

	public void entferneAlleTrackingPunkte() {
		while (trackingPunkte.size() != 0) {
			trackingPunkte.pop().setzeTrackingPunktNachDiesemZug(false);
		}
	}

	private class InnerSpielfeldListener implements ISpielfeldListener {

		@Override
		public void spielsteinChanged(Spielfeld spielfeld, int x, int y,
				Spielstein changedStein) {
			fireSpielsteinChanged(spielfeld, x, y, changedStein);
		}

		@Override
		public void spielmodusChanged(Spielfeld spielfeld,
				SpielmodusEnumeration neuerSpielmodus) {
			fireSpielmodusChanged(neuerSpielmodus);
		}
	}

	/**
	 * @return Die benutzten Versuche
	 * @author fwenisch
	 */
	public int getAnzahlZuege() {
		return anzahlZuege;
	}

	/**
	 * addiert +1 auf den Spielzugcounter des aktuellen Spiels
	 */
	public void addSpielZug() {
		anzahlZuege++;
	}

	public String getSpielZeit() {
		if (getSpielStoppUhr() != null) {
			spielZeit = String.valueOf(getSpielStoppUhr().getSekunden());
			
		}
		// TODO: Formatierung der Zeit (Sekunden)
		return spielZeit;
	}

	/**
	 * Gibt das aktuelle Puzzle zum Spielen frei
	 *
	 * @param spielname Name des freizugebenen Puzzles
	 * @return Erfolgreich freigegeben?
	 * @throws IOException
	 * @throws LoesungswegNichtEindeutigException
	 *
	 */
	public SolvingStrategy gebeSpielFrei(String spielname) throws IOException,
	LoesungswegNichtEindeutigException {
		SolvingStrategy strategy = getSpielfeld().loesungswegUeberpruefen();
		if (strategy.isSolvable() && strategy.isUnique()) {

			getSpielfeld().setSpielmodus(SpielmodusEnumeration.SPIELEN);
			XStream xstream = new XStream(new DomDriver());
			String dateiendung = "." + GlobaleKonstanten.PUZZLE_DATEITYP;
			File verzeichnis = new File(
					GlobaleKonstanten.DEFAULT_FREIGEGEBENE_PUZZLE_SAVE_DIR,
					spielname + dateiendung);
			OutputStream outputStream = new FileOutputStream(verzeichnis);
			xstream.toXML(this, outputStream);
			outputStream.close();
			setSpielmodus(SpielmodusEnumeration.EDITIEREN);
		}
		return strategy;
	}

}
