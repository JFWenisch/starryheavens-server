package severeLobster.frontend.controller;

import severeLobster.backend.spiel.SternenSpielApplicationBackend;
import severeLobster.frontend.view.TrackingControllView;

/**
 * Verbindung zwischen Tracking UI und der Schnittstelle im Backend, ueber die
 * gesteuert werden kann.
 * 
 * @author LKleiber
 * 
 */
public class TrackingControllViewController {

    public static final TrackingControllViewController NULL_OBJECT_INSTANCE = new TrackingControllViewController() {

        public void setzeTrackingPunkt() {
            // Dummy
        }

        public void zurueckZumFehler() {
            // Dummy
        }

        public void zurueckZumLetztenTrackingPunkt() {
            // Dummy
        }
    };

    private final TrackingControllView trackingCtrlView;
    private final SternenSpielApplicationBackend backend;

    /**
     * Konstruktor nur für Null Instanz.
     */
    private TrackingControllViewController() {
        this.trackingCtrlView = null;
        this.backend = null;
    }

    public TrackingControllViewController(
            final TrackingControllView trackingCtrlView,
            final SternenSpielApplicationBackend backend) {

        this.backend = backend;
        this.trackingCtrlView = trackingCtrlView;
        trackingCtrlView.setTrackingControllViewController(this);
    }

    public void setzeTrackingPunkt() {
        backend.setzeTrackingPunkt();
    }

    public void zurueckZumFehler() {
        backend.zurueckZumLetztenFehlerfreienSpielzug();
    }

    public void zurueckZumLetztenTrackingPunkt() {
        backend.zurueckZumLetztenTrackingPunkt();
    }
}
