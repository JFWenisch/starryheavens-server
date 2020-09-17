package severeLobster.frontend.dialogs;

import infrastructure.GlobaleKonstanten;
import infrastructure.ResourceManager;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TutorialDialog extends JOptionPane
{
    private static final ResourceManager resourceManager = ResourceManager.get();
    public static final String nächstes_Spiel = "Fortfahren";
    public static final String zurueck_zum_menue = resourceManager.getText("win.back.to.main.menu");


    public static final String[] options = { nächstes_Spiel,
            zurueck_zum_menue };

    public static int show(Component parentComponent, int highscore,
            String spielzeit, int versuche) {
        JLabel title = new JLabel(resourceManager.getText("win.title"));
        JLabel text= new JLabel(
                "<html><body>Sie haben das Puzzle erfolgreich gelöst! <br>"
                        + "Bei einer Spielzeit von " + spielzeit
                        + " Sekunden haben Sie " + versuche
                        + " Versuche benötigt. <br>"
                        + "Ihre Highscore beträgt sagenhafte " + highscore
                        + " Punkte!");
   
       
        title.setFont(GlobaleKonstanten.MENUFONT.deriveFont((float) 20));
        title.setVisible(true);
        text.setVisible(true);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(title, BorderLayout.NORTH);
        panel.add(text, BorderLayout.CENTER);
    
        return showOptionDialog(parentComponent, panel, "Gewonnen",          DEFAULT_OPTION, PLAIN_MESSAGE,
                resourceManager.getImageIcon("sieg.png"), options, null);
    }

}
