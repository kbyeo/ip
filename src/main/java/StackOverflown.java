public class StackOverflown {
    public static void main(String[] args) {
        String lineSeparation = "____________________________________________________________";
        String botName = "StackOverflown";
        String exitLine = "Aww, you’re leaving already? It’s been such a \n pleasure, can’t wait till next time! :)";
        String message = String.format("%s\n Hey! %s here, thrilled to see you!\n Let's dive RIGHT in, what can I " +
                "do for you? :)\n%s\n %s\n%s", lineSeparation, botName, lineSeparation, exitLine, lineSeparation);
        System.out.println(message);
    }
}
