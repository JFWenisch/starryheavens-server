package severeLobster.backend.spiel;

/**
 * Wenn soviele Felder Sterne sind wie die Zahl ist der Rest Ausschluss
 *
 * @author Christian Lobach
 */
public class SolvingStepExcludeRestInColumn implements SolvingStep {
    @Override
    public Spielfeld execute(Spielfeld input) {

        for (int i = 0; i < input.getBreite(); i++) {
            // Pruefen, ob alle Sterne getippt wurden, wenn ja setze Rest auf Ausschluss
            if (input.countSterneSpalte(i) == countGetippteSterne(input,i))
            {
                for(int h = 0; h < input.getHoehe(); h++)
                {
                    Spielstein current = input.getSpielstein(i,h);
                    if (! (current instanceof Pfeil || current.equals(Stern.getInstance()))){
                        input.setSpielstein(i,h,Ausschluss.getInstance());
                    }
                }
            }

        }
        return input;
    }

    private int countGetippteSterne(Spielfeld input, int spalte) {
        int anzahl = 0;
        for (int zeile = 0; zeile < input.getHoehe(); zeile++) {
            if (input.getSpielstein(spalte, zeile).equals(Stern.getInstance())) {
                anzahl++;
            }

        }
        return anzahl;
    }

}
