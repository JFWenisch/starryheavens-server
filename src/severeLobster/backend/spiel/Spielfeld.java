package severeLobster.backend.spiel;

import infrastructure.constants.enums.SchwierigkeitsgradEnumeration;
import infrastructure.constants.enums.SpielmodusEnumeration;
import infrastructure.exceptions.LoesungswegNichtEindeutigException;
import infrastructure.ResourceManager;

import javax.swing.event.EventListenerList;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Spielfeld eines Spiels - verhaelt sich nach aussen wie ein zweidimensionales
 * Feld. Besteht in wirklichkeit aus je einer zweidimensionalen Schicht pro
 * Spielmodus (also 2). Nach aussen ist jeweils nur die Schicht fuer den
 * jeweiligen Spielmodus sichtbar. Nach der Erstellung ist die Groesse des
 * Spielfeldes konstant. Klasse ist nicht Thread-safe.
 *
 * @author Lars Schlegelmilch, Lutz Kleiber, Christian Lobach, Paul Bruell
 */
public class Spielfeld implements Serializable, ISpielfeldReadOnly {

    private final ResourceManager resourceManager = ResourceManager.get();
    /**
     * Liste mit den fuer die Spielfeldinstanz angemeldeten
     * SpielfeldListenern(Observer).
     */
    private final EventListenerList listeners = new EventListenerList();
    /**
     * Wirklich gesetzte bzw im Editiermodus sichtbare Steine:
     */
    private final Spielstein[][] realSteine;
    /**
     * Geratene bzw. im Spielmodus sichtbare Steine:
     */
    private final Spielstein[][] visibleSteine;

    /*
     * Ist protected, damit man beim Kopie Konstruktor die Modi aendern kann,
     * ohne ueber die Methode setSpielmodus() gehen zu muessen, bei der die
     * Loesbarkeit ueberprueft wird.
     */
    protected SpielmodusEnumeration spielmodus = SpielmodusEnumeration.EDITIEREN;

    /**
     * Erstellt ein neues, leeres Spielfeld der angegebenen Groesse. Alle
     * Feldelemente sind mit KeinStein Instanzen initialisiert. Nach dem
     * erstellen ist man im Spielmodus EDITIEREN.
     *
     * @param breite Breite des Spielfeldes
     * @param hoehe  Hoehe des Spielfeldes
     */
    public Spielfeld(final int breite, final int hoehe) {

        if (breite < 1 || hoehe < 1) {
            throw new IllegalArgumentException(
                    resourceManager.getText("backend.spiel.not.allowed.size"));
        }
        this.realSteine = new Spielstein[breite][hoehe];
        this.visibleSteine = new Spielstein[breite][hoehe];

        /* Beide Feldansichten mit KeinStein Spielsteinen fuellen */
        for (int hoeheIndex = 0; hoeheIndex < hoehe; hoeheIndex++) {

            for (int breiteIndex = 0; breiteIndex < breite; breiteIndex++) {

                realSteine[breiteIndex][hoeheIndex] = KeinStein.getInstance();
                visibleSteine[breiteIndex][hoeheIndex] = KeinStein
                        .getInstance();
            }
        }
    }

    /**
     * Kopie Konstruktor. Erstellt ein neues Spielfeld, das den gleichen Zustand
     * hat, wie das uebergebene Spielfeld. Kopiert bisher nur den Zustand von
     * Spiel- und Editiermodus. - nicht von Loesen.
     *
     * @param quellSpielfeld
     */
    public Spielfeld(final Spielfeld quellSpielfeld) {
        this(quellSpielfeld, quellSpielfeld.getBreite(), quellSpielfeld
                .getHoehe());
    }

