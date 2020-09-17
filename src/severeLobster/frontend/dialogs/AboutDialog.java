package severeLobster.frontend.dialogs;

import infrastructure.GlobaleKonstanten;
import infrastructure.ResourceManager;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

/**
 * Erste Implementation des About Dialogs
 *
 * @author Lars Schlegelmilch
 */
public class AboutDialog extends JDialog {

    private final ResourceManager resourceManager = ResourceManager.get();
    private final Box box;

    private AboutDialog(Frame owner) {
        super(owner);

        setModal(false);
        setTitle("About");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel name = new JLabel(GlobaleKonstanten.SPIELNAME);
        name.setFont(GlobaleKonstanten.MENUFONT.deriveFont((float) 20));
        name.setAlignmentX(0.5f);
        add(name);

        box = Box.createVerticalBox();
        box.add(Box.createGlue());

        String version = GlobaleKonstanten.VERSION;

        initTitle(GlobaleKonstanten.SPIELNAME + version);
        initContent("Last Update: " + new Date(System.currentTimeMillis()));

        box.add(Box.createGlue());
        initContent("                   ");
        initTitle("Publisher & Support");
        initContent("NIRAKO-IT");
        initContent("www.nirako.de");
        initContent("info@nirako.de");
        initContent("                   ");
        initTitle("Developers");
        initContent("Paul Brüll");
        initContent("Lars Schlegelmilch");
        initContent("Lutz Kleiber");
        initContent("Christian Lobach");
        initContent("Jean-Fabian Wenisch");
        initContent("Stefan Frings");
        initContent("                   ");
        initTitle("Musik von http://www.ende.tv");
        initContent("Sascha Ende-blockbuster_atmosphere_1_(calm)");
        box.add(Box.createGlue());
        box.setAlignmentX(0.5f);
        add(box);

        JButton close = new JButton(resourceManager.getText("about.close"));
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                dispose();
            }
        });
        close.setAlignmentX(0.5f);
        close.setAlignmentY(0.1f);
        add(close);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(owner);
    }

    private void initTitle(String text) {
        JLabel title = new JLabel(" " + text);
        title.setFont(GlobaleKonstanten.MENUFONT.deriveFont((float) 11).deriveFont(Font.BOLD));
        box.add(title);
    }

    private void initContent(String text) {
        box.add(new JLabel(" " + text));
    }

    public static void showAboutDialog(Frame owner) {
        new AboutDialog(owner).setVisible(true);
    }
}
