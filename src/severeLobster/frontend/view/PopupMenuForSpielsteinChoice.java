package severeLobster.frontend.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import severeLobster.backend.spiel.Spielstein;
import severeLobster.frontend.controller.SpielfeldDarstellungsSteuerung;

/**
 * TODO An neue Architektur anpassen. Popup Maus Menu zum Editieren des
 * SpielsteinState im Editier Modus dies Spiels.
 * 
 * @author Lutz Kleiber
 * 
 */
public class PopupMenuForSpielsteinChoice extends JPopupMenu {

    public PopupMenuForSpielsteinChoice(
            final SpielfeldDarstellungsSteuerung controller,
            final List<? extends Spielstein> listAvailableStates, final int x,
            final int y) {

        JMenuItem menuItem;

        /**
         * Durchlaufe die fuer diesen Spielstein moeglichen Stati und stelle
         * jeden dieser Stati mit einem Icon und Text zur Auswahl dar.
         */
        for (Spielstein currentstein : listAvailableStates) {
            /** Hole Icon und Namen fuer diesen SpielsteinState: */

            menuItem = new JMenuItem(currentstein.toString(), SimpleIconFactory
                    .getInstance().getIconForSpielstein(currentstein));
            /**
             * Sinn dieser Variable ist es der Anonymen Klasse eine Konstante
             * mitzugeben:
             */
            final Spielstein finalCurrentState = currentstein;
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    /**
                     * Bei Klick auf den Status soll eine Statusaenderung
                     * hierhin gemacht werden.
                     */
                    controller.setSpielstein(finalCurrentState, x, y);
                }
            });
            this.add(menuItem);
        }

    }
}
