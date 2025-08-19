public class StackOverflown {
    public static void main(String[] args) {
        String lineSeparation = "____________________________________________________________";
        String botName = "StackOverflown";
        String exitLine = "Bye. Hope to see you again soon!";
        String message = String.format("%s\n Hello! I'm %s!\n What can I do for you?\n%s\n %s\n%s",
                lineSeparation, botName, lineSeparation, exitLine, lineSeparation);
        System.out.println(message);
    }
}
