package test;

import manager.*;
import task.Epic;
import task.SubTask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = new FileBackedTaskManager(new File("testFile.csv"));
    }

    @Test
    void testTaskCreation() {
        Task task = new Task("Task", "Task description", LocalDateTime.of(2024, 1, 1, 0, 0), Duration.ofMinutes(15));
        taskManager.createTask(task);
        Task retrievedTask = taskManager.getTaskById(task.getIdNumber());
        assertNotNull(retrievedTask);
        assertEquals(task, retrievedTask);
    }

    @Test
    void testSubTaskCreation() {
        Epic epic = new Epic("Epic", "Epic description");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Subtask", "Subtask description", epic.getIdNumber(), LocalDateTime.of(2024, 1, 1, 1, 0), Duration.ofMinutes(15));
        taskManager.createSubTask(subTask);
        SubTask retrievedSubTask = taskManager.getSubTaskById(subTask.getIdNumber());
        assertNotNull(retrievedSubTask);
        assertEquals(subTask, retrievedSubTask);
    }

    @Test
    void testLoadFromFile() {
        assertThrows(ManagerSaveException.class, () -> FileBackedTaskManager.loadFromFile(new File("nonExistentFile.csv")), "Reading from a non-existent file should not be possible.");
    }

    @Test
    void testInvalidFileSave() {
        FileBackedTaskManager invalidManager = new FileBackedTaskManager(new File("/invalid/path/file.csv"));
        Task task = new Task("Task", "Task description", LocalDateTime.of(2024, 1, 1, 0, 0), Duration.ofMinutes(15));
        assertThrows(ManagerSaveException.class, () -> invalidManager.createTask(task), "Saving to a file with an incorrect path should not be possible.");
    }
}
