import java.util.Scanner;

public class StackOverflown {

    public static void main(String[] args) {
        String lineSeparation = "____________________________________________________________";
        String botName = "StackOverflown";
        String exitLine = "Aww, you're leaving already? It's been such a\n pleasure, can't wait till next time! :)";
        String decoratedExit = String.format("%s\n %s\n%s", lineSeparation, exitLine, lineSeparation);
        String introLine = String.format("Hey! %s here, thrilled to see you!\n Let's dive RIGHT in, what can I do for you? :)", botName);
        String decoratedIntro = String.format("%s\n %s\n%s", lineSeparation, introLine, lineSeparation);

        Storage storage = new Storage();
        TaskList currentTasks = new TaskList(storage);

        System.out.println(decoratedIntro);
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();

        while (!userInput.equals("bye")) {
            try {
                handleCommand(currentTasks, userInput, lineSeparation);
            } catch (StackOverflownException e) {
                System.out.println(lineSeparation + "\n " + e.getMessage() + "\n" + lineSeparation);
            }
            userInput = scanner.nextLine();
        }
        System.out.println(decoratedExit);
        scanner.close();

    }

    private static void handleCommand(TaskList currentTasks, String input, String lineSeparation) throws StackOverflownException {
        if (input.equals("list")) {
            String listDisplay = String.format("%s\n%s\n%s", lineSeparation, currentTasks, lineSeparation);
            System.out.println(listDisplay);
        } else if (input.startsWith("mark ")) {
            handleMarkCommand(currentTasks, input, lineSeparation);
        } else if (input.startsWith("unmark ")) {
            handleUnmarkCommand(currentTasks, input, lineSeparation);
        } else if (input.startsWith("todo ")) {
            handleTodoCommand(currentTasks, input, lineSeparation);
        } else if (input.startsWith("deadline ")) {
            handleDeadlineCommand(currentTasks, input, lineSeparation);
        } else if (input.startsWith("event ")) {
            handleEventCommand(currentTasks, input, lineSeparation);
        } else if (input.equals("todo")) {
            throw new EmptyDescriptionException("todo");
        } else if (input.equals("deadline")) {
            throw new EmptyDescriptionException("deadline");
        } else if (input.equals("event")) {
            throw new EmptyDescriptionException("event");
        } else if (input.startsWith("delete ")) {
            handleDeleteCommand(currentTasks, input, lineSeparation);
        } else {
            throw new InvalidCommandException(input);
        }
    }
    private static void handleMarkCommand(TaskList currentTasks, String input, String lineSeparation) throws InvalidTaskNumberException {
        try {
            int taskIndex = Integer.parseInt(input.substring(5)) - 1;
            currentTasks.markTask(taskIndex);
        } catch (NumberFormatException e) {
            throw new InvalidTaskNumberException();
        }
    }
    private static void handleUnmarkCommand(TaskList currentTasks, String input, String lineSeparation) throws InvalidTaskNumberException {
        try {
            int taskIndex = Integer.parseInt(input.substring(7)) - 1;
            currentTasks.unmarkTask(taskIndex);
        } catch (NumberFormatException e) {
            throw new InvalidTaskNumberException();
        }
    }
    private static void handleTodoCommand(TaskList currentTasks, String input, String lineSeparation) throws EmptyDescriptionException {
        String description = input.substring(5);
        currentTasks.addToDo(description);
        int taskNumber = currentTasks.getTaskCount();
        String addedMessage = String.format("%s\nBoom! A ToDo task just joined the party: %s\nYour task arsenal now stands at %s strong!\n%s",
                lineSeparation, currentTasks.getTask(taskNumber - 1), taskNumber, lineSeparation);
        System.out.println(addedMessage);
    }
    private static void handleDeadlineCommand(TaskList currentTasks, String input, String lineSeparation) throws InvalidFormatException, EmptyDescriptionException {
        String content = input.substring(9);
        String[] parts = content.split(" /by ");
        if (parts.length != 2) {
            throw new InvalidFormatException("deadline <DESCRIPTION> /by <TIME>");
        }

        currentTasks.addDeadline(parts[0], parts[1]);
        int taskNumber = currentTasks.getTaskCount();
        String addedMessage = String.format("%s\nAll set! A Deadline task just joined the party: %s\nYour task arsenal now stands at %s strong!\n%s",
                lineSeparation, currentTasks.getTask(taskNumber - 1), taskNumber, lineSeparation);
        System.out.println(addedMessage);
    }
    private static void handleEventCommand(TaskList currentTasks, String input, String lineSeparation) throws InvalidFormatException, EmptyDescriptionException {
        String content = input.substring(6);
        String[] parts = content.split(" /from ");
        if (parts.length != 2) {
            throw new InvalidFormatException("event <DESCRIPTION> /from <START> /to <END>");
        }

        String[] timeParts = parts[1].split(" /to ");
        if (timeParts.length != 2) {
            throw new InvalidFormatException("event <DESCRIPTION> /from <START> /to <END>");
        }

        currentTasks.addEvent(parts[0], timeParts[0], timeParts[1]);
        int taskNumber = currentTasks.getTaskCount();
        String addedMessage = String.format("%s\nTada! An Event task just joined the party: %s\nYour task arsenal now stands at %s strong!\n%s",
                lineSeparation, currentTasks.getTask(taskNumber - 1), taskNumber, lineSeparation);
        System.out.println(addedMessage);
    }

    private static void handleDeleteCommand(TaskList currentTasks, String input, String lineSeparation) throws InvalidTaskNumberException {
        try {
            int taskIndex = Integer.parseInt(input.substring(7)) - 1;
            currentTasks.deleteTask(taskIndex);
        } catch (NumberFormatException e) {
            throw new InvalidTaskNumberException();
        }
    }


}



