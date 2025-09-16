package stackoverflown.task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    public enum SortType {
        CREATION("creation", "Sort by creation order"),
        TYPE("type", "Sort by task type (Todo, Deadline, Event)"),
        STATUS("status", "Sort by completion status (pending first)"),
        DEADLINE("deadline", "Sort by deadline date (chronologically)"),
        ALPHABETICAL("alpha", "Sort alphabetically by description");

        private final String keyword;
        private final String description;

        SortType(String keyword, String description) {
            this.keyword = keyword;
            this.description = description;
        }

        public String getKeyword() {
            return keyword;
        }

        public String getDescription() {
            return description;
        }

        /**
         * Finds SortType by keyword.
         *
         * @param keyword the sort keyword
         * @return matching SortType or null if not found
         */
        public static SortType fromKeyword(String keyword) {
            for (SortType type : values()) {
                if (type.keyword.equalsIgnoreCase(keyword)) {
                    return type;
                }
            }
            return null;
        }
    }

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
    public void addDeadline(String description, String byDate) throws EmptyDescriptionException,
            StackOverflownException {
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
    public void addEvent(String description, String from, String to) throws EmptyDescriptionException,
            StackOverflownException {
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
     * Sorts the task list according to the specified criteria.
     *
     * @param sortType the sorting criteria to apply
     * @throws IllegalArgumentException if sortType is null
     */
    public void sortTasks(SortType sortType) {
        if (sortType == null) {
            throw new IllegalArgumentException("Sort type cannot be null");
        }

        Comparator<Task> comparator = createComparator(sortType);
        Collections.sort(tasks, comparator);
        autoSave();
    }

    /**
     * Creates appropriate comparator for the given sort type.
     *
     * @param sortType the sorting criteria
     * @return comparator for sorting tasks
     */
    private Comparator<Task> createComparator(SortType sortType) {
        switch (sortType) {
            case CREATION:
                return Comparator.comparingInt(this::getTaskCreationOrder);
            case TYPE:
                return Comparator.comparingInt(this::getTaskTypeOrder);
            case STATUS:
                return createStatusComparator();
            case DEADLINE:
                return createDeadlineComparator();
            case ALPHABETICAL:
                return Comparator.comparing(task -> task.toString().toLowerCase());
            default:
                return Comparator.comparingInt(this::getTaskCreationOrder);
        }
    }

    /**
     * Gets the creation order of a task (index in original list).
     * For creation sort, we maintain original order by using current index.
     */
    private int getTaskCreationOrder(Task task) {
        return tasks.indexOf(task);
    }
    /**
     * Gets the type priority for sorting (Todo=0, Deadline=1, Event=2).
     */
    private int getTaskTypeOrder(Task task) {
        if (task instanceof ToDo) {
            return 0;
        } else if (task instanceof Deadline) {
            return 1;
        } else if (task instanceof Event) {
            return 2;
        }
        return 3; // Unknown types go last
    }

    /**
     * Creates comparator for status-based sorting (pending first, then completed).
     */
    private Comparator<Task> createStatusComparator() {
        return (task1, task2) -> {
            boolean task1Done = task1.isDone();
            boolean task2Done = task2.isDone();

            if (task1Done == task2Done) {
                // If same status, sort by creation order
                return Integer.compare(getTaskCreationOrder(task1), getTaskCreationOrder(task2));
            }

            // Pending tasks (false) come first, completed tasks (true) come last
            return Boolean.compare(task1Done, task2Done);
        };
    }

    /**
     * Creates comparator for deadline-based sorting.
     * Deadlines are sorted chronologically, other tasks go to end.
     */
    private Comparator<Task> createDeadlineComparator() {
        return (task1, task2) -> {
            LocalDate date1 = extractDeadlineDate(task1);
            LocalDate date2 = extractDeadlineDate(task2);

            // Both are deadlines
            if (date1 != null && date2 != null) {
                return date1.compareTo(date2);
            }

            // Only task1 is deadline - comes first
            if (date1 != null && date2 == null) {
                return -1;
            }

            // Only task2 is deadline - comes first
            if (date1 == null && date2 != null) {
                return 1;
            }

            // Neither is deadline - sort by creation order
            return Integer.compare(getTaskCreationOrder(task1), getTaskCreationOrder(task2));
        };
    }

    /**
     * Extracts deadline date from a task if it's a Deadline task.
     *
     * @param task the task to check
     * @return LocalDate if task is Deadline, null otherwise
     */
    private LocalDate extractDeadlineDate(Task task) {
        if (!(task instanceof Deadline)) {
            return null;
        }

        try {
            // Extract date from Deadline toString format
            String taskStr = task.toString();
            int byIndex = taskStr.indexOf("(by: ");
            if (byIndex == -1) {
                return null;
            }

            int dateStart = byIndex + 5; // length of "(by: "
            int dateEnd = taskStr.indexOf(")", dateStart);
            if (dateEnd == -1) {
                return null;
            }

            String dateStr = taskStr.substring(dateStart, dateEnd);
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("MMM dd yyyy"));
        } catch (Exception e) {
            return null; // If parsing fails, treat as non-deadline
        }
    }

    /**
     * Gets available sort options as formatted string.
     *
     * @return formatted string describing all sort options
     */
    public static String getSortOptionsDescription() {
        StringBuilder options = new StringBuilder("Available sort options:\n");

        for (SortType sortType : SortType.values()) {
            options.append(String.format("- %s - %s\n",
                    sortType.getKeyword(), sortType.getDescription()));
        }

        options.append("\nUsage: sort [option] (e.g., 'sort deadline', 'sort status')");
        return options.toString();
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