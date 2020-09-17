package severeLobster.frontend.view;

import infrastructure.ResourceManager;
import infrastructure.constants.enums.PfeilrichtungEnumeration;

import javax.swing.Icon;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * IconFactory mit den neuen Icons. Funktioniert aber noch net.
 * 
 * @author Lutz Kleiber
 * 
 */
public class AdvancedDynamicallyResizingIconFactory extends
        SimpleDynamicallyResizingIconFactory {

    private static final AdvancedDynamicallyResizingIconFactory INSTANCE = new AdvancedDynamicallyResizingIconFactory();
    private final ResourceManager resourceManager = ResourceManager.get();
    protected AdvancedDynamicallyResizingIconFactory() {

        final int defaultWidth = SimpleDynamicallyResizingIconFactory.DEFAULT_ICON_WIDTH;
        final int defaultHeight = SimpleDynamicallyResizingIconFactory.DEFAULT_ICON_HEIGHT;

        try {
            /* Nord Pfeile in verschiedenen Aufloesungen */
            final BufferedImage pfeilNord64 = ResourceManager.get()
                    .getIconAsBufferedImage("Pfeil_0_64.png");
            final BufferedImage pfeilNord128 = ResourceManager.get()
                    .getIconAsBufferedImage("Pfeil_0_128.png");
            final BufferedImage pfeilNord256 = ResourceManager.get()
                    .getIconAsBufferedImage("Pfeil_0_256.png");
            final BufferedImage pfeilNord512 = ResourceManager.get()
                    .getIconAsBufferedImage("Pfeil_0.png");

            /* Nord Ost Pfeile in verschiedenen Aufloesungen */
            final BufferedImage pfeilNordOst64 = ResourceManager.get()
                    .getIconAsBufferedImage("Pfeil_45_64.png");
            final BufferedImage pfeilNordOst128 = ResourceManager.get()
                    .getIconAsBufferedImage("Pfeil_45_128.png");
            final BufferedImage pfeilNordOst256 = ResourceManager.get()
                    .getIconAsBufferedImage("Pfeil_45_256.png");
            final BufferedImage pfeilNordOst512 = ResourceManager.get()
                    .getIconAsBufferedImage("Pfeil_45.png");

            /* DeaktivierteNord Pfeile in verschiedenen Aufloesungen */
            final BufferedImage pfeilNord64Deaktiviert = ResourceManager.get()
                    .getIconAsBufferedImage("Pfeil_0_64_deaktiviert.png");
            final BufferedImage pfeilNord128Deaktiviert = ResourceManager.get()
                    .getIconAsBufferedImage("Pfeil_0_128_deaktiviert.png");
            final BufferedImage pfeilNord256Deaktiviert = ResourceManager.get()
                    .getIconAsBufferedImage("Pfeil_0_256_deaktiviert.png");
            final BufferedImage pfeilNord512Deaktiviert = ResourceManager.get()
                    .getIconAsBufferedImage("Pfeil_0_deaktiviert.png");

            /* Deaktiverte Nord Ost Pfeile in verschiedenen Aufloesungen */
            final BufferedImage pfeilNordOst64Deaktiviert = ResourceManager.get()
                    .getIconAsBufferedImage("Pfeil_45_64_deaktiviert.png");
            final BufferedImage pfeilNordOst128Deaktiviert = ResourceManager.get()
                    .getIconAsBufferedImage("Pfeil_45_128_deaktiviert.png");
            final BufferedImage pfeilNordOst256Deaktiviert = ResourceManager.get()
                    .getIconAsBufferedImage("Pfeil_45_256_deaktiviert.png");
            final BufferedImage pfeilNordOst512Deaktiviert = ResourceManager.get()
                    .getIconAsBufferedImage("Pfeil_45_deaktiviert.png");

            /* Stern Icons in verschiedenen Aufloesungen */
            final BufferedImage stern64 = ResourceManager.get()
                    .getIconAsBufferedImage("Stern_64.png");
            final BufferedImage stern128 = ResourceManager.get()
                    .getIconAsBufferedImage("Stern_128.png");
            final BufferedImage stern256 = ResourceManager.get()
                    .getIconAsBufferedImage("Stern_256.png");
            final BufferedImage stern512 = ResourceManager.get()
                    .getIconAsBufferedImage("Stern.png");

            /* Ausschluss Icons in verschiedenen Aufloesungen */
            final BufferedImage ausschluss64 = ResourceManager.get()
                    .getIconAsBufferedImage("Ausschluss_64.png");
            final BufferedImage ausschluss128 = ResourceManager.get()
                    .getIconAsBufferedImage("Ausschluss_128.png");
            final BufferedImage ausschluss256 = ResourceManager.get()
                    .getIconAsBufferedImage("Ausschluss_256.png");
            final BufferedImage ausschluss512 = ResourceManager.get()
                    .getIconAsBufferedImage("Ausschluss.png");

            /* KeinStein Icons in verschiedenen Aufloesungen */
            final BufferedImage keinStein64 = ResourceManager.get()
                    .getIconAsBufferedImage("Blank_64.png");
            final BufferedImage keinStein128 = ResourceManager.get()
                    .getIconAsBufferedImage("Blank_128.png");
            final BufferedImage keinStein256 = ResourceManager.get()
                    .getIconAsBufferedImage("Blank_256.png");
            final BufferedImage keinStein512 = ResourceManager.get()
                    .getIconAsBufferedImage("Blank.png");

            /* Setze allgemeines pfeilNorthIcon als dynamisch skalierend */
            {
                final List<BufferedImage> pfeilNordImages = getRotatedImageListOf(
                        0, pfeilNord64, pfeilNord128, pfeilNord256,
                        pfeilNord512);
                pfeilNorthIcon = new DynamischSkalierendesIcon(pfeilNordImages,
                        defaultWidth, defaultHeight);
                final List<BufferedImage> pfeilNordImagesDeaktiviert = getRotatedImageListOf(
                        0, pfeilNord64Deaktiviert, pfeilNord128Deaktiviert, pfeilNord256Deaktiviert,
                        pfeilNord512Deaktiviert);
                pfeilNorthIcon_deaktivert = new DynamischSkalierendesIcon(pfeilNordImagesDeaktiviert,
                        defaultWidth, defaultHeight);
            }

            /* Setze allgemeines pfeilNorthEastIcon als dynamisch skalierend */
            {
                final List<BufferedImage> pfeilNordOstImages = getRotatedImageListOf(
                        0, pfeilNordOst64, pfeilNordOst128, pfeilNordOst256,
                        pfeilNordOst512);
                pfeilNorthEastIcon = new DynamischSkalierendesIcon(
                        pfeilNordOstImages, defaultWidth, defaultHeight);
                final List<BufferedImage> pfeilNordOstImagesDeaktiviert = getRotatedImageListOf(
                        0, pfeilNordOst64Deaktiviert, pfeilNordOst128Deaktiviert, pfeilNordOst256Deaktiviert,
                        pfeilNordOst512Deaktiviert);
                pfeilNorthEastIcon_deaktivert = new DynamischSkalierendesIcon(
                        pfeilNordOstImagesDeaktiviert, defaultWidth, defaultHeight);
            }

            /* Setze allgemeines pfeilEastIcon als dynamisch skalierend */
            {
                final List<BufferedImage> pfeilOstImages = getRotatedImageListOf(
                        90, pfeilNord64, pfeilNord128, pfeilNord256,
                        pfeilNord512);
                pfeilEastIcon = new DynamischSkalierendesIcon(pfeilOstImages,
                        defaultWidth, defaultHeight);

                final List<BufferedImage> pfeilOstImagesDeaktiviert = getRotatedImageListOf(
                        90, pfeilNord64Deaktiviert, pfeilNord128Deaktiviert, pfeilNord256Deaktiviert,
                        pfeilNord512Deaktiviert);
                pfeilEastIcon_deaktivert = new DynamischSkalierendesIcon(pfeilOstImagesDeaktiviert,
                        defaultWidth, defaultHeight);
            }

            /* Setze allgemeines pfeilSouthEastIcon als dynamisch skalierend */
            {
                final List<BufferedImage> pfeilSuedOstImages = getRotatedImageListOf(
                        90, pfeilNordOst64, pfeilNordOst128, pfeilNordOst256,
                        pfeilNordOst512);
                pfeilSouthEastIcon = new DynamischSkalierendesIcon(
                        pfeilSuedOstImages, defaultWidth, defaultHeight);
                final List<BufferedImage> pfeilSuedOstImagesDeaktiviert = getRotatedImageListOf(
                        90, pfeilNordOst64Deaktiviert, pfeilNordOst128Deaktiviert, pfeilNordOst256Deaktiviert,
                        pfeilNordOst512Deaktiviert);
                pfeilSouthEastIcon_deaktivert = new DynamischSkalierendesIcon(
                        pfeilSuedOstImagesDeaktiviert, defaultWidth, defaultHeight);
            }

            /* Setze allgemeines pfeilSouthIcon als dynamisch skalierend */
            {
                final List<BufferedImage> pfeilSuedImages = getRotatedImageListOf(
                        180, pfeilNord64, pfeilNord128, pfeilNord256,
                        pfeilNord512);
                pfeilSouthIcon = new DynamischSkalierendesIcon(pfeilSuedImages,
                        defaultWidth, defaultHeight);
                final List<BufferedImage> pfeilSuedImagesDeaktiviert = getRotatedImageListOf(
                        180, pfeilNord64Deaktiviert, pfeilNord128Deaktiviert, pfeilNord256Deaktiviert,
                        pfeilNord512Deaktiviert);
                pfeilSouthIcon_deaktivert = new DynamischSkalierendesIcon(pfeilSuedImagesDeaktiviert,
                        defaultWidth, defaultHeight);
            }

            /* Setze allgemeines pfeilSouthWestIcon als dynamisch skalierend */
            {
                final List<BufferedImage> pfeilSuedWestImages = getRotatedImageListOf(
                        180, pfeilNordOst64, pfeilNordOst128, pfeilNordOst256,
                        pfeilNordOst512);
                pfeilSouthWestIcon = new DynamischSkalierendesIcon(
                        pfeilSuedWestImages, defaultWidth, defaultHeight);
                final List<BufferedImage> pfeilSuedWestImagesDeaktiviert = getRotatedImageListOf(
                        180, pfeilNordOst64Deaktiviert, pfeilNordOst128Deaktiviert, pfeilNordOst256Deaktiviert,
                        pfeilNordOst512Deaktiviert);
                pfeilSouthWestIcon_deaktivert = new DynamischSkalierendesIcon(
                        pfeilSuedWestImagesDeaktiviert, defaultWidth, defaultHeight);
            }

            /* Setze allgemeines pfeilWestIcon als dynamisch skalierend */
            {
                final List<BufferedImage> pfeilWestImages = getRotatedImageListOf(
                        270, pfeilNord64, pfeilNord128, pfeilNord256,
                        pfeilNord512);
                pfeilWestIcon = new DynamischSkalierendesIcon(pfeilWestImages,
                        defaultWidth, defaultHeight);
                final List<BufferedImage> pfeilWestImagesDeaktiviert = getRotatedImageListOf(
                        270, pfeilNord64Deaktiviert, pfeilNord128Deaktiviert, pfeilNord256Deaktiviert,
                        pfeilNord512Deaktiviert);
                pfeilWestIcon_deaktivert = new DynamischSkalierendesIcon(pfeilWestImagesDeaktiviert,
                        defaultWidth, defaultHeight);
            }

            /* Setze allgemeines pfeilNorthWestIcon als dynamisch skalierend */
            {
                final List<BufferedImage> pfeilNordWestImages = getRotatedImageListOf(
                        270, pfeilNordOst64, pfeilNordOst128, pfeilNordOst256,
                        pfeilNordOst512);
                pfeilNorthWestIcon = new DynamischSkalierendesIcon(
                        pfeilNordWestImages, defaultWidth, defaultHeight);
                final List<BufferedImage> pfeilNordWestImagesDeaktiviert = getRotatedImageListOf(
                        270, pfeilNordOst64Deaktiviert, pfeilNordOst128Deaktiviert, pfeilNordOst256Deaktiviert,
                        pfeilNordOst512Deaktiviert);
                pfeilNorthWestIcon_deaktivert = new DynamischSkalierendesIcon(
                        pfeilNordWestImagesDeaktiviert, defaultWidth, defaultHeight);
            }

            /* Setze allgemeines SternIcon als dynamisch skalierend */
            {
                final List<BufferedImage> sternImages = getRotatedImageListOf(
                        0, stern64, stern128, stern256, stern512);
                sternIcon = new DynamischSkalierendesIcon(sternImages,
                        defaultWidth, defaultHeight);
            }

            /* Setze allgemeines ausschlussIcon als dynamisch skalierend */
            {
                final List<BufferedImage> ausschlussImages = getRotatedImageListOf(
                        0, ausschluss64, ausschluss128, ausschluss256,
                        ausschluss512);
                ausschlussIcon = new DynamischSkalierendesIcon(
                        ausschlussImages, defaultWidth, defaultHeight);
            }

            /* Setze allgemeines blankIcon als dynamisch skalierend */
            {
                final List<BufferedImage> blankImages = getRotatedImageListOf(
                        0, keinStein64, keinStein128, keinStein256,
                        keinStein512);
                blankIcon = new DynamischSkalierendesIcon(blankImages,
                        defaultWidth, defaultHeight);
            }

        } catch (IOException e) {
            e.printStackTrace();
            // Wenn das fehlschlaegt ist was kaputt
            throw new IllegalStateException(
                    resourceManager.getText("exception.cant.load.all.icons"));
        } catch (Exception e) {
            e.printStackTrace();
        }


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

    private static List<BufferedImage> getRotatedImageListOf(final int degree,
            BufferedImage... images) {
        final List<BufferedImage> result = new LinkedList<BufferedImage>();
        for (BufferedImage currentImage : images) {
            result.add(getRotatedImageOf(currentImage, degree));
        }

        return result;
    }

    public static AdvancedDynamicallyResizingIconFactory getInstance() {
        return INSTANCE;
    }
}
