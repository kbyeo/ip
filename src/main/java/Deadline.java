public class Deadline extends Task {
    private String byDate;

    public Deadline(String description, String byDate) {
        super(description);
        this.byDate = byDate;
    }

    /**
     * Gets the deadline date/time.
     *
     * @return the deadline date/time as a String
     */
    public String getBy() {
        return this.byDate;
    }

    @Override
    public String getTypeIcon() {
        return "[D]";
    }

    @Override
    public String toString() {
        String temp = String.format("%s%s%s (by: %s)", this.getTypeIcon(), this.statusIcon(),
                this.getDescription(), this.byDate);
        return temp;
    }
}