    /**
     * Kopie Konstruktor. Erstellt ein neues Spielfeld der angegebenen Groesse
     * und kopiert den Zustand des uebergebenen Spielfeldes, soweit das bei den
     * Groessenunterschieden moeglich ist . Kopiert nur die Zustaende von Spiel-
     * und Editiermodus. - nicht von Loesen.
     *
     * @param quellSpielfeld
     */
    public Spielfeld(final Spielfeld quellSpielfeld, final int breite,
                     final int hoehe) {

        /*
         * Erstelle ein leeres Spielfeld der angegebenen Groesse. Der Spielmodus
         * des neuen Spielfeldes wird mit dem aktuellen Spielmodus des alten
         * Spielfeldes initialisiert.
         */
        this(breite, hoehe);

        final SpielmodusEnumeration quellSpielfeldAnfangsSpielmodus = quellSpielfeld
                .getSpielmodus();

        /*
         * Uebertrage den Inhalt des Quellspielfeldes in das neu erstellte
         * Spielfeld, soweit das bei den Groessenunterschieden moeglich ist.
         * Durchlaufe das neue Spielfeld und hole den Spielstein mit den jeweils
         * gleichen Koordinaten aus dem Quellspielfeld.
         */
        /* Modus SPIELEN: */
        {
            this.spielmodus = SpielmodusEnumeration.SPIELEN;
            quellSpielfeld.spielmodus = SpielmodusEnumeration.SPIELEN;

            Spielfeld.kopiereSichtbarenZustand(quellSpielfeld, this);
        }
        /* Modus EDITIEREN: */
        {
            this.spielmodus = SpielmodusEnumeration.EDITIEREN;
            quellSpielfeld.spielmodus = SpielmodusEnumeration.EDITIEREN;

            Spielfeld.kopiereSichtbarenZustand(quellSpielfeld, this);
        }
        /* LOESEN */
        {
            this.spielmodus = SpielmodusEnumeration.LOESEN;
            quellSpielfeld.spielmodus = SpielmodusEnumeration.LOESEN;

            Spielfeld.kopiereSichtbarenZustand(quellSpielfeld, this);
        }
        /* Spielmodi zurueck auf Anfang */
        this.spielmodus = quellSpielfeldAnfangsSpielmodus;
        quellSpielfeld.spielmodus = quellSpielfeldAnfangsSpielmodus;

    }

    protected int countPfeileZeile(int y) {
        int result = 0;
        for (int i = 0; i < getBreite(); i++) {
            if (realSteine[i][y] instanceof Pfeil)
                result++;
        }
        return result;
    }

    protected int countPfeileSpalte(int x) {
        int result = 0;
        for (int i = 0; i < getHoehe(); i++) {
            if (realSteine[x][i] instanceof Pfeil)
                result++;
        }
        return result;
    }

    public int countSterneZeile(int y) {
        int result = 0;
        for (int i = 0; i < getBreite(); i++) {
            if (realSteine[i][y] instanceof Stern)
                result++;
        }
        return result;
    }

    public int countSterneSpalte(int x) {
        int result = 0;
        for (int i = 0; i < getHoehe(); i++) {
            if (realSteine[x][i] instanceof Stern)
                result++;
        }
        return result;
    }

    /**
     * Zaehlt die Pfeile auf dem Spielfeld.
     *
     * @return result Die Anzahl der Pfeile auf dem Spielfeld.
     */
    private int countPfeile() {
        int result = 0;
        for (Spielstein[] zeile : realSteine) {
            for (Spielstein stein : zeile) {
                if (stein instanceof Pfeil)
                    result++;
            }
        }
        return result;
    }

    /**
     * Zaehlt die Sterne auf dem Spielfeld.
     *
     * @return result Die Anzahl der Sterne auf dem Spielfeld.
     */
    public int countSterne() {
        int result = 0;
        for (Spielstein[] zeile : realSteine) {
            for (Spielstein stein : zeile) {
                if (stein instanceof Stern)
                    result++;
            }
        }
        return result;
    }

    /**
     * Zaehlt die getippten Sterne auf dem Spielfeld.
     *
     * @return result Die Anzahl der getippten Sterne auf dem Spielfeld.
     */
    public int countSterneGetippt() {
        int result = 0;
        for (Spielstein[] zeile : visibleSteine) {
            for (Spielstein stein : zeile) {
                if (stein instanceof Stern)
                    result++;
            }
        }
        return result;
    }

    /**
     * Zaehlt die Moeglichen Sterne auf dem Spielfeld.
     *
     * @return result Die Anzahl der Moeglichen Sterne auf dem Spielfeld.
     */
    public int countMoeglicheSterne() {
        int result = 0;
        for (Spielstein[] zeile : visibleSteine) {
            for (Spielstein stein : zeile) {
                if (stein instanceof MoeglicherStern)
                    result++;
            }
        }
        return result;
    }


