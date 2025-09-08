package stackoverflown;

import java.util.ArrayList;
import stackoverflown.ui.Ui;
import stackoverflown.parser.Parser;
import stackoverflown.storage.Storage;
import stackoverflown.task.TaskList;
import stackoverflown.task.Task;
import stackoverflown.exception.StackOverflownException;
import stackoverflown.exception.InvalidCommandException;

/**
 * Main class for StackOverflown chatbot application.
 *
 * <p>StackOverflown is a personal task management chatbot that helps users organize
 * their ToDo tasks, Deadlines, and Events. The application provides a command-line
 * interface for adding, managing, and tracking tasks with persistent storage.</p>
 *
 * <p>Key features include:
 * <ul>
 * <li>Adding different types of tasks (ToDo, Deadline, Event)</li>
 * <li>Marking tasks as done/undone</li>
 * <li>Deleting tasks</li>
 * <li>Listing all tasks</li>
 * <li>Automatic file-based storage</li>
 * </ul>
 * </p>
 *
 * @author Yeo Kai Bin
 * @version 0.1
 * @since 2025
 */
public class StackOverflown {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    static final int MARK_PARSE_VALUE = 4;
    static final int UNMARK_PARSE_VALUE = 6;
    static final int DELETE_PARSE_VALUE = 6;

