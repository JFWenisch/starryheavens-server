package severeLobster.backend.spiel;

/**
 * Prüft alle Spalten darauf ob die Anzahl der Sterne 0 ist und setzt dann alles auf Ausschluss
 *
 * @author Christian Lobach
 */
public class SolvingStepCheckZeroColumns implements SolvingStep {

    @Override
    public Spielfeld execute(Spielfeld input) {

        for (int i = 0; i < input.getBreite(); i++) {
            if (input.countSterneSpalte(i) == 0) {

                for (int h = 0; h < input.getHoehe(); h++) {
                    // ist der Platz frei?
                    if (!(input.getSpielstein(i, h) instanceof Pfeil)) {
                        input.setSpielstein(i, h, Ausschluss.getInstance());
                    }
                }

            }
        }

        return input;
    }
}
