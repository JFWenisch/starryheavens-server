package infrastructure;

import infrastructure.constants.GlobaleKonstanten;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;

/**
 * Helperklasse zur Resourcenverwaltung
 *
 * @author Lars Schlegelmilch
 */
public class ResourceManager {



    private Properties propertyfile;
    private Properties userpropertyFile;
    private Locale language;
    private String strUser;
public ResourceManager()
{
	 new ResourceManager("user");
}
    public ResourceManager(String strName) 
    {
    	GlobaleKonstanten.USER_PROPERTIES=new File(GlobaleKonstanten.DEFAULT_CONF_SAVE_DIR, strName+".properties");
    	strUser=strName;
    	propertyfile = new Properties();
        try {
            propertyfile.load(getProperties());
        } catch (IOException e) {
            e.printStackTrace();
        }
        userpropertyFile = new Properties();
        try {
            userpropertyFile.load(getUserProperties(strName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Aender die Spracheinstellung des ResourceManagers, sodass auf Properties
     * der jeweiligen Sprache zurueckgegriffen wird
     *
     * @param locale Sprache/Land
     */
    public void setLanguage(Locale locale) {
        if (locale == null) {
            String localeString = (String) userpropertyFile.get("user.language");
            if (localeString.equals(Locale.GERMAN.toString())) {
                locale = Locale.GERMAN;
            } else if (localeString.equals(Locale.ENGLISH.toString())) {
                locale = Locale.ENGLISH;
            } else {
                locale = Locale.GERMAN;
            }
        }
        language = locale;
        try {
            propertyfile.load(getProperties(language));
        } catch (IOException e) {
            e.printStackTrace();
        }
        userpropertyFile.put("user.language", locale.toString());
        saveUserProperties();

    }
    public void setScore(String strScore, String strPuzzle) {
     
    	String strProperty = userpropertyFile.getProperty(strPuzzle);
    	if(strProperty!=null)
    	{
    		if(Integer.valueOf(strScore)> Integer.valueOf(strProperty))
    		{
    			userpropertyFile.put(strPuzzle, strScore);
    			  saveUserProperties();
    			  System.out.println("Score "+strScore+ " fuer"+"StrPuzzle gespeichert[USER:"+strUser+"]");
    		}
    	}
    	else
    	{
    		userpropertyFile.put(strPuzzle, strScore);
			  saveUserProperties();
			  System.out.println("Score "+strScore+ " fuer"+"StrPuzzle gespeichert[USER:"+strUser+"]");
    	}
     
      

    }

    /**
     * Sichert die Userproperties
     */
    private void saveUserProperties() {
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(new File(GlobaleKonstanten.DEFAULT_CONF_SAVE_DIR,strUser+".properties"));
            userpropertyFile.store(stream, "User.properties");
//            System.out.println(" Benutzereinstellungen  gesichert");
            
        } catch (IOException e) {
            System.out.println("Neue Benutzereinstellungen konnten nicht gesichert werden.");
        }
        finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                System.out.println("Neue Benutzereinstellungen konnten nicht gesichert werden.");
            }
        }
    }

    /**
     * Getter fuer aktuelle Spracheinstellung
     */
    public Locale getLanguage() {
        return language;
    }

    /**
     * Gibt den Text aus der Propertiesdatei zurueck
     *
     * @param key Schluessel des Textes
     * @return Text zum Schluessel
     */
    public String getText(String key) {
        return propertyfile.getProperty(key);
    }

    public void setAvatar(URL value) {
        userpropertyFile.put("user.avatar", new File(value.getFile()).getName());
        saveUserProperties();
    }

    public URL getUserAvatar() {
        String avatarName = (String) userpropertyFile.get("user.avatar");
        if (avatarName == null || getAvatarURL(avatarName) == null) {
            return getAvatarURL("spielinfo_w1.jpg");
        }
        return getAvatarURL(avatarName);
    }

    /**
     * Gibt die Properties-Datei(Standard: Deutsch) im Package
     * src/main/resources/infrastructure/constants zurueck
     *
     * @return InputStream der Properties-Datei
     */
    private InputStream getProperties() {
        return getClass().getResourceAsStream("text.properties");
    }

    /**
     * Gibt die Properties-Datei zu einer bestimmten Sprache im Package
     * src/main/resources/infrastructure/constants zurueck - (Standard: Deutsch)
     *
     * @param locale Sprache/Land
     * @return Propertiesdatei als InputStream
     */
    private InputStream getProperties(Locale locale) {
        if (locale.getLanguage().equals(Locale.GERMAN.getLanguage())) {
            return getClass().getResourceAsStream("text.properties");
        }
        if (locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
            return getClass().getResourceAsStream("text_en.properties");
        }
        // Default Deutsch
        return getClass().getResourceAsStream("text.properties");
    }

    /**
     * Gibt die Userspezifischen Properties zurueck
     *
     * @return Userspezifische Properties
     */
    private InputStream getUserProperties(String strName) {
        InputStream propertiesFile = null;
        try {
            propertiesFile = new FileInputStream
                    (new File(GlobaleKonstanten.DEFAULT_CONF_SAVE_DIR, strName+".properties"));
        } catch (FileNotFoundException e) {
         System.out.println("Neuer USER: "+strName);
            saveUserProperties();
            try {
				propertiesFile = new FileInputStream
				        (new File(GlobaleKonstanten.DEFAULT_CONF_SAVE_DIR, strName+".properties"));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
     
        }
        return propertiesFile;
    }

    /**
     * Gibt die URL einer Grafik im Package
     * src/main/resources/infrastructure/graphics zurueck
     *
     * @param graphicName Dateiname
     * @return URL der Grafik
     */
    public URL getGraphicURL(String graphicName) {
        return this.getClass().getResource("graphics/" + graphicName);
    }

    /**
     * Gibt die URL einer Avatar-Grafik im Package
     * src/main/resources/infrastructure/graphics/avatar zurueck
     *
     * @param avatarName Dateiname
     * @return URL des Avatars
     */
    public URL getAvatarURL(String avatarName) {
        return this.getClass().getResource("graphics/avatar/" + avatarName);
    }

    /**
     * Gibt das ImageIcon eines Avatar-Grafik-Previews im Package
     * src/main/resources/infrastructure/graphics/avatar zurueck
     *
     * @param avatarName Dateiname
     * @return ImageIcon des AvatarPreviews
     */
    public ImageIcon getAvatarPreviewImageIcon(String avatarName) {
        avatarName = avatarName.replace(".", "_preview.");
        return new ImageIcon(this.getClass().getResource("graphics/avatar/" + avatarName));
    }

    /**
     * Gibt die URL eines Icons im Package
     * src/main/resources/infrastructure/graphics/icons zurueck
     *
     * @param iconName Dateiname
     * @return URL des Icons
     */
    public URL getIconURL(String iconName) {
        return getClass().getResource("graphics/icons/" + iconName);
    }

    /**
     * Gibt das ImageIcon anhand des iconNames aus dem Package
     * src/main/resources/infrastructure/graphics/icons zurueck
     *
     * @param iconName Dateiname
     * @return Icon als ImageIcon
     */
    public ImageIcon getImageIcon(String iconName) {
        return new ImageIcon(getClass().getResource(
                "graphics/icons/" + iconName));
    }

    /**
     * Gibt das BufferedImage anhand des iconnamens aus dem Package
     * src/main/resources/infrastructure/graphics/icons zurueck
     *
     * @param imageName Dateiname
     * @return Icon Bild als BufferedImage
     * @throws IOException Wenn die Datei nicht gefunden wird.
     */
    public BufferedImage getIconAsBufferedImage(String imageName)
            throws IOException {
        return ImageIO.read(getClass().getResource(
                "graphics/icons/" + imageName));
    }

    /**
     * Gibt die URL eines Icons im Package
     * src/main/resources/infrastructure/graphics/icons zurueck
     *
     * @param iconName Dateiname
     * @return URL des Icons
     */
    @Deprecated
    public URL getIcon(String iconName) {
        return getClass().getResource("graphics/icons/" + iconName);
    }

    /**
     * Gibt die URL einer Grafik im Package
     * src/main/resources/infrastructure/graphics zurueck
     *
     * @param graphicName Dateiname
     * @return URL der Grafik
     */
    @Deprecated
    public URL getGraphic(String graphicName) {
        return this.getClass().getResource("graphics/" + graphicName);
    }



	public static ResourceManager get(String strUser) {
		// TODO Auto-generated method stub
		return new ResourceManager(strUser);
	}
	public static ResourceManager get() {
		// TODO Auto-generated method stub
		return new ResourceManager("user");
	}
	public void addStars(int countSterne) 
	{
		addCountToProperty("stars",countSterne);
     
		
	}
	public void addKills(int countKills) 
	{
		addCountToProperty("kills",countKills);
     
		
	}
	
	private void addCountToProperty(String strProperty,int iCount)
	{
		String strMyProperty = userpropertyFile.getProperty(strProperty);
		if(strMyProperty!=null)
    	{
    		int iNewCount=iCount+ Integer.valueOf(strMyProperty);
    		{
    			userpropertyFile.put(strProperty, String.valueOf(iNewCount));
    			  saveUserProperties();
    			  System.out.println(strProperty+" "+strMyProperty + " | "+strProperty+" new"+ iNewCount+" gespeichert[USER:"+strUser+"]");
    		}
    	}
    	else
    	{
    		userpropertyFile.put(strProperty, String.valueOf(iCount));
			  saveUserProperties();
			  System.out.println("No "+strProperty+" !"+" Got new "+iCount+ " fuer"+" saved[USER:"+strUser+"]");
    	}
	}
	public String getKills()
	{
		if(userpropertyFile.getProperty("kills")==null)
			addKills(0);
		return userpropertyFile.getProperty("kills");
	}
	public String getRanking()
	{
		 if(userpropertyFile.getProperty("rank")!=null)
			return userpropertyFile.getProperty("rank");
		 return "NOT RANKED";
	}
	public String getStars()
	{
		if(userpropertyFile.getProperty("stars")==null)
			addStars(0);
			
		return userpropertyFile.getProperty("stars");
	}
	public void setRanking(int iOutputCounter) {
		userpropertyFile.put("rank", String.valueOf(iOutputCounter));
		  saveUserProperties();
	
		
	}
	public String getPassword() {
		String strText =userpropertyFile.getProperty("PASSWORD");
		if(strText.equals("")| strText==null)
			strText="REGISTER";
		return strText;
	}
	public void setPassword(String text) {
		userpropertyFile.put("PASSWORD", text);
		saveUserProperties();
	}

}
