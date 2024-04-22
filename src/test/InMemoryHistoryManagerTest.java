package test;

import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;
import task.TaskStatus;

public class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;

    @BeforeEach
    public void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void testAddTaskToHistory() {
        Task task = new Task("Task 1", "Description", TaskStatus.NEW);
        historyManager.add(task);

        Assertions.assertEquals(1, historyManager.getHistory().size());
        Assertions.assertEquals(task, historyManager.getHistory().get(0));
    }

    @Test
    public void testRemoveTaskFromHistory() {
        Task task = new Task("Task 1", "Description", TaskStatus.NEW);
        historyManager.add(task);

        historyManager.remove(task.getTaskID());

        Assertions.assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    public void testGetHistoryReturnsEmptyListWhenNoTasksAdded() {
        // В этом тесте не добавляется ни одной задачи, поэтому история должна быть пустой
        Assertions.assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    public void testGetHistoryReturnsCorrectList() {
        Task task1 = new Task("Task 1", "Description", TaskStatus.NEW);
        Task task2 = new Task("Task 2", "Description", TaskStatus.IN_PROGRESS);
        Task task3 = new Task("Task 3", "Description", TaskStatus.DONE);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        Assertions.assertEquals(3, historyManager.getHistory().size());
        Assertions.assertEquals(task1, historyManager.getHistory().get(0));
        Assertions.assertEquals(task2, historyManager.getHistory().get(1));
        Assertions.assertEquals(task3, historyManager.getHistory().get(2));
    }
}