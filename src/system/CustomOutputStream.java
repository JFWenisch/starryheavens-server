package system;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
 
import javax.swing.JTextArea;
 
/**
 * This class extends from OutputStream to redirect output to a JTextArrea
 * @author www.codejava.net
 *
 */
public class CustomOutputStream extends OutputStream {
    private JTextArea textArea;
    FileWriter writer;
    File file;
    public CustomOutputStream(JTextArea textArea) {
        this.textArea = textArea;
        file = new File("log.txt");
    
        
    }
     
    @Override
    public void write(int b) throws IOException {
        // redirects data to the text area
    //	Date zeitstempel = new Date();
    //	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        try {
    			writer = new FileWriter(file ,true);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        textArea.append(String.valueOf((char)b));
        // scrolls the text area to the end of data
        textArea.setCaretPosition(textArea.getDocument().getLength());
        // Text wird in den Stream geschrieben
        writer.write(String.valueOf((char)b));
        
        // Platformunabh‰ngiger Zeilenumbruch wird in den Stream geschrieben
       // writer.write(System.getProperty("line.separator"));
        writer.flush();
        
        // Schlieﬂt den Stream
        writer.close();
    }
}