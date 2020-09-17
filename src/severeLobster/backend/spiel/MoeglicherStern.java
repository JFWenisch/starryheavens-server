package severeLobster.backend.spiel;
import infrastructure.ResourceManager;

/**
 * Ein Spielstein der angibt dass an dieser Stelle ein Stern möglich ist.
 *
 * @author Christian Lobach
 */
public class MoeglicherStern extends Spielstein {

    private final ResourceManager resourceManager = ResourceManager.get();
    private static final MoeglicherStern INSTANCE = new MoeglicherStern();

    public static MoeglicherStern getInstance() {
        return INSTANCE;
    }


    public String toString() {
        return resourceManager.getText("backend.moeglicher.stern");
    }


    public boolean equals(final Object obj) {
        return (null != obj && obj instanceof MoeglicherStern);
    }


    @Override
    public Spielstein createNewCopy() {
        /* Da es nur eine Instanz gibt, einfach die Instanz zurueckgeben */
        return MoeglicherStern.getInstance();
    }

}

