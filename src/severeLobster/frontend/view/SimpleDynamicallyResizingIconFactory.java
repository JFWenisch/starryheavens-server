package severeLobster.frontend.view;

/**
 * Icon factory, die dynamisch skalierence Icons zurueckgibt. Die Icons
 * skalieren sich immer zur Groesse der Zielkomponente.
 * 
 * @author Lutz Kleiber
 * 
 */
public class SimpleDynamicallyResizingIconFactory extends SimpleIconFactory {

    private static final SimpleDynamicallyResizingIconFactory INSTANCE = new SimpleDynamicallyResizingIconFactory();
    public static final int DEFAULT_ICON_WIDTH = 4;
    public static final int DEFAULT_ICON_HEIGHT = 4;

    protected SimpleDynamicallyResizingIconFactory() {

        // sternIcon = new DynamischSkalierendesIcon(sternIcon,
        // DEFAULT_ICON_WIDTH, DEFAULT_ICON_HEIGHT);
        // ausschlussIcon = new DynamischSkalierendesIcon(ausschlussIcon,
        // DEFAULT_ICON_WIDTH, DEFAULT_ICON_HEIGHT);
        // blankIcon = new DynamischSkalierendesIcon(blankIcon,
        // DEFAULT_ICON_WIDTH, DEFAULT_ICON_HEIGHT);
        // pfeilSouthIcon = new DynamischSkalierendesIcon(pfeilSouthIcon,
        // DEFAULT_ICON_WIDTH, DEFAULT_ICON_HEIGHT);
        // pfeilSouthWestIcon = new
        // DynamischSkalierendesIcon(pfeilSouthWestIcon,
        // DEFAULT_ICON_WIDTH, DEFAULT_ICON_HEIGHT);
        // pfeilWestIcon = new DynamischSkalierendesIcon(pfeilWestIcon,
        // DEFAULT_ICON_WIDTH, DEFAULT_ICON_HEIGHT);
        // pfeilNorthWestIcon = new
        // DynamischSkalierendesIcon(pfeilNorthWestIcon,
        // DEFAULT_ICON_WIDTH, DEFAULT_ICON_HEIGHT);
        // pfeilNorthIcon = new DynamischSkalierendesIcon(pfeilNorthIcon,
        // DEFAULT_ICON_WIDTH, DEFAULT_ICON_HEIGHT);
        // pfeilNorthEastIcon = new
        // DynamischSkalierendesIcon(pfeilNorthEastIcon,
        // DEFAULT_ICON_WIDTH, DEFAULT_ICON_HEIGHT);
        // pfeilEastIcon = new DynamischSkalierendesIcon(pfeilEastIcon,
        // DEFAULT_ICON_WIDTH, DEFAULT_ICON_HEIGHT);
        // pfeilSouthEastIcon = new
        // DynamischSkalierendesIcon(pfeilSouthEastIcon,
        // DEFAULT_ICON_WIDTH, DEFAULT_ICON_HEIGHT);
    }

    public static SimpleDynamicallyResizingIconFactory getInstance() {
        return INSTANCE;
    }

}
