package severeLobster.frontend.dialogs;

import infrastructure.GlobaleKonstanten;
import infrastructure.ResourceManager;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Locale;

/**
 * Gewonnen-Dialog der Application
 * 
 * @author Lars Schlegelmilch
 */
public class GewonnenDialog extends JOptionPane {
    private static final ResourceManager resourceManager = ResourceManager.get();

    public static final String neues_spiel_starten = resourceManager.getText("win.start.new.game");
    public static final String zurueck_zum_menue = resourceManager.getText("win.back.to.main.menu");


    public static final String[] options = { neues_spiel_starten,
            zurueck_zum_menue};

    public static int show(Component parentComponent, int highscore,
            String spielzeit, int versuche) {
        JLabel title = new JLabel(resourceManager.getText("win.title"));
        JLabel text;
        if (resourceManager.getLanguage().equals(Locale.GERMAN)) {
        text = new JLabel(
                "<html><body>Sie haben das Puzzle erfolgreich gelöst! <br>"
                        + "Bei einer Spielzeit von " + spielzeit
                        + " Sekunden haben Sie " + versuche
                        + " Versuche benötigt. <br>"
                        + "Ihre Highscore beträgt sagenhafte " + highscore
                        + " Punkte!");
        }
        else {
            text = new JLabel(
                    "<html><body>The Puzzle were successfully solved! <br>"
                            + "You have played " + spielzeit
                            + " Seconds and you needed " + versuche
                            + " Attempts. <br>"
                            + "Your Highscore is " + highscore
                            + " Points!");
        }
        title.setFont(GlobaleKonstanten.MENUFONT.deriveFont((float) 20));
        title.setVisible(true);
        text.setVisible(true);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(title, BorderLayout.NORTH);
        panel.add(text, BorderLayout.CENTER);

        return showOptionDialog(parentComponent, panel, "Gewonnen",
                DEFAULT_OPTION, PLAIN_MESSAGE,
                resourceManager.getImageIcon("sieg.png"), options, null);
    }

}
