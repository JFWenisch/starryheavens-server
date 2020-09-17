package severeLobster.backend.spiel;

import infrastructure.constants.enums.SchwierigkeitsgradEnumeration;

import java.util.List;

/**
 * Nur lesende Sicht auf das Spielfeld. Damit man nicht versehentlich das
 * Tracking umgeht.
 * 
 * @author Lutz Kleiber
 * 
 */
public interface ISpielfeldReadOnly {

    public int countSterneZeile(int y);

    public int countSterneSpalte(int x);

    /**
     * Schaetzt anhand der Groesse des Spielfeldes und den Verhaeltnisen
     * zwischen Pfeilen und Sternen sowie zwischen belegten und unbelegten
     * Spielfeldern einen Schwierigkeitsgrad
     * 
     * @return Schwierigkeitsgrad des Spielfeldes
     */
    public SchwierigkeitsgradEnumeration getSchwierigkeitsgrad();

    public int getBreite();

    public int getHoehe();

    /**
     * Liefert den Spielstein an der angegebenen Position im Spielfeld.
     * Verhalten unterscheidet sich bei den unterschiedlichen Spielmodi. Beim
     * Modus Spielen wird der sichtbare Stein zurueckgegeben. Beim Modus
     * Editieren wird der reale Stein zurueckgegeben.
     * 
     * @param x
     *            X-Achsen Koordinatenwert
     * @param y
     *            Y-Achsen Koordinatenwert
     */
    public Spielstein getSpielstein(final int x, final int y)
            throws IndexOutOfBoundsException;

    /**
     * Fuegt den angegebenen Listener zu der Liste hinzu.
     * 
     * @param listener
     *            ISpielfeldListener
     */
    public void addSpielfeldListener(final ISpielfeldListener listener);

    /**
     * Entfernt den uebergebenen Listener von der Liste.
     * 
     * @param listener
     *            ISpielsteinListener
     */
    public void removeSpielfeldListener(final ISpielfeldListener listener);

    /**
     * Gibt eine Liste mit den fuer diese Koordinate aktuell setzbaren
     * Spielsteinen zurueck. Die fuer dieses Spielfeldelement aktuell setzbaren
     * Spielsteine haengen vom SpielModus ab.
     * 
     * @return Eine Liste mit den fuer diese Koordinate aktuell auswaehlbaren
     *         Spielsteinen.
     */
    public List<? extends Spielstein> listAvailableStates(final int x,
            final int y);

    /**
     * Ueberprueft ob das Spielfeld geloest wurde (Sieg).
     * 
     * @return sieg
     */
    public boolean isSolved();

    /**
     * Ueberprueft ob Fehler in einem Spielfeld vorhanden sind, d.h. Tipps
     * abgegeben wurden, die nicht der Loesung entsprechen
     * 
     * @return Fehler vorhanden?
     */
    public boolean hasErrors();

}