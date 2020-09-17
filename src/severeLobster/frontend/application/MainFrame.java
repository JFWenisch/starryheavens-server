/*
 * SC_MAIN.java
 *
 * Created on 17.10.2010, 20:21:14
 */

package severeLobster.frontend.application;

import infrastructure.ResourceManager;
import infrastructure.components.AudioPlayer;
import infrastructure.components.BalloonTipManager;
import infrastructure.components.PuzzleView;
import infrastructure.components.SpielView;
import infrastructure.constants.GlobaleKonstanten;
import infrastructure.exceptions.LoesungswegNichtEindeutigException;
import severeLobster.backend.spiel.SolvingStrategy;
import severeLobster.backend.spiel.Spiel;
import severeLobster.backend.spiel.SternenSpielApplicationBackend;
import severeLobster.frontend.dialogs.AboutDialog;
import severeLobster.frontend.dialogs.AvatarAendernDialog;
import severeLobster.frontend.dialogs.ExitDialog;
import severeLobster.frontend.dialogs.LoadGamePreview;
import severeLobster.frontend.dialogs.LoadPuzzlePreview;
import severeLobster.frontend.dialogs.NewGamePreview;
import severeLobster.frontend.dialogs.SpracheAendernDialog;
import severeLobster.frontend.view.MainView;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import com.thoughtworks.xstream.XStream;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Locale;

/**
 * Initialisiert Grafiken
 * 
 * @author Jean-Fabian Wenisch, Lars Schlegelmilch
 */

public class MainFrame extends JMenuBar implements Runnable {
    public JMenu jm_Spiel;
    public JMenu jm_Editieren;
    public JMenu jm_Optionen;
    public JMenu jm_Extras;
    public JMenu jm_Hilfe;
    private static JMenuItem itemSave;
    private static JMenuItem itemSaveAs;
    private static JMenuItem puzzleSave;
    private static JMenuItem puzzleSaveAs;
    private static JMenuItem puzzleFreigeben;
    private static JMenuItem puzzleCheck;
    private static JMenuItem optionenFadenkreuz;
    private static JFileChooser loadGameChooser;
    private static JFileChooser newGameChooser;
    private static JFileChooser loadPuzzleChooser;
    private JFileChooser saveGameChooser;
    private JFileChooser savePuzzleChooser;
    public static JFrame frame;
    public static MainView mainPanel;
    private static Point m_Windowlocation;
    public static JLabel jlOnlineSpiele = new JLabel();
    private final ResourceManager resourceManager = ResourceManager.get();

