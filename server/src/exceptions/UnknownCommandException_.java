package exceptions;

/**
 * Provides a simple exception, indicates that command isn't found in manager
 */
public class UnknownCommandException_ extends Exception {
    /**
     * Constructor with message.
     *
     * @param message Message to show
     */
    public UnknownCommandException_(String message) {
        super(message);
    }
}
