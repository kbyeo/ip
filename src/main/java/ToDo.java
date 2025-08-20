public class ToDo extends Task {
    public ToDo(String description) {
        super(description);
    }

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
