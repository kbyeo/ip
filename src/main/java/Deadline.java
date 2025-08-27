import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    private LocalDateTime byDateTime;
    private static final DateTimeFormatter DATE_INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-M-d");
    private static final DateTimeFormatter DATETIME_INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-M-d HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy h:mma");

    public Deadline(String description, String byDateTime) throws StackOverflownException {
        super(description);
        this.byDateTime = parseDateTime(byDateTime);
    }

    /**
     * Constructor for loading from storage with pre-parsed LocalDateTime.
     *
     * @param description task description
     * @param byDateTime pre-parsed LocalDateTime
     */
    public Deadline(String description, LocalDateTime byDateTime) {
        super(description);
        this.byDateTime = byDateTime;
    }

    /**
     * Parses datetime string in yyyy-mm-dd or yyyy-mm-dd HHmm format to LocalDateTime.
     * If only date is provided, defaults to 11:59 PM of that day.
     *
     * @param dateTimeString date/datetime string
     * @return LocalDateTime object
     * @throws StackOverflownException if date format is invalid
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
     * Gets the deadline datetime as LocalDateTime.
     *
     * @return the deadline datetime
     */
    public LocalDateTime getByDateTime() {
        return this.byDateTime;
    }

    /**
     * Gets the deadline datetime as formatted string for display.
     *
     * @return formatted datetime string (MMM d yyyy h:mma)
     */
    public String getBy() {
        return this.byDateTime.format(OUTPUT_FORMAT);
    }

    /**
     * Gets the deadline datetime in storage format (yyyy-mm-dd HHmm).
     *
     * @return datetime string for file storage
     */
    public String getByForStorage() {
        return this.byDateTime.format(DATETIME_INPUT_FORMAT);
    }

    @Override
    public String getTypeIcon() {
        return "[D]";
    }

    @Override
    public String toString() {
        return String.format("%s%s%s (by: %s)", this.getTypeIcon(), this.statusIcon(),
                this.getDescription(), getBy());
    }
}
