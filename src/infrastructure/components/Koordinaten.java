package infrastructure.components;

/**
 * Koordinatenspeicher. Wenn erstellt, sind die einzelnen Koordinante konstant.
 * 
 * @author Lars Schlegelmilch, Lutz Kleiber
 */
public class Koordinaten {

    private final int x;
    private final int y;

    public Koordinaten(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    /**
     * Gibt die Summe dieser Koordinaten mit den uebergeben Koordinaten zurueck.
     * Dabei wird eine neue Koordinaten Instanz zurueckgeben und die beiden
     * addierten Koordinanten nicht veraendert.
     * 
     * @param koordinaten
     * @return
     */
    public Koordinaten getSummeWith(final Koordinaten koordinaten) {

        if (null == koordinaten) {
            /*
             * Die Exception braucht nicht uebersetzt zu werden -> sollte
             * hoechstens waehrend Tests auftreten
             */
            throw new NullPointerException("Summand ist null");
        }

        return Koordinaten.getSumme(this, koordinaten);
    }

    /**
     * Gibt die Summe der beiden Koordinaten als neue Koordinateninstanz
     * zurueck. Die beiden Koordinaten werden dabei nicht veraendert.
     * 
     * @param summand1
     * @param summand2
     * @return
     */
    public static Koordinaten getSumme(final Koordinaten summand1,
            final Koordinaten summand2) {

        if (null == summand1 || null == summand2) {
            /*
             * Die Exception braucht nicht uebersetzt zu werden -> sollte
             * hoechstens waehrend Tests auftreten
             */
            throw new NullPointerException("Ein Summand ist null");
        }
        return new Koordinaten(summand1.getX() + summand2.getX(),
                summand1.getY() + summand2.getY());
    }

    @Override
    public boolean equals(final Object obj) {
        return (null != obj && obj instanceof Koordinaten
                && ((Koordinaten) obj).x == this.x && ((Koordinaten) obj).y == this.y);
    }

    public String toString() {
        return "X: "+x+", Y: "+y;
    }
}
