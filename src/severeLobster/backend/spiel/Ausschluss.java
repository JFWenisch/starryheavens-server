package severeLobster.backend.spiel;

/**
 * Ausschlussspielstein - Kennzeichnet ein Feld, auf dem sich kein Stern
 * befindet
 * 
 * @author Lars Schlegelmilch, Lutz Kleiber
 */
public class Ausschluss extends Spielstein {

    private static final Ausschluss INSTANCE = new Ausschluss();

    /**
     * Liefert immer dieselbe Instanz. Da sich die Instanzen nicht in ihren
     * Zustaenden unterscheiden, kann man immer die selbe Instanz nehmen.
     * 
     * @return Ausschluss Instanz
     */
    public static Ausschluss getInstance() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return "Ausschluss";
    }

    @Override
    public boolean equals(Object obj) {
        return (null != obj && obj instanceof Ausschluss);
    }

    @Override
    public Spielstein createNewCopy() {
        /* Da es nur eine Instanz gibt, einfach die Instanz zurueckgeben */
        return Ausschluss.getInstance();
    }

}
