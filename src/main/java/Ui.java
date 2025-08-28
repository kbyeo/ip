import java.util.Scanner;

/**
 * Handles interactions with the user.
 * Manages input/output operations and message formatting.
 */
public class Ui {
    private Scanner scanner;
    private static final String LINE_SEPARATION = "____________________________________________________________";

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
     * Shows the welcome message when the application starts.
     */
    public void showWelcome() {
        String botName = "StackOverflown";
        String introLine = String.format("Hey! %s here, thrilled to see you!\n Let's dive RIGHT in, what can I do for you? :)", botName);
        String decoratedIntro = String.format("%s\n %s\n%s", LINE_SEPARATION, introLine, LINE_SEPARATION);
        System.out.println(decoratedIntro);
    }

    /**
     * Shows the goodbye message when the application ends.
     */
    public void showGoodbye() {
        String exitLine = "Aww, you're leaving already? It's been such a\n pleasure, can't wait till next time! :)";
        String decoratedExit = String.format("%s\n %s\n%s", LINE_SEPARATION, exitLine, LINE_SEPARATION);
        System.out.println(decoratedExit);
    }

    /**
     * Shows error messages to the user.
     *
     * @param message the error message to display
     */
    public void showError(String message) {
        System.out.println(LINE_SEPARATION + "\n " + message + "\n" + LINE_SEPARATION);
    }

    /**
     * Shows task added confirmation message.
     *
     * @param task the task that was added
     * @param taskCount current number of tasks
     * @param taskType type of task (ToDo, Deadline, Event)
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
     * Shows task deletion confirmation message.
     *
     * @param task the task that was deleted
     * @param taskCount current number of tasks after deletion
     */
    public void showTaskDeleted(Task task, int taskCount) {
        String deleteMessage = String.format("%s\n Poof! Task vanished from existence:\n   %s\n Your task arsenal now stands at %d strong!\n%s",
                LINE_SEPARATION, task, taskCount, LINE_SEPARATION);
        System.out.println(deleteMessage);
    }

    /**
     * Shows task marked as done message.
     *
     * @param task the task that was marked
     */
    public void showTaskMarked(Task task) {
        String markMessage = String.format("%s\n Boom! That task is history - marked as done and dusted\n   %s\n%s",
                LINE_SEPARATION, task, LINE_SEPARATION);
        System.out.println(markMessage);
    }

    /**
     * Shows task unmarked message.
     *
     * @param task the task that was unmarked
     */
    public void showTaskUnmarked(Task task) {
        String unmarkMessage = String.format("%s\n Aha! This task is no longer done - it's waiting for your magic touch again\n   %s\n%s",
                LINE_SEPARATION, task, LINE_SEPARATION);
        System.out.println(unmarkMessage);
    }

    /**
     * Shows the task list to the user.
     *
     * @param taskList the TaskList object to display
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
}