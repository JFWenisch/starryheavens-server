package severeLobster.backend.spiel;

import infrastructure.components.Koordinaten;

/**
 * Ueberprueft ein Spielfeld im Editiermodus darauf ob die Regeln eingehalten wurden
 * @author Christian Lobach
 */
public interface EditCheck {

    public Koordinaten[] execute(Spielfeld input);
}
