package severeLobster.backend.spiel;

/**
 * Dieser Schritt markiert alle Felder auf denen kein Stern sein kann als Ausschluss.
 * Als Vorbereitung sollten alle Möglichen Sterne markiert worden sein.
 *
 * @author Christian Lobach
 */
public class SolvingStepExcludeImpossibles implements SolvingStep {
    @Override
    public Spielfeld execute(Spielfeld input) {

        for (int colums = 0; colums < input.getBreite(); colums++) {
            for (int rows = 0; rows < input.getHoehe(); rows++) {
                if (input.getSpielstein(colums, rows) instanceof KeinStein) {
                    input.setSpielstein(colums, rows, Ausschluss.getInstance());
                }

            }
        }

        return input;
    }
}
