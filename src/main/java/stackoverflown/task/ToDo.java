package stackoverflown.task;

/**
 * Represents a simple ToDo task without any date or time constraints.
 *
 * <p>ToDo tasks are the simplest form of task in the StackOverflown application,
 * containing only a description and completion status. They are ideal for
 * general tasks that don't have specific deadlines or time requirements.</p>
 *
 * <p>Example usage:
 * <pre>
 * ToDo todo = new ToDo("Buy groceries");
 * System.out.println(todo); // Prints: [T][ ]Buy groceries
 * </pre>
 * </p>
 *
 * @author Yeo Kai Bin
 * @version 0.1
 * @since 2025
 */
public class ToDo extends Task {

    /**
     * Constructs a new ToDo task with the specified description.
     *
     * @param description the description of the task to be done
     */
    public ToDo(String description) {
        super(description);
    }

    /**
     * Returns the type icon for ToDo tasks.
     *
     * @return "[T]" indicating this is a ToDo task
     */
    @Override
    public String getTypeIcon() {
        return "[T]";
    }


    @Override
    public String toString() {
        String temp = String.format("%s%s%s", this.getTypeIcon(), this.statusIcon(),
                this.getDescription());
        return temp;
    }
}
