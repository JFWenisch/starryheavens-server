package infrastructure.constants;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Globale Konstanten, die für Backend und Frontend gueltig sind
 * 
 * @author Lars Schlegelmilch
 */
public abstract class GlobaleKonstanten {


    public static final Font FONT = new Font("Arial", java.awt.Font.PLAIN, 22);

    public static final String USERNAME = System.getProperty("user.name");
    public static final String VERSION ="0.9a";
    public static final String SPIELNAME = "Sternenhimmel DELUXE";
    public static final String SPIELSTAND_DATEITYP = "sav";
    public static final String PUZZLE_DATEITYP = "puz";
    public static final File DEFAULT_DOC_SAVE_DIR = getDefaultDocsSaveDir();
    public static final File DEFAULT_CONF_SAVE_DIR = getDefaultConfigsSaveDir();
    public static final Dimension MINIMUM_APP_SIZE = new Dimension(850, 650);
    public static final File DEFAULT_SPIEL_SAVE_DIR = getDefaultSpielSaveDir();
    public static final File DEFAULT_PUZZLE_SAVE_DIR = getDefaultPuzzleSaveDir();
    public static  File USER_PROPERTIES = new File(DEFAULT_CONF_SAVE_DIR, "user.properties");
   public static final File DEFAULT_FREIGEGEBENE_PUZZLE_SAVE_DIR = getDefaultFreigegebenePuzzleSaveDir();
   public static final String DEFAULT_SOUND_DIR = getDefaultSoundDir();

    /**
     * Gibt das Standardverzeichnis fuer gespeicherte Spiele zurueck - wenn es
     * nicht existiert, wird es angelegt
     * 
     * @return Standardverzeichnis fuer gespeicherte Spiele
     */
    private static File getDefaultSpielSaveDir() {
        File spielverzeichnis = new File("./bin/save");
        boolean success = true;
        if (!spielverzeichnis.exists()) {
            success = spielverzeichnis.mkdir();
        }
        if (!success) {
            return null;
        } else {
            return spielverzeichnis;
        }
    }

    /**
     * Gibt das Standardverzeichnis fuer Docs zurueck -
     * wenn es nicht existiert, wird es angelegt
     *
     * @return Standardverzeichnis fuer Docs
     */
    private static File getDefaultDocsSaveDir() {
        File docverzeichnis = new File("./bin/doc");
        boolean success = true;
        if (!docverzeichnis.exists()) {
            success = docverzeichnis.mkdir();
        }
        if (!success) {
            return null;
        } else {
            return docverzeichnis;
        }
    }

    /**
     * Gibt das Standardverzeichnis fuer Cinfigs zurueck -
     * wenn es nicht existiert, wird es angelegt
     *
     * @return Standardverzeichnis fuer Configs
     */
    private static File getDefaultConfigsSaveDir() {
        File docverzeichnis = new File("./conf");
        boolean success = true;
        if (!docverzeichnis.exists()) {
            success = docverzeichnis.mkdir();
        }
        if (!success) {
            return null;
        } else {
            return docverzeichnis;
        }
    }

    /**
     * Gibt das Standardverzeichnis fuer gespeicherte erstellte Puzzle zurueck -
     * wenn es nicht existiert, wird es angelegt
     * 
     * @return Standardverzeichnis fuer gespeicherte erstelle Puzzle
     */
    private static File getDefaultPuzzleSaveDir() {
        File spielverzeichnis = new File("./bin/edit");
        boolean success = true;
        if (!spielverzeichnis.exists()) {
            success = spielverzeichnis.mkdir();
        }
        if (!success) {
            return null;
        } else {
            return spielverzeichnis;
        }
    }

    /**
     * Gibt das Standardverzeichnis fuer gespeicherte freigegebene Puzzle zurueck -
     * wenn es nicht existiert, wird es angelegt
     *
     * @return Standardverzeichnis fuer gespeicherte freigegebene Puzzle
     */
    private static File getDefaultFreigegebenePuzzleSaveDir() {
        File spielverzeichnis = new File("./bin/puzzles");
        boolean success = true;
        if (!spielverzeichnis.exists()) 
        {
        	try {
				System.out.println("Spieleverzeichnis existiert nicht "+spielverzeichnis.getCanonicalPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            success = spielverzeichnis.mkdir();
            
        }
        if (!success) 
        {
        	try {
				System.out.println("Spieleverzeichnis nicht erstellt"+spielverzeichnis.getCanonicalPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return null;
        } else {
            return spielverzeichnis;
        }
    }
    private static String getDefaultSoundDir() {
            return "./bin/sound";
    }
}