    /**
     * Initialisiert das Menue
     * 
     * @author Jean-Fabian Wenisch
     * @version 1.0 06.12.2010
     */
    public MainFrame(final SternenSpielApplicationBackend backend)
            throws IOException {
        // ////////////////////////////////////////////////////////////////////////////////////////////////
        /*
         * Frame wird erzeugt
         */
        // ////////////////////////////////////////////////////////////////////////////////////////////////
        mainPanel = new MainView(backend);
        frame = new JFrame("Sternenhimmel Deluxe");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setIconImage(getToolkit().getImage(
                resourceManager.getGraphicURL("icons/SternIcon128.png")));
        frame.setSize(GlobaleKonstanten.MINIMUM_APP_SIZE);
        frame.setMinimumSize(GlobaleKonstanten.MINIMUM_APP_SIZE);
        frame.setLocationRelativeTo(null);
        frame.setBackground(Color.BLACK);
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

        // ////////////////////////////////////////////////////////////////////////////////////////////////
        /*
         * JMenue wird mit allen Eintraegen erzeugt Actionlistener fuer die
         * Menueeintraege wird hinzugefuegt
         */
        // ////////////////////////////////////////////////////////////////////////////////////////////////
        jm_Spiel = new JMenu(resourceManager.getText("spiel.menu.text"));
        jm_Editieren = new JMenu(resourceManager.getText("editieren.menu.text"));
      //  jm_Extras = new JMenu(resourceManager.getText("extras.menu.text"));
        jm_Optionen = new JMenu(resourceManager.getText("optionen.menu.text"));
        jm_Hilfe = new JMenu(resourceManager.getText("hilfe.menu.text"));
        m_Windowlocation = new Point();
        ActionListener menuAction = new ActionListener() {
            public void actionPerformed(ActionEvent event) {

                if (event.getActionCommand().equals(
                        resourceManager.getText("neues.spiel.text"))) {
                    neuesSpielOeffnen();
                }
                if (event.getActionCommand().equals(
                        resourceManager.getText("load.text"))) {
                    loadSpiel();
                }
                if (event.getActionCommand().equals(
                        resourceManager.getText("save.text"))) {
                    Spiel spiel = mainPanel.getCurrentSpiel();
                    try {
                        if (spiel.getSaveName() == null) {
                            spielSpeichernUnter();
                        } else {
                            mainPanel.getBackend().saveCurrentSpielTo(
                                    spiel.getSaveName());
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (event.getActionCommand().equals(
                        resourceManager.getText("save.as.text"))) {
                    try {
                        spielSpeichernUnter();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (event.getActionCommand().equals(
                        resourceManager.getText("exit.text"))) {
                    spielBeenden();
                }
                if (event.getActionCommand().equals(
                        resourceManager.getText("puzzle.erstellen"))) {
                    mainPanel.addSpielErstellenPanel();
                    controlEditierMenue(true);
                }
                if (event.getActionCommand().equals(
                        resourceManager.getText("load.puzzle"))) {
                    int result = loadPuzzleChooser.showOpenDialog(frame);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        loadPuzzle();
                    }
                }
                if (event.getActionCommand().equals(
                        resourceManager.getText("save.puzzle"))) {
                    Spiel spiel = mainPanel.getCurrentSpiel();
                    try {
                        if (spiel.getSaveName() == null) {
                            puzzleSpeichernUnter();
                        } else {
                            mainPanel.getBackend().saveCurrentPuzzleTo(
                                    spiel.getSaveName());
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (event.getActionCommand().equals(
                        resourceManager.getText("save.as.puzzle"))) {
                    try {
                        puzzleSpeichernUnter();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (event.getActionCommand().equals("Mute / Unmute")) {
                    AudioPlayer.mute();
                }
                if (event.getActionCommand().equals(resourceManager.getText("puzzle.freigeben"))) 
                {
                	
                	  try {
						puzzleSpeichernUnter();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                    String savename = mainPanel.getCurrentSpiel().getSaveName();
                    if (savename == null) {
                        JOptionPane
                                .showMessageDialog(
                                        frame,
                                        resourceManager
                                                .getText("mainFrame.freigabe.speichern"),
                                        resourceManager
                                                .getText("mainFrame.freigabe.speichern.title"),
                                        JOptionPane.ERROR_MESSAGE);
                    } else {
                        try {
                           XStream xstream= mainPanel.getBackend().saveCurrentPuzzleTo(savename);
                            SolvingStrategy strategy = mainPanel.getBackend().puzzleFreigeben(savename);
                            if (strategy.isSolvable() && strategy.isUnique()) {
                            JOptionPane
                                    .showMessageDialog(
                                            frame,
                                            resourceManager
                                                    .getText("mainFrame.freigabe.freigegeben"),
                                            resourceManager
                                                    .getText("mainFrame.freigabe.freigegeben.title"),
                                            JOptionPane.INFORMATION_MESSAGE);
                            if (MainView.ftpConnector.isOnline()) {
                                int reply = javax.swing.JOptionPane
                                        .showConfirmDialog(
                                                frame,
                                                resourceManager
                                                        .getText("mainFrame.upload.body"),
                                                resourceManager
                                                        .getText("mainFrame.upload.title"),
                                                javax.swing.JOptionPane.YES_NO_OPTION);
                                if (reply == JOptionPane.YES_OPTION) {
                                    mainPanel.getBackend().uploadPuzzle(xstream);
                                }
                            }
                            } else if (strategy.isSolvable() && !strategy.isUnique()){
                                JOptionPane.showMessageDialog(MainFrame.frame, resourceManager.getText("mainFrame.freigabe.not.unique.failed")
                                        , resourceManager.getText("mainFrame.freigabe.failed.title"), JOptionPane.ERROR_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(MainFrame.frame, resourceManager.getText("mainFrame.freigabe.failed")
                                        , resourceManager.getText("mainFrame.freigabe.failed.title"), JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (LoesungswegNichtEindeutigException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (event.getActionCommand().equals(
                        resourceManager.getText("check.puzzle"))) {
                    new BalloonTipManager(mainPanel.getEditiermodusView().getLoesungswegBtn(),
                            mainPanel.getBackend().getSpiel().getSpielfeld().loesungswegUeberpruefen())
                    .showBalloonTip();
                }

                if (event.getActionCommand().equals(
                        resourceManager.getText("download.puzzles"))) {
                    mainPanel.addOnlineSpielAuswahlPanel();
                }

                if (event.getActionCommand().equals(
                        resourceManager.getText("optionen.sprache"))) {
                    spracheAendern();
                }
                if (event.getActionCommand().equals(
                        resourceManager.getText("optionen.fadenkreuz"))) {
                    if (optionenFadenkreuz.isSelected()) {
                        backend.setFadenkreuzAktiviert(true);
                    } else {
                        backend.setFadenkreuzAktiviert(false);
                    }

                }
                if (event.getActionCommand().equals(
                        resourceManager.getText("optionen.avatar"))) {
                    avatarAendern();
                }
                if (event.getActionCommand().equals(
                        resourceManager.getText("hilfe.about"))) {
                    AboutDialog.showAboutDialog(frame);
                }
                if (event.getActionCommand().equals(
                        resourceManager.getText("hilfe.user.manual"))) {
                    anleitungOeffnen();
                }
                if (event.getActionCommand().equals(
                        resourceManager.getText("hilfe.kontakt"))) {
                    kontaktMail();
                }
            }
        };

        JMenuItem item;

        jm_Spiel.add(item = new JMenuItem(resourceManager
                .getText("neues.spiel.text")));
        item.addActionListener(menuAction);
        item.setIcon(resourceManager.getImageIcon("neu.png"));
    //    jm_Spiel.add(item = new JMenuItem(resourceManager.getText("load.text")));
     //   item.addActionListener(menuAction);
       // item.setIcon(resourceManager.getImageIcon("load.png"));
        jm_Spiel.add(item = new JMenuItem(resourceManager
                .getText("download.puzzles")));
        item.addActionListener(menuAction);
        item.setIcon(resourceManager.getImageIcon("download.png"));
        //  jm_Spiel.add(itemSave = new JMenuItem(resourceManager.getText("save.text")));
       // itemSave.setEnabled(false);
      //  itemSave.addActionListener(menuAction);
   
       // itemSave.setIcon(resourceManager.getImageIcon("save.png"));
       // jm_Spiel.add(itemSaveAs = new JMenuItem(resourceManager.getText("save.as.text")));
      //  itemSaveAs.setIcon(resourceManager.getImageIcon("saveas.png"));
      //  itemSaveAs.setEnabled(false);
     //   itemSaveAs.addActionListener(menuAction);
        jm_Spiel.add(item = new JMenuItem(resourceManager.getText("exit.text")));
        item.addActionListener(menuAction);
        item.setIcon(resourceManager.getImageIcon("ende.png"));
        jm_Editieren.add(item = new JMenuItem(resourceManager
                .getText("puzzle.erstellen")));
        item.addActionListener(menuAction);
        item.setIcon(resourceManager.getImageIcon("edit.png"));
    /*   // jm_Editieren.add(item = new JMenuItem(resourceManager.getText("load.puzzle")));
       // item.addActionListener(menuAction);
       // item.setIcon(resourceManager.getImageIcon("load.png"));
        jm_Editieren.add(puzzleSave = new JMenuItem(resourceManager
                .getText("save.puzzle")));
        puzzleSave.setEnabled(false);
        puzzleSave.setIcon(resourceManager.getImageIcon("save.png"));
        puzzleSave.addActionListener(menuAction);
        jm_Editieren.add(puzzleSaveAs = new JMenuItem(resourceManager
                .getText("save.as.puzzle")));
        puzzleSaveAs.setIcon(resourceManager.getImageIcon("saveas.png"));
        puzzleSaveAs.setEnabled(false);
        puzzleSaveAs.addActionListener(menuAction);
          */
        jm_Editieren.add(puzzleFreigeben = new JMenuItem(resourceManager
                .getText("puzzle.freigeben")));
        puzzleFreigeben.setEnabled(false);
      
        puzzleFreigeben.setIcon(resourceManager.getImageIcon("freigeben.png"));
        puzzleFreigeben.addActionListener(menuAction);
        jm_Editieren.add(puzzleCheck = new JMenuItem(resourceManager
                .getText("check.puzzle")));
        puzzleCheck.setEnabled(false);
        puzzleCheck.setIcon(resourceManager.getImageIcon("check.png"));
        puzzleCheck.addActionListener(menuAction);

      
        jm_Optionen.add(optionenFadenkreuz = new JCheckBoxMenuItem(
                resourceManager.getText("optionen.fadenkreuz")));
        optionenFadenkreuz.setSelected(backend.istFadenkreuzAktiviert());
        optionenFadenkreuz.addActionListener(menuAction);
        jm_Optionen.add(item = new JMenuItem(resourceManager
                .getText("optionen.sprache")));
        item.addActionListener(menuAction);
        item.setIcon(resourceManager.getImageIcon("language.png"));
        jm_Optionen.add(item = new JMenuItem(resourceManager
                .getText("optionen.avatar")));
        item.addActionListener(menuAction);
        item.setIcon(resourceManager.getImageIcon("avatar.png"));
        jm_Optionen.add(item = new JMenuItem("Mute / Unmute"));
        item.setIcon(resourceManager.getImageIcon("AusschlussIcon24.png"));
        item.addActionListener(menuAction);
        jm_Hilfe.add(item = new JMenuItem(resourceManager
                .getText("hilfe.user.manual")));
        item.addActionListener(menuAction);
        item.setIcon(resourceManager.getImageIcon("docu.png"));
        jm_Hilfe.add(item = new JMenuItem(resourceManager
                .getText("hilfe.kontakt")));
        item.addActionListener(menuAction);
        item.setIcon(resourceManager.getImageIcon("kontakt.png"));
        jm_Hilfe.add(item = new JMenuItem(resourceManager
                .getText("hilfe.about")));
        item.addActionListener(menuAction);
        item.setIcon(resourceManager.getImageIcon("about.png"));

      //  jm_Spiel.insertSeparator(3);

       // jm_Editieren.insertSeparator(4);

        add(jm_Spiel);
        add(jm_Editieren);
       // add(jm_Extras);
        add(jm_Optionen);
        add(jm_Hilfe);

        jlOnlineSpiele.setEnabled(false);
        add(jlOnlineSpiele, BorderLayout.EAST);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                m_Windowlocation.x = e.getX();
                m_Windowlocation.y = e.getY();
            }
        });

        frame.setJMenuBar(this);
    }  
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        this.setBackground(Color.black);
        this.setForeground(Color.yellow);

    }
    private void avatarAendern() {
        URL result = new AvatarAendernDialog(frame).showDialog();
        if (result != null) {
            mainPanel.getSpielInfoView().changeAvatar(result);
        }
    }

    private void spracheAendern() {
        SpracheAendernDialog dialog = new SpracheAendernDialog(frame);
        Locale result = dialog.showDialog();
        if (result != null) {
            frame.dispose();
            StartApplication.restart(result);
        }
    }

    /**
     * Laed ein Puzzle aus vorhandenen Spieldateien in das Spielfeld
     */
    private void loadPuzzle() {
        String spielname = loadPuzzleChooser.getSelectedFile().getName()
                .replace("." + GlobaleKonstanten.PUZZLE_DATEITYP, "");
        try {
            mainPanel.addSpielErstellenPanel(spielname);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Laed ein Spiel aus vorhandenen Spieldateien in das Spielfeld
     */
    public static void loadSpiel() {
        int result = loadGameChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            String spielname = loadGameChooser.getSelectedFile().getName()
                    .replace("." + GlobaleKonstanten.SPIELSTAND_DATEITYP, "");
            mainPanel.addSpielmodusPanelAndStartSpiel(spielname, true);
        }
    }

    private void kontaktMail() {
        Desktop desktop = Desktop.getDesktop();
        String message = "mailto:entwickler@nirako.de?subject=Kontakt%20Sternenhimmel%20Deluxe";
        URI uri = URI.create(message);
        try {
            if (!Desktop.isDesktopSupported()) {
                throw new IOException();
            }
            desktop.mail(uri);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    resourceManager.getText("mainFrame.mail.error"),
                    resourceManager.getText("mainFrame.mail.error.title"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void anleitungOeffnen() {
        Desktop desktop = Desktop.getDesktop();
        File anleitungFile = new File(GlobaleKonstanten.DEFAULT_DOC_SAVE_DIR,
                "Sternenhimmel DELUXE.html");
        try {
            if (!Desktop.isDesktopSupported()) {
                throw new IOException();
            }
            desktop.open(anleitungFile.getCanonicalFile());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    resourceManager.getText("mainFrame.doku.error"),
                    resourceManager.getText("mainFrame.doku.error.title"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void neuesSpielOeffnen() {
        int result = newGameChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            mainPanel.addSpielmodusPanelAndStartSpiel(
                    (newGameChooser.getSelectedFile().getName().replace("."
                            + GlobaleKonstanten.PUZZLE_DATEITYP, "")), false);
            controlSpielMenue(true);
            controlEditierMenue(false);
        }
    }

    /**
     * Aktiviert oder Deaktivert die Controleigenschaften des Spiels
     * 
     * @param enabled
     *            Menueoptionen aktivieren / deaktivieren
     */
    public static void controlSpielMenue(boolean enabled) {
        if (itemSave != null && itemSaveAs != null) {
            itemSave.setEnabled(enabled);
            itemSaveAs.setEnabled(enabled);
        }
    }

    /**
     * Aktiviert oder Deaktivert die Controleigenschaften des Editiermodus
     * 
     * @param enabled
     *            Menueoptionen aktivieren / deaktivieren
     */
    public static void controlEditierMenue(boolean enabled) 
    {
        if (puzzleCheck != null && puzzleFreigeben != null) 
        {
        
            puzzleCheck.setEnabled(enabled);
            puzzleFreigeben.setEnabled(enabled);
        }
    }

    /**
     * Oeffnet den FileChooser, um das Spiel unter einem gewissen Namen
     * abzuspeichern
     */
    private void spielSpeichernUnter() throws IOException {
        int result = saveGameChooser.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            Spiel spiel = mainPanel.getCurrentSpiel();
            String filename = saveGameChooser.getSelectedFile().getName()
                    .replace("." + GlobaleKonstanten.SPIELSTAND_DATEITYP, "");
            spiel.setSaveName(filename);
            mainPanel.getBackend().saveCurrentSpielTo(filename);
        }
    }

    /**
     * Oeffnet den FileChooser, um das Puzzle unter einem gewissen Namen
     * abzuspeichern
     */
    private void puzzleSpeichernUnter() throws IOException {
        int result = savePuzzleChooser.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            Spiel spiel = mainPanel.getCurrentSpiel();
            String filename = savePuzzleChooser.getSelectedFile().getName()
                    .replace("." + GlobaleKonstanten.PUZZLE_DATEITYP, "");
            spiel.setSaveName(filename);
            mainPanel.getBackend().saveCurrentPuzzleTo(filename);
        }
    }

    /**
     * Oeffnet den Beenden-Dialog
     */
    public void spielBeenden() {
        int result = ExitDialog.show(frame);
        if (ExitDialog.beenden_option.equals(ExitDialog.options[result])) {
            frame.dispose();
            System.exit(0);
        }
    }

    /**
     * Frame wird initialisiert & Hauptpanel wird hinzugefuegt Ausserdem werden
     * Mouselistener hinzugefuegt mit denen sich das Frame verschieben laesst
     * 
     * @author fwenisch
     * @version 1.0 08.10.2012
     */
    private void init() {
        loadGameChooser = initFileChooser(
                GlobaleKonstanten.DEFAULT_SPIEL_SAVE_DIR,
                new SpielView(),
                new FileNameExtensionFilter(resourceManager
                        .getText("load.dialog.extension.description"),
                        GlobaleKonstanten.SPIELSTAND_DATEITYP),
                resourceManager.getText("load.dialog.text"),
                resourceManager.getText("load.dialog.title"));
        loadGameChooser.setAccessory(new LoadGamePreview(loadGameChooser));

        loadPuzzleChooser = initFileChooser(
                GlobaleKonstanten.DEFAULT_PUZZLE_SAVE_DIR,
                new SpielView(),
                new FileNameExtensionFilter(resourceManager
                        .getText("puzzle.load.dialog.extension.description"),
                        GlobaleKonstanten.PUZZLE_DATEITYP),
                resourceManager.getText("puzzle.load.dialog.text"),
                resourceManager.getText("puzzle.load.dialog.title"));
        loadPuzzleChooser
                .setAccessory(new LoadPuzzlePreview(loadPuzzleChooser));

        newGameChooser = initFileChooser(
                GlobaleKonstanten.DEFAULT_FREIGEGEBENE_PUZZLE_SAVE_DIR,
                new PuzzleView(),
                new FileNameExtensionFilter(resourceManager
                        .getText("new.dialog.extension.description"),
                        GlobaleKonstanten.PUZZLE_DATEITYP),
                resourceManager.getText("new.dialog.text"),
                resourceManager.getText("new.dialog.title"));
        newGameChooser.setAccessory(new NewGamePreview(newGameChooser));

        saveGameChooser = initFileChooser(
                GlobaleKonstanten.DEFAULT_SPIEL_SAVE_DIR,
                new SpielView(),
                new FileNameExtensionFilter(resourceManager
                        .getText("save.dialog.extension.description"),
                        GlobaleKonstanten.SPIELSTAND_DATEITYP),
                resourceManager.getText("save.dialog.text"),
                resourceManager.getText("save.dialog.title"));

        savePuzzleChooser = initFileChooser(
                GlobaleKonstanten.DEFAULT_PUZZLE_SAVE_DIR,
                new SpielView(),
                new FileNameExtensionFilter(resourceManager
                        .getText("puzzle.save.dialog.extension.description"),
                        GlobaleKonstanten.PUZZLE_DATEITYP),
                resourceManager.getText("puzzle.save.dialog.text"),
                resourceManager.getText("puzzle.save.dialog.title"));

        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                spielBeenden();
            }
        });
    }

    private JFileChooser initFileChooser(File defaultDir,
            FileSystemView fileSystemView, FileFilter fileFilter,
            String approveButtonText, String dialogTitle) {
        JFileChooser fileChooser = new JFileChooser(defaultDir);
        fileChooser.setFileSystemView(fileSystemView);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(fileFilter);
        fileChooser.setApproveButtonText(approveButtonText);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogTitle(dialogTitle);
        fileChooser.setMultiSelectionEnabled(false);
        return fileChooser;
    }

    /**
     * Beim starten des Hauptthreads wird die Methode <init()> Aufgerufen in der
     * die Gesamte GUI aufgebaut werden muss
     * 
     * @author fwenisch
     * @version 08.10.2012
     */
    @Override
    public void run() {
        frame.setVisible(true);
        init();
    }
}
