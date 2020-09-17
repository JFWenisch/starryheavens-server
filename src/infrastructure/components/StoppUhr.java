package infrastructure.components;

/**
 * StoppUhr Klasse zur Nachverfolgung der gespielten Zeit. Vor dem Serialisieren
 * von Instanzen dieser Klasse muss manuell stop() aufgerufen werden, wenn
 * gerade eine Messung läuft, da die Messung sonst weiter läuft.
 * 
 * @author fwenisch, Lars Schlegelmilch, Lutz Kleiber
 */
public class StoppUhr {

    /**
     * Die Summe der Millisekunden aus den bisherigen Messungen. Hat nichts mit
     * der aktuell laufenden Messung zu tun. Ist ein relativer Zeitraum (bsp:
     * 135 Millisekunden) und kein Zeitpunkt.
     */
    private long summeDerBisherigenMessungen = 0L;

    /** Zeitpunkt des Starts der aktuell laufenden Messung. */
    private long aktuelleMessungAbsoluterStartZeitpunkt = 0L;

    private volatile boolean messungLaeuft = false;

    /**
     * Startet die Messung oder setzt die gestoppte Messung fort. Wenn bereits
     * eine Messung laeuft, die noch nicht gestoppt wurde, hat der Aufruf keine
     * Auswirkungen.
     */
    public synchronized void start() {

        if (!messungLaeuft) {
            this.aktuelleMessungAbsoluterStartZeitpunkt = System
                    .currentTimeMillis();
            messungLaeuft = true;
        }
    }

    /**
     * Stoppt die Messung. Die Messung kann mit start() spaeter fortgesetzt
     * werden. Wenn gerade keine Messung läuft, hat der Aufruf keine
     * Auswirkungen.
     */
    public synchronized void stop() {
        if (messungLaeuft) {
            messungLaeuft = false;
            final long aktuelleMessungAbsoluterJetztZeitpunkt = System
                    .currentTimeMillis();
            final long waehrendAktuellerMessungVergangeneZeit = aktuelleMessungAbsoluterJetztZeitpunkt
                    - this.aktuelleMessungAbsoluterStartZeitpunkt;
            this.summeDerBisherigenMessungen += waehrendAktuellerMessungVergangeneZeit;
        }
    }

    /**
     * Gibt die aktuelle Anzeige der Stoppuhr zurueck. Beruecksichtigt auch eine
     * eventuell gerade laufende Messung.
     * 
     * @return Die während aller Messungen bis zum aktuellen Zeitpunkt vergangen
     *         Sekunden.
     */
    public long getSekunden() {

        long millisSumme = summeDerBisherigenMessungen;
        if (messungLaeuft) {
            /*
             * Addiere zur komplett vergangenen Zeit der alten Messungen die
             * während der aktuellen Messung bis hierhin vergangene Zeit.
             */
            final long aktuelleMessungAbsoluterJetztZeitpunkt = System
                    .currentTimeMillis();
            millisSumme += (aktuelleMessungAbsoluterJetztZeitpunkt - this.aktuelleMessungAbsoluterStartZeitpunkt);

        }
        /* Millisekunden in Sekunden umrechnen */
        return (millisSumme / 1000);
    }
}
