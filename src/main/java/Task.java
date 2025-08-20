public abstract class Task {
    //description of the task involved
    private String description;
    //boolean flag for status of task
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isDone() {
        return this.isDone;
    }

    public void markDone() {
        this.isDone = true;
    }

    public void unmarkDone() {
        this.isDone = false;
    }

    //returns appropriate string based on task status
    public String statusIcon() {
        return this.isDone ? "[X]" : "[]";
    }

    public abstract String getTypeIcon();


}
