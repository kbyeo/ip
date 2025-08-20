import java.util.Scanner;

public class StackOverflown {

    public static void main(String[] args) {
        String lineSeparation = "____________________________________________________________";
        String botName = "StackOverflown";
        String exitLine = "Aww, you're leaving already? It's been such a\n pleasure, can't wait till next time! :)";
        String decoratedExit = String.format("%s\n %s\n%s", lineSeparation, exitLine, lineSeparation);
        String introLine = String.format("Hey! %s here, thrilled to see you!\n Let's dive RIGHT in, what can I do " +
                "for you? :)", botName);
        String decoratedIntro = String.format("%s\n %s\n%s", lineSeparation, introLine, lineSeparation);

        TaskList currentTasks = new TaskList();

        System.out.println(decoratedIntro);

        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();

        while (!userInput.equals("bye")) {
            if (userInput.equals("list")) {
                String listDisplay = String.format("%s\n%s\n%s", lineSeparation, currentTasks, lineSeparation);
                System.out.println(listDisplay);
            } else if (userInput.startsWith("mark ")) {
                try {
                    int taskIndex = Integer.parseInt(userInput.substring(5)) - 1;
                    currentTasks.markTask(taskIndex);
                } catch (Exception e) {
                    System.out.println(lineSeparation + "\nUh-oh! That task number doesn't exist " +
                            "in my universe - try again?\n" + lineSeparation);
                }
            } else if (userInput.startsWith("unmark ")) {
                try {
                    int taskIndex = Integer.parseInt(userInput.substring(7)) - 1;
                    currentTasks.unmarkTask(taskIndex);
                } catch (Exception e) {
                    System.out.println(lineSeparation + "\nUh-oh! That task number doesn't exist " +
                            "in my universe - try again?\n" + lineSeparation);
                }
            } else if (userInput.startsWith("todo ")) {
                String description = userInput.substring(5);
                currentTasks.addToDo(description);
                int taskNumber = currentTasks.getTaskCount();
                String addedMessage = String.format("%s\nBoom! A ToDo task just joined the party:" +
                                " %s\nYour task arsenal now stands at %s strong!\n%s",
                        lineSeparation, currentTasks.getTask(taskNumber - 1), taskNumber, lineSeparation);
                System.out.println(addedMessage);
            } else if (userInput.startsWith("deadline ")) {
                String content = userInput.substring(9); // Remove "deadline "
                String[] parts = content.split(" /by ");
                if (parts.length == 2) {
                    currentTasks.addDeadline(parts[0], parts[1]);
                    int taskNumber = currentTasks.getTaskCount();
                    String addedMessage = String.format("%s\nAll set! A Deadline task just joined the party:" +
                                    " %s\nYour task arsenal now stands at %s strong!\n%s",
                            lineSeparation, currentTasks.getTask(taskNumber - 1), taskNumber, lineSeparation);
                    System.out.println(addedMessage);
                } else {
                    System.out.println(lineSeparation + "\nInvalid deadline format! Use: deadline" +
                            " <DESCRIPTION> /by <TIME>\n" + lineSeparation);
                }
            } else if (userInput.startsWith("event ")) {
                String content = userInput.substring(6);
                String[] parts = content.split(" /from ");
                if (parts.length == 2) {
                    String[] timeParts = parts[1].split(" /to ");
                    if (timeParts.length == 2) {
                        currentTasks.addEvent(parts[0], timeParts[0], timeParts[1]);
                        int taskNumber = currentTasks.getTaskCount();
                        String addedMessage = String.format("%s\nTada! An Event task just joined the party:" +
                                        " %s\nYour task arsenal now stands at %s strong!\n%s",
                                lineSeparation, currentTasks.getTask(taskNumber - 1), taskNumber, lineSeparation);
                        System.out.println(addedMessage);
                    } else {
                        System.out.println(lineSeparation + "\nInvalid event format! Use: event <DESCRIPTION> " +
                                "/from <START> /to <END>\n" + lineSeparation);
                    }
                } else {
                    System.out.println(lineSeparation + "\nInvalid event format! Use: event <DESCRIPTION> " +
                            "/from <START> /to <END>\n" + lineSeparation);
                }
            } else {
                //treat as "todo" by default
                currentTasks.addToDo(userInput);
                int taskNumber = currentTasks.getTaskCount();
                String addedMessage = String.format("%s\nBoom! A ToDo task just joined the party:" +
                                " %s\nYour task arsenal now stands at %s strong!\n%s",
                        lineSeparation, currentTasks.getTask(taskNumber - 1), taskNumber, lineSeparation);
                System.out.println(addedMessage);
            }
            userInput = scanner.nextLine();
        }

        System.out.println(decoratedExit);

        scanner.close();

    }


}



