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

import stackoverflown.task.Task;
import stackoverflown.task.ToDo;
import stackoverflown.task.Deadline;
import stackoverflown.task.Event;
import stackoverflown.exception.StackOverflownException;

/**
 * Handles persistent storage of tasks to and from the file system.
 *
 * <p>The Storage class manages serialization and deserialization of task data
 * using a custom pipe-delimited text format. It automatically creates necessary
 * directories and provides robust error handling for file operations.</p>
 *
 * <p>Storage format for each task type:
 * <ul>
 * <li>ToDo: "T | 0/1 | description"</li>
 * <li>Deadline: "D | 0/1 | description | yyyy-M-d HHmm"</li>
 * <li>Event: "E | 0/1 | description | yyyy-M-d HHmm | yyyy-M-d HHmm"</li>
 * </ul>
 * Where 0/1 represents isDone status (0=not done, 1=done)</p>
 *
 * <p>Files are stored in "../data/stackoverflown.txt" relative to the application
 * working directory, with automatic directory creation if needed.</p>
 *
 * @author Yeo Kai Bin
 * @version 0.1
 * @since 2025
 */
public class Storage {
    private static final String DATA_FOLDER = "../data";
    private static final String FILE_PATH = "../data/stackoverflown.txt";
    private static final DateTimeFormatter STORAGE_FORMAT = DateTimeFormatter.ofPattern("yyyy-M-d HHmm");

    /**
     * Loads all tasks from the storage file.
     *
     * <p>Creates the data directory and file if they don't exist. Parses each
     * line of the file and reconstructs the appropriate Task objects. Empty lines
     * are skipped, and corrupted data triggers appropriate exceptions.</p>
     *
     * <p>For first-time users where no storage file exists, returns an empty
     * task list without throwing an exception.</p>
     *
     * @return ArrayList of loaded Task objects, empty if no storage file exists
     * @throws StackOverflownException if file reading fails or data corruption is detected
     */
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
     * Saves all tasks to the storage file.
     *
     * <p>Overwrites the existing file with current task data. Each task is
     * serialized to a pipe-delimited format appropriate for its type. Creates
     * the data directory if it doesn't exist.</p>
     *
     * @param tasks the ArrayList of Task objects to save
     * @throws StackOverflownException if file writing fails or directory creation fails
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

    /**
     * Creates the data directory if it doesn't exist.
     *
     * <p>Uses NIO Files API to create the directory structure safely. If the
     * directory already exists, no action is taken.</p>
     *
     * @throws IOException if directory creation fails due to permissions or file system issues
     */
    private void createDataDirectoryIfNotExists() throws IOException {
        Path dataDir = Paths.get(DATA_FOLDER);
        if (!Files.exists(dataDir)) {
            Files.createDirectories(dataDir);
        }
    }

    /**
     * Parses a single line from storage file into a Task object.
     *
     * <p>Expected format: "TYPE | DONE | DESCRIPTION [| DATETIME] [| DATETIME]"
     * <ul>
     * <li>TYPE: T, D, or E</li>
     * <li>DONE: 0 (not done) or 1 (done)</li>
     * <li>DESCRIPTION: task description text</li>
     * <li>DATETIME: date-time in yyyy-M-d HHmm format (for D and E types)</li>
     * </ul>
     * </p>
     *
     * @param line the line from storage file to parse
     * @return the reconstructed Task object (ToDo, Deadline, or Event)
     * @throws StackOverflownException if line format is invalid or date parsing fails
     */
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

    /**
     * Formats a Task object into storage file format.
     *
     * <p>Creates pipe-delimited string representation appropriate for each task type:
     * <ul>
     * <li>ToDo: "T | DONE | DESCRIPTION"</li>
     * <li>Deadline: "D | DONE | DESCRIPTION | DATE_TIME"</li>
     * <li>Event: "E | DONE | DESCRIPTION | FROM_TIME | TO_TIME"</li>
     * </ul>
     * </p>
     *
     * @param task the Task object to format for storage
     * @return the formatted string ready for writing to file
     */
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
