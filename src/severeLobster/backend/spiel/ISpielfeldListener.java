package severeLobster.backend.spiel;

import infrastructure.constants.enums.SpielmodusEnumeration;

import java.io.Serializable;
import java.util.EventListener;

/**
 * 
 * @author Lutz Kleiber
 * 
 */
public interface ISpielfeldListener extends Serializable, EventListener {

    void spielsteinChanged(Spielfeld spielfeld, int x, int y,
            Spielstein changedStein);

    void spielmodusChanged(Spielfeld spielfeld,
            SpielmodusEnumeration neuerSpielmodus);
}
