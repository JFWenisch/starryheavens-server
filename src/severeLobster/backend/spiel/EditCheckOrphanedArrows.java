package severeLobster.backend.spiel;

import infrastructure.components.Koordinaten;

import java.util.ArrayList;

/**
 * Prueft ob es Pfeile im Spielfeld gibt, die auf keinen Stern zeigen
 *
 * @author Christian Lobach
 */
public class EditCheckOrphanedArrows implements EditCheck {
    @Override
    public Koordinaten[] execute(Spielfeld input) {

        ArrayList<Koordinaten> koordinaten = new ArrayList<Koordinaten>();

        // Spielfeld Spalten durchlaufen
        for (int breite = 0; breite < input.getBreite(); breite++) {
            // Spielfeld Zeilen durchlaufen
            for (int hoehe = 0; hoehe < input.getHoehe(); hoehe++) {

                // Auf Pfeil prüfen
                if (input.getSpielstein(breite, hoehe) instanceof Pfeil) {
                    Pfeil currentPfeil = (Pfeil) input.getSpielstein(breite, hoehe);

                    int anzahlSterne = 0;

                    // Der Pfeilrichtung folgen zaehlen wie viele Sterne auf dem Weg liegen
                    // Liegen keine Sterne auf dem weg, wird die Position des Pfeils gespeichert
                    switch (currentPfeil.getPfeilrichtung()) {

                        case NORD:
                            for (int curY = hoehe; curY >= 0; curY--) {
                                if (input.getSpielstein(breite, curY).equals(Stern.getInstance())) {
                                    anzahlSterne++;
                                }
                            }
                            if (anzahlSterne == 0) koordinaten.add(new Koordinaten(breite, hoehe));
                            break;

                        case NORDOST:
                            for (int curX = breite, curY = hoehe; curX < input.getBreite() && curY >= 0; curX++, curY--) {
                                if (input.getSpielstein(curX, curY).equals(Stern.getInstance())) {
                                    anzahlSterne++;
                                }
                            }
                            if (anzahlSterne == 0) koordinaten.add(new Koordinaten(breite, hoehe));
                            break;

                        case OST:
                            for (int curX = breite; curX < input.getBreite(); curX++) {
                                if (input.getSpielstein(curX, hoehe).equals(Stern.getInstance())) {
                                    anzahlSterne++;
                                }
                            }
                            if (anzahlSterne == 0) koordinaten.add(new Koordinaten(breite, hoehe));
                            break;

                        case SUEDOST:
                            for (int curX = breite, curY = hoehe; curX < input.getBreite() && curY < input.getHoehe(); curX++, curY++) {
                                if (input.getSpielstein(curX, curY).equals(Stern.getInstance())) {
                                    anzahlSterne++;
                                }
                            }
                            if (anzahlSterne == 0) koordinaten.add(new Koordinaten(breite, hoehe));
                            break;

                        case SUED:
                            for (int curY = hoehe; curY < input.getHoehe(); curY++) {
                                if (input.getSpielstein(breite, curY).equals(Stern.getInstance())) {
                                    anzahlSterne++;
                                }
                            }
                            if (anzahlSterne == 0) koordinaten.add(new Koordinaten(breite, hoehe));
                            break;

                        case SUEDWEST:
                            for (int curX = breite, curY = hoehe; curX >= 0 && curY < input.getHoehe(); curX--, curY++) {
                                if (input.getSpielstein(curX, curY).equals(Stern.getInstance())) {
                                    anzahlSterne++;
                                }
                            }
                            if (anzahlSterne == 0) koordinaten.add(new Koordinaten(breite, hoehe));
                            break;

                        case WEST:
                            for (int curX = breite; curX >= 0; curX--) {
                                if (input.getSpielstein(curX, hoehe).equals(Stern.getInstance())) {
                                    anzahlSterne++;
                                }
                            }
                            if (anzahlSterne == 0) koordinaten.add(new Koordinaten(breite, hoehe));
                            break;

                        case NORDWEST:
                            for (int curX = breite, curY = hoehe; curX >= 0 && curY >= 0; curX--, curY--) {
                                if (input.getSpielstein(curX, curY).equals(Stern.getInstance())) {
                                    anzahlSterne++;
                                }
                            }
                            if (anzahlSterne == 0) koordinaten.add(new Koordinaten(breite, hoehe));
                            break;
                    }

                }
            }


        }

        Koordinaten[] fehler = new Koordinaten[koordinaten.size()];
        for (int i = 0; i < koordinaten.size(); i++) {
            fehler[i] = koordinaten.get(i);
        }


        return fehler;
    }
}
