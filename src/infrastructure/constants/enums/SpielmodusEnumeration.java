package infrastructure.constants.enums;

/**
 * Spielmodus Enumeration - Gibt an, in welchem Modus sich der Spieler bzw. das
 * aktuelle Spiel gerade befindet.
 *
 * @author Lars Schlegelmilch, Christian Lobach
 */
public enum SpielmodusEnumeration {

    /**
     * Spielmodus: Spielen - Der Benutzer befindet sich im Spiel, indem er
     * Sterne bzw. leere Felder tippen muss.
     */
    SPIELEN,

    /**
     * Spielmodus: Reploay - Das Spiel wird im Zeitraffer wiedergegeben
     */
    REPLAY,

    /**
     * Spielmodus: Editieren - Der Benutzer befindet sich im Editiermodus, indem
     * er ein eigenes Puzzle erstellt.
     */
    EDITIEREN,

    /**
     * Spielmodus: Loesen - Das Spielfeld wird nur intern bearbeitet um nachher ein
     * gelöstes Spielfeld rauszubekommen.
     */
    LOESEN
}
