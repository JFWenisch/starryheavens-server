package severeLobster.frontend.view;

import infrastructure.ResourceManager;
import infrastructure.components.BalloonTipManager;
import infrastructure.components.Koordinaten;
import infrastructure.constants.GlobaleKonstanten;
import infrastructure.constants.enums.SchwierigkeitsgradEnumeration;
import severeLobster.backend.spiel.SolvingStrategy;
import severeLobster.backend.spiel.SternenSpielApplicationBackend;
import severeLobster.frontend.application.MainFrame;
import severeLobster.frontend.dialogs.SpielfeldGroessenDialog;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * View des Editiermodus
 *
 * @author Lars Schlegelmilch
 */
public class EditiermodusView extends JPanel {

	private final ResourceManager resourceManager = ResourceManager.get();
	private final SternenSpielApplicationBackend backend;
	private final JButton loesungswegBtn;

	public EditiermodusView(SternenSpielApplicationBackend backend, final SpielfeldDarstellung spielfeldDarstellung) {
		setLayout(null);
		this.backend = backend;
		setOpaque(false);
		setPreferredSize(new Dimension(200, 500));
		setVisible(true);
		JLabel loesungswegLabel = new JLabel(
				resourceManager.getText("check.loesungsweg.btn.text"));
		loesungswegLabel.setForeground(Color.YELLOW);
		loesungswegLabel.setSize(80, 50);
		loesungswegLabel.setOpaque(false);
		loesungswegLabel.setLocation(15, 288);
		loesungswegLabel.setVisible(true);
		loesungswegBtn = new JButton(resourceManager.getImageIcon("checkLoesungsweg.png"));
		loesungswegBtn.setSize(53, 53);
		loesungswegBtn.setMargin(new Insets(1, 1, 1, 1));
		loesungswegBtn.setFocusable(false);
		loesungswegBtn.setOpaque(false);
		loesungswegBtn.setLocation(90, 288);
		loesungswegBtn.setVisible(true);

		loesungswegBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				SolvingStrategy strategy = EditiermodusView.this.backend.getSpiel().getSpielfeld().loesungswegUeberpruefen();
				if (strategy.getNotUnique() != null) {
					for (Koordinaten koordinaten : strategy.getNotUnique()) {
						spielfeldDarstellung.highlightSpielstein(koordinaten.getX(), koordinaten.getY(), Color.RED);
					}
				}
				if (strategy.getErrors() != null) {
					for (Koordinaten koordinaten : strategy.getErrors()) {
						spielfeldDarstellung.highlightSpielstein(koordinaten.getX(), koordinaten.getY(), Color.RED);
					}
				}
				new BalloonTipManager(loesungswegBtn, strategy
						)
				.showBalloonTip();
			}
		});

		add(loesungswegLabel);
		add(loesungswegBtn);

		JLabel groesseAendernLabel = new JLabel(
				resourceManager.getText("change.size.btn.text"));
		groesseAendernLabel.setForeground(Color.YELLOW);
		groesseAendernLabel.setSize(80, 50);
		groesseAendernLabel.setOpaque(false);
		groesseAendernLabel.setLocation(15, 380);
		groesseAendernLabel.setVisible(true);

		JButton groesseAendernBtn = new JButton(resourceManager.getImageIcon("groesser.png"));
		groesseAendernBtn.setMargin(new Insets(1, 1, 1, 1));
		groesseAendernBtn.setFocusable(false);
		groesseAendernBtn.setSize(53, 53);
		groesseAendernBtn.setOpaque(false);
		groesseAendernBtn.setLocation(90, 380);
		groesseAendernBtn.setVisible(true);

		groesseAendernBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Koordinaten koordinaten = SpielfeldGroessenDialog
						.show(MainFrame.frame);
				if (koordinaten != null) {
					EditiermodusView.this.backend.aendereSpielfeldGroesse(
							koordinaten.getX(), koordinaten.getY());
				}
			}
		});
		JButton zumHauptmenubtn = new JButton(resourceManager.getImageIcon("Hauptmenu_small.png"));
		zumHauptmenubtn.setMargin(new Insets(1, 1, 1, 1));
		zumHauptmenubtn.setFocusable(false);
		zumHauptmenubtn.setSize(188,88);
		zumHauptmenubtn.setOpaque(false);
		zumHauptmenubtn.setLocation(5, 450);
		zumHauptmenubtn.setVisible(true);

		zumHauptmenubtn.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				MainFrame.mainPanel.addMenuPanel();

			};
		});
		add(groesseAendernLabel);
		add(groesseAendernBtn);
		add(zumHauptmenubtn);
	}

	public JButton getLoesungswegBtn() {
		return loesungswegBtn;
	}


	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		/**
		 * Anfang Test von TrackingControllView
		 */
		int sterne = backend.getSpiel().getSpielfeld().countSterne();
		SchwierigkeitsgradEnumeration schwierigkeitsgrad = backend.getSpiel()
				.getSchwierigkeitsgrad();

		Image sImage = getToolkit().getImage(
				resourceManager.getGraphicURL("editview.jpg"));
		/**
		 * Ende Test von TrackingControllView
		 */
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.drawImage(sImage, 0, 0, this);
		g2d.setFont(GlobaleKonstanten.FONT);
		g2d.setColor(Color.BLACK);

		g2d.drawString(
				resourceManager.getText("star.count") + " \t"
						+ String.valueOf(sterne), 30, 170);
		g2d.drawString(resourceManager.getText("actual.difficulty"), 30, 220);
		g2d.drawString(schwierigkeitsgrad.toString(), 30, 239);

		g2d.setColor(Color.YELLOW);
		g2d.setFont(GlobaleKonstanten.FONT.deriveFont(Font.BOLD));
		g2d.drawString(resourceManager.getText("puzzle.edit.title"), 30, 70);

		validate();
		repaint();
	}

}
