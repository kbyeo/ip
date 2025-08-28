package stackoverflown.exception;

public class InvalidTaskNumberException extends StackOverflownException {
    public InvalidTaskNumberException() {
        super("Uh-oh! That task number doesn't exist in my universe - try again?");
    }
}
