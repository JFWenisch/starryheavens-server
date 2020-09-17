package severeLobster.frontend.view;

import infrastructure.ResourceManager;
import infrastructure.constants.enums.PfeilrichtungEnumeration;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Factory Implementation fuer das Simple Icon Paket.
 * 
 * @author LKleiber
 * 
 */
public class SimpleIconFactory extends IconFactory {

    private static final SimpleIconFactory INSTANCE = new SimpleIconFactory();

    private final ResourceManager resourceManager = ResourceManager.get();

    /**
     * Quelle:
     * http://www.iconarchive.com/show/colobrush-icons-by-eponas-deeway/system
     * -star-icon.html
     */
    protected ImageIcon sternIcon;
    /**
     * Quelle:
     * http://www.iconarchive.com/show/button-icons-by-deleket/Button-Cancel
     * -icon.html
     */
    protected ImageIcon ausschlussIcon;
    /**
     * Quelle:
     * http://www.iconarchive.com/show/soft-scraps-icons-by-deleket/Button
     * -Blank-Blue-icon.html
     */
    protected ImageIcon blankIcon;

    /**
     * Quelle:
     * http://www.iconarchive.com/show/soft-scraps-icons-by-deleket/Button
     * -Download-icon.html
     */
    protected ImageIcon pfeilSouthIcon;
    protected ImageIcon pfeilSouthWestIcon;
    protected ImageIcon pfeilWestIcon;
    protected ImageIcon pfeilNorthWestIcon;
    protected ImageIcon pfeilNorthIcon;
    protected ImageIcon pfeilNorthEastIcon;
    protected ImageIcon pfeilEastIcon;
    protected ImageIcon pfeilSouthEastIcon;

    protected ImageIcon pfeilSouthIcon_deaktivert;
    protected ImageIcon pfeilSouthWestIcon_deaktivert;
    protected ImageIcon pfeilWestIcon_deaktivert;
    protected ImageIcon pfeilNorthWestIcon_deaktivert;
    protected ImageIcon pfeilNorthIcon_deaktivert;
    protected ImageIcon pfeilNorthEastIcon_deaktivert;
    protected ImageIcon pfeilEastIcon_deaktivert;
    protected ImageIcon pfeilSouthEastIcon_deaktivert;

    protected SimpleIconFactory() {

        sternIcon = resourceManager.getImageIcon("Stern_Popup.png");
        ausschlussIcon = resourceManager.getImageIcon("AusschlussIcon24.png");
        blankIcon = resourceManager.getImageIcon("Blank_Popup.png");

        final BufferedImage pfeilNorthBuffImage;
        final BufferedImage pfeilNorthEastBuffImage;
        try {
            pfeilNorthBuffImage = resourceManager
                    .getIconAsBufferedImage("Pfeil_0_Popup.png");
            pfeilNorthEastBuffImage = resourceManager
                    .getIconAsBufferedImage("Pfeil_45_Popup.png");
        } catch (IOException e) {
            e.printStackTrace();
            // Wenn das fehlschlaegt ist was kaputt
            throw new IllegalStateException(
                    resourceManager.getText("exception.simple.icon.factory.cannot.load.all.pictures"));
        }

        pfeilNorthIcon = new ImageIcon(pfeilNorthBuffImage);
        pfeilEastIcon = new ImageIcon(
                getRotatedImageOf(pfeilNorthBuffImage, 90));
        pfeilWestIcon = new ImageIcon(getRotatedImageOf(pfeilNorthBuffImage,
                270));
        pfeilSouthIcon = new ImageIcon(getRotatedImageOf(pfeilNorthBuffImage,
                180));
        pfeilNorthWestIcon = new ImageIcon(getRotatedImageOf(
                pfeilNorthEastBuffImage, 270));
        pfeilSouthWestIcon = new ImageIcon(getRotatedImageOf(
                pfeilNorthEastBuffImage,
                180));
        pfeilSouthEastIcon = new ImageIcon(getRotatedImageOf(
                pfeilNorthEastBuffImage, 90));
        pfeilNorthEastIcon = new ImageIcon(getRotatedImageOf(
                pfeilNorthEastBuffImage, 0));

    }

    public static SimpleIconFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public Icon sternIcon() {
        return sternIcon;
    }

    @Override
    public Icon ausschlussIcon() {
        return ausschlussIcon;
    }

    @Override
    public Icon blankIcon() {
        return blankIcon;
    }

    @Override
    public Icon pfeilIcon(PfeilrichtungEnumeration richtung) {
        if (richtung == null) {
            return ausschlussIcon;
        }
        if (richtung == PfeilrichtungEnumeration.SUED) {
            return pfeilSouthIcon;
        }
        if (richtung == PfeilrichtungEnumeration.SUEDWEST) {
            return pfeilSouthWestIcon;
        }
        if (richtung == PfeilrichtungEnumeration.WEST) {
            return pfeilWestIcon;
        }
        if (richtung == PfeilrichtungEnumeration.NORDWEST) {
            return pfeilNorthWestIcon;
        }
        if (richtung == PfeilrichtungEnumeration.NORD) {
            return pfeilNorthIcon;
        }
        if (richtung == PfeilrichtungEnumeration.NORDOST) {
            return pfeilNorthEastIcon;
        }
        if (richtung == PfeilrichtungEnumeration.OST) {
            return pfeilEastIcon;
        }
        if (richtung == PfeilrichtungEnumeration.SUEDOST) {
            return pfeilSouthEastIcon;
        }
        return ausschlussIcon;
    }

    @Override
    public Icon disabledPfeilIcon(PfeilrichtungEnumeration richtung) {
        if (richtung == null) {
            return ausschlussIcon;
        }
        if (richtung == PfeilrichtungEnumeration.SUED) {
            return pfeilSouthIcon_deaktivert;
        }
        if (richtung == PfeilrichtungEnumeration.SUEDWEST) {
            return pfeilSouthWestIcon_deaktivert;
        }
        if (richtung == PfeilrichtungEnumeration.WEST) {
            return pfeilWestIcon_deaktivert;
        }
        if (richtung == PfeilrichtungEnumeration.NORDWEST) {
            return pfeilNorthWestIcon_deaktivert;
        }
        if (richtung == PfeilrichtungEnumeration.NORD) {
            return pfeilNorthIcon_deaktivert;
        }
        if (richtung == PfeilrichtungEnumeration.NORDOST) {
            return pfeilNorthEastIcon_deaktivert;
        }
        if (richtung == PfeilrichtungEnumeration.OST) {
            return pfeilEastIcon_deaktivert;
        }
        if (richtung == PfeilrichtungEnumeration.SUEDOST) {
            return pfeilSouthEastIcon_deaktivert;
        }
        return ausschlussIcon;
    }


    /***
     * Ganz dreist geklaut.
     * http://www.tutorials.de/java/238054-bild-drehen.html#post1241082
     * 
     * @param src
     * @param degrees
     * @return
     */
    protected static BufferedImage getRotatedImageOf(BufferedImage src,
            double degrees) {
        AffineTransform affineTransform = AffineTransform.getRotateInstance(
                Math.toRadians(degrees), src.getWidth() / 2,
                src.getHeight() / 2);

        // TODO
        // Quickfix
        int imageType = src.getType();
        if (imageType == 0) {
            imageType = 6;
        }
        BufferedImage rotatedImage = new BufferedImage(src.getWidth(),
                src.getHeight(), imageType);
        Graphics2D g = (Graphics2D) rotatedImage.getGraphics();
        g.setTransform(affineTransform);
        g.drawImage(src, 0, 0, null);
        return rotatedImage;
    }

}
