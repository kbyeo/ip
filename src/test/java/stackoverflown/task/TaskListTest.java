package stackoverflown.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import stackoverflown.exception.EmptyDescriptionException;
import stackoverflown.exception.InvalidTaskNumberException;
import stackoverflown.exception.StackOverflownException;

class TaskListTest {

    private TaskList taskList;

    @BeforeEach
    void setUp() {
        taskList = new TaskList();
    }

    @Test
    @DisplayName("Should start with empty task list")
    void constructor_newTaskList_isEmpty() {
        assertTrue(taskList.isEmpty());
        assertEquals(0, taskList.getTaskCount());
    }

    @Test
    @DisplayName("Should add ToDo task correctly")
    void addToDo_validDescription_addsTask() throws EmptyDescriptionException {
        taskList.addToDo("buy milk");

        assertEquals(1, taskList.getTaskCount());
        assertFalse(taskList.isEmpty());

        Task addedTask = taskList.getTask(0);
        assertEquals("buy milk", addedTask.getDescription());
        assertEquals("[T]", addedTask.getTypeIcon());
        assertFalse(addedTask.isDone());
    }

    @Test
    @DisplayName("Should throw exception for empty ToDo description")
    void addToDo_emptyDescription_throwsException() {
        assertThrows(EmptyDescriptionException.class, () -> {
            taskList.addToDo("");
        });

        assertThrows(EmptyDescriptionException.class, () -> {
            taskList.addToDo("   "); // Only whitespace
        });

        assertEquals(0, taskList.getTaskCount()); // No task should be added
    }

    @Test
    @DisplayName("Should add Deadline task correctly")
    void addDeadline_validInput_addsTask() throws Exception {
        taskList.addDeadline("submit assignment", "2023-12-01");

        assertEquals(1, taskList.getTaskCount());

        Task addedTask = taskList.getTask(0);
        assertEquals("submit assignment", addedTask.getDescription());
        assertEquals("[D]", addedTask.getTypeIcon());
        assertFalse(addedTask.isDone());
        assertTrue(addedTask instanceof Deadline);
    }

    @Test
    @DisplayName("Should throw exception for empty Deadline description or date")
    void addDeadline_emptyFields_throwsException() {
        assertThrows(EmptyDescriptionException.class, () -> {
            taskList.addDeadline("", "2023-12-01");
        });

        assertThrows(EmptyDescriptionException.class, () -> {
            taskList.addDeadline("submit assignment", "");
        });

        assertThrows(EmptyDescriptionException.class, () -> {
            taskList.addDeadline("   ", "2023-12-01");
        });
    }

    @Test
    @DisplayName("Should add Event task correctly")
    void addEvent_validInput_addsTask() throws Exception {
        taskList.addEvent("meeting", "2023-12-01 1400", "2023-12-01 1600");

        assertEquals(1, taskList.getTaskCount());

        Task addedTask = taskList.getTask(0);
        assertEquals("meeting", addedTask.getDescription());
        assertEquals("[E]", addedTask.getTypeIcon());
        assertFalse(addedTask.isDone());
        assertTrue(addedTask instanceof Event);
    }

    @Test
    @DisplayName("Should throw exception for empty Event fields")
    void addEvent_emptyFields_throwsException() {
        assertThrows(EmptyDescriptionException.class, () -> {
            taskList.addEvent("", "2023-12-01 1400", "2023-12-01 1600");
        });

        assertThrows(EmptyDescriptionException.class, () -> {
            taskList.addEvent("meeting", "", "2023-12-01 1600");
        });

        assertThrows(EmptyDescriptionException.class, () -> {
            taskList.addEvent("meeting", "2023-12-01 1400", "");
        });
    }

    @Test
    @DisplayName("Should mark task as done correctly")
    void markTask_validIndex_marksTaskDone() throws Exception {
        taskList.addToDo("buy milk");

        Task markedTask = taskList.markTask(0);

        assertTrue(markedTask.isDone());
        assertTrue(taskList.getTask(0).isDone());
    }

    @Test
    @DisplayName("Should throw exception for invalid mark index")
    void markTask_invalidIndex_throwsException() throws Exception {
        taskList.addToDo("buy milk");

        assertThrows(InvalidTaskNumberException.class, () -> {
            taskList.markTask(-1);
        });

        assertThrows(InvalidTaskNumberException.class, () -> {
            taskList.markTask(1); // Out of bounds
        });

        assertThrows(InvalidTaskNumberException.class, () -> {
            taskList.markTask(10); // Way out of bounds
        });
    }

    @Test
    @DisplayName("Should unmark task correctly")
    void unmarkTask_validIndex_unmarksTask() throws Exception {
        taskList.addToDo("buy milk");
        taskList.markTask(0); // Mark it first

        Task unmarkedTask = taskList.unmarkTask(0);

        assertFalse(unmarkedTask.isDone());
        assertFalse(taskList.getTask(0).isDone());
    }

    @Test
    @DisplayName("Should delete task correctly")
    void deleteTask_validIndex_deletesTask() throws Exception {
        taskList.addToDo("buy milk");
        taskList.addToDo("buy bread");

        Task deletedTask = taskList.deleteTask(0);

        assertEquals("buy milk", deletedTask.getDescription());
        assertEquals(1, taskList.getTaskCount());
        assertEquals("buy bread", taskList.getTask(0).getDescription());
    }

    @Test
    @DisplayName("Should throw exception for invalid delete index")
    void deleteTask_invalidIndex_throwsException() throws Exception {
        taskList.addToDo("buy milk");

        assertThrows(InvalidTaskNumberException.class, () -> {
            taskList.deleteTask(-1);
        });

        assertThrows(InvalidTaskNumberException.class, () -> {
            taskList.deleteTask(1); // Out of bounds
        });
    }

    @Test
    @DisplayName("Should handle multiple task operations")
    void multipleOperations_mixedTasks_worksCorrectly() throws Exception {
        // Add different types of tasks
        taskList.addToDo("buy milk");
        taskList.addDeadline("submit assignment", "2023-12-01");
        taskList.addEvent("meeting", "2023-12-01 1400", "2023-12-01 1600");

        assertEquals(3, taskList.getTaskCount());

        // Mark some tasks
        taskList.markTask(0);
        taskList.markTask(2);

        assertTrue(taskList.getTask(0).isDone());
        assertFalse(taskList.getTask(1).isDone());
        assertTrue(taskList.getTask(2).isDone());

        // Delete a task
        taskList.deleteTask(1);
        assertEquals(2, taskList.getTaskCount());
    }

    @Test
    @DisplayName("Should display correct string representation")
    void toString_withTasks_displaysCorrectFormat() throws Exception {
        // Test empty list
        String emptyResult = taskList.toString();
        assertTrue(emptyResult.contains("empty"));

        // Add tasks
        taskList.addToDo("buy milk");
        taskList.addDeadline("submit assignment", "2023-12-01");
        taskList.markTask(0);

        String result = taskList.toString();
        assertTrue(result.contains("1."));
        assertTrue(result.contains("2."));
        assertTrue(result.contains("buy milk"));
        assertTrue(result.contains("submit assignment"));
    }
}