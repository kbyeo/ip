public class Event extends Task {
    private String from;
    private String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Gets the event start time.
     *
     * @return the start time as a String
     */
    public String getFrom() {
        return this.from;
    }
    /**
     * Gets the event end time.
     *
     * @return the end time as a String
     */
    public String getTo() {
        return this.to;
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
