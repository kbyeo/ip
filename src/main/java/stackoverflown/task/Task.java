package stackoverflown.task;

/**
 * Abstract base class for all task types in the StackOverflown application.
 *
 * <p>This class provides common functionality for task management including
 * task completion status, description storage, and basic task operations.
 * All concrete task types (ToDo, Deadline, Event) extend this base class.</p>
 *
 * <p>Each task maintains:
 * <ul>
 * <li>A description of what needs to be done</li>
 * <li>A completion status (done/not done)</li>
 * <li>A type-specific icon for display purposes</li>
 * </ul>
 * </p>
 *
 * @author Yeo Kai Bin
 * @version 0.1
 * @since 2025
 */
public abstract class Task {
    //description of the task involved
    private String description;
    //boolean flag for status of task
    private boolean isDone;

    /**
     * Constructs a new Task with the specified description.
     *
     * <p>The task is initially marked as not done (false).</p>
     *
     * @param description the description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the description of this task.
     *
     * @return the task description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns whether this task has been completed.
     *
     * @return true if the task is done, false otherwise
     */
    public boolean isDone() {
        return this.isDone;
    }

    /**
     * Marks this task as completed.
     */
    public void markDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not completed.
     */
    public void unmarkDone() {
        this.isDone = false;
    }

    /**
     * Returns the status icon representing completion state.
     *
     * @return "[X]" if done, "[ ]" if not done
     */
    public String statusIcon() {
        return this.isDone ? "[X]" : "[]";
    }

    /**
     * Returns the type-specific icon for this task.
     *
     * <p>This method must be implemented by concrete subclasses to provide
     * their specific type identifier (e.g., "[T]", "[D]", "[E]").</p>
     *
     * @return the type icon string for this task type
     */
    public abstract String getTypeIcon();


}
