package exceptions;

/**
 An exception that is thrown when an invalid EAN code is encountered.
 */
public class InvalidEanException extends Exception {
    /**
     * Constructs an InvalidEanException with the specified detail message.
     * @param message the detail message
     */
    public InvalidEanException(String message) {
        super(message);
    }
}
