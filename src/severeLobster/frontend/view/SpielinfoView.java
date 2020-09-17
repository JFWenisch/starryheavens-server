package severeLobster.frontend.view;

import infrastructure.ResourceManager;
import infrastructure.constants.GlobaleKonstanten;
import severeLobster.backend.spiel.SternenSpielApplicationBackend;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.net.URL;

/**
 * View für die Spielinfo
 *
 * @author Jean-Fabian Wenisch, Lars Schlegelmilch
 */
public class SpielinfoView extends JPanel {

    private final ResourceManager resourceManager = ResourceManager.get();
    private final SternenSpielApplicationBackend backend;
    private URL avatar = resourceManager.getUserAvatar();

    public SpielinfoView(SternenSpielApplicationBackend backend) {
        setLayout(null);
        this.backend = backend;
        setOpaque(false);
        setPreferredSize(new Dimension(200, 500));
        setVisible(true);
    }

    public SpielinfoView(final TrackingControllView trackingControllView,
            SternenSpielApplicationBackend backend) {
        setLayout(null);
        this.backend = backend;
        setOpaque(false);
        trackingControllView.setBounds(0, 365, 200, 500);
        add(trackingControllView, BorderLayout.SOUTH);
        setPreferredSize(new Dimension(200, 500));
        setVisible(true);
    }

    public void changeAvatar(URL avatar) {
        this.avatar = avatar;
        resourceManager.setAvatar(avatar);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d=(Graphics2D)g;

        /**
         * Anfang Test von TrackingControllView
         */
        int zugcount = backend.getSpiel().getAnzahlZuege();
        String strSpielZeit = backend.getSpiel().getSpielZeit();
        int sterneGetippt = backend.getSpiel().getSpielfeld().countSterneGetippt();
        int sterne = backend.getSpiel().getSpielfeld().countSterne();

        Image sImage = getToolkit().getImage(avatar);

        /**
         * Ende Test von TrackingControllView
         */
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawImage(sImage, 0, 0, this);
        g2d.setFont(GlobaleKonstanten.FONT);
        g2d.setColor(Color.YELLOW);
        g2d.drawString(System.getProperty("user.name"), 90, 80);
        g2d.setColor(Color.BLACK);
        g2d.drawString(resourceManager.getText("try") + " \t" + String.valueOf(zugcount), 30, 271);
        g2d.drawString(resourceManager.getText("time") + " \t" + strSpielZeit + " "
                   + resourceManager.getText("seconds"), 30, 321);
        if (sterneGetippt > sterne) {
            g2d.setColor(Color.RED);
            g2d.setFont(GlobaleKonstanten.FONT.deriveFont(Font.BOLD));
        }
        g2d.drawString(resourceManager.getText("stars") + " \t" + String.valueOf(sterneGetippt) + " / "
                + String.valueOf(sterne), 30, 221);
        validate();
        repaint();
    }

}
