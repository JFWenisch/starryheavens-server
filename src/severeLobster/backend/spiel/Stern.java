package severeLobster.backend.spiel;

import infrastructure.ResourceManager;

/**
 * Sternenspielstein
 * 
 * @author Lars Schlegelmilch, Lutz Kleiber
 */
public class Stern extends Spielstein {
    private final ResourceManager resourceManager = ResourceManager.get();
    private static final Stern INSTANCE = new Stern();

    public static Stern getInstance() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return resourceManager.getText("backend.stern");
    }

    @Override
    public boolean equals(final Object obj) {
        return (null != obj && obj instanceof Stern);
    }

    @Override
    public Spielstein createNewCopy() {
        /* Da es nur eine Instanz gibt, einfach die Instanz zurueckgeben */
        return Stern.getInstance();
    }
}
