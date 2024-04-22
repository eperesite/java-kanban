package test;

import manager.*;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import task.Task;

import task.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTests {
    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager(historyManager);
    }

    @Test
    public void testCreateNewTask() {
        Task task = new Task("Test Task", "Test Description", TaskStatus.NEW);
        Task createdTask = taskManager.createNewTask(task);
        assertNotNull(createdTask);
        assertEquals(task, createdTask);
    }

    @Test
    public void testDeleteAllTask() {
        Task task = new Task("Test Task", "Test Description", TaskStatus.NEW);
        taskManager.createNewTask(task);
        taskManager.deleteAllTask();
        List<Task> tasks = taskManager.getAllTask();
        assertEquals(0, tasks.size());
    }

}
