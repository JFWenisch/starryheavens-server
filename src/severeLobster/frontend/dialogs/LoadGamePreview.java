package severeLobster.frontend.dialogs;

import infrastructure.GlobaleKonstanten;
import infrastructure.ResourceManager;
import infrastructure.constants.enums.SpielmodusEnumeration;
import infrastructure.graphics.GraphicUtils;
import severeLobster.backend.spiel.Spiel;
import severeLobster.frontend.view.SpielfeldDarstellung;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

/**
 * Preview-Anzeige beim Laden eines Spiels
 *
 * @author Lars Schlegelmilch
 */
public class LoadGamePreview extends JPanel implements PropertyChangeListener {

    private final ResourceManager resourceManager = ResourceManager.get();

    private JLabel schwierigkeitsgradValue = new JLabel();
    private JLabel schwierigkeitsgradTitle = new JLabel();
    private JLabel spielfeldPreviewLabel = new JLabel();
    private Spiel spiel;

    public LoadGamePreview(JFileChooser chooser) {
        setLayout(new BorderLayout());
        chooser.addPropertyChangeListener(this);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 0));
        schwierigkeitsgradValue.setFont(schwierigkeitsgradValue.getFont()
                .deriveFont(Font.BOLD));
        schwierigkeitsgradTitle.setHorizontalAlignment(SwingConstants.CENTER);
        schwierigkeitsgradTitle.setBorder(BorderFactory.createEmptyBorder(12,
                0, 0, 0));
        schwierigkeitsgradValue.setHorizontalAlignment(SwingConstants.CENTER);
        spielfeldPreviewLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel schwierigkeitsgradPanel = new JPanel(new BorderLayout());
        schwierigkeitsgradPanel.add(spielfeldPreviewLabel, BorderLayout.NORTH);
        schwierigkeitsgradPanel.add(schwierigkeitsgradTitle,
                BorderLayout.CENTER);
        schwierigkeitsgradPanel
                .add(schwierigkeitsgradValue, BorderLayout.SOUTH);

        add(schwierigkeitsgradPanel, BorderLayout.NORTH);
    }

    @Override
    public void propertyChange(PropertyChangeEvent changeEvent) {
        String changeName = changeEvent.getPropertyName();
        if (changeName.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
            File file = (File) changeEvent.getNewValue();
            if (file != null) {
                try {
                    spiel = Spiel
                            .loadSpiel(
                                    file.getName()
                                            .replace(
                                                    "."
                                                            + GlobaleKonstanten.SPIELSTAND_DATEITYP,
                                                    ""),
                                    SpielmodusEnumeration.SPIELEN);
                    SpielfeldDarstellung spielfeldView = new SpielfeldDarstellung();
                    spielfeldView.setAngezeigtesSpielfeld(spiel.getSpielfeld());
                    spielfeldView.setSize(200, 200);
                    BufferedImage bufferedImage = GraphicUtils
                            .createComponentShot(spielfeldView);
                    bufferedImage = GraphicUtils.getScaledIconImage(
                            bufferedImage, 100, 100);
                    spielfeldPreviewLabel.setIcon(new ImageIcon(bufferedImage));
                } catch (IOException e) {
                    System.out.println(resourceManager.getText("load.dialog.invalid.format"));
                }
                if (spiel != null) {
                    schwierigkeitsgradTitle.setText(resourceManager
                            .getText("load.dialog.difficulty"));
                    schwierigkeitsgradValue.setText(spiel
                            .getSchwierigkeitsgrad().toString());
                }
            } else {
                schwierigkeitsgradTitle.setText("");
                schwierigkeitsgradValue.setText("");
                spielfeldPreviewLabel.setIcon(null);
            }
        }
    }

    /**
     * Gibt das Spiel, aus dem Preview zurueck
     * 
     * @return Spiel aus Preview
     */
    public Spiel getSpiel() {
        return spiel;
    }
}
