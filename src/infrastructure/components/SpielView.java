package infrastructure.components;

import infrastructure.constants.GlobaleKonstanten;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;

/**
 * Manipulierte View um nicht Verzeichnis zu wechseln
 * 
 * @author Lars Schlegelmilch
 */
public class SpielView extends FileSystemView {

    @Override
    public File createNewFolder(File containingDir) throws IOException {
        return null;
    }

    @Override
    public Boolean isTraversable(File f) {
        return GlobaleKonstanten.DEFAULT_SPIEL_SAVE_DIR.equals(f);
    }

    @Override
    public File[] getRoots() {
        return new File[] { GlobaleKonstanten.DEFAULT_SPIEL_SAVE_DIR };
    }

    @Override
    public File getHomeDirectory() {
        return GlobaleKonstanten.DEFAULT_SPIEL_SAVE_DIR;
    }
}
