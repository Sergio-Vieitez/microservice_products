package exceptions;

/**
 * Exception to be thrown when attempting to add a product that already exists.
 */
public class ProductAlreadyExistsException extends Exception{
    /**
     * Constructs a new ProductAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message
     */
    public ProductAlreadyExistsException(String message) {
        super(message);
    }
}
