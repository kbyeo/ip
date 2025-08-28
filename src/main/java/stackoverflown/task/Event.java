package stackoverflown.task;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import stackoverflown.exception.StackOverflownException;

/**
 * Represents a task with specific start and end date/time periods.
 *
 * <p>Event tasks extend the basic Task functionality by adding start and end
 * date/time components, making them suitable for meetings, appointments, and
 * time-bounded activities. They support flexible date-time input formats and
 * provide formatted output for user display and storage persistence.</p>
 *
 * <p>Required input format:
 * <ul>
 * <li>"yyyy-M-d HHmm" - Both start and end times must include date and time</li>
 * </ul>
 * </p>
 *
 * <p>Example usage:
 * <pre>
 * Event event = new Event("Team meeting", "2023-12-01 1400", "2023-12-01 1600");
 * System.out.println(event);
 * // Prints: [E][ ]Team meeting (from: Dec 1 2023 2:00PM to: Dec 1 2023 4:00PM)
 * </pre>
 * </p>
 *
 * @author Yeo Kai Bin
 * @version 0.1
 * @since 2025
 */
public class Event extends Task {
    private LocalDateTime from;
    private LocalDateTime to;
    private static final DateTimeFormatter DATE_INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-M-d");
    private static final DateTimeFormatter DATETIME_INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-M-d HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy h:mma");

    /**
     * Constructs a new Event task with description and time period strings.
     *
     * <p>Both from and to parameters must be in the format "yyyy-M-d HHmm".
     * The method validates that both times can be parsed successfully.</p>
     *
     * @param description the description of the event
     * @param from the start date/time string in format "yyyy-M-d HHmm"
     * @param to the end date/time string in format "yyyy-M-d HHmm"
     * @throws StackOverflownException if either date/time format is invalid
     */
    public Event(String description, String from, String to) throws StackOverflownException {
        super(description);
        this.from = parseDateTime(from);
        this.to = parseDateTime(to);
    }

    /**
     * Constructs a new Event task with description and LocalDateTime objects.
     *
     * <p>This constructor is typically used when loading events from storage
     * where the date/time values have already been parsed.</p>
     *
     * @param description the description of the event
     * @param from the start date/time as LocalDateTime object
     * @param to the end date/time as LocalDateTime object
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Parses datetime string in yyyy-mm-dd or yyyy-mm-dd HHmm format to LocalDateTime.
     * If only date is provided, from defaults to 12:00 AM, to defaults to 11:59 PM.
     *
     * @param dateTimeString datetime string
     * @return LocalDateTime object
     * @throws StackOverflownException if datetime format is invalid
     */
    private LocalDateTime parseDateTime(String dateTimeString) throws StackOverflownException {
        try {
            String trimmed = dateTimeString.trim();

            // Check if it contains time (has space and 4 digits for time)
            if (trimmed.matches(".*\\s\\d{4}$")) {
                // Has time: yyyy-mm-dd HHmm
                return LocalDateTime.parse(trimmed, DATETIME_INPUT_FORMAT);
            } else {
                // Only date: yyyy-mm-dd
                LocalDate date = LocalDate.parse(trimmed, DATE_INPUT_FORMAT);
                // For events, we can't assume default times, so require time
                throw new StackOverflownException("Events need specific times! Try yyyy-mm-dd HHmm " +
                        "format (like 2019-12-02 1400)");
            }
        } catch (StackOverflownException e) {
            throw e; // Re-throw our custom message
        } catch (Exception e) {
            throw new StackOverflownException("That time format needs work! Try yyyy-mm-dd HHmm " +
                    "(like 2019-12-02 1800)");
        }
    }

    /**
     * Returns the start date/time as LocalDateTime object.
     *
     * @return the LocalDateTime representing when this event starts
     */
    public LocalDateTime getFromDateTime() {
        return this.from;
    }

    /**
     * Returns the end date/time as LocalDateTime object.
     *
     * @return the LocalDateTime representing when this event ends
     */
    public LocalDateTime getToDateTime() {
        return this.to;
    }

    /**
     * Returns the start date/time formatted for user display.
     *
     * @return human-readable start date/time string (e.g., "Dec 1 2023 2:00PM")
     */
    public String getFrom() {
        return this.from.format(OUTPUT_FORMAT);
    }

    /**
     * Returns the end date/time formatted for user display.
     *
     * @return human-readable end date/time string (e.g., "Dec 1 2023 4:00PM")
     */
    public String getTo() {
        return this.to.format(OUTPUT_FORMAT);
    }

    /**
     * Returns the start date/time formatted for storage persistence.
     *
     * @return storage format start date/time string (e.g., "2023-12-1 1400")
     */
    public String getFromForStorage() {
        return this.from.format(DATETIME_INPUT_FORMAT);
    }

    /**
     * Returns the end date/time formatted for storage persistence.
     *
     * @return storage format end date/time string (e.g., "2023-12-1 1600")
     */
    public String getToForStorage() {
        return this.to.format(DATETIME_INPUT_FORMAT);
    }

    /**
     * Returns the type icon for Event tasks.
     *
     * @return "[E]" indicating this is an Event task
     */
    @Override
    public String getTypeIcon() {
        return "[E]";
    }

    /**
     * Returns a formatted string representation of this event task.
     *
     * <p>Format: [E][STATUS]DESCRIPTION (from: START_TIME to: END_TIME)</p>
     *
     * @return the formatted string representation including time period
     */
    @Override
    public String toString() {
        return String.format("%s%s%s (from: %s to: %s)", this.getTypeIcon(), this.statusIcon(),
                this.getDescription(), getFrom(), getTo());
    }
}
