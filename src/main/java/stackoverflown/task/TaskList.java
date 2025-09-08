package stackoverflown.task;

import java.util.ArrayList;
import stackoverflown.storage.Storage;
import stackoverflown.exception.StackOverflownException;
import stackoverflown.exception.EmptyDescriptionException;
import stackoverflown.exception.InvalidTaskNumberException;

/**
 * Manages a collection of tasks with CRUD operations and persistence support.
 *
 * <p>TaskList provides a comprehensive interface for managing tasks including:
 * <ul>
 * <li>Adding different types of tasks (ToDo, Deadline, Event)</li>
 * <li>Marking and unmarking task completion</li>
 * <li>Deleting tasks from the list</li>
 * <li>Automatic persistence through integrated Storage</li>
 * <li>Task retrieval and list management operations</li>
 * </ul>
 * </p>
 *
 * <p>The TaskList automatically saves changes when connected to a Storage instance,
 * ensuring data persistence across application sessions.</p>
 *
 * @author Yeo Kai Bin
 * @version 0.1
 * @since 2025
 */
public class TaskList {
    private ArrayList<Task> tasks;
    private Storage storage;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a TaskList with the provided tasks.
     *
     * @param tasks the initial list of tasks, or null for empty list
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks != null ? tasks : new ArrayList<>();
    }

    /**
     * Sets the storage component for automatic persistence.
     *
     * <p>When storage is set, all modifications to the task list will
     * automatically trigger a save operation.</p>
     *
     * @param storage the Storage instance to use for persistence
     */
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    /**
     * Adds a new ToDo task to the list.
     *
     * @param description the description of the ToDo task
     * @throws EmptyDescriptionException if the description is empty or only whitespace
     */
    public void addToDo(String description) throws EmptyDescriptionException {
        if (description.trim().isEmpty()) {
            throw new EmptyDescriptionException("todo");
        }
        ToDo newTask = new ToDo(description);
        this.tasks.add(newTask);
        autoSave();
    }

    /**
     * Adds a new Deadline task to the list.
     *
     * @param description the description of the Deadline task
     * @param byDate the due date/time in format "yyyy-mm-dd" or "yyyy-mm-dd HHmm"
     * @throws EmptyDescriptionException if description or date is empty
     * @throws StackOverflownException if date parsing fails
     */
    public void addDeadline(String description, String byDate) throws EmptyDescriptionException, StackOverflownException {
        if (description.trim().isEmpty()) {
            throw new EmptyDescriptionException("deadline");
        }
        if (byDate.trim().isEmpty()) {
            throw new EmptyDescriptionException("deadline date");
        }
        Deadline newTask = new Deadline(description, byDate);
        this.tasks.add(newTask);
        autoSave();
    }

    /**
     * Adds a new Event task to the list.
     *
     * @param description the description of the Event task
     * @param from the start date/time in format "yyyy-mm-dd HHmm"
     * @param to the end date/time in format "yyyy-mm-dd HHmm"
     * @throws EmptyDescriptionException if any parameter is empty
     * @throws StackOverflownException if date parsing fails
     */
    public void addEvent(String description, String from, String to) throws EmptyDescriptionException, StackOverflownException {
        if (description.trim().isEmpty()) {
            throw new EmptyDescriptionException("event");
        }
        if (from.trim().isEmpty()) {
            throw new EmptyDescriptionException("event start time");
        }
        if (to.trim().isEmpty()) {
            throw new EmptyDescriptionException("event end time");
        }
        Event newTask = new Event(description, from, to);
        this.tasks.add(newTask);
        autoSave();
    }

    /**
     * Removes a task from the list at the specified index.
     *
     * @param index the 0-based index of the task to delete
     * @return the deleted Task object
     * @throws InvalidTaskNumberException if index is out of bounds
     */
    public Task deleteTask(int index) throws InvalidTaskNumberException {
        if (index < 0 || index >= tasks.size()) {
            throw new InvalidTaskNumberException();
        }
        Task removedTask = tasks.remove(index);
        assert removedTask != null : "Removed task should not be null";
        autoSave();
        return removedTask; // Return the deleted task for UI display
    }

    /**
     * Marks a task as completed at the specified index.
     *
     * @param index the 0-based index of the task to mark
     * @return the marked Task object
     * @throws InvalidTaskNumberException if index is out of bounds
     */
    public Task markTask(int index) throws InvalidTaskNumberException {
        if (index < 0 || index >= tasks.size()) {
            throw new InvalidTaskNumberException();
        }
        tasks.get(index).markDone();
        assert tasks.get(index).isDone() : "Task should be marked as done after markAsDone() call";
        autoSave();
        return tasks.get(index); // Return the marked task for UI display
    }

    /**
     * Marks a task as not completed at the specified index.
     *
     * @param index the 0-based index of the task to unmark
     * @return the unmarked Task object
     * @throws InvalidTaskNumberException if index is out of bounds
     */
    public Task unmarkTask(int index) throws InvalidTaskNumberException {
        if (index < 0 || index >= tasks.size()) {
            throw new InvalidTaskNumberException();
        }
        tasks.get(index).unmarkDone();
        autoSave();
        return tasks.get(index); // Return the unmarked task for UI display
    }

    /**
     * Automatically saves the task list if storage is configured.
     *
     * <p>Silently handles save errors to avoid disrupting user experience.</p>
     */
    private void autoSave() {
        if (storage != null) {
            try {
                storage.saveTasks(tasks);
            } catch (StackOverflownException e) {
                // Silently handle save errors - don't disrupt user experience
                // In production, you might want to log this error
            }
        }
    }

    /**
     * Returns the total number of tasks in the list.
     *
     * @return the number of tasks
     */
    public int getTaskCount() {
        return this.tasks.size();
    }

    /**
     * Retrieves a task at the specified index.
     *
     * @param index the 0-based index of the task
     * @return the Task at the specified index
     * @throws IndexOutOfBoundsException if index is out of bounds
     */
    public Task getTask(int index) {
        return this.tasks.get(index);
    }

    /**
     * Checks if the task list is empty.
     *
     * @return true if no tasks exist, false otherwise
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Searches for tasks containing the specified keyword in their description.
     *
     * <p>Performs case-insensitive search through all task descriptions and
     * returns a list of matching tasks. The search matches partial strings
     * anywhere within the task description.</p>
     *
     * @param keyword the search keyword to look for in task descriptions
     * @return ArrayList of tasks whose descriptions contain the keyword
     */
    public ArrayList<Task> findTasks(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();

        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                matchingTasks.add(task);
            }
        }

        return matchingTasks;
    }

    /**
     * Returns a formatted string representation of all tasks.
     *
     * <p>If the list is empty, returns a friendly empty message.
     * Otherwise, returns a numbered list of all tasks with their status.</p>
     *
     * @return the formatted string representation of the task list
     */
    @Override
    public String toString() {
        if (tasks.isEmpty()) {
            return "Your task list is as empty as my coffee cup. Time to add some tasks!";
        }
        String result = "buckle up! Here comes your grand, magnificent, absolutely dazzling list of tasks:\n";
        for (int i = 0; i < tasks.size(); i++) {
            String temp = String.format("%s. %s\n", i + 1, this.tasks.get(i));
            result = result + temp;
        }
        return result.trim();
    }
}