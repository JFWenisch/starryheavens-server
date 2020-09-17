package severeLobster.backend.spiel;

/**
 * Wenn soviele Felder Sterne sind wie die Zahl ist der Rest Ausschluss
 *
 * @author Christian Lobach
 */
public class SolvingStepExcludeRestInRow implements SolvingStep {
    @Override
    public Spielfeld execute(Spielfeld input) {

        for (int i = 0; i < input.getHoehe(); i++) {
            // Pruefen, ob alle Sterne getippt wurden, wenn ja setze Rest auf Ausschluss
            if (input.countSterneZeile(i) == countGetippteSterne(input,i))
            {
                for(int b = 0; b < input.getBreite(); b++)
                {
                    Spielstein current = input.getSpielstein(b,i);
                    if (! (current instanceof Pfeil || current.equals(Stern.getInstance()))){
                        input.setSpielstein(b,i,Ausschluss.getInstance());
                    }
                }
            }

        }
        return input;
    }

    private int countGetippteSterne(Spielfeld input, int zeile) {
        int anzahl = 0;
        for (int spalte = 0; spalte < input.getBreite(); spalte++) {
            if (input.getSpielstein(spalte, zeile).equals(Stern.getInstance())) {
                anzahl++;
            }

        }
        return anzahl;
    }

}