    /**
     * Constructs a new StackOverflown instance with default components.
     *
     * <p>Initializes the UI, Storage, and TaskList components. Attempts to load
     * existing tasks from storage, and falls back to empty task list if loading fails.</p>
     *
     * <p>Automatically sets up auto-save functionality by connecting TaskList to Storage.</p>
     */
    public StackOverflown() {
        ui = new Ui();
        storage = new Storage();
        try {
            tasks = new TaskList(storage.loadTasks());
        } catch (StackOverflownException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
        tasks.setStorage(storage); // Enable auto-save functionality
    }

    // ===== GUI INTERFACE METHODS =====
    /**
     * Gets the welcome message for GUI display.
     *
     * @return formatted welcome message string
     */
    public String getWelcomeMessage() {
        return "Hey! StackOverflown here, thrilled to see you!\nLet's dive RIGHT in, what can I do for you? :)";
    }

    /**
     * Processes user input and returns appropriate response for GUI.
     *
     * <p>This method provides a GUI-compatible interface that returns string responses
     * instead of directly displaying output through the UI component.</p>
     *
     * @param input user input string
     * @return formatted response string
     */
    public String getResponse(String input) {
        try {
            Parser.CommandType command = Parser.getCommandType(input);

            switch (command) {
                case BYE:
                    return "Aww, you're leaving already? It's been such a\npleasure, can't wait till next time! :)";
                case LIST:
                    return getTaskListResponse();
                case TODO:
                    return handleTodoResponse(input);
                case DEADLINE:
                    return handleDeadlineResponse(input);
                case EVENT:
                    return handleEventResponse(input);
                case MARK:
                    return handleMarkResponse(input);
                case UNMARK:
                    return handleUnmarkResponse(input);
                case DELETE:
                    return handleDeleteResponse(input);
                case FIND:
                    return handleFindResponse(input);
                default:
                    throw new InvalidCommandException(input);
            }

        } catch (StackOverflownException e) {
            return e.getMessage();
        }
    }

    /**
     * Gets formatted task list for GUI display.
     *
     * @return formatted task list string
     */
    private String getTaskListResponse() {
        if (tasks.isEmpty()) {
            return "Your task list is as empty as my coffee cup. Time to add some tasks!";
        }

        StringBuilder result = new StringBuilder("Here are your tasks:\n\n");
        for (int i = 0; i < tasks.getTaskCount(); i++) {
            result.append(String.format("%d. %s\n", i + 1, tasks.getTask(i)));
        }
        return result.toString().trim();
    }

    /**
     * Handles todo command for GUI and returns response string.
     *
     * @param input user input string
     * @return success message with task details
     * @throws StackOverflownException if parsing or task creation fails
     */
    private String handleTodoResponse(String input) throws StackOverflownException {
        String description = Parser.parseTodoCommand(input);
        tasks.addToDo(description);
        Task newTask = tasks.getTask(tasks.getTaskCount() - 1);
        return String.format("Boom! A ToDo task just joined the party:\n%s\n\nYour task arsenal now stands at %d strong!",
                newTask, tasks.getTaskCount());
    }

    /**
     * Handles deadline command for GUI and returns response string.
     *
     * @param input user input string
     * @return success message with task details
     * @throws StackOverflownException if parsing or task creation fails
     */
    private String handleDeadlineResponse(String input) throws StackOverflownException {
        String[] parts = Parser.parseDeadlineCommand(input);
        tasks.addDeadline(parts[0], parts[1]);
        Task newTask = tasks.getTask(tasks.getTaskCount() - 1);
        return String.format("All set! A Deadline task just joined the party:\n%s\n\nYour task arsenal now stands at %d strong!",
                newTask, tasks.getTaskCount());
    }

    /**
     * Handles event command for GUI and returns response string.
     *
     * @param input user input string
     * @return success message with task details
     * @throws StackOverflownException if parsing or task creation fails
     */
    private String handleEventResponse(String input) throws StackOverflownException {
        String[] parts = Parser.parseEventCommand(input);
        tasks.addEvent(parts[0], parts[1], parts[2]);
        Task newTask = tasks.getTask(tasks.getTaskCount() - 1);
        return String.format("Tada! An Event task just joined the party:\n%s\n\nYour task arsenal now stands at %d strong!",
                newTask, tasks.getTaskCount());
    }
    /**
     * Handles mark command for GUI and returns response string.
     *
     * @param input user input string
     * @return success message with task details
     * @throws StackOverflownException if parsing fails or invalid task number
     */
    private String handleMarkResponse(String input) throws StackOverflownException {
        int taskIndex = Parser.parseTaskIndex(input, MARK_PARSE_VALUE);
        Task markedTask = tasks.markTask(taskIndex);
        return String.format("Boom! That task is history - marked as done and dusted:\n%s", markedTask);
    }

    /**
     * Handles unmark command for GUI and returns response string.
     *
     * @param input user input string
     * @return success message with task details
     * @throws StackOverflownException if parsing fails or invalid task number
     */
    private String handleUnmarkResponse(String input) throws StackOverflownException {
        int taskIndex = Parser.parseTaskIndex(input, UNMARK_PARSE_VALUE);
        Task unmarkedTask = tasks.unmarkTask(taskIndex);
        return String.format("Aha! This task is no longer done - it's waiting for your magic touch again:\n%s", unmarkedTask);
    }
    /**
     * Handles delete command for GUI and returns response string.
     *
     * @param input user input string
     * @return success message with task details
     * @throws StackOverflownException if parsing fails or invalid task number
     */
    private String handleDeleteResponse(String input) throws StackOverflownException {
        int taskIndex = Parser.parseTaskIndex(input, DELETE_PARSE_VALUE);
        Task deletedTask = tasks.deleteTask(taskIndex);
        return String.format("Poof! Task vanished from existence:\n%s\n\nYour task arsenal now stands at %d strong!",
                deletedTask, tasks.getTaskCount());
    }

    /**
     * Handles find command for GUI and returns response string.
     *
     * @param input user input string
     * @return search results formatted for display
     * @throws StackOverflownException if parsing fails or keyword is empty
     */
    private String handleFindResponse(String input) throws StackOverflownException {
        String keyword = Parser.parseFindCommand(input);
        ArrayList<Task> foundTasks = tasks.findTasks(keyword);

        if (foundTasks.isEmpty()) {
            return String.format("No tasks found with keyword '%s'. Time to add some tasks!", keyword);
        }

        StringBuilder result = new StringBuilder("Here are the matching tasks in your list:\n\n");
        for (int i = 0; i < foundTasks.size(); i++) {
            result.append(String.format("%d. %s\n", i + 1, foundTasks.get(i)));
        }
        return result.toString().trim();
    }



    // ===== CLI INTERFACE METHODS =====
    /**
     * Starts the main application loop.
     *
     * <p>Handles the complete application lifecycle including:
     * <ul>
     * <li>Displaying welcome message</li>
     * <li>Reading and processing user commands</li>
     * <li>Handling exceptions and displaying error messages</li>
     * <li>Displaying goodbye message and cleanup</li>
     * </ul>
     * </p>
     *
     * <p>The loop continues until the user enters the 'bye' command.</p>
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;

        while (!isExit) {
            try {
                String input = ui.readCommand();
                Parser.CommandType command = Parser.getCommandType(input);

                switch (command) {
                case BYE:
                    isExit = true;
                    break;
                case LIST:
                    ui.showTaskList(tasks);
                    break;
                case TODO:
                    handleTodoCommand(input);
                    break;
                case DEADLINE:
                    handleDeadlineCommand(input);
                    break;
                case EVENT:
                    handleEventCommand(input);
                    break;
                case MARK:
                    handleMarkCommand(input);
                    break;
                case UNMARK:
                    handleUnmarkCommand(input);
                    break;
                case DELETE:
                    handleDeleteCommand(input);
                    break;
                case FIND:
                    handleFindCommand(input);
                    break;
                default:
                    throw new InvalidCommandException(input);
                }

            } catch (StackOverflownException e) {
                ui.showError(e.getMessage());
            }
        }

        ui.showGoodbye();
        ui.close();
    }

    /**
     * Handles todo command by parsing input and adding the task.
     *
     * @param input the raw user input containing todo command and description
     * @throws StackOverflownException if parsing fails or task creation fails
     */
    private void handleTodoCommand(String input) throws StackOverflownException {
        String description = Parser.parseTodoCommand(input);
        tasks.addToDo(description);
        ui.showTaskAdded(tasks.getTask(tasks.getTaskCount() - 1), tasks.getTaskCount(), "todo");
    }

    /**
     * Handles deadline command by parsing input and adding the deadline task.
     *
     * @param input the raw user input containing deadline command, description, and date
     * @throws StackOverflownException if parsing fails, date parsing fails, or task creation fails
     */
    private void handleDeadlineCommand(String input) throws StackOverflownException {
        String[] parts = Parser.parseDeadlineCommand(input);
        tasks.addDeadline(parts[0], parts[1]);
        ui.showTaskAdded(tasks.getTask(tasks.getTaskCount() - 1), tasks.getTaskCount(), "deadline");
    }

    /**
     * Handles event command by parsing input and adding the event task.
     *
     * @param input the raw user input containing event command, description, from time, and to time
     * @throws StackOverflownException if parsing fails, date parsing fails, or task creation fails
     */
    private void handleEventCommand(String input) throws StackOverflownException {
        String[] parts = Parser.parseEventCommand(input);
        tasks.addEvent(parts[0], parts[1], parts[2]);
        ui.showTaskAdded(tasks.getTask(tasks.getTaskCount() - 1), tasks.getTaskCount(), "event");
    }

    /**
     * Handles mark command by parsing task index and marking task as done.
     *
     * @param input user input string
     * @throws StackOverflownException if parsing fails or invalid task number
     */
    private void handleMarkCommand(String input) throws StackOverflownException {
        int taskIndex = Parser.parseTaskIndex(input, MARK_PARSE_VALUE);
        Task markedTask = tasks.markTask(taskIndex);
        ui.showTaskMarked(markedTask);
    }

    /**
     * Handles unmark command by parsing task index and unmarking task.
     *
     * @param input user input string
     * @throws StackOverflownException if parsing fails or invalid task number
     */
    private void handleUnmarkCommand(String input) throws StackOverflownException {
        int taskIndex = Parser.parseTaskIndex(input, UNMARK_PARSE_VALUE);
        Task unmarkedTask = tasks.unmarkTask(taskIndex);
        ui.showTaskUnmarked(unmarkedTask);
    }

    /**
     * Handles delete command by parsing task index and deleting task.
     *
     * @param input user input string
     * @throws StackOverflownException if parsing fails or invalid task number
     */
    private void handleDeleteCommand(String input) throws StackOverflownException {
        int taskIndex = Parser.parseTaskIndex(input, DELETE_PARSE_VALUE);
        Task deletedTask = tasks.deleteTask(taskIndex);
        ui.showTaskDeleted(deletedTask, tasks.getTaskCount());
    }

    /**
     * Handles find command by parsing keyword and searching tasks.
     *
     * @param input the user input string containing find command and keyword
     * @throws StackOverflownException if parsing fails or keyword is empty
     */
    private void handleFindCommand(String input) throws StackOverflownException {
        String keyword = Parser.parseFindCommand(input);
        ArrayList<Task> foundTasks = tasks.findTasks(keyword);
        ui.showFindResults(foundTasks, keyword);
    }

    /**
     * Main entry point for the application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        new StackOverflown().run();
    }
}