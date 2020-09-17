package severeLobster.backend.spiel;

import infrastructure.constants.enums.PfeilrichtungEnumeration;

/**
 * Wenn in einer Spalte ein Stern und ein Pfeil nach oben oder unten ist
 * ist hinter dem Pfeil ein Ausschluss
 *
 * @author Christian Lobach
 */
public class SolvingStepExcludeBehindInColumn implements SolvingStep {
    @Override
    public Spielfeld execute(Spielfeld input) {
        // Alle Spalten durchlaufen
        for (int i = 0; i < input.getBreite(); i++) {
            if (input.countSterneSpalte(i) == 1 && input.countPfeileSpalte(i) == 1) {

                for (int h = 0; h < input.getHoehe(); h++) {
                    // Pfeile suchen
                    if (input.getSpielstein(i, h) instanceof Pfeil) {
                        Pfeil currentPfeil = (Pfeil) input.getSpielstein(i, h);

                        if (currentPfeil.getPfeilrichtung() == PfeilrichtungEnumeration.NORD) {
                            for (int y = h; y < input.getHoehe(); y++) {
                                if (input.getSpielstein(i, y) instanceof KeinStein) {
                                    input.setSpielstein(i, y, Ausschluss.getInstance());
                                }
                            }
                        } else if (currentPfeil.getPfeilrichtung() == PfeilrichtungEnumeration.SUED) {
                            for (int y = h; y >= 0; y--) {
                                if (input.getSpielstein(i, y) instanceof KeinStein) {
                                    input.setSpielstein(i, y, Ausschluss.getInstance());
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
