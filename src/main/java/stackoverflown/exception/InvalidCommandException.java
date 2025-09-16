package stackoverflown.exception;
/**
 * Exception thrown when user provides an unrecognized or invalid command.
 *
 * <p>This exception is thrown by the Parser when it encounters user input
 * that doesn't match any of the supported command formats. It provides
 * helpful feedback to guide users toward valid command usage.</p>
 *
 * <p>Supported commands include: todo, deadline, event, list, mark, unmark,
 * delete, find, sort, and bye.</p>
 *
 * <h3>Example scenarios that trigger this exception:</h3>
 * <ul>
 * <li>Typing "add task" instead of "todo task"</li>
 * <li>Using "remove 1" instead of "delete 1"</li>
 * <li>Entering gibberish or typos in command names</li>
 * <li>Using unsupported command variations</li>
 * </ul>
 *
 * @author Yeo Kai Bin
 * @version 1.0
 * @since 2025
 * @see stackoverflown.parser.Parser#getCommandType(String)
 */
//The javaDocs here was generated using Claude 4.0, as part of the A-AiAssisted increment.
public class InvalidCommandException extends StackOverflownException {
    public InvalidCommandException(String command) {
        super("Hmm, '" + command + "' isn't in my vocabulary yet - try 'todo', 'deadline', 'event', 'list', " +
                "'mark', or 'unmark'!");
    }
}
