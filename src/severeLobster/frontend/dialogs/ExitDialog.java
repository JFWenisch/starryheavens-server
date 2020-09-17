package severeLobster.frontend.dialogs;

import infrastructure.ResourceManager;

import javax.swing.JOptionPane;
import java.awt.Component;

/**
 * Exit-Dialog der Application
 * 
 * @author Lars Schlegelmilch
 */
public class ExitDialog extends JOptionPane {

    private static final ResourceManager resourceManager = ResourceManager
            .get();

    public static final String abbrechen_option = resourceManager
            .getText("exit.application.abbrechen.option");
    public static final String beenden_option = resourceManager
            .getText("exit.application.beenden.option");

    public static final String[] options = { beenden_option, abbrechen_option };

    public static int show(Component parentComponent) {
        return showOptionDialog(parentComponent,
                resourceManager.getText("exit.application.question"),
                resourceManager.getText("exit.application.title"),
                DEFAULT_OPTION, QUESTION_MESSAGE, null, options, null);
    }
}
