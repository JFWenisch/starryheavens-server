package severeLobster.frontend.view;

import infrastructure.ResourceManager;
import infrastructure.constants.enums.SpielmodusEnumeration;
import infrastructure.graphics.GraphicUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import severeLobster.backend.spiel.Spiel;
import severeLobster.backend.spiel.SternenSpielApplicationBackend;
import severeLobster.frontend.application.MainFrame;

/**
 * Erstellt das Ein Panel auf die Preview zu sehen ist und ein Button zum
 * Starten des Puzzles
 * 
 * @author fwenisch
 * @date 10.11.2012
 * 
 */
public class PuzzlePreviewView extends JPanel {
	private final ResourceManager resourceManager = ResourceManager.get();
	private String strPuzzleName = null;
	private final Image backgroundimage = getToolkit().getImage(
			resourceManager.getGraphicURL("Onlinebackground.jpg"));

	PuzzlePreviewView(String strPuzzleName) {
		JPanel SpielfeldInfo = new JPanel();
		JLabel spielfeldPreviewLabel = new JLabel();
		JPanel jpLeft = new JPanel();
		jpLeft.setOpaque(true);
		jpLeft.setLayout(new GridLayout(3, 0));
		jpLeft.setMaximumSize(new Dimension (250,400));

		try {
			this.strPuzzleName = strPuzzleName;
			SternenSpielApplicationBackend backend = SternenSpielApplicationBackend
					.getInstance();

			backend.startNewSpielFrom(strPuzzleName);

			Spiel spiel = backend.getSpiel();
			SpielfeldDarstellung spielfeldView = new SpielfeldDarstellung();
			spielfeldView.setAngezeigtesSpielfeld(spiel.getSpielfeld());
			spielfeldView.setSize(150, 150);
			BufferedImage bufferedImage = GraphicUtils
					.createComponentShot(spielfeldView);
			bufferedImage = GraphicUtils.getScaledIconImage(bufferedImage, 150,150);

			spielfeldPreviewLabel.setIcon(new ImageIcon(bufferedImage));
			/*
			 * 
			 * JLabel schwierigkeitsgradTitle = new JLabel(""); JLabel
			 * schwierigkeitsgradValue = new JLabel("");
			 * schwierigkeitsgradTitle.
			 * setText(resourceManager.getText("load.dialog.difficulty"));
			 * schwierigkeitsgradValue
			 * .setText(spiel.getSchwierigkeitsgrad().toString());
			 */

			JLabel jlName = new JLabel("<html>Spielname: "+ strPuzzleName.substring(0,strPuzzleName.indexOf('-'))+"<br>"+resourceManager.getText("difficulty") + " "+ spiel.getSchwierigkeitsgrad().toString()+"<br>"+resourceManager.getText("fields")+ " " + spiel.getSpielfeld().getHoehe()* spiel.getSpielfeld().getBreite()+"</html>");
			jlName.setForeground(Color.YELLOW);
			jlName.setFont(new Font("Verdana", 0, 13));

			/**
			 * Highscore aufbauen
			 */
			String[] strScores = new String[10];
			String[] strName = new String[10];
			String[] strUsers =spiel.score.get();
			for(int i =0;i < strUsers.length;i++)
				{
				if(strUsers[i]==null)
					strUsers[i]="  -  ";
				if(strUsers[i].equals(" -0"))
					strUsers[i]="  -  ";
				strName[i]=strUsers[i].substring(0,strUsers[i].indexOf("-"));
				strScores[i]=strUsers[i].substring(strUsers[i].indexOf("-")+1,strUsers[i].length());
				}
			String[][] rowData = {
					{ strName[0], strScores[0] },
					{ strName[1], strScores[1]},
					{ strName[2], strScores[2] },  
					{ strName[3], strScores[3] },
					{strName[4], strScores[4]} ,
					{strName[5], strScores[5] },
					{ strName[6],strScores[6] },
					{strName[7], strScores[7] },
					{ strName[8], strScores[8] },
					{strName[9], strScores[9]}
			};
			String[] columnNames = {
					"User", "Highscore"
			};

			JTable table = new JTable( rowData, columnNames );



			JButton jbSSpielen = new JButton(resourceManager.getText("play"));
			jbSSpielen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {

					try {
						MainFrame.mainPanel.addSpielmodusPanelAndStartSpiel(getSpielName(), false);
					} catch (Exception ex) {

					}
				}
			});
			jbSSpielen.setPreferredSize(new Dimension (300,20));
			jbSSpielen.setMaximumSize(new Dimension (300,20));
			jbSSpielen.setSize(new Dimension(300,20));
			SpielfeldInfo.setPreferredSize(new Dimension (500,400));
		
			jpLeft.add(spielfeldPreviewLabel);
			jpLeft.add(jlName);

			JScrollPane jscroll = new JScrollPane(table);
			jscroll.setOpaque(false);
			jscroll.setPreferredSize(new Dimension (400,200));
			table.setForeground(Color.YELLOW);
			table.setBackground( new Color(87,71,140));
			jscroll.setForeground(Color.YELLOW);
			jscroll.setBackground( new Color(87,71,140));
			SpielfeldInfo.add( jscroll );
			SpielfeldInfo.add(jbSSpielen);
			SpielfeldInfo.setOpaque(false);

		} catch (Exception e) {
			spielfeldPreviewLabel.setIcon(resourceManager
					.getImageIcon("Ausschluss_128.png"));
			SpielfeldInfo.add(new JLabel(resourceManager
					.getText("not.available")));
		}




		//jpLeft.add(jpMainInfo, BorderLayout.CENTER);
		jpLeft.setOpaque(false);
		this.add(jpLeft, BorderLayout.WEST);
		this.add(SpielfeldInfo, BorderLayout.CENTER);
		this.setPreferredSize(new Dimension(700, 400));
		this.setOpaque(false);
		this.setVisible(true);

	}

	private String getSpielName() {
		return strPuzzleName;
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Insets insets = getInsets();
		int x = insets.left;
		int y = insets.top;
		int width = getWidth() - x - insets.right;
		int height = getHeight() - y - insets.bottom;
		g.drawImage(backgroundimage, x, y, width, height, this);

	}
}
