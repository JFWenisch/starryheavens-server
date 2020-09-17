package severeLobster.backend.spiel;

/**
 *  Die Klasse stellt einen einzelnen Schritt zur Lösung eines Spielfeldes dar.
 *
 *  @author Christian Lobach
 */
public interface SolvingStep {
    public Spielfeld execute(Spielfeld input);


}
