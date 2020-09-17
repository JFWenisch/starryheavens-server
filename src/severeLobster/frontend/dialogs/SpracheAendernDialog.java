package severeLobster.frontend.dialogs;

import infrastructure.ResourceManager;


import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Dialog zur Sprachenaenderung
 *
 * @author Lars Schlegelmilch
 */
public class SpracheAendernDialog extends JDialog {

    private final ResourceManager resourceManager = ResourceManager.get();

    private Frame owner;
    private Map<String, Locale> map = new HashMap<String, Locale>();

    private final String[] sprachen = {
            resourceManager.getText("language.german"),
            resourceManager.getText("language.english")
    };

    public SpracheAendernDialog(Frame owner) {
        this.owner = owner;
        map.put(sprachen[0], Locale.GERMAN);
        map.put(sprachen[1], Locale.ENGLISH);
    }

    public Locale showDialog() {
        String result = (String)JOptionPane.showInputDialog(owner,
                resourceManager.getText("change.language.text"),
                resourceManager.getText("change.language.title"),
                JOptionPane.INFORMATION_MESSAGE,
                null, sprachen, sprachen[0]);
        if (result != null && !resourceManager.getLanguage().equals(map.get(result))) {
            int answer = JOptionPane.showConfirmDialog(owner,
                    resourceManager.getText("change.language.warning.text"),
                    resourceManager.getText("change.language.warning.title"),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (answer == JOptionPane.YES_OPTION) {
                return map.get(result);
            }
        }
        return null;
    }
}
