package severeLobster.backend.spiel;

import infrastructure.constants.GlobaleKonstanten;
import infrastructure.constants.enums.SpielmodusEnumeration;
import infrastructure.exceptions.LoesungswegNichtEindeutigException;
import severeLobster.backend.command.PrimaerAktion;
import severeLobster.frontend.view.MainView;

import javax.swing.event.EventListenerList;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Schnittstelle zwischen Backendlogik und Frontenddarstellung. Logik und
 * Informationen, die von der GUI aufgerufen bzw angezeigt werden, sind ueber
 * diese Klasse zugaengich sein - Direkt oder gekapselt. Instanz benachrichtigt
 * angemeldete ISternenSpielApplicationBackendListener, wenn sich irgendetwas am
 * Zustand der Anwendung aendert. Es gibt von dieser Klasse nur eine Instanz
 * (Singleton Pattern).
 * 
 * @author Lutz Kleiber, Paul Bruell, Lars Schlegelmilch
 * 
 */
public class SternenSpielApplicationBackend {

    /** Einzige Instanz */
    private static final SternenSpielApplicationBackend UNIQUE_INSTANCE = new SternenSpielApplicationBackend();
    private final EventListenerList listeners = new EventListenerList();
    private final ISpielListener innerSpielListener = new InnerSpielListener();
    private Spiel currentlyPlayedSpiel;
    private boolean zeigeFadenkreuz;
    /**
     * Highscore
     */
    OnlineHighscore score;
    public SternenSpielApplicationBackend() {
        this.currentlyPlayedSpiel = new Spiel();
        score=this.currentlyPlayedSpiel.score;
        currentlyPlayedSpiel.addSpielListener(innerSpielListener);
    }

    public static SternenSpielApplicationBackend getInstance() {
        return UNIQUE_INSTANCE;
    }

    public Spiel getSpiel()
    {
    
      try {
		this.currentlyPlayedSpiel.score.readObject(new FileInputStream(GlobaleKonstanten.DEFAULT_FREIGEGEBENE_PUZZLE_SAVE_DIR+"\\"+this.currentlyPlayedSpiel.getSaveName()+".scr"));
	} catch (Exception e) 
    {
	// Highschore konnte nicht geladen werden
	}
      
        return this.currentlyPlayedSpiel;
    }

    public void zeitrafferSetup()
    {
        try {
            currentlyPlayedSpiel.setSpielmodus(SpielmodusEnumeration.REPLAY);
        } catch (LoesungswegNichtEindeutigException e) {
        }
        currentlyPlayedSpiel.getSpielZuege().zeitrafferSetup();
    }

    public boolean zeitrafferSchritt()
    {
        return currentlyPlayedSpiel.getSpielZuege().zeitrafferSchritt();
    }

    public void zeitrafferCleanup()
    {
        currentlyPlayedSpiel.getSpielZuege().zeitrafferCleanup();
        try {
            currentlyPlayedSpiel.setSpielmodus(SpielmodusEnumeration.SPIELEN);
        } catch (LoesungswegNichtEindeutigException e) {
            e.printStackTrace();
        }
    }

    public void setzeTrackingPunkt() {
        ActionHistoryObject current = currentlyPlayedSpiel.getSpielZuege()
                .getCurrent();
        current.setzeTrackingPunktNachDiesemZug(true);
        currentlyPlayedSpiel.getTrackingPunkte().push(current);
    }

    public void zurueckZumLetztenFehlerfreienSpielzug() {
        currentlyPlayedSpiel.getSpielZuege().zurueckZuLetztemFehlerfreiemZug();
    }

    public void zurueckZumLetztenTrackingPunkt() {
        currentlyPlayedSpiel.getSpielZuege().zurueckZuLetztemCheckpoint();
        if (currentlyPlayedSpiel.getTrackingPunkte().size() > 0)
            currentlyPlayedSpiel.getTrackingPunkte().pop()
                    .setzeTrackingPunktNachDiesemZug(false);
    }

