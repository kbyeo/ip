public class Event extends Task {
    private String from;
    private String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
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
