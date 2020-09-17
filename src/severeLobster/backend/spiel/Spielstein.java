package severeLobster.backend.spiel;

/**
 * Abstrakter Spielstein.
 * 
 * @author Lars Schlegelmilch, Lutz Kleiber
 */
public abstract class Spielstein {

    /**
     * Abgeleitete Klassen sollen eigene toString Methode implementieren.
     */
    @Override
    public abstract String toString();

    /**
     * Abgeleitete Klassen sollen eigene equals Methode implementieren.
     */
    @Override
    public abstract boolean equals(Object obj);

    /**
     * Gibt eine Kopie dieses Spielsteins zurueck.
     * 
     * @return Eine Kopie des Spielstein, niemals null.
     */
    public abstract Spielstein createNewCopy();
}
