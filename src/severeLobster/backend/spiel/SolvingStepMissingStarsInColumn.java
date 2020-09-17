package severeLobster.backend.spiel;

/**
 * Wenn genau soviel Felder frei sind wie Zahl (minus schon vorhandener Sterne), dann sind alles Sterne
 *
 * @author Christian Lobach
 */
public class SolvingStepMissingStarsInColumn implements SolvingStep {


    @Override
    public Spielfeld execute(Spielfeld input) {

        for (int i = 0; i < input.getBreite(); i++) {

            int sterne = input.countSterneSpalte(i);
            int nochMoeglich = 0;
            int getippteSterne = 0;
            // noch moegliche Sterne zaehlen

            for (int h = 0; h < input.getHoehe(); h++) {
                if (input.getSpielstein(i, h).equals(MoeglicherStern.getInstance())) {
                    nochMoeglich++;
                }
                if (input.getSpielstein(i, h).equals(Stern.getInstance())) {
                    getippteSterne++;
                }
            }

            if (sterne - getippteSterne == nochMoeglich) {
                for (int h = 0; h < input.getHoehe(); h++) {
                    if (input.getSpielstein(i, h).equals(MoeglicherStern.getInstance())) {
                        input.setSpielstein(i, h, Stern.getInstance());
                    }

                }
            }

        }

        return input;
    }
}
