import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    private LocalDateTime from;
    private LocalDateTime to;
    private static final DateTimeFormatter DATE_INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-M-d");
    private static final DateTimeFormatter DATETIME_INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-M-d HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy h:mma");

    public Event(String description, String from, String to) throws StackOverflownException {
        super(description);
        this.from = parseDateTime(from);
        this.to = parseDateTime(to);
    }

    /**
     * Constructor for loading from storage with pre-parsed LocalDateTime.
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
     * Gets the event start time as LocalDateTime.
     */
    public LocalDateTime getFromDateTime() {
        return this.from;
    }

    /**
     * Gets the event end time as LocalDateTime.
     */
    public LocalDateTime getToDateTime() {
        return this.to;
    }

    /**
     * Gets the event start time as formatted string for display.
     */
    public String getFrom() {
        return this.from.format(OUTPUT_FORMAT);
    }

    /**
     * Gets the event end time as formatted string for display.
     */
    public String getTo() {
        return this.to.format(OUTPUT_FORMAT);
    }

    /**
     * Gets the event start time for storage.
     */
    public String getFromForStorage() {
        return this.from.format(DATETIME_INPUT_FORMAT);
    }

    /**
     * Gets the event end time for storage.
     */
    public String getToForStorage() {
        return this.to.format(DATETIME_INPUT_FORMAT);
    }

    @Override
    public String getTypeIcon() {
        return "[E]";
    }

    @Override
    public String toString() {
        String temp = String.format("%s%s%s (from: %s to: %s)", this.getTypeIcon(), this.statusIcon(),
                this.getDescription(), this.from, this.to);
        return temp;
    }
}
