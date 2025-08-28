package stackoverflown.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import stackoverflown.exception.EmptyDescriptionException;
import stackoverflown.exception.InvalidFormatException;
import stackoverflown.exception.InvalidTaskNumberException;

class ParserTest {

    @Test
    @DisplayName("Should correctly identify command types")
    void getCommandType_validCommands_returnsCorrectType() {
        assertEquals(Parser.CommandType.TODO, Parser.getCommandType("todo buy milk"));
        assertEquals(Parser.CommandType.DEADLINE, Parser.getCommandType("deadline submit assignment /by 2023-12-01"));
        assertEquals(Parser.CommandType.EVENT, Parser.getCommandType("event meeting /from 2023-12-01 1400 /to 2023-12-01 1600"));
        assertEquals(Parser.CommandType.LIST, Parser.getCommandType("list"));
        assertEquals(Parser.CommandType.BYE, Parser.getCommandType("bye"));
        assertEquals(Parser.CommandType.MARK, Parser.getCommandType("mark 1"));
        assertEquals(Parser.CommandType.UNMARK, Parser.getCommandType("unmark 1"));
        assertEquals(Parser.CommandType.DELETE, Parser.getCommandType("delete 1"));
        assertEquals(Parser.CommandType.UNKNOWN, Parser.getCommandType("invalidcommand"));
    }

    @Test
    @DisplayName("Should handle command types with different casing")
    void getCommandType_differentCasing_returnsCorrectType() {
        assertEquals(Parser.CommandType.TODO, Parser.getCommandType("TODO buy milk"));
        assertEquals(Parser.CommandType.TODO, Parser.getCommandType("ToDo buy milk"));
        assertEquals(Parser.CommandType.LIST, Parser.getCommandType("LIST"));
        assertEquals(Parser.CommandType.BYE, Parser.getCommandType("BYE"));
    }

    @Test
    @DisplayName("Should parse todo command correctly")
    void parseTodoCommand_validInput_returnsDescription() throws Exception {
        assertEquals("buy milk", Parser.parseTodoCommand("todo buy milk"));
        assertEquals("finish homework", Parser.parseTodoCommand("todo finish homework"));
        assertEquals("call mom", Parser.parseTodoCommand("todo    call mom   ")); // Test trimming
    }

    @Test
    @DisplayName("Should throw exception for empty todo description")
    void parseTodoCommand_emptyDescription_throwsException() {
        assertThrows(EmptyDescriptionException.class, () -> {
            Parser.parseTodoCommand("todo");
        });

        assertThrows(EmptyDescriptionException.class, () -> {
            Parser.parseTodoCommand("todo   "); // Only whitespace
        });
    }

    @Test
    @DisplayName("Should parse deadline command correctly")
    void parseDeadlineCommand_validInput_returnsDescriptionAndDate() throws Exception {
        String[] result = Parser.parseDeadlineCommand("deadline submit assignment /by 2023-12-01");
        assertEquals("submit assignment", result[0]);
        assertEquals("2023-12-01", result[1]);

        // Test with time
        result = Parser.parseDeadlineCommand("deadline project submission /by 2023-12-01 2359");
        assertEquals("project submission", result[0]);
        assertEquals("2023-12-01 2359", result[1]);
    }

    @Test
    @DisplayName("Should throw exception for invalid deadline format")
    void parseDeadlineCommand_invalidFormat_throwsException() {
        assertThrows(EmptyDescriptionException.class, () -> {
            Parser.parseDeadlineCommand("deadline");
        });

        assertThrows(InvalidFormatException.class, () -> {
            Parser.parseDeadlineCommand("deadline submit assignment"); // Missing /by
        });

        assertThrows(InvalidFormatException.class, () -> {
            Parser.parseDeadlineCommand("deadline submit assignment /by"); // Missing date
        });
    }

    @Test
    @DisplayName("Should parse event command correctly")
    void parseEventCommand_validInput_returnsDescriptionFromTo() throws Exception {
        String[] result = Parser.parseEventCommand("event meeting /from 2023-12-01 1400 /to 2023-12-01 1600");
        assertEquals("meeting", result[0]);
        assertEquals("2023-12-01 1400", result[1]);
        assertEquals("2023-12-01 1600", result[2]);

        // Test with longer description
        result = Parser.parseEventCommand("event team building activity /from 2023-12-01 0900 /to 2023-12-01 1700");
        assertEquals("team building activity", result[0]);
        assertEquals("2023-12-01 0900", result[1]);
        assertEquals("2023-12-01 1700", result[2]);
    }

    @Test
    @DisplayName("Should throw exception for invalid event format")
    void parseEventCommand_invalidFormat_throwsException() {
        assertThrows(EmptyDescriptionException.class, () -> {
            Parser.parseEventCommand("event");
        });

        assertThrows(InvalidFormatException.class, () -> {
            Parser.parseEventCommand("event meeting /from 2023-12-01 1400"); // Missing /to
        });

        assertThrows(InvalidFormatException.class, () -> {
            Parser.parseEventCommand("event meeting /to 2023-12-01 1600"); // Missing /from
        });

        assertThrows(InvalidFormatException.class, () -> {
            Parser.parseEventCommand("event meeting"); // Missing both
        });
    }

    @Test
    @DisplayName("Should parse task index correctly")
    void parseTaskIndex_validInput_returnsZeroBasedIndex() throws Exception {
        assertEquals(0, Parser.parseTaskIndex("mark 1", 4));
        assertEquals(4, Parser.parseTaskIndex("mark 5", 4));
        assertEquals(2, Parser.parseTaskIndex("delete 3", 6));
        assertEquals(0, Parser.parseTaskIndex("unmark 1", 6));
    }

    @Test
    @DisplayName("Should throw exception for invalid task index")
    void parseTaskIndex_invalidInput_throwsException() {
        assertThrows(InvalidTaskNumberException.class, () -> {
            Parser.parseTaskIndex("mark abc", 4);
        });

        assertThrows(InvalidTaskNumberException.class, () -> {
            Parser.parseTaskIndex("mark", 4); // Missing number
        });

        assertThrows(InvalidTaskNumberException.class, () -> {
            Parser.parseTaskIndex("mark ", 4); // Only space
        });
    }
}