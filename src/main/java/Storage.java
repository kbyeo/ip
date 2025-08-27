import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
/**
 * Handles loading and saving of tasks to the hard disk.
 * Uses relative file path for OS independence.
 */
public class Storage {
    private static final String DATA_FOLDER = "../data";
    private static final String FILE_PATH = "../data/stackoverflown.txt";

    /**
     * Loads tasks from the data file.
     * Handles cases where file or folder doesn't exist.
     *
     * @return ArrayList of loaded tasks, empty if file doesn't exist
     * @throws StackOverflownException if data file is corrupted
     */
    public ArrayList<Task> loadTasks() throws StackOverflownException {
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            createDataDirectoryIfNotExists();

            File file = new File(FILE_PATH);
            if (!file.exists()) {
                return tasks; //return empty list for first time users
            }

            // Read and parse file
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            for (String line : lines) {
                Task task = parseTaskFromLine(line);
                tasks.add(task);
            }
            System.out.println("Tasks loaded");

        } catch (IOException e) {
            throw new StackOverflownException("Oops! Had trouble reading your saved tasks: " + e.getMessage());
        }

        return tasks;
    }

    /**
     * Saves all tasks to the data file.
     *
     * @param tasks ArrayList of tasks to save
     * @throws StackOverflownException if saving fails
     */
    public void saveTasks(ArrayList<Task> tasks) throws StackOverflownException {
        try {
            createDataDirectoryIfNotExists();

            FileWriter writer = new FileWriter(FILE_PATH);
            for (Task task : tasks) {
                writer.write(formatTaskForFile(task) + System.lineSeparator());
            }
            System.out.println("task written");
            writer.close();

        } catch (IOException e) {
            throw new StackOverflownException("Oops! Had trouble saving your tasks: " + e.getMessage());
        }
    }

    private void createDataDirectoryIfNotExists() throws IOException {
        Path dataDir = Paths.get(DATA_FOLDER);
        if (!Files.exists(dataDir)) {
            Files.createDirectories(dataDir);
        }
    }

    private Task parseTaskFromLine(String line) throws StackOverflownException {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            throw new StackOverflownException("Corrupted data detected in save file - please check the format");
        }

        String taskType = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task;
        switch (taskType) {
        case "T":
            task = new ToDo(description);
            break;
        case "D":
            if (parts.length < 4) {
                throw new StackOverflownException("Corrupted deadline data in save file");
            }
            task = new Deadline(description, parts[3]);
            break;
        case "E":
            if (parts.length < 5) {
                throw new StackOverflownException("Corrupted event data in save file");
            }
            task = new Event(description, parts[3], parts[4]);
            break;
        default:
            throw new StackOverflownException("Unknown task type in save file: " + taskType);
        }

        if (isDone) {
            task.markDone();
        }

        return task;
    }

    private String formatTaskForFile(Task task) {
        String taskType = task.getTypeIcon().replace("[", "").replace("]", "");
        String isDone = task.isDone() ? "1" : "0";
        String description = task.getDescription();

        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return String.format("%s | %s | %s | %s", taskType, isDone, description, deadline.getBy());
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return String.format("%s | %s | %s | %s | %s", taskType, isDone, description,
                    event.getFrom(), event.getTo());
        } else {
            return String.format("%s | %s | %s", taskType, isDone, description);
        }
    }


}
