package infrastructure.components;

import infrastructure.ResourceManager;
import infrastructure.constants.GlobaleKonstanten;

import javax.swing.*;
import java.awt.*;

/**
 * An/Auswahl fuer den Tracking-Modus
 * 
 * @author Lars Schlegelmilch
 */
public class AnAusAuswahl extends JCheckBox {
    private final ResourceManager resourceManager = ResourceManager.get();

    private Icon icon = resourceManager.getImageIcon("switch-off.png");
    private Icon iconSelected = resourceManager.getImageIcon("switch-on.png");

    public AnAusAuswahl(boolean visible) {
        super.setFont(GlobaleKonstanten.FONT.deriveFont((float) 17));
        super.setFocusable(false);
        super.setText(resourceManager.getText("tracking.an.aus.text"));
        super.setHorizontalTextPosition(2);
        super.setIcon(icon);
        super.setSelectedIcon(iconSelected);
        super.setContentAreaFilled(false);
        super.setMargin(new Insets(0, 0, 0, 0));
        super.setSelected(false);
        super.setVisible(visible);
    }
}
