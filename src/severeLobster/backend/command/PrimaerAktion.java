package severeLobster.backend.command;

import infrastructure.constants.enums.SpielmodusEnumeration;
import severeLobster.backend.spiel.Pfeil;
import severeLobster.backend.spiel.Spiel;
import severeLobster.backend.spiel.Spielstein;

/**
 * Primaeraktion - Primaere Aktion die vom Spieler aus gesteuert wird. (Z.B.
 * Mauslinksklick)
 * *************************************************************************
 * *************************************************************************
 * gesetzt wird. Um die Richtung zu bestimmen koennte man auch mehrfach angaben
 * verwenden
 *
 * @author Lars Schlegelmilch, Paul Bruell
 */
public class PrimaerAktion implements Aktion {

    private Spiel spiel_;
    private int x_;
    private int y_;
    private Spielstein neuerSpielstein_;
    private Spielstein alterSpielstein_;

    private PrimaerAktion() {
    }

    public PrimaerAktion(Spiel spiel) {
        this.spiel_ = spiel;
    }

    /**
     * Fuehrt anhand des aktuellem Spielmodus verschieden Methoden aus: Befinden
     * wir uns im Modus "SPIELEN", so wird ein Spielstein auf einem Feld
     * getippt, im Modus "EDITIEREN" wird ein Spielstein platziert.
     *
     * @param x X-Achsenwert auf dem der Spielstein gesetzt/getippt wird
     * @param y Y-Achsenwert auf dem der Spielstein gesetzt/getippt wird
     */
    @Override
    public boolean execute(int x, int y, Spielstein spielstein) {
        x_ = x;
        y_ = y;
        neuerSpielstein_ = spielstein;
        alterSpielstein_ = spiel_.getSpielstein(x, y);
        if (spiel_.getSpielmodus().equals(SpielmodusEnumeration.SPIELEN) &&
                !(alterSpielstein_ instanceof Pfeil)) {
            spiel_.addSpielZug();
        }

        return spiel_.setSpielstein(x_, y_, neuerSpielstein_);
    }

    @Override
    public void redo() {
        spiel_.setSpielstein(x_, y_, neuerSpielstein_);
    }

    @Override
    public void reundo() {
        spiel_.setSpielstein(x_, y_, alterSpielstein_);
    }

    @Override
    public boolean undo() {
        if (spiel_.getSpielmodus().equals(SpielmodusEnumeration.SPIELEN) &&
                !(alterSpielstein_ instanceof Pfeil)) {
            spiel_.addSpielZug();
        }
        return spiel_.setSpielstein(x_, y_, alterSpielstein_);
    }
}
