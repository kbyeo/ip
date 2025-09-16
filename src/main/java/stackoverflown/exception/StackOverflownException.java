package stackoverflown.exception;

/**
 * Base exception class for all StackOverflown application-specific exceptions.
 *
 * <p>This class serves as the parent exception for all custom exceptions thrown
 * by the StackOverflown task management application. It extends the standard
 * Java Exception class to provide application-specific error handling.</p>
 *
 * <p>All StackOverflown exceptions inherit from this class, allowing for
 * centralized exception handling and consistent error messaging throughout
 * the application.</p>
 *
 * @author Yeo Kai Bin
 * @version 1.0
 * @since 2025
 * @see Exception
 */
//The javaDocs here was generated using Claude 4.0, as part of the A-AiAssisted increment.
public class StackOverflownException extends Exception {
    public StackOverflownException(String message) {
        super(message);
    }
}
