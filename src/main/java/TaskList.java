public class TaskList {
    //list of tasks are stored as an array of Tasks
    private Task[] tasks;
    private int taskCount;
    private static final int MAX_TASKS = 100;
    //line separation variable here to minimise duplication of code
    private String lineSeparation = "____________________________________________________________";

    public TaskList() {
        this.tasks = new Task[MAX_TASKS];
        taskCount = 0;
    }



    //methods to add various tasks types to the list
    public void addToDo(String description) {
        ToDo newTask = new ToDo(description);
        this.tasks[taskCount] = newTask;
        this.taskCount++;
    }

    public void addDeadline(String description, String byDate) {
        Deadline newTask = new Deadline(description, byDate);
        this.tasks[taskCount] = newTask;
        this.taskCount++;
    }

    public void addEvent(String description, String from, String to) {
        Event newTask = new Event(description, from, to);
        this.tasks[taskCount] = newTask;
        this.taskCount++;
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

    public void markTask(int index) {
        this.tasks[index].markDone();
        String markMessage = String.format("%s\n Boom! That task is history — marked as done and dusted\n%s\n%s",
                lineSeparation, this.tasks[index], lineSeparation);
        System.out.println(markMessage);
    }

    public void unmarkTask(int index) {
        this.tasks[index].unmarkDone();
        String unmarkMessage = String.format("%s\n Aha! This task is no longer done — it’s waiting for your magic" +
                        " touch again\n%s\n%s", lineSeparation, this.tasks[index], lineSeparation);
        System.out.println(unmarkMessage);
    }

    //override toString to simplifly handling of TaskList in other classes
    @Override
    public String toString() {
        String result = "buckle up! Here comes your grand, magnificent, absolutely dazzling list of tasks:\n";
        for (int i = 0; i < taskCount; i++) {
            String temp = String.format("%s. %s\n", i + 1, this.tasks[i]);
            result = result + temp;
        }
        return result.trim();
    }

}
