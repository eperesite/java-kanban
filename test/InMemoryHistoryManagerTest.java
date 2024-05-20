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

class InMemoryHistoryManagerTest {

    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    TaskManager manager = new InMemoryTaskManager();

    @Test
    public void testHistoryManagerStoresPreviousTaskVersions() {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        Task task3 = new Task("Task 3", "Description 3");

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);

        List<Task> history = manager.getHistory();

        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
        assertEquals(task3, history.get(2));
        assertEquals(3, history.size());
    }

    @Test
    public void addToHistory() {
        Task task1 = new Task("Task", "Task description");
        Epic epic2 = new Epic("Epic", "Epic description");
        SubTask subTask3 = new SubTask("SubTask", "SubTask description", 1);

        manager.createTask(task1);
        manager.createEpic(epic2);
        manager.createSubTask(subTask3);

        assertEquals(3, manager.getHistory().size(), "Список истории сохраняет повторные просмотры задач.");
    }

    @Test
    public void removeToHistory() {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        Task task3 = new Task("Task 3", "Description 3");
        Task task4 = new Task("Task 4", "Description 4");
        Task task5 = new Task("Task 5", "Description 5");
        Task task6 = new Task("Task 6", "Description 6");

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);
        manager.createTask(task4);
        manager.createTask(task5);
        manager.createTask(task6);

        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task3);
        inMemoryHistoryManager.add(task4);
        inMemoryHistoryManager.add(task5);
        inMemoryHistoryManager.add(task6);

        assertEquals(6, inMemoryHistoryManager.getHistory().size());
        inMemoryHistoryManager.add(task1);
        assertEquals(6, inMemoryHistoryManager.getHistory().size());
        inMemoryHistoryManager.remove(task1.getIdNumber());
        assertEquals(5, inMemoryHistoryManager.getHistory().size());
    }

    @Test
    public void removeTest() {
        Task task1 = new Task("Task", "Task description");
        Epic epic2 = new Epic("Epic", "Epic description");
        SubTask subTask3 = new SubTask("SubTask", "SubTask description", 2);

        manager.createTask(task1);
        manager.createEpic(epic2);
        manager.createSubTask(subTask3);

        manager.getTaskById(task1.getIdNumber());
        manager.getEpicById(epic2.getIdNumber());
        manager.getSubTaskById(subTask3.getIdNumber());

        inMemoryHistoryManager.remove(task1.getIdNumber());
        inMemoryHistoryManager.remove(epic2.getIdNumber());
        inMemoryHistoryManager.remove(subTask3.getIdNumber());

        assertEquals(0, inMemoryHistoryManager.getHistory().size(), "Список истории не пуст.");
    }

    @Test
    public void getHistoryTest() {
        Task task1 = new Task("Task", "Task description");
        Epic epic2 = new Epic("Epic", "Epic description");
        SubTask subTask3 = new SubTask("SubTask", "SubTask description", 1);

        manager.createTask(task1);
        manager.createEpic(epic2);
        manager.createSubTask(subTask3);

        assertEquals(3, manager.getHistory().size(), "Список истории не пуст.");

        manager.removeAllTasks();
        manager.removeAllEpics();
        manager.removeAllSubtasks();

        assertTrue(manager.getHistory().isEmpty(), "Список истории не пуст.");
    }

    @Test
    public void shouldReturnEmptyHistoryTest() {
        assertTrue(manager.getHistory().isEmpty(), "Возвращается непустая история.");
    }

    @Test
    public void duplicateTasksViewsTest() {
        Task task = new Task("Task", "Task description");

        manager.createTask(task);
        manager.getTaskById(task.getIdNumber());
        manager.getTaskById(task.getIdNumber());
        manager.getTaskById(task.getIdNumber());

        assertEquals(1, manager.getHistory().size(), "Дублирующиеся просмотры задач не исключаются из истории.");
    }

    @Test
    public void removeFromBeginningOfHistoryTest() {
        Task task1 = new Task("Task1", "Task description");
        Task task2 = new Task("Task2", "Task description");
        Task task3 = new Task("Task3", "Task description");

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);

        manager.getTaskById(task1.getIdNumber());
        manager.getTaskById(task2.getIdNumber());
        manager.getTaskById(task3.getIdNumber());

        inMemoryHistoryManager.remove(task1.getIdNumber());

        assertEquals(List.of(task2, task3), inMemoryHistoryManager.getHistory(), "Истории просмотров не соответствуют.");
    }

    @Test
    public void removeFromMiddleOfHistoryTest() {
        Task task1 = new Task("Task1", "Task description");
        Task task2 = new Task("Task2", "Task description");
        Task task3 = new Task("Task3", "Task description");

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);

        manager.getTaskById(task1.getIdNumber());
        manager.getTaskById(task2.getIdNumber());
        manager.getTaskById(task3.getIdNumber());

        inMemoryHistoryManager.remove(task2.getIdNumber());

        assertEquals(List.of(task1, task3), inMemoryHistoryManager.getHistory(), "Задача не удалена из истории просмотров.");
        assertFalse(inMemoryHistoryManager.getHistory().contains(task2), "Задача не удалена из истории просмотров.");
    }

    @Test
    public void removeFromEndOfHistoryTest() {
        Task task1 = new Task("Task1", "Task description");
        Task task2 = new Task("Task2", "Task description");
        Task task3 = new Task("Task3", "Task description");

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);


        manager.getTaskById(task1.getIdNumber());
        manager.getTaskById(task2.getIdNumber());
        manager.getTaskById(task3.getIdNumber());

        inMemoryHistoryManager.remove(task3.getIdNumber());

        assertEquals(List.of(task1, task2), inMemoryHistoryManager.getHistory(), "Истории просмотров не соответствуют.");
    }

    @Test
    public void testAddTaskToHistory() {
        Task task = new Task("Задача 1", "Описание 1");

        inMemoryHistoryManager.add(task);

        assertTrue(inMemoryHistoryManager.getHistory().contains(task));
    }

    @Test
    public void testRemoveTaskFromHistory() {
        Task task = new Task("Задача 1", "Описание 1");

        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.remove(task.getIdNumber());

        assertFalse(inMemoryHistoryManager.getHistory().contains(task));
    }
}