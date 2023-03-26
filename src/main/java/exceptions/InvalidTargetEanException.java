package exceptions;

/**
 Exception thrown when a target EAN code is invalid.
 */
public class InvalidTargetEanException extends Exception{
    /**
     Constructs an InvalidTargetEanException with the specified detail message.
     @param message the detail message
     */
    public InvalidTargetEanException(String message) {
        super(message);
    }
}
