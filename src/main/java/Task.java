public class Task {
    //description of the task involved
    private String description;

    public Task(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    //override toString to simplify the handling of tasks in other classes
    @Override
    public String toString() {
        return this.description;
    }
}
