package severeLobster.backend.spiel;

import infrastructure.constants.enums.PfeilrichtungEnumeration;

/**
 * Wenn in einer Zeile ein Stern und ein Pfeil nach oben oder unten ist
 * ist hinter dem Pfeil ein Ausschluss
 *
 * @author Christian Lobach
 */
public class SolvingStepExcludeBehindInRow implements SolvingStep {
    @Override
    public Spielfeld execute(Spielfeld input) {
        // Alle Zeilen durchlaufen
        for (int i = 0; i < input.getHoehe(); i++) {
            if (input.countSterneZeile(i) == 1 && input.countPfeileZeile(i) == 1) {

                for (int b = 0; b < input.getBreite(); b++) {
                    // Pfeile suchen
                    if (input.getSpielstein(b, i) instanceof Pfeil) {
                        Pfeil currentPfeil = (Pfeil) input.getSpielstein(b, i);

                        if (currentPfeil.getPfeilrichtung() == PfeilrichtungEnumeration.WEST) {
                            for (int x = b; x < input.getBreite(); x++) {
                                if (input.getSpielstein(x, i) instanceof KeinStein) {
                                    input.setSpielstein(x, i, Ausschluss.getInstance());
                                }
                            }
                        } else if (currentPfeil.getPfeilrichtung() == PfeilrichtungEnumeration.OST) {
                            for (int x = b; x >= 0; x--) {
                                if (input.getSpielstein(x, i) instanceof KeinStein) {
                                    input.setSpielstein(x, i, Ausschluss.getInstance());
                                }
                            }
                        }
                    }
                }

            }
        }
        return input;
    }
}
