package severeLobster.frontend.view;

import infrastructure.ResourceManager;
import infrastructure.components.AnAusAuswahl;
import severeLobster.frontend.application.MainFrame;
import severeLobster.frontend.controller.TrackingControllViewController;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Erstes Skelett fuer das Tracking Kontroll UI.
 * 
 * @author Lutz Kleiber
 */
public class TrackingControllView extends JPanel {
    private static final ResourceManager resourceManager = ResourceManager
            .get();
    private TrackingControllViewController currentController;
    private final JButton zurueckZumLetztenPunktBtn;
    private final JButton setzeTrackingPunktBtn;
    private final JButton zurueckZumFehlerBtn;
    private final JPanel anzahlTrackingPunktePanel;

    public TrackingControllView() {
        setBackground(Color.BLACK);
        setLayout(new FlowLayout(FlowLayout.CENTER));

        final AnAusAuswahl auswahl = new AnAusAuswahl(true);
        auswahl.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                zurueckZumLetztenPunktBtn.setVisible(!auswahl.isSelected());
                setzeTrackingPunktBtn.setVisible(!auswahl.isSelected());
                zurueckZumFehlerBtn.setVisible(auswahl.isSelected());
            }
        });
        this.add(auswahl);

        // Zurueck zum letzten Tracking punkt Button setzen
        {
            ImageIcon zurueckZumLetztenPunktIcon = ResourceManager.get()
                    .getImageIcon("PfeilLinks48.png");
            zurueckZumLetztenPunktBtn = new JButton(zurueckZumLetztenPunktIcon);
            zurueckZumLetztenPunktBtn.setToolTipText(resourceManager
                    .getText("tracking.backToLastTrackingPoint"));
            zurueckZumLetztenPunktBtn.setMargin(new Insets(1, 1, 1, 1));
            zurueckZumLetztenPunktBtn.setFocusable(false);
            zurueckZumLetztenPunktBtn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    getCurrentTrackingController()
                            .zurueckZumLetztenTrackingPunkt();
                    if (anzahlTrackingPunktePanel.getComponentCount() > 0) {
                        anzahlTrackingPunktePanel
                                .remove(anzahlTrackingPunktePanel
                                        .getComponentCount() - 1);
                    }
                }
            });
            zurueckZumLetztenPunktBtn.setVisible(false);
            zurueckZumLetztenPunktBtn.setOpaque(false);
            this.add(zurueckZumLetztenPunktBtn);
        }
        // Tracking Punkt Button setzen
        {
            ImageIcon trackingPunktIcon = ResourceManager.get().getImageIcon(
                    "SetzeTrackingPointIcon48.png");
            setzeTrackingPunktBtn = new JButton(trackingPunktIcon);
            setzeTrackingPunktBtn.setToolTipText(resourceManager
                    .getText("tracking.setPoint"));
            setzeTrackingPunktBtn.setMargin(new Insets(1, 1, 1, 1));
            setzeTrackingPunktBtn.setFocusable(false);
            setzeTrackingPunktBtn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    getCurrentTrackingController().setzeTrackingPunkt();
                    JLabel trackingIcon = new JLabel(resourceManager
                            .getImageIcon("tracking.png"));
                    anzahlTrackingPunktePanel.add(trackingIcon);
                }
            });
            setzeTrackingPunktBtn.setVisible(false);
            setzeTrackingPunktBtn.setOpaque(false);
            this.add(setzeTrackingPunktBtn);
        }
        // Zurueck zum Fehler Button setzen
        {
            ImageIcon zurueckZumFehler = ResourceManager.get().getImageIcon(
                    "DoppelPfeilLinks48.png");
            zurueckZumFehlerBtn = new JButton(zurueckZumFehler);
            zurueckZumFehlerBtn.setToolTipText(resourceManager
                    .getText("tracking.backToMistake"));
            zurueckZumFehlerBtn.setMargin(new Insets(1, 1, 1, 1));
            zurueckZumFehlerBtn.setFocusable(false);
            zurueckZumFehlerBtn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    getCurrentTrackingController().zurueckZumFehler();
                }
            });
            zurueckZumFehlerBtn.setOpaque(false);
            zurueckZumFehlerBtn.setVisible(true);
            this.add(zurueckZumFehlerBtn);
        }
        {
            anzahlTrackingPunktePanel = new JPanel(new GridBagLayout());
            anzahlTrackingPunktePanel.setPreferredSize(new Dimension(200, 40));
            anzahlTrackingPunktePanel.setOpaque(false);
            anzahlTrackingPunktePanel.setToolTipText("Gesetzte Trackingpunkte");
            this.add(anzahlTrackingPunktePanel);
        }
        JButton zumHauptmenubtn = new JButton(resourceManager.getImageIcon("Hauptmenu_small.png"));
		zumHauptmenubtn.setMargin(new Insets(1, 1, 1, 1));
		zumHauptmenubtn.setFocusable(false);
		zumHauptmenubtn.setSize(188, 88);
		zumHauptmenubtn.setOpaque(false);
		zumHauptmenubtn.setLocation(50, -30);
		zumHauptmenubtn.setVisible(true);

		zumHauptmenubtn.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				MainFrame.mainPanel.addMenuPanel();

			};
		});
		 this.add(zumHauptmenubtn);
    }

    private TrackingControllViewController getCurrentTrackingController() {
        if (null == currentController) {
            return TrackingControllViewController.NULL_OBJECT_INSTANCE;
        } else {
            return this.currentController;
        }

    }

    public void aktualisiereTrackingIcons(int anzahlTrackingPunkte) {
        anzahlTrackingPunktePanel.removeAll();
        for (int i = 0; i < anzahlTrackingPunkte; i++) {
        JLabel trackingIcon = new JLabel(resourceManager
                .getImageIcon("tracking.png"));
            anzahlTrackingPunktePanel.add(trackingIcon);
        }
    }

    /**
     * Wird vom TrackingControllViewController im Konstruktor aufgerufen.
     * 
     * @param controller
     */
    public void setTrackingControllViewController(
            final TrackingControllViewController controller) {
        this.currentController = controller;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image sImage = getToolkit().getImage(
                resourceManager.getGraphicURL("trackingview.jpg"));
        g.drawImage(sImage, 0, -1, this);
        validate();
        repaint();
    }
}
