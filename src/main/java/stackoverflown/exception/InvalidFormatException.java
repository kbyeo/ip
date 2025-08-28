package stackoverflown.exception;

public class InvalidFormatException extends StackOverflownException {
    public InvalidFormatException(String correctFormat) {
        super("Oops! Format mixup detected. Try this instead: " + correctFormat);
    }
}