    public void entferneAlleTrackingPunkte() {
        currentlyPlayedSpiel.entferneAlleTrackingPunkte();
    }

    public void aendereSpielfeldGroesse(int x, int y) {
        getSpiel().aendereSpielfeldGroesse(x, y);
    }

    /***
     * Setzt beim aktuellen Spielfeld einen Stein. Verhalten ist nach aussen so
     * wie: Spielfeld.setSpielstein().
     * 
     * @param x
     * @param y
     * @param spielstein
     */
    public void setSpielstein(final int x, final int y,
            final Spielstein spielstein) {
        boolean fehler;

        PrimaerAktion spielZug = new PrimaerAktion(getSpiel());
        if (getSpiel().getSpielmodus().equals(SpielmodusEnumeration.SPIELEN)) {
            currentlyPlayedSpiel.getSpielZuege().neuerSpielzug(spielZug);
        }
        fehler = spielZug.execute(x, y, spielstein);
        if (getSpiel().getSpielmodus().equals(SpielmodusEnumeration.SPIELEN)) {
            currentlyPlayedSpiel.getSpielZuege().setzeFehlerhaft(fehler);
        }
    }

    /***
     * Gibt vom aktuellen Spielfeld den Spielstein an der Stelle.
     * 
     * @param x
     * @param y
     */
    public Spielstein getSpielstein(final int x, final int y) {
        return getSpiel().getSpielfeld().getSpielstein(x, y);
    }

    public int getSpielfeldBreite() {
        return getSpiel().getSpielfeld().getBreite();
    }

    public int getSpielfeldHoehe() {
        return getSpiel().getSpielfeld().getHoehe();
    }

    public int getCountSterneSpale(final int x) {
        return getSpiel().getSpielfeld().countSterneSpalte(x);
    }

    public int getCountSterneZeile(final int y) {
        return getSpiel().getSpielfeld().countSterneZeile(y);
    }

    public List<? extends Spielstein> listAvailableStates(int x, int y) {
        return getSpiel().getSpielfeld().listAvailableStates(x, y);
    }

    public void startNewSpielFrom(final String spielname) throws IOException,
            LoesungswegNichtEindeutigException {
        final Spiel newGame = Spiel.newSpiel(spielname);
        setSpiel(newGame);
    }


    public SolvingStrategy puzzleFreigeben(String spielname)
            throws LoesungswegNichtEindeutigException, IOException {
          return getSpiel().gebeSpielFrei(spielname);
    }

    public void uploadPuzzle(XStream xstream) {
        try {
            if (MainView.ftpConnector.isOnline()) 
            {
          
                MainView.ftpConnector.updateFiles();
            }

        } catch (Exception e) {
            System.out.println("Fehler beim Upload " + e.toString());
        }
    }

    public void loadSpielFrom(final String spielname) throws IOException {
        final Spiel loadedSpiel = Spiel.loadSpiel(spielname,
                SpielmodusEnumeration.SPIELEN);
        setSpiel(loadedSpiel);
    }

    public void saveCurrentSpielTo(final String spielname) throws IOException {
        getSpiel().saveSpiel(spielname);
    }

    public void loadPuzzleFrom(final String puzzlename) throws IOException {
        final Spiel loadedPuzzle = Spiel.loadSpiel(puzzlename,
                SpielmodusEnumeration.EDITIEREN);
        setSpiel(loadedPuzzle);
    }

    public XStream saveCurrentPuzzleTo(final String puzzlename) throws IOException {
    	return getSpiel().saveSpiel(puzzlename);
    }

    public void setSpiel(final Spiel spiel) {
        final Spiel currentlyListenedSpiel = getSpiel();
        if (null != currentlyListenedSpiel) {
            currentlyListenedSpiel.removeSpielListener(innerSpielListener);
        }
        spiel.addSpielListener(innerSpielListener);
        this.currentlyPlayedSpiel = spiel;
        try 
        {
			score.readObject(new FileInputStream(GlobaleKonstanten.DEFAULT_FREIGEGEBENE_PUZZLE_SAVE_DIR+"\\"+this.currentlyPlayedSpiel.getSaveName()+".scr"));
        } catch (Exception e) 
        {
		// Highschore konnte nicht geladen werden
		}
        fireSpielChanged(spiel);
    }

