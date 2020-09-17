package severeLobster.frontend.view;

import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.ImageIcon;

/**
 * Kontainer fuer ein ImageIcon in verschiedenen Aufloesungen.
 * 
 * @author Lutz Kleiber
 * 
 */
public class BufferedImageInVerschiedenenAufloesungen {

    /**
     * Verfuegbare Aufloesungen des Bildes. Bilder sind nach dem erstellen in
     * aufsteigender Aufloesung sortiert.
     */
    private final BufferedImage[] verschiedeneAufloesungen;

    public BufferedImageInVerschiedenenAufloesungen(
            final List<BufferedImage> gleichesBildInVerschiedenenAufloesungen) {
        if (null == gleichesBildInVerschiedenenAufloesungen) {
            throw new NullPointerException("Liste von BufferedImages ist null");
        }
        this.verschiedeneAufloesungen = new BufferedImage[gleichesBildInVerschiedenenAufloesungen
                .size()];
        for (int index = 0; index < verschiedeneAufloesungen.length; index++) {
            verschiedeneAufloesungen[index] = gleichesBildInVerschiedenenAufloesungen
                    .get(index);
        }
        sortiereVonNiedrigerZuHoherAufloesung(verschiedeneAufloesungen);
    }

    public BufferedImage getNaechstHoehereAufloesung(final int targetWidth) {

        BufferedImage result;
        int index = 0;
        do {
            result = verschiedeneAufloesungen[index];
            index++;
        } while ((result.getWidth() < targetWidth)
                && (index < verschiedeneAufloesungen.length));

        return result;
    }

    /**
     * Momentan als Bubblesort.
     * 
     * @param array
     */
    private static void sortiereVonNiedrigerZuHoherAufloesung(
            final BufferedImage[] array) {
        for (int n = array.length; n > 1; n = n - 1) {
            for (int i = 0; i < n - 1; i = i + 1) {
                if (array[i].getWidth() > array[i + 1].getWidth()) {
                    /* Tauschen: */
                    final BufferedImage temp = array[i];
                    array[i] = array[i + 1];
                    array[i + 1] = temp;
                }
            }
        }
    }
}
