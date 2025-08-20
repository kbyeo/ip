public class InvalidCommandException extends StackOverflownException {
    public InvalidCommandException(String command) {
        super("Hmm, '" + command + "' isn't in my vocabulary yet - try 'todo', 'deadline', 'event', 'list', " +
                "'mark', or 'unmark'!");
    }
}