    /**
     * Schaetzt anhand der Groesse des Spielfeldes und den Verhaeltnisen
     * zwischen Pfeilen und Sternen sowie zwischen belegten und unbelegten
     * Spielfeldern einen Schwierigkeitsgrad
     *
     * @return Schwierigkeitsgrad des Spielfeldes
     */
    public SchwierigkeitsgradEnumeration getSchwierigkeitsgrad() {
        try {
            float sterne = countSterne();
            float pfeile = countPfeile();
            double leicht = 80.0;
            double mittel = 10.0;
            float spielfeldFlaeche = getBreite() * getHoehe();
            
            double schwierigkeit = Math.pow(pfeile, 3);
            schwierigkeit += pfeile * sterne;
            schwierigkeit /= sterne * Math.pow(spielfeldFlaeche, 2);
            schwierigkeit *= 100;
            
            
            if (spielfeldFlaeche <= 9)
            {
                leicht /= 16;
                mittel /= 16;
            }
            else
            {
                leicht /= 8;
                mittel /= 8;
            }
            
            
            if (schwierigkeit > leicht)
                return SchwierigkeitsgradEnumeration.LEICHT;
            else if (schwierigkeit > mittel)
                return SchwierigkeitsgradEnumeration.MITTEL;
            else
                return SchwierigkeitsgradEnumeration.SCHWER;
        } catch (ArithmeticException e) {
            return SchwierigkeitsgradEnumeration.LEICHT;
        }

    }

    public int getBreite() {
        return realSteine.length;
    }

    public int getHoehe() {
        return realSteine[0].length;
    }

    public SpielmodusEnumeration getSpielmodus() {
        return this.spielmodus;
    }

    /**
     * Liefert den Spielstein an der angegebenen Position im Spielfeld.
     * Verhalten unterscheidet sich bei den unterschiedlichen Spielmodi. Beim
     * Modus Spielen wird der sichtbare Stein zurueckgegeben. Bei den Modi
     * Editieren und Loesen wird der reale Stein zurueckgegeben.
     *
     * @param x X-Achsen Koordinatenwert
     * @param y Y-Achsen Koordinatenwert
     */
    public Spielstein getSpielstein(final int x, final int y)
            throws IndexOutOfBoundsException {

        throwExceptionIfIndexOutOfBounds(x, y);
        if (isEditierModus()) {
            return realSteine[x][y];
        } else {
            return visibleSteine[x][y];
        }
    }

    /**
     * Setzt den Spielmodus auf den uebergebenen Wert, wenn dies moeglich ist.
     * Wenn der neue Spielmodus Spielen ist, wird das Spielfeld auf loebarkeit
     * ueberprueft. Sollte der Loesungsweg nicht eindeutig sein, wird eine
     * LoesungswegNichtEindeutigException geworfen und der Spielmodus wird nicht
     * veraendert.
     *
     * @param neuerSpielmodus
     * @throws LoesungswegNichtEindeutigException
     *
     */
    public void setSpielmodus(final SpielmodusEnumeration neuerSpielmodus)
            throws LoesungswegNichtEindeutigException {
        if (null == neuerSpielmodus) {
            /* Braucht nicht uebersetzt zu werden, da nur fuer Testphase */
            throw new NullPointerException("Spielmodus ist null");
        }
        /*
         * Bei Umstellen auf Spielmodus muss der Loesungsweg eindeutig sein. Die
         * Logik hier ersetzt das vorige freigeben in Spiel.
         */
        /* if (neuerSpielmodus.equals(SpielmodusEnumeration.SPIELEN)
                && !(loesungswegUeberpruefen().isSolvable() && loesungswegUeberpruefen().isUnique())) {
            throw new LoesungswegNichtEindeutigException(
                    "Spielmodus kann nicht auf Spielen geaendert werden, da das Spielfeld nicht eindeutig loesbar ist.");
        } */
        this.spielmodus = neuerSpielmodus;
        fireSpielmodusChanged(neuerSpielmodus);
    }

