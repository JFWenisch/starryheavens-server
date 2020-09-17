package severeLobster.frontend.view;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

class OnlineTableModel implements TableModel{
	private Vector Spiele = new Vector();
	private Vector listeners = new Vector();

	public void updateData(  )
	{
		
		int index = Spiele.size();
		for(int i=0;i<MainView.ftpConnector.files.length;i++)
		{
			Spiele.add( MainView.ftpConnector.files[i].getName() );
		}

		// Jetzt werden alle Listeners benachrichtigt

		// Zuerst ein Event, "neue Row an der Stelle index" herstellen
		TableModelEvent e = new TableModelEvent( this, index, index, 
				TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT );

		// Nun das Event verschicken
		for( int i = 0, n = listeners.size(); i<n; i++ ){
			((TableModelListener)listeners.get( i )).tableChanged( e );
		}
	}


	public int getColumnCount() {
		return 4;
	}


	public int getRowCount() {
		return Spiele.size();
	}

	// Die Titel der einzelnen Columns
	public String getColumnName(int column) 
	{
		switch( column ){
		case 0: return "Spielname";
		case 1: return "Schwierigkeitsgrad";
		case 2: return "Felder";
		case 3: return "Ersteller";
		default: return null;
		}
	}

	// Der Wert der Zelle (rowIndex, columnIndex)
	public Object getValueAt(int rowIndex, int columnIndex) 
	{
		String spielName = (String) Spiele.get( rowIndex );
		String[] strColumString = new String[4];

		for( int iTemp=0;iTemp<4;iTemp++)
		{
			strColumString[iTemp] = spielName.substring(0,spielName.indexOf('-'));
			if(iTemp<3)
				spielName=spielName.substring(spielName.indexOf('-')+1,spielName.length());

		}

		switch( columnIndex ){

		case 0: return strColumString[0];
		case 1: return strColumString[1];
		case 2: return strColumString[2];
		case 3:return strColumString[3]; 
		default: return strColumString;
		}
	}

	// Eine Angabe, welchen Typ von Objekten in den Columns angezeigt werden soll
	public Class getColumnClass(int columnIndex) {
		switch( columnIndex ){
		case 0: return String.class;
		case 1: return String.class;
		case 2: return String.class;
		case 3: return String.class;
		default: return null;
		}   
	}

	public void addTableModelListener(TableModelListener l) {
		listeners.add( l );
	}
	public void removeTableModelListener(TableModelListener l) {
		listeners.remove( l );
	}


	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// nicht beachten
	}
	public class MySelectionListener implements ListSelectionListener
	{

		JTable table;

		public MySelectionListener(JTable table) {
			this.table = table;
		}

		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting()) {
				System.out.println(table.getSelectedRow());
			}
		}
	}
}



