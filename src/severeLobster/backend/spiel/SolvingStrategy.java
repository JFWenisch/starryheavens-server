package severeLobster.backend.spiel;

import infrastructure.components.Koordinaten;

/**
 * Die Klasse soll eine Strategie zum lösen eines Spielfeldes darstellen
 * Sie kann nacheinander mehrere einzelne SolvingSteps ausführen
 *
 *  @author Christian Lobach
 */

public abstract class SolvingStrategy {

    protected Boolean _solvable = null;
    protected Boolean _unique = null;
    protected Spielfeld _solvedField = null;
    protected Koordinaten[] _errors = null;
    protected Koordinaten[] _notUnique = null;

    protected abstract void solve(Spielfeld input);

    /**
     * Prueft ob ein Spielfeld loesbar ist
     * @return loesbar
     */
    public boolean isSolvable() {

        return _solvable;
    }

    /**
     * Prueft ob ein Spielfeld loesbar ist
     * @return eindeutig
     */
    public boolean isUnique() {
        return _unique;
    }

    /**
     * Gibt die Koordinaten der Fehler zurueck, die ein Spielfeld unloesbar machen
     * @return fehler
     */
    public Koordinaten[] getErrors() {
        return _errors;
    }

    /**
     * Gibt die Koordinaten der Felder zurueck, die zur Uneindeutigkeit fuehren
     * @return uneindeutigeFelder
     */
    public Koordinaten[] getNotUnique() {
        return _notUnique;
    }


    /**
     * gibt den naechstmöglichen Stern zurück
     *
     * @param offset die Anzahl der Sterne die übersprungen wird, bevor einer zurueckgegeben wird
     * @return Point mit den Koordinaten des nächstmöglichen Sterns
     */
    protected Koordinaten nextPossibleStar(Spielfeld input, int offset) {
        // Die Anzahl moeglicher Sterne auf die man beim Durchlaufen trifft
        int metPossibilities = 0;

        for (int colums = 0; colums < input.getBreite(); colums++) {
            for (int rows = 0; rows < input.getHoehe(); rows++) {
                if (input.getSpielstein(colums, rows) instanceof MoeglicherStern) {
                    // Wurde die gewuenschte Anzahl gefundener Moeglichkeiten uebersprungen, wird die Position zurueckgegeben
                    if (offset == metPossibilities) {
                        return new Koordinaten(colums, rows);
                    }
                    metPossibilities++;
                }
            }

        }
        return null;
    }

}