    /**
     * Setzt einen Spielstein an eine bestimmte Koordinate. Verhalten
     * unterscheidet sich bei den unterschiedlichen Spielmodi.
     * ......................................................................
     * Beim Modus Spielen wird der sichtbare Stein gesetzt - der reale Stein
     * wird davon nicht beeinflusst.
     * ......................................................................
     * Beim Modus Editieren wird der reale Stein gesetzt. Darueber hinaus wird
     * der sichtbare Stein gesetzt, wenn es sich beim neu gesetzten Stein um
     * einen Pfeil handelt (weil diese ja in beiden Modi dargestellt werden
     * muessen).
     * ......................................................................
     * Spielfelder lassen sich zu jedem Zeitpunkt und in jedem Zustand komplett
     * speichern und wieder laden. Bereits angefangene Spielfelder sind immer
     * noch editierbar. Wenn newStein null ist, wird KeinStein als Stein
     * gesetzt.
     *
     * @param x        X-Achsen Koordinatenwert
     * @param y        Y-Achsen Koordinatenwert
     * @param newStein Spielstein der gesetzt werden soll
     */
    public void setSpielstein(final int x, final int y, Spielstein newStein)
            throws IndexOutOfBoundsException {

        throwExceptionIfIndexOutOfBounds(x, y);
        if (null == newStein) {
            newStein = KeinStein.getInstance();
        }
        if (isLoesenModus()) {
            /**
             * Im Loesen-Modus dürfen Stern, MoeglicherStern, Ausschluss und
             * KeinStein gesetzt werden Observer müssen nicht benachrichtigt
             * werden
             */
            if (newStein instanceof Stern
                    || newStein instanceof MoeglicherStern
                    || newStein instanceof Ausschluss
                    || newStein instanceof KeinStein) {
                visibleSteine[x][y] = newStein;
            }

        } else if (isEditierModus()) {
            /**
             * Im Editiermodus duerfen Pfeil, Stern und KeinStein gesetzt werden
             */
            if (newStein instanceof Pfeil || newStein instanceof Stern
                    || newStein instanceof KeinStein) {

                /**
                 * Visible in jedem Fall auf leer setzen, da hier vorher evtl
                 * ein Pfeil von realSteine uebertragen wurde.
                 */
                visibleSteine[x][y] = KeinStein.getInstance();

                /** Neuen Stein in der realen Schicht setzen */
                realSteine[x][y] = newStein;
                if (newStein instanceof Pfeil) {
                    /**
                     * Wenn neuer Stein ein Pfeil ist, dann auch in der
                     * sichtbaren Schicht setzen, da Pfeile in jedem Modus
                     * gleichermassen sichtbar sind.
                     */
                    visibleSteine[x][y] = newStein;
                }
            }
            /**
             * Listener/Observer ueber Aenderungen im Spielfeld benachrichtigen.
             * Dabei werden die Koordinaten der Aenderung und der neue
             * Spielstein an der Stelle mitgegeben.
             */
            fireSpielsteinChanged(x, y, newStein);
        } else {

            /**
             * Ausser Pfeilen darf im Spielmodus alles gesetzt werden. Es
             * duerfen aber auch keine Pfeile ueberschrieben werden.
             */
            if (!(getSpielstein(x, y) instanceof Pfeil)
                    && !(newStein instanceof Pfeil)) {
                visibleSteine[x][y] = newStein;

                /**
                 * Listener/Observer ueber Aenderungen im Spielfeld
                 * benachrichtigen. Dabei werden die Koordinaten der Aenderung
                 * und der neue Spielstein an der Stelle mitgegeben.
                 */
                fireSpielsteinChanged(x, y, newStein);
            }
        }
    }

    /**
     * Benachrichtigt alle Listener dieses Spielsfelds ueber den neuen
     * Spielstein an der angegebenen Koordinate. Implementation ist glaube ich
     * aus JComponent oder Component kopiert.
     *
     * @param newStein - Der neue Stein, der an die Listener mitgeteilt wird.
     */
    private void fireSpielsteinChanged(final int x, final int y,
                                       Spielstein newStein) {

        /** Gibt ein Array zurueck, das nicht null ist */
        final Object[] currentListeners = listeners.getListenerList();
        /**
         * Rufe die Listener auf, die als ISpielfeldListener angemeldet sind.
         */
        for (int i = currentListeners.length - 2; i >= 0; i -= 2) {
            if (currentListeners[i] == ISpielfeldListener.class) {
                ((ISpielfeldListener) currentListeners[i + 1])
                        .spielsteinChanged(this, x, y, newStein);
            }
        }
    }

