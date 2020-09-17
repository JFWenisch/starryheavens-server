package severeLobster.backend.spiel;

import infrastructure.components.Koordinaten;

/**
 * Prueft ob noch kein Spielstein gesetzt wurde
 *
 * @author Christian Lobach
 */
public class EditCheckEmptyField implements EditCheck {
    @Override
    public Koordinaten[] execute(Spielfeld input) {
        Koordinaten[] errors = new Koordinaten[0];
        if (input.countSterne() == 0) {
           errors = new Koordinaten[input.getBreite()*input.getHoehe()];
        }
        return errors;
    }
}
