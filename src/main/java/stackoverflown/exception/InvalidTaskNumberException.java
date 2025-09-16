package stackoverflown.exception;
/**
 * Exception thrown when user references a task number that doesn't exist.
 *
 * <p>This exception is thrown when users attempt to perform operations on
 * tasks using invalid task numbers. Task numbers are 1-based indices that
 * correspond to the task list positions shown to the user.</p>
 *
 * <p>Common scenarios that trigger this exception:</p>
 * <ul>
 * <li>Trying to mark/unmark a task with number greater than list size</li>
 * <li>Attempting to delete a non-existent task</li>
 * <li>Using task number 0 or negative numbers</li>
 * <li>Referencing tasks after some have been deleted</li>
 * <li>Using non-numeric values where task numbers are expected</li>
 * </ul>
 *
 * <h3>Example scenarios:</h3>
 * <pre>
 * mark 5        // When only 3 tasks exist
 * delete 0      // Task numbers start from 1
 * unmark -1     // Negative numbers are invalid
 * mark abc      // Non-numeric task reference
 * </pre>
 *
 * @author Yeo Kai Bin
 * @version 1.0
 * @since 2025
 * @see stackoverflown.parser.Parser#parseTaskIndex(String, int)
 * @see stackoverflown.task.TaskList#markTask(int)
 * @see stackoverflown.task.TaskList#deleteTask(int)
 */
//The javaDocs here was generated using Claude 4.0, as part of the A-AiAssisted increment.
public class InvalidTaskNumberException extends StackOverflownException {
    public InvalidTaskNumberException() {
        super("Uh-oh! That task number doesn't exist in my universe - try again?");
    }
}