    /**
     * Benachrichtigt alle Listener dieses Spielsfelds ueber den neuen
     * Spielmodus. Implementation ist glaube ich aus JComponent oder Component
     * kopiert.
     *
     * @param neuerSpielmodus - Der neue Spielmodus, der an die Listener mitgeteilt wird.
     */
    private void fireSpielmodusChanged(
            final SpielmodusEnumeration neuerSpielmodus) {

        /** Gibt ein Array zurueck, das nicht null ist */
        final Object[] currentListeners = listeners.getListenerList();
        /**
         * Rufe die Listener auf, die als ISpielfeldListener angemeldet sind.
         */
        for (int i = currentListeners.length - 2; i >= 0; i -= 2) {
            if (currentListeners[i] == ISpielfeldListener.class) {
                ((ISpielfeldListener) currentListeners[i + 1])
                        .spielmodusChanged(this, neuerSpielmodus);
            }
        }
    }

    /**
     * Fuegt den angegebenen Listener zu der Liste hinzu.
     *
     * @param listener ISpielfeldListener
     */
    public void addSpielfeldListener(final ISpielfeldListener listener) {
        listeners.add(ISpielfeldListener.class, listener);
    }

    /**
     * Entfernt den uebergebenen Listener von der Liste.
     *
     * @param listener ISpielsteinListener
     */
    public void removeSpielfeldListener(final ISpielfeldListener listener) {
        listeners.remove(ISpielfeldListener.class, listener);
    }

    /**
     * Wirft eine IndexOutOfBoundException mit eigener Nachricht, wenn
     * Koordinaten ausserhalb des Spielfeldes liegen
     */
    private void throwExceptionIfIndexOutOfBounds(final int x, final int y) {
        if ((x < 0) || (x > getBreite() - 1) || (y < 0) || (y > getHoehe() - 1)) {
            throw new ArrayIndexOutOfBoundsException(
                    resourceManager.getText("backend.spiel.given.coordinate")
                            + " X:"
                            + x
                            + " Y:"
                            + y
                            + resourceManager
                            .getText("backend.spiel.outside.playing.field"));
        }
    }

    /**
     * Gibt eine Liste mit den fuer diese Koordinate aktuell setzbaren
     * Spielsteinen zurueck. Die fuer dieses Spielfeldelement aktuell setzbaren
     * Spielsteine haengen vom SpielModus ab.
     *
     * @return Eine Liste mit den fuer diese Koordinate aktuell auswaehlbaren
     *         Spielsteinen.
     */
    public List<? extends Spielstein> listAvailableStates(final int x,
                                                          final int y) {

        if (isEditierModus()) {
            /**
             * Im Editiermodus kann KeinStein, Stern und die verschiedenen
             * Pfeile gesetzt werden.
             */
            final List<Spielstein> editierModusList = new ArrayList<Spielstein>(
                    11);
            editierModusList.add(new Stern());
            editierModusList.addAll(Pfeil.listAlleMoeglichenPfeile());
            editierModusList.add(new KeinStein());
            /* Stein, der bereits gesetzt ist, nicht zur Auswahl stellen */
            editierModusList.remove(getSpielstein(x, y));
            return editierModusList;
        } else {
            /** Spielmodus */
            /**
             * Wenn an der Stelle ein Pfeil ist und man im Spielmodus ist, kann
             * man nichts auswaehlen.
             */
            if (getSpielstein(x, y) instanceof Pfeil) {
                /** Gebe leere Liste zurueck */
                return new ArrayList<Spielstein>();
            } else {
                /**
                 * Ansonsten kann man im Spielmodus KeinStein setzen, Ausschluss
                 * und Stern raten.
                 */
                final List<Spielstein> spielModusList = new ArrayList<Spielstein>(
                        3);
                spielModusList.add(new KeinStein());
                spielModusList.add(new Ausschluss());
                spielModusList.add(new Stern());
                return spielModusList;
            }
        }

    }

    /**
     * Convenience Methode. Holt von IGotSpielstein den aktuell eingestellten
     * Spielmodus. Gibt true zurueck, wenn der Spielmodus EDITIEREN ist.
     *
     * @return True, wenn der Spielmodus EDITIEREN ist.
     */
    private boolean isEditierModus() {
        return getSpielmodus().equals(SpielmodusEnumeration.EDITIEREN);
    }

    /**
     * analog zu isEditierModus() nur für den Modus LOESEN.
     *
     * @return true, wenn der Spielmodus LOESEN ist.
     */
    private boolean isLoesenModus() {
        return getSpielmodus().equals(SpielmodusEnumeration.LOESEN);
    }

