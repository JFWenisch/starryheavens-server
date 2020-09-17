package severeLobster.backend.spiel;

import infrastructure.constants.enums.PfeilrichtungEnumeration;

/**
 * Dies sollte der erste Schritt sein. Es werden alle Positionen als Möglicher Stern
 * markiert, auf die ein Pfeil zeigt.
 *
 * @author Christian Lobach
 */
public class SolvingStepPossibleStars implements SolvingStep {
    @Override
    public Spielfeld execute(Spielfeld input) {

        for (int columns = 0; columns < input.getBreite(); columns++) {
            for (int rows = 0; rows < input.getHoehe(); rows++) {

                if (input.getSpielstein(columns, rows) instanceof Pfeil) {

                    Pfeil currentPfeil = (Pfeil) input.getSpielstein(columns, rows);

                    // Richtung der Pfeile folgen, mögliche Sterne setzen
                    switch (currentPfeil.getPfeilrichtung()) {
                        case NORD:
                            for (int curY = rows; curY >= 0; curY--) {
                                if (!(input.getSpielstein(columns, curY) instanceof Pfeil)) {
                                    input.setSpielstein(columns, curY, MoeglicherStern.getInstance());
                                }
                            }
                            break;

                        case NORDOST:
                            for (int curX = columns, curY = rows; curX < input.getBreite() && curY >= 0; curX++, curY--) {
                                if (!(input.getSpielstein(curX, curY) instanceof Pfeil)) {
                                    input.setSpielstein(curX, curY, MoeglicherStern.getInstance());
                                }
                            }

                            break;

                        case OST:
                            for (int curX = columns; curX < input.getBreite(); curX++) {
                                if (!(input.getSpielstein(curX, rows) instanceof Pfeil)) {
                                    input.setSpielstein(curX, rows, MoeglicherStern.getInstance());
                                }
                            }
                            break;

                        case SUEDOST:
                            for (int curX = columns, curY = rows; curX < input.getBreite() && curY < input.getHoehe(); curX++, curY++) {
                                if (!(input.getSpielstein(curX, curY) instanceof Pfeil)) {
                                    input.setSpielstein(curX, curY, MoeglicherStern.getInstance());
                                }
                            }
                            break;

                        case SUED:
                            for (int curY = rows; curY < input.getHoehe(); curY++) {
                                if (!(input.getSpielstein(columns, curY) instanceof Pfeil)) {
                                    input.setSpielstein(columns, curY, MoeglicherStern.getInstance());
                                }
                            }
                            break;

                        case SUEDWEST:
                            for (int curX = columns, curY = rows; curX >= 0 && curY < input.getHoehe(); curX--, curY++) {
                                if (!(input.getSpielstein(curX, curY) instanceof Pfeil)) {
                                    input.setSpielstein(curX, curY, MoeglicherStern.getInstance());
                                }
                            }
                            break;

                        case WEST:
                            for (int curX = columns; curX >= 0; curX--) {
                                if (!(input.getSpielstein(curX, rows) instanceof Pfeil)) {
                                    input.setSpielstein(curX, rows, MoeglicherStern.getInstance());
                                }
                            }
                            break;

                        case NORDWEST:
                            for (int curX = columns, curY = rows; curX >= 0 && curY >= 0; curX--, curY--) {
                                if (!(input.getSpielstein(curX, curY) instanceof Pfeil)) {
                                    input.setSpielstein(curX, curY, MoeglicherStern.getInstance());
                                }
                            }
                            break;
                    }


                }


            }

        }


        return input;
    }
}
