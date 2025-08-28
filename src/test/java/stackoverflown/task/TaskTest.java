package stackoverflown.task;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    @DisplayName("Should create ToDo task correctly")
    void toDo_creation_setsPropertiesCorrectly() {
        ToDo todo = new ToDo("buy milk");

        assertEquals("buy milk", todo.getDescription());
        assertEquals("[T]", todo.getTypeIcon());
        assertFalse(todo.isDone());
        assertEquals("[]", todo.statusIcon());
    }

    @Test
    @DisplayName("Should mark and unmark ToDo task")
    void toDo_markAndUnmark_changesStatus() {
        ToDo todo = new ToDo("buy milk");

        todo.markDone();
        assertTrue(todo.isDone());
        assertEquals("[X]", todo.statusIcon());

        todo.unmarkDone();
        assertFalse(todo.isDone());
        assertEquals("[]", todo.statusIcon());
    }

    @Test
    @DisplayName("Should format ToDo toString correctly")
    void toDo_toString_formatsCorrectly() {
        ToDo todo = new ToDo("buy milk");
        assertEquals("[T][]buy milk", todo.toString());

        todo.markDone();
        assertEquals("[T][X]buy milk", todo.toString());
    }

    @Test
    @DisplayName("Should create Deadline task with valid date")
    void deadline_validDate_createsCorrectly() throws Exception {
        Deadline deadline = new Deadline("submit assignment", "2023-12-01");

        assertEquals("submit assignment", deadline.getDescription());
        assertEquals("[D]", deadline.getTypeIcon());
        assertFalse(deadline.isDone());
        assertNotNull(deadline.getByDateTime());
    }

    @Test
    @DisplayName("Should create Deadline task with date and time")
    void deadline_validDateTime_createsCorrectly() throws Exception {
        Deadline deadline = new Deadline("submit assignment", "2023-12-01 2359");

        assertEquals("submit assignment", deadline.getDescription());
        assertEquals("[D]", deadline.getTypeIcon());
        assertNotNull(deadline.getByDateTime());
        assertTrue(deadline.toString().contains("by:"));
    }

    @Test
    @DisplayName("Should throw exception for invalid Deadline date format")
    void deadline_invalidDate_throwsException() {
        assertThrows(Exception.class, () -> {
            new Deadline("submit assignment", "invalid-date");
        });

        assertThrows(Exception.class, () -> {
            new Deadline("submit assignment", "2023/12/01"); // Wrong format
        });
    }

    @Test
    @DisplayName("Should create Event task with valid times")
    void event_validTimes_createsCorrectly() throws Exception {
        Event event = new Event("meeting", "2023-12-01 1400", "2023-12-01 1600");

        assertEquals("meeting", event.getDescription());
        assertEquals("[E]", event.getTypeIcon());
        assertFalse(event.isDone());
        assertNotNull(event.getFromDateTime());
        assertNotNull(event.getToDateTime());
    }

    @Test
    @DisplayName("Should throw exception for invalid Event time format")
    void event_invalidTimes_throwsException() {
        assertThrows(Exception.class, () -> {
            new Event("meeting", "2023-12-01", "2023-12-01 1600"); // Missing time in 'from'
        });

        assertThrows(Exception.class, () -> {
            new Event("meeting", "invalid-time", "2023-12-01 1600");
        });
    }

    @Test
    @DisplayName("Should format Event toString correctly")
    void event_toString_formatsCorrectly() throws Exception {
        Event event = new Event("meeting", "2023-12-01 1400", "2023-12-01 1600");

        String result = event.toString();
        assertTrue(result.contains("[E]"));
        assertTrue(result.contains("meeting"));
        assertTrue(result.contains("from:"));
        assertTrue(result.contains("to:"));
    }
}