package stackoverflown.ui;

import java.util.ArrayList;
import java.util.Scanner;
import stackoverflown.task.TaskList;
import stackoverflown.task.Task;

/**
 * Handles all user interface operations for the StackOverflown application.
 *
 * <p>The Ui class manages all interactions between the user and the application,
 * including input reading, output formatting, and display of various application
 * states. It provides a consistent command-line interface with formatted messages
 * and visual separators.</p>
 *
 * <p>Key responsibilities include:
 * <ul>
 * <li>Reading user input commands</li>
 * <li>Displaying welcome and goodbye messages</li>
 * <li>Formatting and displaying task lists and individual tasks</li>
 * <li>Showing error messages and loading notifications</li>
 * <li>Providing feedback for user actions (add, mark, delete, etc.)</li>
 * </ul>
 * </p>
 *
 * <p>All output is formatted with consistent visual separators and informative
 * messages to enhance user experience.</p>
 *
 * @author Yeo Kai Bin
 * @version 0.1
 * @since 2025
 */
public class Ui {
    private Scanner scanner;
    private static final String LINE_SEPARATION = "____________________________________________________________";

    /**
     * Constructs a new Ui instance and initializes input scanner.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads user input from the console.
     *
     * @return user input as a string
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Closes the scanner when the application ends.
     */
    public void close() {
        scanner.close();
    }

    /**
     * Displays the welcome message when the application starts.
     *
     * <p>Shows the application logo and a friendly greeting message with
     * visual formatting.</p>
     */
    public void showWelcome() {
        String botName = "StackOverflown";
        String introLine = String.format("Hey! %s here, thrilled to see you!\n Let's dive RIGHT in, " +
                "what can I do for you? :)", botName);
        String decoratedIntro = String.format("%s\n %s\n%s", LINE_SEPARATION, introLine, LINE_SEPARATION);
        System.out.println(decoratedIntro);
    }

    /**
     * Displays the goodbye message when the application exits.
     *
     * <p>Shows a farewell message with visual formatting to provide
     * closure to the user session.</p>
     */
    public void showGoodbye() {
        String exitLine = "Aww, you're leaving already? It's been such a\n pleasure, can't wait till next time! :)";
        String decoratedExit = String.format("%s\n %s\n%s", LINE_SEPARATION, exitLine, LINE_SEPARATION);
        System.out.println(decoratedExit);
    }

    /**
     * Displays an error message to the user.
     *
     * <p>Formats error messages with visual separators for clear visibility.
     * Used for displaying exceptions and validation errors.</p>
     *
     * @param message the error message to display
     */
    public void showError(String message) {
        System.out.println(LINE_SEPARATION + "\n " + message + "\n" + LINE_SEPARATION);
    }

    /**
     * Shows confirmation message when a task is successfully added.
     *
     * <p>Displays the newly added task and updates the user on the total
     * number of tasks in their list. Includes contextual messaging based
     * on the type of task added.</p>
     *
     * @param task the Task object that was added
     * @param taskCount the new total number of tasks
     * @param taskType the type of task added ("todo", "deadline", "event")
     */
    public void showTaskAdded(Task task, int taskCount, String taskType) {
        String message;
        switch (taskType.toLowerCase()) {
        case "todo":
            message = "Boom! A ToDo task just joined the party: ";
            break;
        case "deadline":
            message = "All set! A Deadline task just joined the party: ";
            break;
        case "event":
            message = "Tada! An Event task just joined the party: ";
            break;
        default:
            message = "Task added: ";
        }

        String addedMessage = String.format("%s\n%s%s\nYour task arsenal now stands at %d strong!\n%s",
                LINE_SEPARATION, message, task, taskCount, LINE_SEPARATION);
        System.out.println(addedMessage);
    }

    /**
     * Shows confirmation message when a task is deleted.
     *
     * <p>Displays the deleted task and updates the user on the remaining
     * number of tasks in their list.</p>
     *
     * @param task the Task object that was deleted
     * @param taskCount the number of tasks remaining after deletion
     */
    public void showTaskDeleted(Task task, int taskCount) {
        String deleteMessage = String.format("%s\n Poof! Task vanished from existence:\n   %s\n Your " +
                        "task arsenal now stands at %d strong!\n%s",
                LINE_SEPARATION, task, taskCount, LINE_SEPARATION);
        System.out.println(deleteMessage);
    }

    /**
     * Shows confirmation message when a task is marked as done.
     *
     * <p>Displays the task that was marked with congratulatory messaging
     * to provide positive feedback for task completion.</p>
     *
     * @param task the Task object that was marked as done
     */
    public void showTaskMarked(Task task) {
        String markMessage = String.format("%s\n Boom! That task is history - marked as done and dusted\n   %s\n%s",
                LINE_SEPARATION, task, LINE_SEPARATION);
        System.out.println(markMessage);
    }

    /**
     * Shows confirmation message when a task is unmarked (set as not done).
     *
     * <p>Displays the task that was unmarked with appropriate messaging
     * to confirm the status change.</p>
     *
     * @param task the Task object that was unmarked
     */
    public void showTaskUnmarked(Task task) {
        String unmarkMessage = String.format("%s\n Aha! This task is no longer done - it's waiting " +
                        "for your magic touch again\n   %s\n%s",
                LINE_SEPARATION, task, LINE_SEPARATION);
        System.out.println(unmarkMessage);
    }

    /**
     * Displays the complete task list to the user.
     *
     * <p>Shows all tasks in a formatted list with visual separators. If the
     * list is empty, displays an appropriate empty state message.</p>
     *
     * @param taskList the TaskList containing all current tasks
     */
    public void showTaskList(TaskList taskList) {
        String listDisplay = String.format("%s\n%s\n%s", LINE_SEPARATION, taskList, LINE_SEPARATION);
        System.out.println(listDisplay);
    }

    /**
     * Shows loading error message when tasks cannot be loaded from file.
     */
    public void showLoadingError() {
        showError("Oops! Had trouble loading your saved tasks. Starting with a fresh task list!");
    }

    /**
     * Displays the results of a task search operation.
     *
     * <p>If no tasks are found, shows a helpful message suggesting to add tasks.
     * If tasks are found, displays them in a numbered list format similar to
     * the main task list display.</p>
     *
     * @param foundTasks ArrayList of tasks that match the search keyword
     * @param keyword the search keyword that was used
     */
    public void showFindResults(ArrayList<Task> foundTasks, String keyword) {
        if (foundTasks.isEmpty()) {
            String message = String.format("No tasks found with keyword '%s'. Time to add some tasks!", keyword);
            String display = String.format("%s\n %s\n%s", LINE_SEPARATION, message, LINE_SEPARATION);
            System.out.println(display);
        } else {
            String header = String.format("Here are the matching tasks in your list:");
            StringBuilder result = new StringBuilder(LINE_SEPARATION + "\n " + header + "\n");

            for (int i = 0; i < foundTasks.size(); i++) {
                result.append(String.format(" %d.%s\n", i + 1, foundTasks.get(i)));
            }

            result.append(LINE_SEPARATION);
            System.out.println(result.toString());
        }
    }

}