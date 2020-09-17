package infrastructure.constants.enums;

import infrastructure.ResourceManager;

/**
 * Enumeration des Schwierigkeitsgrad des eines Spielfelds.
 * 
 * @author Lars Schlegelmilch
 */
public enum SchwierigkeitsgradEnumeration {

    /**
     * Schwierigkeitsgrad Leicht: Das Spielfeld ist der Groesse und der Anzahl
     * der Spielsteine entsprechend als leicht eingestuft.
     */
    LEICHT {
        private final ResourceManager resourceManager = ResourceManager.get();

        @Override
        public String toString() {
            return "Leicht";
        }
    },
    /**
     * Schwierigkeitsgrad Mittel: Das Spielfeld ist der Groesse und der Anzahl
     * der Spielsteine entsprechend als mittel eingestuft.
     */
    MITTEL {
        private final ResourceManager resourceManager = ResourceManager.get();

        @Override
        public String toString() {
            return "Mittel";
        }
    },
    /**
     * Schwierigkeitsgrad Schwer: Das Spielfeld ist der Groesse und der Anzahl
     * der Spielsteine entsprechend als schwer eingestuft.
     */
    SCHWER {
        private final ResourceManager resourceManager = ResourceManager.get();

        @Override
        public String toString() {
            return "Schwer";
        }
    },
}
