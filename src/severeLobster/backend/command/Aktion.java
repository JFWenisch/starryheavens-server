package severeLobster.backend.command;

import severeLobster.backend.spiel.Spielstein;

/**
 * Aktionsinterface
 * 
 * @author Lars Schlegelmilch
 */
public interface Aktion {

    public boolean execute(int x, int y, Spielstein spielstein);

    public void redo();
    public void reundo();
    public boolean undo();
}
