package severeLobster.backend.spiel;

/**
 * Wenn genau soviel Felder frei sind wie Zahl (minus schon vorhandener Sterne), dann sind alles Sterne
 *
 * @author Christian Lobach
 */
public class SolvingStepMissingStarsInRow implements SolvingStep {

    @Override
    public Spielfeld execute(Spielfeld input) {

        for (int i = 0; i < input.getHoehe(); i++) {

            int sterne = input.countSterneZeile(i);
            int nochMoeglich = 0;
            int getippteSterne = 0;
            // noch moegliche Sterne zaehlen

            for (int b = 0; b < input.getBreite(); b++) {
                if (input.getSpielstein(b, i).equals(MoeglicherStern.getInstance())) {
                    nochMoeglich++;
                }
                if (input.getSpielstein(b, i).equals(Stern.getInstance())) {
                    getippteSterne++;
                }
            }

            if (sterne - getippteSterne == nochMoeglich) {
                for (int b = 0; b < input.getBreite(); b++) {
                    if (input.getSpielstein(b, i).equals(MoeglicherStern.getInstance())) {
                        input.setSpielstein(b, i, Stern.getInstance());
                    }

                }
            }

        }

        return input;
    }

}
