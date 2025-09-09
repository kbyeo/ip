package stackoverflown.exception;

/**
 * Exception thrown when user provides an invalid sort type.
 *
 * <p>This exception is thrown when the user attempts to sort tasks using
 * an unrecognized or invalid sorting criteria, helping guide users toward
 * valid sort options.</p>
 *
 * @author Yeo Kai Bin
 * @version 1.0
 * @since 2025
 */
public class InvalidSortTypeException extends StackOverflownException {

    /**
     * Constructs an InvalidSortTypeException with specified detail message.
     *
     * @param message the detail message explaining the invalid sort type
     */
    public InvalidSortTypeException(String message) {
        super("Sort Error: " + message);
    }
}