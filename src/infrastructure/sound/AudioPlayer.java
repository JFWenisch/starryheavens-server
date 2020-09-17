package infrastructure.sound;

import infrastructure.GlobaleKonstanten;
import infrastructure.ResourceManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;



/**
 * @author fwenisch
 */
public abstract class AudioPlayer {
    /**
     * Spielt die Datei <strName> im SoundVerzeichnis ab
     *
     * @param strName
     * @throws UnsupportedAudioFileException
     * @throws IOException
     * @throws LineUnavailableException
     */
	private static Clip clip=null;
	private static boolean isMuted=false;
    public static void playBackground(String strName) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
    
   
    }
    
    public static void mute()
    {
    	if(isMuted)
    	{
    		clip.start();
    		isMuted=false;
    	}	
    	else
    	{
    		clip.stop();
        	isMuted=true;
    	}
    }

    /**
     * Spielt den Siegessound ab
     *
     * @throws UnsupportedAudioFileException
     * @throws IOException
     * @throws LineUnavailableException
     */
    public static void playWinSound() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
     
    }

}