    /**
     * Ueberprueft ob das Spielfeld geloest wurde (Sieg).
     *
     * @return sieg
     */
    public boolean isSolved() {

        if (countSterneGetippt() != countSterne()) {
            return false;
        }

        for (int i = 0; i < this.getBreite(); i++) {

            for (int k = 0; k < this.getHoehe(); k++) {
                Spielstein currentVisibleItem = visibleSteine[i][k];
                Spielstein currentRealItem = realSteine[i][k];

                if (currentVisibleItem instanceof Stern
                        && !(currentRealItem instanceof Stern)) {

                    // System.out.println("kein Stern, Stern getippt");
                    return false;
                } else if ((currentVisibleItem instanceof KeinStein || currentVisibleItem instanceof Ausschluss)
                        && !(currentRealItem instanceof KeinStein)) {
                    // System.out.println("nicht NullState, Ausschluss oder nichts getippt");
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Ueberprueft ob Fehler in einem Spielfeld vorhanden sind, d.h. Tipps
     * abgegeben wurden, die nicht der Loesung entsprechen
     *
     * @return Fehler vorhanden?
     */
    public boolean hasErrors() {

        for (int i = 0; i < this.getBreite(); i++) {

            for (int k = 0; k < this.getHoehe(); k++) {
                Spielstein currentVisibleItem = visibleSteine[i][k];
                Spielstein currentRealItem = realSteine[i][k];
                if (currentVisibleItem instanceof Ausschluss
                        && currentRealItem instanceof Stern) {
                    return true;
                } else if (currentVisibleItem instanceof Stern
                        && currentRealItem instanceof KeinStein) {
                    return true;
                }

            }
        }
        return false;
    }

    public SolvingStrategy loesungswegUeberpruefen() {

        SolvingStrategy strategy = new SolvingStrategyStandard();
        strategy.solve(this);

        return strategy;
    }

    /**
     * Liest den aktuell ueber {@link getSpielstein()} sichtbaren Zustand aus
     * quellSpielfeld und schreibt ihn ueber {@link setSpielstein()} in das
     * zielSpielfeld. Da die Sichtbarkeit und das Verhalten der Setzbarkeit vom
     * jeweils eingestellten Spielmodus abhängig sind, muss dieser vor dem
     * Aufruf der Methode auf den gewuenschten Wert gesetzt werden. Die
     * Spielmodi der uebergebenen Spielfelder werden nicht veraendert. Das
     * Quellspielfeld wird nicht veraendert.
     *
     * @param quellSpielfeld
     * @param zielSpielfeld
     */
    public static void kopiereSichtbarenZustand(final Spielfeld quellSpielfeld,
                                                final Spielfeld zielSpielfeld) {
        Spielstein quellSpielstein;
        for (int hoeheIndex = 0; hoeheIndex < zielSpielfeld.getHoehe()
                && hoeheIndex < quellSpielfeld.getHoehe(); hoeheIndex++) {

            for (int breiteIndex = 0; breiteIndex < zielSpielfeld.getBreite()
                    && breiteIndex < quellSpielfeld.getBreite(); breiteIndex++) {

                quellSpielstein = quellSpielfeld.getSpielstein(breiteIndex,
                        hoeheIndex);
                /*
                 * Erstelle Kopie des Spielsteins und setze ihn in das neue
                 * Spielfeld
                 */
                zielSpielfeld.setSpielstein(breiteIndex, hoeheIndex,
                        quellSpielstein.createNewCopy());
            }
        }
    }

    @Override
    public boolean equals(final Object obj) {

        if (null != obj && obj instanceof Spielfeld) {

            final Spielfeld verglSpielfeld = (Spielfeld) obj;

            if (this.getBreite() == verglSpielfeld.getBreite()
                    && this.getHoehe() == verglSpielfeld.getHoehe()) {

                for (int hoeheIndex = 0; hoeheIndex < this.getHoehe(); hoeheIndex++) {

                    for (int breiteIndex = 0; breiteIndex < this.getBreite(); breiteIndex++) {

                        /*
                         * Sobald ein Stein nicht identisch ist, gib false
                         * zurueck und verlasse Schleife vorzeitig
                         */
                        if (!this.getSpielstein(breiteIndex, hoeheIndex)
                                .equals(verglSpielfeld.getSpielstein(
                                        breiteIndex, hoeheIndex))) {
                            return false;
                        }
                    }
                }
                /*
                 * Wenn Schleife nicht vorzeitig verlassen wurde, waren alle
                 * Steine identisch
                 */
                return true;
            }
        }
        return false;
    }
}
