package test;

import manager.FileBackedTaskManager;
import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;

import java.io.*;
import java.util.ArrayList;

public class FileBackedManagerTest {

    private File file = new File("fileCSV/FileTest.csv");

    public TaskManager taskManager;
    public HistoryManager historyManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = new FileBackedTaskManager(file);
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    public void readHistoryFromFileTest() {
        Task task = new Task("Task", "Task description");
        Epic epic = new Epic("Epic", "Epic description");
        SubTask subTask = new SubTask("Subtask", "Subtask description", 1);

        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);

        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getSubTaskById(3);

        FileBackedTaskManager fromFileManager = FileBackedTaskManager.loadFromFile(new File("fileCSV/FileTest.csv"));

        Assertions.assertEquals(taskManager.getHistory(), fromFileManager.getHistory(),
                "Содержимое истории не соответствует.");
        Assertions.assertEquals(taskManager.getTaskById(1), fromFileManager.getTaskById(1),
                "Содержимое task не соответствует.");
        Assertions.assertEquals(taskManager.getEpicById(2), fromFileManager.getEpicById(2),
                "Содержимое epic не соответствует.");
        Assertions.assertEquals(taskManager.getSubTaskById(3), fromFileManager.getSubTaskById(3),
                "Содержимое epic не соответствует.");
    }

    @Test
    public void saveToFileFest() {
        Task task = new Task("Task", "Task description");
        Epic epic = new Epic("Epic", "Epic description");
        SubTask subTask = new SubTask("Subtask", "Subtask description", 1);

        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);

        FileBackedTaskManager fromFileManager = FileBackedTaskManager.loadFromFile(new File("fileCSV/FileTest.csv"));

        ArrayList<Task> tasks = new ArrayList<>();

        tasks.add(fromFileManager.getTaskById(task.getIdNumber()));
        tasks.add(fromFileManager.getEpicById(epic.getIdNumber()));
        tasks.add(fromFileManager.getSubTaskById(subTask.getIdNumber()));

        Assertions.assertFalse(tasks.isEmpty());
        Assertions.assertTrue(tasks.contains(task));
        Assertions.assertTrue(tasks.contains(epic));
        Assertions.assertTrue(tasks.contains(subTask));
        Assertions.assertEquals(taskManager.getAllTasks(), fromFileManager.getAllTasks(),
                "Task'и не соответствуют.");
        Assertions.assertEquals(taskManager.getAllEpic(), fromFileManager.getAllEpic(),
                "Epic'и не соответствуют.");
        Assertions.assertEquals(taskManager.getAllSubTask(), fromFileManager.getAllSubTask(),
                "SubTask'и не соответствуют.");
    }

    @AfterEach
    public void clearTestCSVFile() {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}