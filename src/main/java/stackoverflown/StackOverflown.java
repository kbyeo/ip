package stackoverflown;

import stackoverflown.ui.Ui;
import stackoverflown.parser.Parser;
import stackoverflown.storage.Storage;
import stackoverflown.task.TaskList;
import stackoverflown.task.Task;
import stackoverflown.exception.StackOverflownException;
import stackoverflown.exception.InvalidCommandException;
/**
 * Main class for StackOverflown chatbot application.
 * Orchestrates interaction between Ui, Storage, TaskList, and Parser components.
 */
public class StackOverflown {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Constructor that initializes all components.
     * Uses default data file path.
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

    /**
     * Main application loop.
     * Handles user commands and coordinates between components.
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
     * Handles todo command by parsing input and adding task.
     *
     * @param input user input string
     * @throws StackOverflownException if parsing or task creation fails
     */
    private void handleTodoCommand(String input) throws StackOverflownException {
        String description = Parser.parseTodoCommand(input);
        tasks.addToDo(description);
        ui.showTaskAdded(tasks.getTask(tasks.getTaskCount() - 1), tasks.getTaskCount(), "todo");
    }

    /**
     * Handles deadline command by parsing input and adding deadline task.
     *
     * @param input user input string
     * @throws StackOverflownException if parsing, date parsing, or task creation fails
     */
    private void handleDeadlineCommand(String input) throws StackOverflownException {
        String[] parts = Parser.parseDeadlineCommand(input);
        tasks.addDeadline(parts[0], parts[1]);
        ui.showTaskAdded(tasks.getTask(tasks.getTaskCount() - 1), tasks.getTaskCount(), "deadline");
    }

    /**
     * Handles event command by parsing input and adding event task.
     *
     * @param input user input string
     * @throws StackOverflownException if parsing, date parsing, or task creation fails
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
        int taskIndex = Parser.parseTaskIndex(input, 4);
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
        int taskIndex = Parser.parseTaskIndex(input, 6);
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
        int taskIndex = Parser.parseTaskIndex(input, 6);
        Task deletedTask = tasks.deleteTask(taskIndex);
        ui.showTaskDeleted(deletedTask, tasks.getTaskCount());
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