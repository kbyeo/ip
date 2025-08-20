public class TaskList {
    //list of tasks are stored as an array of Tasks
    private Task[] tasks;
    private int taskCount;
    private static final int MAX_TASKS = 100;

    public TaskList() {
        this.tasks = new Task[MAX_TASKS];
        taskCount = 0;
    }

    public void addTask(String description) {
        //create a new Task first
        Task newTask = new Task(description);
        //add it to the list of current tasks and increment task count
        tasks[taskCount] = newTask;
        taskCount++;
    }

    //get the task count
    public int getTaskCount() {
        return this.taskCount;
    }

    //get the task at index
    public Task getTask(int index) {
        return this.tasks[index];
    }

    public boolean isEmpty() {
        return taskCount == 0;
    }

    //override toString to simplifly handling of TaskList in other classes
    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < taskCount; i++) {
            String temp = String.format("%s. %s\n", i + 1, this.tasks[i]);
            result = result + temp;
        }
        return result.trim();
    }

}
