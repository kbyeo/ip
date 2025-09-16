package stackoverflown.exception;
/**
 * Exception thrown when user attempts to create a task with empty description.
 *
 * <p>This exception is thrown when the Parser detects that a user is trying
 * to create a task (todo, deadline, or event) without providing a meaningful
 * description. All tasks require non-empty descriptions to be useful for
 * task management purposes.</p>
 *
 * <p>This exception is triggered in the following scenarios:</p>
 * <ul>
 * <li>Typing just "todo" without any task description</li>
 * <li>Creating deadline with only "/by" date but no task description</li>
 * <li>Creating events with only time information but no event description</li>
 * <li>Providing only whitespace characters as task description</li>
 * </ul>
 *
 * <h3>Example invalid commands:</h3>
 * <pre>
 * todo                                    // No description
 * deadline /by 2023-12-01                 // Missing description
 * event /from 2023-12-01 1400 /to 1600    // Missing event description
 * todo    [only spaces]                   // Only whitespace
 * </pre>
 *
 * <h3>Valid alternatives:</h3>
 * <pre>
 * todo read book                          // Has meaningful description
 * deadline submit project /by 2023-12-01  // Complete with description
 * event team meeting /from 1400 /to 1600  // Complete event details
 * </pre>
 *
 * @author Yeo Kai Bin
 * @version 1.0
 * @since 2025
 * @see stackoverflown.parser.Parser#parseTodoCommand(String)
 * @see stackoverflown.parser.Parser#parseDeadlineCommand(String)
 * @see stackoverflown.parser.Parser#parseEventCommand(String)
 */
//The javaDocs here was generated using Claude 4.0, as part of the A-AiAssisted increment.
public class EmptyDescriptionException extends StackOverflownException {
    public EmptyDescriptionException(String taskType) {
        super("Hold up! Your " + taskType + " task needs some substance - what exactly should I track?");
    }
}
