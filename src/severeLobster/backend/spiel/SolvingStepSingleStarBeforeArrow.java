package severeLobster.backend.spiel;

/**
 * Wenn auf der Bahn von einem Pfeil ausgehend nur noch ein Feld frei ist, und noch kein Stern auf der Bahn liegt
 * kann ein Stern gesetzt werden.
 *
 * @author Christian Lobach
 */
public class SolvingStepSingleStarBeforeArrow implements SolvingStep {
    @Override
    public Spielfeld execute(Spielfeld input) {

        // Spielfeld Spalten durchlaufen
        for (int breite = 0; breite < input.getBreite(); breite++) {
            // Spielfeld Zeilen durchlaufen
            for (int hoehe = 0; hoehe < input.getHoehe(); hoehe++) {

                // Auf Pfeil prüfen
                if (input.getSpielstein(breite, hoehe) instanceof Pfeil) {
                    Pfeil currentPfeil = (Pfeil) input.getSpielstein(breite, hoehe);

                    int moeglicheSterne = 0;
                    boolean stop = false;
                    int sternKandidatX = -1;
                    int sternKandidatY = -1;

                    // Der Pfeilrichtung folgen und speichern wie viele Mögliche Sterne auf dem Weg liegen.
                    // Liegt ein Stern auf dem Weg kann nicht auf einen weiteren geschlossen werden.
                    switch (currentPfeil.getPfeilrichtung()) {

                        case NORD:
                            for (int curY = hoehe; curY >= 0; curY--) {
                                if (input.getSpielstein(breite, curY).equals(MoeglicherStern.getInstance())) {
                                    moeglicheSterne++;
                                    sternKandidatX = breite;
                                    sternKandidatY = curY;
                                } else if (input.getSpielstein(breite, curY).equals(Stern.getInstance()))
                                {
                                    // Wenn schon ein Stern auf der Bahn liegt, setze Abbruchbedingung
                                    stop = true;
                                }
                            }
                            break;

                        case NORDOST:
                            for (int curX = breite, curY = hoehe; curX < input.getBreite() && curY >= 0; curX++, curY--) {
                                if (input.getSpielstein(curX, curY).equals(MoeglicherStern.getInstance())) {
                                    moeglicheSterne++;
                                    sternKandidatX = curX;
                                    sternKandidatY = curY;
                                } else if (input.getSpielstein(curX, curY).equals(Stern.getInstance()))
                                {
                                    // Wenn schon ein Stern auf der Bahn liegt, setze Abbruchbedingung
                                    stop = true;
                                }
                            }

                            break;

                        case OST:
                            for (int curX = breite; curX < input.getBreite(); curX++) {
                                if (input.getSpielstein(curX, hoehe).equals(MoeglicherStern.getInstance())) {
                                    moeglicheSterne++;
                                    sternKandidatX = curX;
                                    sternKandidatY = hoehe;
                                } else if (input.getSpielstein(curX, hoehe).equals(Stern.getInstance()))
                                {
                                    // Wenn schon ein Stern auf der Bahn liegt, setze Abbruchbedingung
                                    stop = true;
                                }

                            }
                            break;

                        case SUEDOST:
                            for (int curX = breite, curY = hoehe; curX < input.getBreite() && curY < input.getHoehe(); curX++, curY++) {
                                if (input.getSpielstein(curX, curY).equals(MoeglicherStern.getInstance())) {
                                    moeglicheSterne++;
                                    sternKandidatX = curX;
                                    sternKandidatY = curY;
                                } else if (input.getSpielstein(curX, curY).equals(Stern.getInstance()))
                                {
                                    // Wenn schon ein Stern auf der Bahn liegt, setze Abbruchbedingung
                                    stop = true;
                                }

                            }
                            break;

                        case SUED:
                            for (int curY = hoehe; curY < input.getHoehe(); curY++) {
                                if (input.getSpielstein(breite, curY).equals(MoeglicherStern.getInstance())) {
                                    moeglicheSterne++;
                                    sternKandidatX = breite;
                                    sternKandidatY = curY;
                                } else if (input.getSpielstein(breite, curY).equals(Stern.getInstance()))
                                {
                                    // Wenn schon ein Stern auf der Bahn liegt, setze Abbruchbedingung
                                    stop = true;
                                }
                            }
                            break;

                        case SUEDWEST:
                            for (int curX = breite, curY = hoehe; curX >= 0 && curY < input.getHoehe(); curX--, curY++) {
                                if (input.getSpielstein(curX, curY).equals(MoeglicherStern.getInstance())) {
                                    moeglicheSterne++;
                                    sternKandidatX = curX;
                                    sternKandidatY = curY;
                                } else if (input.getSpielstein(curX, curY).equals(Stern.getInstance()))
                                {
                                    // Wenn schon ein Stern auf der Bahn liegt, setze Abbruchbedingung
                                    stop = true;
                                }
                            }
                            break;

                        case WEST:
                            for (int curX = breite; curX >= 0; curX--) {
                                if (input.getSpielstein(curX, hoehe).equals(MoeglicherStern.getInstance())) {
                                    moeglicheSterne++;
                                    sternKandidatX = curX;
                                    sternKandidatY = hoehe;
                                } else if (input.getSpielstein(curX, hoehe).equals(Stern.getInstance()))
                                {
                                    // Wenn schon ein Stern auf der Bahn liegt, setze Abbruchbedingung
                                    stop = true;
                                }
                            }
                            break;

                        case NORDWEST:
                            for (int curX = breite, curY = hoehe; curX >= 0 && curY >= 0; curX--, curY--) {
                                if (input.getSpielstein(curX, curY).equals(MoeglicherStern.getInstance())) {
                                    moeglicheSterne++;
                                    sternKandidatX = curX;
                                    sternKandidatY = curY;
                                } else if (input.getSpielstein(curX, curY).equals(Stern.getInstance()))
                                {
                                    // Wenn schon ein Stern auf der Bahn liegt, setze Abbruchbedingung
                                    stop = true;
                                }
                            }
                            break;
                    }
                    // Wenn noch ein mögliche Stern auf der Bahn gefunden wurde, setze ihn hier
                    // anhand der vorher gespeicherten Koordinaten
                    if (!stop && moeglicheSterne == 1){
                        input.setSpielstein(sternKandidatX,sternKandidatY,Stern.getInstance());
                    }


                }
            }


        }


        return input;
    }
}
