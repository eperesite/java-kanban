package test;

import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedManagerTest {

    private File file = new File("fileCSV/FileTest.csv");

    public TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = new FileBackedTaskManager(file);
    }

    @Test
    public void readHistoryFromEmptyFileTest() {
        Task task = new Task("Task", "Task description", LocalDateTime.of(2024, 01, 01, 00, 00),
                Duration.ofMinutes(15));
        Epic epic = new Epic("Epic", "Epic description");
        SubTask subTask = new SubTask("Subtask", "Subtask description", 1, LocalDateTime.of(2024, 01, 01, 01, 00),
                Duration.ofMinutes(15));

        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);

        FileBackedTaskManager fromFileManager = FileBackedTaskManager.loadFromFile(new File("fileCSV/FileTest.csv"));

        Assertions.assertTrue(taskManager.getHistory().isEmpty());
        Assertions.assertTrue(fromFileManager.getHistory().isEmpty());
        Assertions.assertEquals(taskManager.getHistory(), fromFileManager.getHistory(),
                "Содержимое истории не соответствует.");
        Assertions.assertEquals(taskManager.getTaskById(0), fromFileManager.getTaskById(0),
                "Содержимое task не соответствует.");
        Assertions.assertEquals(taskManager.getEpicById(1), fromFileManager.getEpicById(1),
                "Содержимое epic не соответствует.");
        Assertions.assertEquals(taskManager.getSubTaskById(2), fromFileManager.getSubTaskById(2),
                "Содержимое epic не соответствует.");
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

    @Test
    public void readingFromNonExistentFileThrowingException() {
        assertThrows(ManagerSaveException.class, () ->
                        FileBackedTaskManager.loadFromFile(new File("resources/file.csv")),
                "Чтение из несуществующего файла не должно осуществляться.");
    }

    @Test
    public void savingInFileWithWrongPathThrowingException() {
        FileBackedTaskManager invalidManager = new FileBackedTaskManager(new File("sources/test.csv"));
        Task task = new Task("Task", "Test description");

        assertThrows(ManagerSaveException.class, () -> invalidManager.createTask(task),
                "Сохранение в файл с некорректным адресом не должно осуществляться.");
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