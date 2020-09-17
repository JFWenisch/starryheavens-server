package infrastructure.components;

import javax.swing.Icon;
import javax.swing.JButton;

/**
 * Buttons fuer das Hauuptmenue
 * @author Lars Schlegelmilch
 */
public class MenuButton extends JButton {

    public MenuButton(final Icon defaultIcon,
                      final Icon rolloverIcon,
                      final Icon pressedIcon) {

        setOpaque(false);
        setFocusable(false);
        setContentAreaFilled(false);
        setIcon(defaultIcon);
        setRolloverIcon(rolloverIcon);
        setPressedIcon(pressedIcon);
    }


}
