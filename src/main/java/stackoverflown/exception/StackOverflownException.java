package stackoverflown.exception;

/**
 * Base exception class for all StackOverflown application errors.
 * Provides user-friendly error messages.
 */
public class StackOverflownException extends Exception {
    public StackOverflownException(String message) {
        super(message);
    }
}
