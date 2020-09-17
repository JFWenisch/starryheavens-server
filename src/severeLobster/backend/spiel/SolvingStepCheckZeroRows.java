package severeLobster.backend.spiel;

/**
 * Prüft alle Zeilen darauf ob die Anzahl der Sterne 0 ist und setzt dann alles auf Ausschluss
 *
 * @author Christian Lobach
 */
public class SolvingStepCheckZeroRows implements SolvingStep {

    @Override
    public Spielfeld execute(Spielfeld input) {

        for (int i = 0; i < input.getHoehe(); i++) {
            if (input.countSterneZeile(i) == 0) {
                for (int b = 0; b < input.getBreite(); b++) {
                    // ist der Platz frei?
                    if (!(input.getSpielstein(b, i) instanceof Pfeil)) {
                        input.setSpielstein(b, i, Ausschluss.getInstance());
                    }
                }

            }
        }

        return input;
    }
}
