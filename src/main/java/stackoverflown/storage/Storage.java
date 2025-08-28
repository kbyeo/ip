package stackoverflown.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
/**
 * Handles loading and saving of tasks to the hard disk.
 * Uses relative file path for OS independence.
 * Supports LocalDateTime parsing for Deadline and Event tasks.
 */
public class Storage {
    private static final String DATA_FOLDER = "../data";
    private static final String FILE_PATH = "../data/stackoverflown.txt";
    private static final DateTimeFormatter STORAGE_FORMAT = DateTimeFormatter.ofPattern("yyyy-M-d HHmm");

    public ArrayList<Task> loadTasks() throws StackOverflownException {
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            createDataDirectoryIfNotExists();

            File file = new File(FILE_PATH);
            if (!file.exists()) {
                return tasks; // Return empty list for first-time users
            }

            // Read and parse file
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            for (String line : lines) {
                if (!line.trim().isEmpty()) { // Skip empty lines
                    Task task = parseTaskFromLine(line);
                    tasks.add(task);
                }
            }

            System.out.println("Loaded " + tasks.size() + " tasks from storage");

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
            writer.close();

            System.out.println("Saved " + tasks.size() + " tasks to storage");

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
        try {
            switch (taskType) {
            case "T":
                task = new ToDo(description);
                break;
            case "D":
                if (parts.length < 4) {
                    throw new StackOverflownException("Corrupted deadline data in save file");
                }
                // Parse LocalDateTime from storage format
                LocalDateTime deadlineDateTime = LocalDateTime.parse(parts[3], STORAGE_FORMAT);
                task = new Deadline(description, deadlineDateTime);
                break;
            case "E":
                if (parts.length < 5) {
                    throw new StackOverflownException("Corrupted event data in save file");
                }
                // Parse LocalDateTime for both from and to
                LocalDateTime eventFrom = LocalDateTime.parse(parts[3], STORAGE_FORMAT);
                LocalDateTime eventTo = LocalDateTime.parse(parts[4], STORAGE_FORMAT);
                task = new Event(description, eventFrom, eventTo);
                break;
            default:
                throw new StackOverflownException("Unknown task type in save file: " + taskType);
            }

            if (isDone) {
                task.markDone();
            }

        } catch (Exception e) {
            if (e instanceof StackOverflownException) {
                throw e; // Re-throw our custom exceptions
            }
            throw new StackOverflownException("Corrupted date/time data in save file: " + e.getMessage());
        }

        return task;
    }

    private String formatTaskForFile(Task task) {
        String taskType = task.getTypeIcon().replace("[", "").replace("]", "");
        String isDone = task.isDone() ? "1" : "0";
        String description = task.getDescription();

        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return String.format("%s | %s | %s | %s", taskType, isDone, description, deadline.getByForStorage());
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return String.format("%s | %s | %s | %s | %s", taskType, isDone, description,
                    event.getFromForStorage(), event.getToForStorage());
        } else {
            return String.format("%s | %s | %s", taskType, isDone, description);
        }
    }


}
