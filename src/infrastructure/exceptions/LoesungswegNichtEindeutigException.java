package infrastructure.exceptions;

/**
 * Wird geworfen, wenn auf einen Spielmodus gestellt werden soll, bei dem ein
 * eindeutiger Loesungsweg gegeben sein muss, aber der vorhandene Loesungsweg
 * dies nicht ist.
 * 
 * @author Lutz Kleiber
 * 
 */
public class LoesungswegNichtEindeutigException extends Exception {

    public LoesungswegNichtEindeutigException() {
        super();
    }

    public LoesungswegNichtEindeutigException(String message) {
        super(message);
    }
}
