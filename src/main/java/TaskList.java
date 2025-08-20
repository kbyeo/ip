import java.util.ArrayList;

public class TaskList {
    //list of tasks are stored as an array of Tasks
    private ArrayList<Task> tasks;
    private int taskCount;
    private static final int MAX_TASKS = 100;
    //line separation variable here to minimise duplication of code
    private String lineSeparation = "____________________________________________________________";

    public TaskList() {
        this.tasks = new ArrayList<>();
        taskCount = 0;
    }



    //methods to add various tasks types to the list
    public void addToDo(String description) throws EmptyDescriptionException {
        if (description.trim().isEmpty()) {
            throw new EmptyDescriptionException("todo");
        }
        ToDo newTask = new ToDo(description);
        this.tasks.add(newTask);
        this.taskCount++;
    }

    public void addDeadline(String description, String byDate) throws EmptyDescriptionException {
        if (description.trim().isEmpty()) {
            throw new EmptyDescriptionException("deadline");
        }
        if (byDate.trim().isEmpty()) {
            throw new EmptyDescriptionException("deadline date");
        }
        Deadline newTask = new Deadline(description, byDate);
        this.tasks.add(newTask);
        this.taskCount++;
    }

    public void addEvent(String description, String from, String to) throws EmptyDescriptionException {
        if (description.trim().isEmpty()) {
            throw new EmptyDescriptionException("event");
        }
        if (from.trim().isEmpty()) {
            throw new EmptyDescriptionException("event start time");
        }
        if (to.trim().isEmpty()) {
            throw new EmptyDescriptionException("event end time");
        }
        Event newTask = new Event(description, from, to);
        this.tasks.add(newTask);
        this.taskCount++;
    }

    public void deleteTask(int index) throws InvalidTaskNumberException {
        if (index < 0 || index >= tasks.size()) {
            throw new InvalidTaskNumberException();
        }
        Task removedTask = tasks.remove(index);
        this.taskCount--;
        String deleteMessage = String.format("%s\n Poof! Task vanished from existence:\n   %s\n Your task arsenal now stands at %d strong!\n%s",
                lineSeparation, removedTask, tasks.size(), lineSeparation);
        System.out.println(deleteMessage);
    }

    //get the task count
    public int getTaskCount() {
        return this.taskCount;
    }

    //get the task at index
    public Task getTask(int index) {
        return this.tasks.get(index);
    }

    public boolean isEmpty() {
        return taskCount == 0;
    }

    public void markTask(int index) throws InvalidTaskNumberException {
        if (index < 0 || index >= taskCount) {
            throw new InvalidTaskNumberException();
        }
        tasks.get(index).markDone();
        String markMessage = String.format("%s\n Boom! That task is history - marked as done and dusted\n%s\n%s",
                lineSeparation, this.tasks.get(index), lineSeparation);
        System.out.println(markMessage);
    }

    public void unmarkTask(int index) throws InvalidTaskNumberException {
        if (index < 0 || index >= taskCount) {
            throw new InvalidTaskNumberException();
        }
        this.tasks.get(index).unmarkDone();
        String unmarkMessage = String.format("%s\n Aha! This task is no longer done - it's waiting for your magic" +
                        " touch again\n%s\n%s", lineSeparation, this.tasks.get(index), lineSeparation);
        System.out.println(unmarkMessage);
    }

    //override toString to simplifly handling of TaskList in other classes
    @Override
    public String toString() {
        String result = "buckle up! Here comes your grand, magnificent, absolutely dazzling list of tasks:\n";
        for (int i = 0; i < taskCount; i++) {
            String temp = String.format("%s. %s\n", i + 1, this.tasks.get(i));
            result = result + temp;
        }
        return result.trim();
    }

}
