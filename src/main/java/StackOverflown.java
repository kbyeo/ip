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

        System.out.println(decoratedIntro);

        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();

        while (!userInput.equals("bye")) {
            String decoratedEcho = String.format("%s\n %s\n%s", lineSeparation, userInput, lineSeparation);
            System.out.println(decoratedEcho);
            userInput = scanner.nextLine();
        }

        System.out.println(decoratedExit);

        scanner.close();

    }
}
