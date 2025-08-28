package stackoverflown.task;

import stackoverflown.storage.Storage;

import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> tasks;
    private Storage storage;

    /**
     * Constructor that initializes empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructor that initializes task list with existing tasks.
     *
     * @param tasks ArrayList of existing tasks
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks != null ? tasks : new ArrayList<>();
    }

    /**
     * Sets the storage for auto-saving functionality.
     *
     * @param storage Storage instance for file operations
     */
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    // Update all task modification methods to remove UI logic and simplify auto-save
    public void addToDo(String description) throws EmptyDescriptionException {
        if (description.trim().isEmpty()) {
            throw new EmptyDescriptionException("todo");
        }
        ToDo newTask = new ToDo(description);
        this.tasks.add(newTask);
        autoSave();
    }

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

    public Task deleteTask(int index) throws InvalidTaskNumberException {
        if (index < 0 || index >= tasks.size()) {
            throw new InvalidTaskNumberException();
        }
        Task removedTask = tasks.remove(index);
        autoSave();
        return removedTask; // Return the deleted task for UI display
    }

    public Task markTask(int index) throws InvalidTaskNumberException {
        if (index < 0 || index >= tasks.size()) {
            throw new InvalidTaskNumberException();
        }
        tasks.get(index).markDone();
        autoSave();
        return tasks.get(index); // Return the marked task for UI display
    }

    public Task unmarkTask(int index) throws InvalidTaskNumberException {
        if (index < 0 || index >= tasks.size()) {
            throw new InvalidTaskNumberException();
        }
        tasks.get(index).unmarkDone();
        autoSave();
        return tasks.get(index); // Return the unmarked task for UI display
    }

    /**
     * Automatically saves tasks after any modification.
     * Silently handles save errors to avoid disrupting user experience.
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

    public int getTaskCount() {
        return this.tasks.size();
    }

    public Task getTask(int index) {
        return this.tasks.get(index);
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

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