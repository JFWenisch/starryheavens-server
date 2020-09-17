package severeLobster.frontend.view;

import infrastructure.ResourceManager;
import infrastructure.components.FTPConnector;
import infrastructure.constants.GlobaleKonstanten;
import severeLobster.frontend.application.MainFrame;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Erstellt das Ein Panel auf die Preview zu sehen ist und ein Button zum
 * Starten des Puzzles
 *
 * @author fwenisch
 * @date 10.11.2012
 */
public class OnlineTable extends JTable implements ListSelectionListener 
{
    private final ResourceManager resourceManager = ResourceManager.get();
    private String strPuzzleName = null;
    private JPanel 	previewPanel= null;
 
    OnlineTable(OnlineTableModel model, JPanel previewPanel) 
    {
    	super( model );
    	this.previewPanel=previewPanel;
          model.updateData();
          setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setForeground(Color.YELLOW);
 setBackground( new Color(87,71,140));
       setAutoCreateRowSorter(true);
    }
    
      

    public void valueChanged(ListSelectionEvent e) {
    
		if (!e.getValueIsAdjusting())
		{
		
			String strPuzzleName="";
			strPuzzleName+=(getValueAt(getSelectedRow(), 0));
			strPuzzleName+="-"+(getValueAt(getSelectedRow(), 1));
			strPuzzleName+="-"+(getValueAt(getSelectedRow(), 2));
			strPuzzleName+="-"+(getValueAt(getSelectedRow(), 3));
			strPuzzleName+="-.puz";
			MainView.ftpConnector.getFile(previewPanel, strPuzzleName);
			if(this.strPuzzleName!=null)
			{
			 File file = new File(GlobaleKonstanten.DEFAULT_FREIGEGEBENE_PUZZLE_SAVE_DIR,  this.strPuzzleName);
			 file.delete();
			 System.out.println(this.strPuzzleName+" entfernt");
			}
			 this.strPuzzleName=strPuzzleName;
			refreshPreviewPanel();
		}
	repaint();
    }
    private void refreshPreviewPanel()
    {
    	previewPanel.removeAll();
    	previewPanel.add(new PuzzlePreviewView(strPuzzleName.substring(0,strPuzzleName.length()-4)));
    	previewPanel.validate();
    	previewPanel.repaint();
    }
    
}