    /**
     * Fuegt listener zu der Liste hinzu.
     * 
     * @param listener
     *            ISpielfeldListener
     */
    public void addApplicationBackendListener(
            final ISternenSpielApplicationBackendListener listener) {
        listeners.add(ISternenSpielApplicationBackendListener.class, listener);
    }

    /**
     * Entfernt listener von der Liste.
     * 
     * @param listener
     *            ISpielsteinListener
     */
    public void removeApplicationBackendListener(
            final ISternenSpielApplicationBackendListener listener) {
        listeners.remove(ISternenSpielApplicationBackendListener.class,
                listener);
    }

    private void fireSpielChanged(Spiel spiel) {
        /** Gibt ein Array zurueck, das nicht null ist */
        final Object[] currentListeners = listeners.getListenerList();
        for (int i = currentListeners.length - 2; i >= 0; i -= 2) {
            if (currentListeners[i] == ISternenSpielApplicationBackendListener.class) {
                ((ISternenSpielApplicationBackendListener) currentListeners[i + 1])
                        .spielChanged(this, spiel);
            }
        }
    }

    private void fireSpielmodusChanged(final Spiel spiel,
            final SpielmodusEnumeration newSpielmodus) {

        /** Gibt ein Array zurueck, das nicht null ist */
        final Object[] currentListeners = listeners.getListenerList();
        for (int i = currentListeners.length - 2; i >= 0; i -= 2) {
            if (currentListeners[i] == ISternenSpielApplicationBackendListener.class) {
                ((ISternenSpielApplicationBackendListener) currentListeners[i + 1])
                        .spielmodusChanged(this, spiel, newSpielmodus);
            }
        }
    }

    private void fireSpielsteinChanged(final Spiel spiel,
            final Spielfeld spielfeld, final int x, final int y,
            Spielstein newStein) {

        /** Gibt ein Array zurueck, das nicht null ist */
        final Object[] currentListeners = listeners.getListenerList();
        for (int i = currentListeners.length - 2; i >= 0; i -= 2) {
            if (currentListeners[i] == ISternenSpielApplicationBackendListener.class) {
                ((ISternenSpielApplicationBackendListener) currentListeners[i + 1])
                        .spielsteinChanged(this, spiel, spielfeld, x, y,
                                newStein);
            }
        }
    }

    private void fireSpielfeldChanged(final Spiel spiel,
            final Spielfeld newSpielfeld) {

        /** Gibt ein Array zurueck, das nicht null ist */
        final Object[] currentListeners = listeners.getListenerList();
        for (int i = currentListeners.length - 2; i >= 0; i -= 2) {
            if (currentListeners[i] == ISternenSpielApplicationBackendListener.class) {
                ((ISternenSpielApplicationBackendListener) currentListeners[i + 1])
                        .spielfeldChanged(this, spiel, newSpielfeld);
            }
        }
    }

    /**
     * Zur Weiterleitung.
     * 
     * @author Lutz Kleiber
     * 
     */
    private class InnerSpielListener implements ISpielListener {

        @Override
        public void spielsteinChanged(Spiel spiel, Spielfeld spielfeld, int x,
                int y, Spielstein newStein) {
            fireSpielsteinChanged(spiel, spielfeld, x, y, newStein);

        }

        @Override
        public void spielfeldChanged(Spiel spiel, Spielfeld newSpielfeld) {
            fireSpielfeldChanged(spiel, newSpielfeld);
        }

        @Override
        public void spielmodusChanged(Spiel spiel,
                SpielmodusEnumeration newSpielmodus) {
            fireSpielmodusChanged(spiel, newSpielmodus);
        }

    }

    public void setFadenkreuzAktiviert(boolean newValue) {
        this.zeigeFadenkreuz = newValue;
    }

    public boolean istFadenkreuzAktiviert() {
        return this.zeigeFadenkreuz;
    }

}
