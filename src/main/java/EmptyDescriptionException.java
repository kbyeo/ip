public class EmptyDescriptionException extends StackOverflownException {
    public EmptyDescriptionException(String taskType) {
        super("Hold up! Your " + taskType + " task needs some substance - what exactly should I track?");
    }
}
