package stackoverflown.parser;

import stackoverflown.exception.StackOverflownException;
import stackoverflown.exception.EmptyDescriptionException;
import stackoverflown.exception.InvalidFormatException;
import stackoverflown.exception.InvalidTaskNumberException;

/**
 * Parses user input commands and extracts relevant information.
 * Handles command validation and parameter extraction.
 */
public class Parser {

    /**
     * Represents different types of commands the user can input.
     */
    public enum CommandType {
        TODO, DEADLINE, EVENT, LIST, MARK, UNMARK, DELETE, BYE, FIND, UNKNOWN
    }

    /**
     * Parses user input and returns the command type.
     *
     * @param input user input string
     * @return CommandType enum representing the command
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
        } else if (command.startsWith("mark ")) {
            return CommandType.MARK;
        } else if (command.startsWith("unmark ")) {
            return CommandType.UNMARK;
        } else if (command.startsWith("delete ")) {
            return CommandType.DELETE;
        } else if (command.startsWith("find ")) {
            return CommandType.FIND;
        } else {
            return CommandType.UNKNOWN;
        }
    }

    /**
     * Extracts todo description from user input.
     *
     * @param input user input string
     * @return todo description
     * @throws StackOverflownException if description is empty
     */
    public static String parseTodoCommand(String input) throws StackOverflownException {
        if (input.trim().equals("todo")) {
            throw new EmptyDescriptionException("todo");
        }
        return input.substring(5).trim();
    }

    /**
     * Extracts deadline information from user input.
     *
     * @param input user input string
     * @return String array with [description, by_date]
     * @throws StackOverflownException if format is invalid or description is empty
     */
    public static String[] parseDeadlineCommand(String input) throws StackOverflownException {
        if (input.trim().equals("deadline")) {
            throw new EmptyDescriptionException("deadline");
        }

        String content = input.substring(9).trim();
        String[] parts = content.split(" /by ");
        if (parts.length != 2) {
            throw new InvalidFormatException("deadline <DESCRIPTION> /by <DATE/TIME>");
        }

        return parts;
    }

    /**
     * Extracts event information from user input.
     *
     * @param input user input string
     * @return String array with [description, from_time, to_time]
     * @throws StackOverflownException if format is invalid or description is empty
     */
    public static String[] parseEventCommand(String input) throws StackOverflownException {
        if (input.trim().equals("event")) {
            throw new EmptyDescriptionException("event");
        }

        String content = input.substring(6).trim();
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
     * Extracts task index from mark/unmark/delete commands.
     *
     * @param input user input string
     * @param commandLength length of the command word (4 for "mark", 6 for "unmark", 6 for "delete")
     * @return task index (0-based)
     * @throws InvalidTaskNumberException if index is invalid
     */
    public static int parseTaskIndex(String input, int commandLength) throws InvalidTaskNumberException {
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
}