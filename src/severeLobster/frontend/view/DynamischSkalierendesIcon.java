package severeLobster.frontend.view;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.List;

import infrastructure.ResourceManager;

import javax.swing.ImageIcon;

/**
 * Icon Implementation, die das gezeichnete Icon dynamisch immer auf die Groesse
 * der Zielkomponente skaliert. Dabei koennen als Quelle mehrere Aufloesungen
 * desselben Bildes uebergeben werden, um den Skalierungsaufwand gering zu
 * halten.
 * 
 * 
 * @author Lutz Kleiber
 * 
 */
public class DynamischSkalierendesIcon extends ImageIcon {

    private final BufferedImageInVerschiedenenAufloesungen sourceImages;
    private final ResourceManager resourceManager = ResourceManager.get();
    /** Aendert sich je nach Ausmassen der Zielkomponente: */
    private ImageIcon scaledImageIcon;

    public DynamischSkalierendesIcon(final List<BufferedImage> bufferedImages,
            final int defaultWidth, final int defaultHeight) {

        this.sourceImages = new BufferedImageInVerschiedenenAufloesungen(
                bufferedImages);
        final BufferedImage naechstHoehereStartGroesse = sourceImages
                .getNaechstHoehereAufloesung(defaultWidth);

        /**
         * Sofort in default Groesse skalieren, damit getIconWidth() und
         * getIconHeight() kalkulierbare Werte zurueckgeben.
         */
        this.scaledImageIcon = new ImageIcon(getScaledInstance(
                naechstHoehereStartGroesse, defaultWidth, defaultHeight));
    }

    @Override
    public int getIconHeight() {
        return scaledImageIcon.getIconHeight();
    }

    @Override
    public int getIconWidth() {
        return scaledImageIcon.getIconWidth();
    }

    @Override
    public void paintIcon(Component targetComponent, Graphics g, int x, int y) {

        /*
         * Ausmasse von Zielkomponente auslesen und mit Ausmassen von
         * gespeichertem, skaliertem ImageIcon vergleichen:
         */
        int targetWidth = targetComponent.getWidth()-2;
        int targetHeigth = targetComponent.getHeight()-2;

        boolean groesseIstSchonIdentisch = (targetWidth == scaledImageIcon
                .getIconWidth() && targetHeigth == scaledImageIcon
                .getIconHeight());

        /*
         * Wenn aktuelle Groesse bereits Zielgroesse entspricht, direkt zeichnen
         * -> sollte nach dem ersten laden staendig vorkommen. Ansonsten
         * skalieren:
         */
        if (!groesseIstSchonIdentisch) {
            /*
             * x und y fuer neue Groesse anpassen, da das Icon sonst nach dem
             * ersten Skalieren verschoben gezeichnet wird. x und y muessen um
             * die haelfte des Zuwachses verringert werden, oder um die Haelfte
             * der Abnahme erhoeht werden:
             */
            /* Bei Verbreiterung: */
            if (targetWidth > getIconWidth()) {
                /* Zuwachs ausrechnen: */
                final int zuwachs = targetWidth - getIconWidth();
                final int halberZuwachs = zuwachs / 2;
                x = x - halberZuwachs;
            } else {
                /* bei Verschmaelerung */
                /* abnahme ausrechnen: */
                final int abnahme = getIconWidth() - targetWidth;
                final int halbeeabnahme = abnahme / 2;
                x = x + halbeeabnahme;
            }
            /* Bei Verlaengerung (vertikal) */
            if (targetHeigth > getIconHeight()) {
                /* Zuwachs ausrechnen: */
                final int zuwachs = targetHeigth - getIconHeight();
                final int halberZuwachs = zuwachs / 2;
                y = y - halberZuwachs;
            } else {
                /* Bei Verkuerzung: (vertikal) */
                /* Abnahme ausrechnen: */
                final int abnahme = getIconHeight() - targetHeigth;
                final int halbeeabnahme = abnahme / 2;
                y = y + halbeeabnahme;
            }

            /* Bild in naechsthoeherer Aufloesung zum skalieren laden */
            final BufferedImage naechstHoehereAufloesung = sourceImages
                    .getNaechstHoehereAufloesung(targetWidth);

            /*
             * Bild herunter skalieren und ImageIcon mit neu skaliertem Bild
             * erzeugen
             */
            this.scaledImageIcon = new ImageIcon(getScaledInstance(
                    naechstHoehereAufloesung, targetWidth, targetHeigth));
        }
        /* Hier hat scaledImage in jedem Fall die richtige Groesse */
        /* Wirkliches Zeichnen in Zielkomponente */
        scaledImageIcon.paintIcon(targetComponent, g, x, y);
    }

    private static BufferedImage getScaledInstance(
            final BufferedImage sourceImage, final int targetWidth,
            final int targetHeight) {
        // System.out.println("Skaliere Quellimage der Groesse: "
        // + sourceImage.getWidth() + "X" + sourceImage.getHeight()
        // + " herunter auf " + targetWidth + "X" + targetHeight);
        final int imageType;
        if (sourceImage.getTransparency() == Transparency.OPAQUE) {
            imageType = BufferedImage.TYPE_INT_RGB;
        } else {
            imageType = BufferedImage.TYPE_INT_ARGB;
        }

        final BufferedImage result = new BufferedImage(targetWidth,
                targetHeight, imageType);
        final Graphics2D g2 = result.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(sourceImage, 0, 0, targetWidth, targetHeight, null);
        g2.dispose();

        return result;
    }
}
