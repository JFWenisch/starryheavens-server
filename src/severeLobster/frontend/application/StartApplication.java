package severeLobster.frontend.application;

import infrastructure.ResourceManager;
import infrastructure.components.AudioPlayer;
import infrastructure.components.FTPConnector;
import severeLobster.backend.spiel.SternenSpielApplicationBackend;
import severeLobster.frontend.view.MainView;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Locale;

/**
 * 
 * @author Jean-Fabian Wenisch
 * @version 1.0
 */
public class StartApplication extends JFrame implements Runnable {

    private final ResourceManager resourceManager = ResourceManager.get();

    /**
     * Die einzige Backend Instanz fuers gesamte Spiel. Wird in alle Klassen
     * weitergegeben, die die Methoden brauchen. Bleibt durchgehend gleich. Wird
     * niemals null.
     */
    private final SternenSpielApplicationBackend backend;

    /**
     * Startet das Programm(startet Splaschreen und startet Initialisierung)
     * 
     * @param args
     *            - werden momentan nicht verwendet
     * @author fwenisch
     * @version 1.0 08.10.2012
     */
    private static Thread splahscreen;

    public static void main(String[] args) {
        final SternenSpielApplicationBackend backend = SternenSpielApplicationBackend.getInstance();
        splahscreen = new Thread(new StartApplication(backend, null));
        splahscreen.start();
    }

    public static void restart(Locale locale) {
        /* Alten Thread beenden */
        splahscreen.interrupt();
        final SternenSpielApplicationBackend backend = SternenSpielApplicationBackend.getInstance();

        splahscreen = new Thread(new StartApplication(backend, locale));
        Locale.setDefault(locale);
        JComponent.setDefaultLocale(locale);
        splahscreen.start();
    }

    public StartApplication(final SternenSpielApplicationBackend backend, Locale locale) {
        if (null == backend) {
            throw new NullPointerException("Backend ist null");
        }
        resourceManager.setLanguage(locale);
        Locale.setDefault(resourceManager.getLanguage());
        JComponent.setDefaultLocale(resourceManager.getLanguage());
        this.backend = backend;
    }

    /**
     * Liefert das Backend des Spiels - niemals null.
     * 
     * @return Das Backend des Spiels - niemals null.
     */
    public SternenSpielApplicationBackend getBackend() {
        return this.backend;
    }

    /**
     * Initialisiert Mainframe & laedt Ressourcen bevor das Mainframe gestartet
     * wird
     * 
     * @author fwenisch
     * @version 1.0 07.10.2012
     */
    @Override
    public void run() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        Thread SC_MAIN = null;
        setSize(550, 360);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setVisible(true);

        try {
            SC_MAIN = new Thread(new MainFrame(getBackend()));
            MainView.ftpConnector = new FTPConnector("ftp.strato.de",
                    "user@sternenhimmel-deluxe.de", "12YXasdfg", 21);
            
            Thread.sleep(2000);
            AudioPlayer.playBackground("Sascha Ende-Imagefilm001.wav");
            MainView.ftpConnector.connect();
            
            SC_MAIN.start();
           
        } catch (Exception e) {
            dispose();
            e.printStackTrace();
        } finally {
            dispose();
         //  

        }
    }

    /**
     * Wird ueberschrieben um Splash anzuzeigen
     * 
     * @author fwenisch
     * @version 1.0 08.10.2012
     */
    public void paint(Graphics g) {
        Image sImage = getToolkit().getImage(
                resourceManager.getGraphicURL("Splashscreen.jpg"));
        g.drawImage(sImage, 0, 0, this);
        Font myFont = new Font("Arial", Font.PLAIN, 12);
        g.setFont(myFont);
        g.setColor(Color.YELLOW);
        g.drawString(
                resourceManager.getText("splashscreen.text.1") + " "
                        + System.getProperty("user.name") + " "
                        + resourceManager.getText("splashscreen.text.2"), 5,
                280);
    }

}
