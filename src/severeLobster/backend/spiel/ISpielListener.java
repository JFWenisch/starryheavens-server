package severeLobster.backend.spiel;

import java.util.EventListener;

import infrastructure.constants.enums.SpielmodusEnumeration;

/**
 * 
 * @author Lutz Kleiber
 * 
 */
public interface ISpielListener extends EventListener {

    /***
     * Wird aufgerufen, wenn bei einer einzelnen Koordinate im Spielfeld ein
     * Stein im aktuellen Spielmodus veraendert wurde.
     * 
     * @param spiel
     * @param spielfeld
     * @param x
     * @param y
     * @param changedStein
     */
    void spielsteinChanged(Spiel spiel, Spielfeld spielfeld, int x, int y,
            Spielstein changedStein);

    /**
     * Wird aufgerufen, wenn das Spielfeld gewechselt oder neu erstellt wurde.
     * 
     * @return
     */
    void spielfeldChanged(Spiel spiel, Spielfeld newSpielfeld);

    void spielmodusChanged(Spiel spiel, SpielmodusEnumeration newSpielmodus);
}
