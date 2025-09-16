package stackoverflown.parser;

import stackoverflown.exception.StackOverflownException;
import stackoverflown.exception.EmptyDescriptionException;
import stackoverflown.exception.InvalidFormatException;
import stackoverflown.exception.InvalidTaskNumberException;
import stackoverflown.exception.InvalidSortTypeException;
import stackoverflown.task.TaskList;

/**
 * Utility class for parsing user input commands and extracting relevant information.
 *
 * <p>The Parser class provides static methods to parse different types of user commands
 * and validate their format. It handles command type identification and parameter extraction
 * for all supported StackOverflown commands.</p>
 *
 * <p>Supported commands include:
 * <ul>
 * <li>todo - for creating ToDo tasks</li>
 * <li>deadline - for creating Deadline tasks with due dates</li>
 * <li>event - for creating Event tasks with start/end times</li>
 * <li>list - for displaying all tasks</li>
 * <li>mark/unmark - for changing task completion status</li>
 * <li>delete - for removing tasks</li>
 * <li>bye - for exiting the application</li>
 * </ul>
 * </p>
 *
 * @author Yeo Kai Bin
 * @version 0.1
 * @since 2025
 */
public class Parser {

    /**
     * Enumeration of all supported command types in the StackOverflown application.
     *
     * <p>UNKNOWN represents any unrecognized command input.</p>
     */
    public enum CommandType {
        TODO, DEADLINE, EVENT, LIST, MARK, UNMARK, DELETE, BYE, FIND, UNKNOWN, SORT
    }

    static final int TODO_PARSE_VALUE = 5;
    static final int DEADLINE_PARSE_VALUE = 9;
    static final int EVENT_PARSE_VALUE = 6;

    /**
     * Determines the command type from user input.
     *
     * <p>Performs case-insensitive matching of command keywords. Commands are identified
     * by their starting keyword, allowing for additional parameters.</p>
     *
     * @param input the user input string to analyze
     * @return the corresponding CommandType enum value, or UNKNOWN if not recognized
     */
    public static CommandType getCommandType(String input) {
        String command = input.trim().toLowerCase();

        if (command.equals("list")) {
            return CommandType.LIST;
        } else if (command.equals("bye")) {
            return CommandType.BYE;
        } else if (command.startsWith("todo")) {
            return CommandType.TODO;
        } else if (command.startsWith("deadline")) {
            return CommandType.DEADLINE;
        } else if (command.startsWith("event")) {
            return CommandType.EVENT;
        } else if (command.startsWith("mark")) {
            return CommandType.MARK;
        } else if (command.startsWith("unmark")) {
            return CommandType.UNMARK;
        } else if (command.startsWith("delete")) {
            return CommandType.DELETE;
        } else if (command.startsWith("find")) {
            return CommandType.FIND;
        } else if (command.startsWith("sort")) {
            return CommandType.SORT;
        } else {
            return CommandType.UNKNOWN;
        }
    }

    /**
     * Extracts the description from a todo command.
     *
     * <p>Parses input in the format: "todo DESCRIPTION"</p>
     *
     * @param input the user input string containing the todo command
     * @return the task description with leading/trailing whitespace trimmed
     * @throws EmptyDescriptionException if the description is empty or only whitespace
     */
    public static String parseTodoCommand(String input) throws StackOverflownException {
        assert input != null : "Input should not be null";
        if (input.trim().equals("todo")) {
            throw new EmptyDescriptionException("todo");
        }
        return input.substring(TODO_PARSE_VALUE).trim();
    }

