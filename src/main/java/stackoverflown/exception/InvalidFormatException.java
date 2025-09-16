package stackoverflown.exception;
/**
 * Exception thrown when user input has correct command but incorrect format.
 *
 * <p>This exception is thrown when the Parser recognizes a valid command type
 * but the command arguments don't follow the expected format or structure.
 * It provides specific guidance on the correct format to use.</p>
 *
 * <p>This exception is commonly thrown for:</p>
 * <ul>
 * <li>Deadline tasks missing the "/by" keyword</li>
 * <li>Event tasks missing "/from" or "/to" keywords</li>
 * <li>Incorrect date format in deadline or event commands</li>
 * <li>Missing required parameters in commands</li>
 * <li>Malformed time specifications</li>
 * </ul>
 *
 * <h3>Example scenarios:</h3>
 * <pre>
 * deadline submit project 2023-12-01     // Missing /by
 * event meeting tomorrow 2pm to 4pm      // Missing /from /to format
 * deadline project /by invalid-date      // Invalid date format
 * </pre>
 *
 * @author Yeo Kai Bin
 * @version 1.0
 * @since 2025
 * @see stackoverflown.parser.Parser#parseDeadlineCommand(String)
 * @see stackoverflown.parser.Parser#parseEventCommand(String)
 */
//The javaDocs here was generated using Claude 4.0, as part of the A-AiAssisted increment.
public class InvalidFormatException extends StackOverflownException {
    public InvalidFormatException(String correctFormat) {
        super("Oops! Format mixup detected. Try this instead: " + correctFormat);
    }
}
