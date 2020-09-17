package infrastructure.exceptions;

import infrastructure.ResourceManager;

import java.io.IOException;

/**
 * Exception fuer ein nicht loeschbares Spiel
 * 
 * @author Lars Schlegelmilch
 */
public class SpielNichtLoeschbarException extends IOException {

    private static final ResourceManager resourceManager = ResourceManager
            .get();

    public SpielNichtLoeschbarException() {
        super(resourceManager.getText("exception.cant.delete.gamefile"));
    }
}
