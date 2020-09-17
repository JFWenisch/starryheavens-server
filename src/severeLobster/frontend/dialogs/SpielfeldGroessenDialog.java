package severeLobster.frontend.dialogs;

/**
 * Groessendialog fuer Spielfeldgroesse
 *
 * @author Lars Schlegelmilch
 */

import com.jgoodies.validation.formatter.EmptyNumberFormatter;
import infrastructure.ResourceManager;
import infrastructure.components.Koordinaten;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog zur Groessenaenderung
 *
 * @author Lars Schlegelmilch
 */
public class SpielfeldGroessenDialog {

    private final static ResourceManager resourceManager = ResourceManager.get();


    public static Koordinaten show(Frame owner) {
        EmptyNumberFormatter numberFormatter = new EmptyNumberFormatter();
        numberFormatter.setAllowsInvalid(false);

        JFormattedTextField xAchse = new JFormattedTextField(numberFormatter);
        JFormattedTextField yAchse = new JFormattedTextField(numberFormatter);
        xAchse.setColumns(3);
        yAchse.setColumns(3);

        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel(resourceManager.getText("spielfeldgroesse.breite")));
        myPanel.add(xAchse);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel(resourceManager.getText("spielfeldgroesse.hoehe")));
        myPanel.add(yAchse);

        int result = JOptionPane.showConfirmDialog(owner, myPanel,
                resourceManager.getText("spielfeldgroesse.input.size"), JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            int xWert;
            int yWert;
            try {
                xWert = Integer.parseInt(xAchse.getText());
                yWert = Integer.parseInt(yAchse.getText());
            } catch (NumberFormatException e) {
                xWert = 0;
                yWert = 0;
            }
            if (xWert < 2 || yWert < 2 || xWert > 40 || yWert > 40) {
                JOptionPane.showMessageDialog(owner, resourceManager.getText("spielfeldgroesse.error"),
                        resourceManager.getText("spielfeldgroesse.error.title"), JOptionPane.ERROR_MESSAGE);
                return show(owner);
            } else {
                return new Koordinaten(xWert, yWert);
            }
        }
        return null;
    }
}
