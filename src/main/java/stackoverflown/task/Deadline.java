package stackoverflown.task;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import stackoverflown.exception.StackOverflownException;

/**
 * Represents a task with a specific due date and optional time.
 *
 * <p>Deadline tasks extend the basic Task functionality by adding a due date/time
 * component. They support flexible date input formats and provide formatted
 * output for user display and storage persistence.</p>
 *
 * <p>Supported input formats:
 * <ul>
 * <li>"yyyy-M-d" - Date only (defaults to 11:59 PM)</li>
 * <li>"yyyy-M-d HHmm" - Date and time</li>
 * </ul>
 * </p>
 *
 * <p>Example usage:
 * <pre>
 * Deadline deadline = new Deadline("Submit assignment", "2023-12-01");
 * System.out.println(deadline); // Prints: [D][ ]Submit assignment (by: Dec 1 2023 11:59PM)
 * </pre>
 * </p>
 *
 * @author Yeo Kai Bin
 * @version 0.1
 * @since 2025
 */
public class Deadline extends Task {
    private LocalDateTime byDateTime;
    private static final DateTimeFormatter DATE_INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-M-d");
    private static final DateTimeFormatter DATETIME_INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-M-d HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy h:mma");

    /**
     * Constructs a new Deadline task with description and due date string.
     *
     * @param description the description of the deadline task
     * @param byDateTime the due date/time string in supported format
     * @throws StackOverflownException if the date/time format is invalid
     */
    public Deadline(String description, String byDateTime) throws StackOverflownException {
        super(description);
        this.byDateTime = parseDateTime(byDateTime);
    }

    /**
     * Constructs a new Deadline task with description and LocalDateTime.
     *
     * <p>This constructor is typically used when loading tasks from storage
     * where the date/time has already been parsed.</p>
     *
     * @param description the description of the deadline task
     * @param byDateTime the due date/time as LocalDateTime object
     */
    public Deadline(String description, LocalDateTime byDateTime) {
        super(description);
        this.byDateTime = byDateTime;
    }

    /**
     * Parses a date/time string into LocalDateTime object.
     *
     * <p>Supports two formats:
     * <ul>
     * <li>Date with time: "yyyy-M-d HHmm"</li>
     * <li>Date only: "yyyy-M-d" (defaults to 23:59)</li>
     * </ul>
     * </p>
     *
     * @param dateTimeString the input date/time string
     * @return the parsed LocalDateTime object
     * @throws StackOverflownException if the format is invalid or unparseable
     */
    private LocalDateTime parseDateTime(String dateTimeString) throws StackOverflownException {
        try {
            String trimmed = dateTimeString.trim();

            // Check if it contains time (has space and 4 digits for time)
            if (trimmed.matches(".*\\s\\d{4}$")) {
                // Has time: yyyy-mm-dd HHmm
                return LocalDateTime.parse(trimmed, DATETIME_INPUT_FORMAT);
            } else {
                // Only date: yyyy-mm-dd, default to 11:59 PM
                LocalDate date = LocalDate.parse(trimmed, DATE_INPUT_FORMAT);
                return date.atTime(23, 59);
            }
        } catch (Exception e) {
            throw new StackOverflownException("That date format needs work! Try yyyy-mm-dd or yyyy-mm-dd HHmm " +
                    "(like 2019-12-02 or 2019-12-02 1800)");
        }
    }

    /**
     * Returns the due date/time as LocalDateTime object.
     *
     * @return the LocalDateTime representing when this task is due
     */
    public LocalDateTime getByDateTime() {
        return this.byDateTime;
    }

    /**
     * Returns the due date/time formatted for user display.
     *
     * @return human-readable date/time string (e.g., "Dec 1 2023 11:59PM")
     */
    public String getBy() {
        return this.byDateTime.format(OUTPUT_FORMAT);
    }

    /**
     * Returns the due date/time formatted for storage persistence.
     *
     * @return storage format date/time string (e.g., "2023-12-1 2359")
     */
    public String getByForStorage() {
        return this.byDateTime.format(DATETIME_INPUT_FORMAT);
    }

    /**
     * Returns the type icon for Deadline tasks.
     *
     * @return "[D]" indicating this is a Deadline task
     */
    @Override
    public String getTypeIcon() {
        return "[D]";
    }

    /**
     * Returns a formatted string representation of this deadline task.
     *
     * <p>Format: [D][STATUS]DESCRIPTION (by: FORMATTED_DATE)</p>
     *
     * @return the formatted string representation
     */
    @Override
    public String toString() {
        return String.format("%s%s%s (by: %s)", this.getTypeIcon(), this.statusIcon(),
                this.getDescription(), getBy());
    }
}
