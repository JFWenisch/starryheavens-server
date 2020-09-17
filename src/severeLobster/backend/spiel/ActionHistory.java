package severeLobster.backend.spiel;

import severeLobster.backend.command.Aktion;
import severeLobster.backend.spiel.ActionHistoryObject.PathAndStepStatus;

import java.util.LinkedList;

/**
 * @author Paul Bruell
 */
public class ActionHistory {
    private final LinkedList<ActionHistoryObject> spielzuege_;
    private int currentAHO_;
    private int currentVisibleStep_;

    public ActionHistory() {
        currentAHO_ = 1;
        spielzuege_ = new LinkedList<ActionHistoryObject>();
        spielzuege_.add(new ActionHistoryObject());
        currentVisibleStep_ = spielzuege_.size() - 1;
    }

    public void neuerSpielzug(Aktion spielZug) {
        spielzuege_.add(new ActionHistoryObject(spielZug, false));
        currentVisibleStep_ = spielzuege_.size() - 1;
    }

    public void setzeTrackingPunktNachDiesemZug(boolean istVorTrackingPunkt) {
        spielzuege_.getLast().setzeTrackingPunktNachDiesemZug(istVorTrackingPunkt);
    }

    public void setzeFehlerhaft(boolean fehler) {
        spielzuege_.getLast().setzeFehlerhaft(fehler);
    }

    public boolean istVorTrackingPunkt() {
        return spielzuege_.getLast().istVorTrackingPunkt();
    }

    public boolean verursachtFehler() {
        return spielzuege_.getLast().verursachtFehler();
    }

    private LinkedList<ActionHistoryObject> moveLastToAlternativeHistory(LinkedList<ActionHistoryObject> alternativeHistory) {
        ActionHistoryObject current = spielzuege_.getLast();
        spielzuege_.removeLast();
        current.getSpielzug().undo();
        if (alternativeHistory.isEmpty())
            alternativeHistory.add(current);
        else
            alternativeHistory.add(0, current);
        return alternativeHistory;
    }

    public void zurueckZuLetztemFehlerfreiemZug() {
        ActionHistoryObject letzterZugOhneFehler = null;
        int anzahlZuege = spielzuege_.size();
        for (int i = anzahlZuege - 1; i >= 0; i--) {
            if (!spielzuege_.get(i).verursachtFehler()) {
                letzterZugOhneFehler = spielzuege_.get(i);
                break;
            }
        }

        if (letzterZugOhneFehler != null) {
            LinkedList<ActionHistoryObject> alternativeHistory = new LinkedList<ActionHistoryObject>();
            while (spielzuege_.getLast().verursachtFehler()) {
                alternativeHistory = moveLastToAlternativeHistory(alternativeHistory);
            }
            letzterZugOhneFehler.addToAlternativePath(alternativeHistory);
            currentVisibleStep_ = spielzuege_.size() - 1;
        }
    }

    public void zurueckZuLetztemCheckpoint() {
        ActionHistoryObject letzterZugVorCheckpoint = null;
        int anzahlZuege = spielzuege_.size();
        for (int i = anzahlZuege - 1; i >= 0; i--) {
            if (spielzuege_.get(i).istVorTrackingPunkt()) {
                letzterZugVorCheckpoint = spielzuege_.get(i);
                break;
            }
        }

        if (letzterZugVorCheckpoint != null) {
            LinkedList<ActionHistoryObject> alternativeHistory = new LinkedList<ActionHistoryObject>();
            while (!spielzuege_.getLast().istVorTrackingPunkt()) {
                alternativeHistory = moveLastToAlternativeHistory(alternativeHistory);
            }
            letzterZugVorCheckpoint.addToAlternativePath(alternativeHistory);
            currentVisibleStep_ = spielzuege_.size() - 1;
        }
    }

    public ActionHistoryObject getCurrent() {
        return spielzuege_.getLast();
    }

    public void zeitrafferSetup() {
        if (currentVisibleStep_ == spielzuege_.size() - 1)
            for (int i = spielzuege_.size() - 1; i > 0; i--) {
                if (spielzuege_.get(i).getSpielzug() != null) {
                    spielzuege_.get(i).getSpielzug().reundo();

                    currentVisibleStep_--;
                } else
                    return;
            }
    }

    public void zeitrafferCleanup() {
        if (currentVisibleStep_ != spielzuege_.size())
            for (int i = currentAHO_; i < spielzuege_.size(); i++) {
                if (spielzuege_.get(i).getSpielzug() != null) {
                    spielzuege_.get(i).getSpielzug().redo();
                    currentVisibleStep_++;
                } else
                    return;
            }
    }

    public boolean zeitrafferSchritt() {
        if (currentAHO_ >= 0 && currentAHO_ < spielzuege_.size()) {
            ActionHistoryObject AHO = spielzuege_.get(currentAHO_);
            PathAndStepStatus traversionState = AHO.traverse(false);
            if (traversionState != PathAndStepStatus.stepGotValidPath) {
                currentAHO_++;
                currentVisibleStep_ = currentAHO_;
                if (!(currentAHO_ >= 0 && currentAHO_ < spielzuege_.size())) {
                    currentAHO_ = spielzuege_.size() - 1;
                    currentVisibleStep_ = currentAHO_;
                    return false;
                }
            }
            return true;
        } else
            return false;
    }
}
