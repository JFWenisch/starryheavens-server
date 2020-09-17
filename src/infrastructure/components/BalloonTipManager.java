package infrastructure.components;

import infrastructure.ResourceManager;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.ModernBalloonStyle;
import net.java.balloontip.utils.TimingUtils;
import severeLobster.backend.spiel.SolvingStrategy;

import javax.swing.JComponent;
import java.awt.Color;

/**
 * BalloonTip für die Rückmeldung eines lösbaren Puzzles
 *
 * @author Lars Schlegelmilch
 */
public class BalloonTipManager {

    private final ResourceManager resourceManager = ResourceManager.get();
    private BalloonTip balloonTip;

    /**
     * Erstellt einen BalloonTip
     *
     * @param strategy Objekt zum ueberpruefen, ob Spielfeld eindeutig loesbar ist oder nicht?
     */
    public BalloonTipManager(JComponent owner, SolvingStrategy strategy) {

        // Spiel ist loesbar
        if (strategy.isSolvable() && strategy.isUnique()) {
            balloonTip = new BalloonTip(owner, resourceManager.getText("puzzle.solvable"));
            balloonTip.setStyle(new ModernBalloonStyle(10, 10, Color.WHITE, new Color(153, 255, 153), Color.GREEN));
            balloonTip.setCloseButton(null);

        }
        // Spiel nicht eindeutig loesbar
        else if (strategy.isSolvable() && !strategy.isUnique()) {
            balloonTip  = new BalloonTip(owner, resourceManager.getText("puzzle.not.unique.solvable"));
            balloonTip.setCloseButton(null);
            balloonTip.setStyle(new ModernBalloonStyle(10, 10, Color.WHITE, new Color(255, 99, 71), Color.RED));
        }
        else {
            balloonTip  = new BalloonTip(owner, resourceManager.getText("puzzle.not.solvable"));
            balloonTip.setCloseButton(null);
            balloonTip.setStyle(new ModernBalloonStyle(10, 10, Color.WHITE, new Color(255, 99, 71), Color.RED));
        }
    }

    /**
     * Zeigt den BalloonTip zeitlich begrenzt an
     */
    public void showBalloonTip() {
        TimingUtils.showTimedBalloon(balloonTip , 3000);
    }
}
