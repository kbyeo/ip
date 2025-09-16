


# StackOverflown User Guide

![StackOverflown Logo](https://img.shields.io/badge/StackOverflown-Personal%20Task%20Manager-blue?style=for-the-badge&logo=java)

**StackOverflown** is a feature-rich personal task management application designed for users who prefer a blend of **Command Line Interface (CLI) efficiency** and **Graphical User Interface (GUI) convenience**. Organize your tasks, set deadlines, plan events, and boost your productivity with our intuitive chat-style interface!

---

## Table of Contents

- [Quick Start](#quick-start)
- [Features](#features)
  - [Adding Tasks](#adding-tasks)
  - [Managing Tasks](#managing-tasks)
  - [Viewing Tasks](#viewing-tasks)
  - [Organizing Tasks](#organizing-tasks)
  - [Exiting the Application](#exiting-the-application)
- [Command Summary](#command-summary)
- [FAQ](#faq)
- [Known Issues](#known-issues)

---

## Quick Start

1. **Prerequisites**: Ensure you have **Java 11** or later installed on your computer.
   ```bash
   java -version

2. **Download**: Get the latest `stackoverflown.jar` file from the [releases page](https://github.com/kbyeo/ip/releases).

3. **Launch**: Double-click the JAR file or run the following command:
   ```bash
   java -jar stackoverflown.jar
   ```

4. **Start Managing**: The GUI will appear with a welcome message. Type your first command in the text field and press **Enter** or click **">"**!

  

5. **First Steps**: Try these commands to get started:
   ```
   todo read book
   list
   ```

> [TIP]
> You can use both the GUI chat interface and CLI mode interchangeably. The GUI provides visual feedback while the CLI offers quick keyboard-driven interaction.

---

## Features

### Adding Tasks

StackOverflown supports three types of tasks to cover all your productivity needs:

#### Adding a Todo Task
Create simple tasks without specific deadlines.

**Format:** `todo DESCRIPTION`

**Example:**
```
todo read Java programming book
```

**Expected output:**
```
Boom! A ToDo task just joined the party:
[T][] read Java programming book

Your task arsenal now stands at 1 strong!.
```

#### Adding a Deadline Task
Create tasks with specific due dates for time-sensitive work.

**Format:** `deadline DESCRIPTION /by YYYY-MM-DD HHMM`

**Examples:**
```
deadline submit assignment /by 2025-12-01
deadline project report /by 2025-11-30 2200
```

**Expected output:**
```
All set! A Deadline task just joined the party:
[D][]submit assignment (by: Dec 01 2025 11:59pm)
Your task arsenal now stands at 2 strong!

All set! A Deadline task just joined the party:
[D][]project report (by: Nov 30 2025 10:00pm)
Your task arsenal now stands at 3 strong!
```

> [NOTE]
> Time is optional for deadline tasks. If not specified, it defaults to end of day (11:59pm).

#### Adding an Event Task
Create tasks with start and end times for meetings and appointments.

**Format:** `event DESCRIPTION /from YYYY-MM-DD HHMM /to YYYY-MM-DD HHMM`

**Example:**
```
event team meeting /from 2025-12-10 1400 /to 2025-12-10 1600
```

**Expected output:**
```
Tada! An Event task just joined the party:
[E][]team meeting (from: Dec 10 2025 2:00pm to: Dec 10 2025 4:00pm)
Your task arsenal now stands at 4 strong!
```

---

### Managing Tasks

#### Marking Tasks as Done
Mark completed tasks with a checkmark for visual progress tracking.

**Format:** `mark INDEX`

**Example:**
```
mark 2
```

**Expected output:**
```
Boom! That task is history - marked as done and dusted:
[D][X]submit assignment (by: Dec 01 2025 11:59pm)
```

#### Unmarking Tasks
Remove the done status from tasks if you need to work on them again.

**Format:** `unmark INDEX`

**Example:**
```
unmark 2
```

**Expected output:**
```
Aha! This task is no longer done - it's waiting for your magic touch again:
[D][]submit assignment (by: Dec 01 2025 11:59pm)
```

#### Deleting Tasks
Permanently remove tasks you no longer need.

**Format:** `delete INDEX`

**Example:**
```
delete 3
```

**Expected output:**
```
Poof! Task vanished from existence:
[E][]team meeting (from: Dec 10 2025 2:00pm to: Dec 10 2025 4:00pm)

Your task arsenal now stands at 3 strong!
```

> [WARNING]
> Deleted tasks cannot be recovered. Make sure you really want to remove the task before confirming.

---

### Viewing Tasks

#### Listing All Tasks
Display your complete task list with status indicators and indices.

**Format:** `list`

**Example output:**
```
Here are your tasks:
1. [T][]read Java programming book
2. [D][]submit assignment (by: Dec 01 2025 11:59pm)
3. [E][]team meeting (from: Dec 10 2023 2:00PM to: Dec 10 2023 4:00PM)
```

**Task Status Indicators:**
- `[T]` = ToDo task
- `[D]` = Deadline task
- `[E]` = Event task
- `[X]` = Completed task
- `[ ]` = Pending task

---

### Organising Tasks

#### Sorting Tasks
Organise your task list by various criteria for better productivity management.

**Format:** `sort CRITERIA`

**Available Sort Options:**

| Sort Criteria | Command | Description |
|---------------|---------|-------------|
| **Creation Order** | `sort creation` | Original task entry sequence |
| **Task Type** | `sort type` | Groups Todo → Deadline → Event |
| **Completion Status** | `sort status` | Pending tasks first, completed last |
| **Deadline Date** | `sort deadline` | Chronological order by due dates |
| **Alphabetical** | `sort alpha` | A-Z order by task description |

**Examples:**
```
sort deadline
sort status  
sort type
```


> [TIP]
> Use `sort deadline` before important due dates to prioritise time-sensitive tasks!

---

### Exiting the Application

#### Graceful Exit
Close the application and save all your data automatically.

**Format:** `bye`


> [NOTE]
> All your tasks are automatically saved when you exit. No manual saving required!

---

## Command Summary

| Action | Format                                            | Example                                                   |
|--------|---------------------------------------------------|-----------------------------------------------------------|
| **Add Todo** | `todo DESCRIPTION`                                | `todo read book`                                          |
| **Add Deadline** | `deadline DESCRIPTION /by YYYY-MM-DD HHMM`        | `deadline assignment /by 2025-12-01`                      |
| **Add Event** | `event DESCRIPTION /from DATE TIME /to DATE TIME` | `event meeting /from 2025-12-10 1400 /to 2025-12-10 1600` |
| **List Tasks** | `list`                                            | `list`                                                    |
| **Mark Done** | `mark INDEX`                                      | `mark 1`                                                  |
| **Mark Undone** | `unmark INDEX`                                    | `unmark 1`                                                |
| **Delete Task** | `delete INDEX`                                    | `delete 2`                                                |
| **Sort Tasks** | `sort CRITERIA`                                   | `sort deadline`                                           |
| **Exit** | `bye`                                             | `bye`                                                     |

---

## FAQ

**Q: Where are my tasks stored?**
A: Tasks are automatically saved to a local data file in the same directory as the application. Your data persists between sessions.

**Q: Can I use the application offline?**
A: Yes! StackOverflown works completely offline. No internet connection required.

**Q: What happens if I enter an invalid date?**
A: The application will show an error message with the correct date format. Please use `YYYY-MM-DD` format.

**Q: Can I edit existing tasks?**
A: Currently, you need to delete and recreate tasks to modify them. Task editing is planned for future versions.

**Q: How do I backup my tasks?**
A: Your tasks are stored in a local file. Copy the data file to backup your tasks to another location.

**Q: Why can't I see my profile pictures?**
A: Place your avatar images in the `src/main/resources/images/` directory as `ipUserAvatar.png` and `ipBotAvatar.jpg`.

---


## Technical Information

- **Java Version**: Requires Java 11 or later
- **GUI Framework**: JavaFX 17.0.2
- **Platforms**: Windows, macOS, Linux
- **Architecture**: Clean MVC design with Parser, TaskList, Storage, and UI components

---



