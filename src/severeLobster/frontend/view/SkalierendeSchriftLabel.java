package severeLobster.frontend.view;

import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;

/**
 * Label, das die Schriftgroesse an die gezeichnete Gesamtgroesse von sich
 * selbst anpasst.
 * 
 * @author Lutz Kleiber
 * 
 */
public class SkalierendeSchriftLabel extends JLabel {

    private int lastKnownBreite;
    private int lastKnownHoehe;

    @Override
    public void paint(final Graphics graphics) {
        super.paint(graphics);
        if (this.lastKnownBreite != getWidth()
                || this.lastKnownHoehe != getHeight()) {
            this.lastKnownBreite = getWidth();
            this.lastKnownHoehe = getHeight();

            final int flaechenInhalt = lastKnownBreite * lastKnownHoehe;
            final int neueSchriftGroesse = getSchriftGroesseFuerFlaechenInhalt(flaechenInhalt);
            // System.out.println(flaechenInhalt);

            final Font currentFont = this.getFont();
            final Font newFont = new Font(currentFont.getName(),
                    currentFont.getStyle(), neueSchriftGroesse);
            this.setFont(newFont);
        }
    }

    private static int getSchriftGroesseFuerFlaechenInhalt(
            final int flaechenInhalt) {
        if (flaechenInhalt >= 3000) {
            return 30;
        }
        if (flaechenInhalt >= 2500) {
            return 25;
        }
        if (flaechenInhalt >= 2025) {
            return 21;
        }
        if (flaechenInhalt >= 961) {
            return 17;
        }
        if (flaechenInhalt >= 300) {
            return 15;
        }
        return 14;
    }

}