    /**
     * Extracts description and due date from a deadline command.
     *
     * <p>Parses input in the format: "deadline DESCRIPTION /by DATE_TIME"</p>
     *
     * @param input the user input string containing the deadline command
     * @return string array with [description, due_date]
     * @throws EmptyDescriptionException if the description is empty
     * @throws InvalidFormatException if the format doesn't match expected pattern
     */
    public static String[] parseDeadlineCommand(String input) throws StackOverflownException {
        if (input.trim().equals("deadline")) {
            throw new EmptyDescriptionException("deadline");
        }

        String content = input.substring(DEADLINE_PARSE_VALUE).trim();
        String[] parts = content.split(" /by ");
        if (parts.length != 2) {
            throw new InvalidFormatException("deadline <DESCRIPTION> /by <DATE/TIME>");
        }

        return parts;
    }

    /**
     * Extracts description and time range from an event command.
     *
     * <p>Parses input in the format: "event DESCRIPTION /from START_TIME /to END_TIME"</p>
     *
     * @param input the user input string containing the event command
     * @return string array with [description, from_time, to_time]
     * @throws EmptyDescriptionException if the description is empty
     * @throws InvalidFormatException if the format doesn't match expected pattern
     */
    public static String[] parseEventCommand(String input) throws StackOverflownException {
        if (input.trim().equals("event")) {
            throw new EmptyDescriptionException("event");
        }

        String content = input.substring(EVENT_PARSE_VALUE).trim();
        String[] parts = content.split(" /from ");
        if (parts.length != 2) {
            throw new InvalidFormatException("event <DESCRIPTION> /from <START> /to <END>");
        }

        String[] timeParts = parts[1].split(" /to ");
        if (timeParts.length != 2) {
            throw new InvalidFormatException("event <DESCRIPTION> /from <START> /to <END>");
        }

        return new String[]{parts[0].trim(), timeParts[0].trim(), timeParts[1].trim()};
    }

    /**
     * Extracts and validates task index from mark/unmark/delete commands.
     *
     * <p>Converts 1-based user input to 0-based array index for internal use.</p>
     *
     * @param input the user input string containing the command and task number
     * @param commandLength the length of the command word (e.g., 4 for "mark", 6 for "delete")
     * @return the task index converted to 0-based indexing
     * @throws InvalidTaskNumberException if the index is not a valid number
     */
    public static int parseTaskIndex(String input, int commandLength) throws InvalidTaskNumberException {
        assert input != null : "Input should not be null";
        assert commandLength > 0 : "Prefix length should be positive";
        try {
            String indexStr = input.substring(commandLength + 1).trim();
            int taskIndex = Integer.parseInt(indexStr) - 1; // Convert to 0-based
            return taskIndex;
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            throw new InvalidTaskNumberException();
        }
    }

    /**
     * Extracts search keyword from find command.
     *
     * <p>Parses input in the format: "find KEYWORD"</p>
     *
     * @param input the user input string containing the find command
     * @return the search keyword with leading/trailing whitespace trimmed
     * @throws EmptyDescriptionException if no keyword is provided after "find"
     */
    public static String parseFindCommand(String input) throws StackOverflownException {
        if (input.trim().equals("find")) {
            throw new EmptyDescriptionException("find keyword");
        }
        return input.substring(5).trim();
    }

    /**
     * Parses sort command to extract sort type.
     *
     * @param input the full user input (e.g., "sort deadline")
     * @return the SortType to apply
     * @throws StackOverflownException if sort type is invalid or missing
     */
    public static TaskList.SortType parseSortCommand(String input) throws StackOverflownException {
        String[] parts = input.trim().split("\\s+", 2);

        if (parts.length == 1) {
            // Just "sort" with no parameters - show help
            throw new InvalidSortTypeException("Please specify a sort type. " +
                    TaskList.getSortOptionsDescription());
        }

        String sortKeyword = parts[1].toLowerCase().trim();
        TaskList.SortType sortType = TaskList.SortType.fromKeyword(sortKeyword);

        if (sortType == null) {
            throw new InvalidSortTypeException("Invalid sort type: '" + sortKeyword + "'. " +
                    TaskList.getSortOptionsDescription());
        }

        return sortType;
    }
}