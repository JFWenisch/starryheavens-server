package severeLobster.backend.spiel;

import infrastructure.components.Koordinaten;
import infrastructure.constants.enums.SpielmodusEnumeration;
import infrastructure.exceptions.LoesungswegNichtEindeutigException;

import java.util.ArrayList;

/**
 * Soll ein Spielfeld lösen.
 *
 * @author Christian Lobach
 */
public class SolvingStrategyStandard extends SolvingStrategy {


    public void solve(Spielfeld input) {


        EditCheckOrphanedArrows pfeile = new EditCheckOrphanedArrows();
        _errors = pfeile.execute(input);
        if (_errors.length > 0) {
            _solvable = false;
        }
        EditCheckEmptyField leeresFeld = new EditCheckEmptyField();
        if (leeresFeld.execute(input).length > 0) {
            _solvable = false;
        }

        if (_solvable == null) {


            Spielfeld solvedField = new Spielfeld(input);

            try {
                solvedField.setSpielmodus(SpielmodusEnumeration.LOESEN);
            } catch (LoesungswegNichtEindeutigException e) {
                /*
                * Try-Catch nur formal. Vorerst wird die Exception nur beim
                * Umstellen auf Spielmodus geworfen.
                */
                e.printStackTrace();
            }

            // Loesungsschritte die nur einmalig zu Beginn ausgeführt werden;

            solvedField = new SolvingStepPossibleStars().execute(solvedField);
            solvedField = new SolvingStepExcludeImpossibles().execute(solvedField);
            solvedField = new SolvingStepCheckZeroColumns().execute(solvedField);
            solvedField = new SolvingStepCheckZeroRows().execute(solvedField);


            boolean abbruch = false;
            while (!solvedField.isSolved() && !abbruch) {

                Spielfeld before = new Spielfeld(solvedField);

                // Schritte ausführen die mehrmals aufgerufen werden
                solvedField = new SolvingStepMissingStarsInColumn().execute(solvedField);
                solvedField = new SolvingStepMissingStarsInRow().execute(solvedField);
                solvedField = new SolvingStepExcludeRestInColumn().execute(solvedField);
                solvedField = new SolvingStepExcludeRestInRow().execute(solvedField);
                solvedField = new SolvingStepSingleStarBeforeArrow().execute(solvedField);


                /**
                 * Wenn sich in einem Spielfeld nach einem Durchlauf nichts geaendert hat,
                 * ist das Spiel nicht lösbar
                 */
                if (solvedField.equals(before)) {
                    abbruch = true;
                }

            }

            // Das Spielfeld ist nicht, oder nicht eindeutig loesbar
            if (abbruch) {
                int moeglich = solvedField.countMoeglicheSterne();
                //System.out.println(moeglich+ " mögliche Sterne");
                ArrayList<Koordinaten> loesungswegEinsprungspunkte = new ArrayList<Koordinaten>();

                for (int i = 0; i < moeglich; i++) {

                    Spielfeld raten = new Spielfeld(solvedField);
                    try {
                        raten.setSpielmodus(SpielmodusEnumeration.LOESEN);
                    } catch (LoesungswegNichtEindeutigException e) {
                        /*
                        * Try-Catch nur formal. Vorerst wird die Exception nur beim
                        * Umstellen auf Spielmodus geworfen.
                        */
                        e.printStackTrace();
                    }


                    // einen möglichen Stern setzen
                    Koordinaten nextGuess = nextPossibleStar(raten, i);
                    loesungswegEinsprungspunkte.add(nextGuess);
                    raten.setSpielstein(nextGuess.getX(), nextGuess.getY(), Stern.getInstance());


                    // Wieder die Loesungsschritte anwenden
                    boolean ratenAbbruch = false;
                    while (!raten.isSolved() && !ratenAbbruch) {

                        Spielfeld beforeRaten = new Spielfeld(raten);

                        // Schritte ausführen die mehrmals aufgerufen werden
                        raten = new SolvingStepMissingStarsInColumn().execute(raten);
                        raten = new SolvingStepMissingStarsInRow().execute(raten);
                        raten = new SolvingStepExcludeRestInColumn().execute(raten);
                        raten = new SolvingStepExcludeRestInRow().execute(raten);
                        raten = new SolvingStepSingleStarBeforeArrow().execute(raten);

                        if (raten.equals(beforeRaten)) ratenAbbruch = true;
                    }

                    //Wenn es mit diesem erratenen Stern gelöst werden konnte, bleibt es als moeglicher Loesungsweg
                    // in der Liste drin, sonst wird es wieder entfernt
                    if (ratenAbbruch) {
                        loesungswegEinsprungspunkte.remove(nextGuess);
                    }

                }

                // Spielfeld ist durch raten lösbar
                if (loesungswegEinsprungspunkte.size() > 0) {

                    _notUnique = new Koordinaten[loesungswegEinsprungspunkte.size()];
                    for (int i = 0; i < loesungswegEinsprungspunkte.size(); i++) {
                        _notUnique[i] = loesungswegEinsprungspunkte.get(i);
                    }
                    _solvable = true;
                    _unique = false;
                } else {
                    _solvable = false;
                }


            } else {
                _solvable = true;
                _unique = true;
            }

        }
    }


}
