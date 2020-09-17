package severeLobster.backend.spiel;

import infrastructure.ResourceManager;
import infrastructure.components.Koordinaten;
import infrastructure.constants.enums.PfeilrichtungEnumeration;

import java.util.ArrayList;
import java.util.List;

/**
 * Pfeilspielstein - Zeigt immer auf mindestens einen Pfeil
 * 
 * @author Lars Schlegelmilch, Lutz Kleiber
 */
public class Pfeil extends Spielstein {
    private final ResourceManager resourceManager = ResourceManager.get();

    private static final Pfeil NORD_PFEIL = new Pfeil(
            PfeilrichtungEnumeration.NORD);
    private static final Pfeil NORD_OST_PFEIL = new Pfeil(
            PfeilrichtungEnumeration.NORDOST);
    private static final Pfeil OST_PFEIL = new Pfeil(
            PfeilrichtungEnumeration.OST);
    private static final Pfeil SUED_OST_PFEIL = new Pfeil(
            PfeilrichtungEnumeration.SUEDOST);
    private static final Pfeil SUED_PFEIL = new Pfeil(
            PfeilrichtungEnumeration.SUED);
    private static final Pfeil SUED_WEST_PFEIL = new Pfeil(
            PfeilrichtungEnumeration.SUEDWEST);
    private static final Pfeil WEST_PFEIL = new Pfeil(
            PfeilrichtungEnumeration.WEST);
    private static final Pfeil NORD_WEST_PFEIL = new Pfeil(
            PfeilrichtungEnumeration.NORDWEST);

    private final PfeilrichtungEnumeration pfeilrichtung;

    /**
     * Default-Konstruktor nur fuer Serialisierung vorhanden
     */
    @Deprecated
    private Pfeil() {
        pfeilrichtung = PfeilrichtungEnumeration.NORD;
    }

    /**
     * Statt des Konstruktors lieber die statischen Factory Methoden nehmen, da
     * spart man sich das setzen von pfeilrichtung.
     * 
     * @param pfeilrichtung
     *            Richtung des Pfeiles
     */
    public Pfeil(final PfeilrichtungEnumeration pfeilrichtung) {
        this.pfeilrichtung = pfeilrichtung;
    }

    public PfeilrichtungEnumeration getPfeilrichtung() {
        return pfeilrichtung;
    }

    public static Pfeil getNordPfeil() {
        return NORD_PFEIL;
    }

    public static Pfeil getNordOstPfeil() {
        return NORD_OST_PFEIL;
    }

    public static Pfeil getOstPfeil() {
        return OST_PFEIL;
    }

    public static Pfeil getSuedOstPfeil() {
        return SUED_OST_PFEIL;
    }

    public static Pfeil getSuedPfeil() {
        return SUED_PFEIL;
    }

    public static Pfeil getSuedWestPfeil() {
        return SUED_WEST_PFEIL;
    }

    public static Pfeil getWestPfeil() {
        return WEST_PFEIL;
    }

    public static Pfeil getNordWestPfeil() {
        return NORD_WEST_PFEIL;
    }

    public static List<Pfeil> listAlleMoeglichenPfeile() {

        final List<Pfeil> modifiableList = new ArrayList<Pfeil>(8);
        modifiableList.add(NORD_PFEIL);
        modifiableList.add(NORD_OST_PFEIL);
        modifiableList.add(OST_PFEIL);
        modifiableList.add(SUED_OST_PFEIL);
        modifiableList.add(SUED_PFEIL);
        modifiableList.add(SUED_WEST_PFEIL);
        modifiableList.add(WEST_PFEIL);
        modifiableList.add(NORD_WEST_PFEIL);

        return modifiableList;
    }

    @Override
    public String toString() {
        return resourceManager.getText("backend.pfeil")
                + getPfeilrichtung().toString();
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (null != obj && obj instanceof Pfeil) {
            /**
             * Vergleiche die fremde und die eigene Pfeilrichtung.
             */
            if (((Pfeil) obj).pfeilrichtung.equals(this.pfeilrichtung)) {
                result = true;
            }
        }
        return result;
    }

    public Koordinaten getRichtungsKoordinaten() {

        final Koordinaten result;
        switch (this.pfeilrichtung) {
        case NORD:
            result = new Koordinaten(0, -1);
            break;
        case NORDOST:
            result = new Koordinaten(1, -1);
            break;
        case OST:
            result = new Koordinaten(1, 0);
            break;
        case SUEDOST:
            result = new Koordinaten(1, 1);
            break;
        case SUED:
            result = new Koordinaten(0, 1);
            break;
        case SUEDWEST:
            result = new Koordinaten(-1, 1);
            break;
        case WEST:
            result = new Koordinaten(-1, 0);
            break;
        default: // NORDWEST
            result = new Koordinaten(-1, -1);
            break;
        }
        return result;
    }

    @Override
    public Spielstein createNewCopy() {
        /*
         * Pfeile haben konstante Attribute und koennten auch als Singletons
         * angeboten werden, daher einfach nur die aktuelle Instanz auf dem die
         * Methode aufgerufen wird zurueckgeben
         */
        return this;
    }

}
