import java.util.Scanner;

public class StackOverflown {

    public static void main(String[] args) {
        String lineSeparation = "____________________________________________________________";
        String botName = "StackOverflown";
        String exitLine = "Aww, you’re leaving already? It’s been such a \n pleasure, can’t wait till next time! :)";
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
                    System.out.println("Uh-oh! That task number doesn’t exist in my universe — try again?");
                }
            } else if (userInput.startsWith("unmark ")) {
                try {
                    int taskIndex = Integer.parseInt(userInput.substring(7)) - 1;
                    currentTasks.unmarkTask(taskIndex);
                } catch (Exception e) {
                    System.out.println("Uh-oh! That task number doesn’t exist in my universe — try again?");
                }
            } else {
                currentTasks.addTask(userInput);
                String addedMessage = String.format("%s\n added: %s\n%s", lineSeparation, userInput, lineSeparation);
                System.out.println(addedMessage);
            }
            userInput = scanner.nextLine();
        }

        System.out.println(decoratedExit);

        scanner.close();

    }
}



