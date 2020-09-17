package severeLobster.backend.spiel;

/**
 * Wenn bisher noch kein anderer Status fuer das Feld gesetzt wurde oder hier
 * einfach nichts ist. Hiess vorher BlankState.
 * 
 * @author Lutz Kleiber
 * 
 */
public class KeinStein extends Spielstein {

    private static final KeinStein INSTANCE = new KeinStein();

    /**
     * Liefert immer dieselbe Instanz. Da sich die Instanzen nicht in ihren
     * Zustaenden unterscheiden, kann man immer die selbe Instanz nehmen.
     * 
     * @return KeinStern Instanz
     */
    public static KeinStein getInstance() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return "Leeres Feld";
    }

    @Override
    public boolean equals(final Object obj) {
        return (null != obj && obj instanceof KeinStein);
    }

    @Override
    public Spielstein createNewCopy() {
        /* Da es nur eine Instanz gibt, einfach die Instanz zurueckgeben */
        return KeinStein.getInstance();
    }
}
