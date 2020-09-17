package severeLobster.frontend.view;

import infrastructure.constants.enums.PfeilrichtungEnumeration;
import severeLobster.backend.spiel.Ausschluss;
import severeLobster.backend.spiel.Pfeil;
import severeLobster.backend.spiel.Spielstein;
import severeLobster.backend.spiel.Stern;

import javax.swing.Icon;

/**
 * Abstract Factory zur Anforderung Icons.
 * 
 * @author LKleiber
 * 
 */
public abstract class IconFactory {

    protected IconFactory() {
    }

    public abstract Icon pfeilIcon(PfeilrichtungEnumeration richtung);

    public abstract Icon disabledPfeilIcon(PfeilrichtungEnumeration richtung);

    public abstract Icon sternIcon();

    public abstract Icon ausschlussIcon();

    public abstract Icon blankIcon();

    public final Icon getIconForSpielstein(final Spielstein state) {
        if (null == state) {
            return blankIcon();
        }
        if (state instanceof Stern) {
            return sternIcon();
        }
        if (state instanceof Ausschluss) {
            return ausschlussIcon();
        }
        if (state instanceof Pfeil) {

            return pfeilIcon(((Pfeil) state).getPfeilrichtung());

        }
        return blankIcon();
    }

    public final Icon getDisabledIconForPfeil(final Pfeil pfeil) {

            return disabledPfeilIcon(( pfeil).getPfeilrichtung());
    }
}
