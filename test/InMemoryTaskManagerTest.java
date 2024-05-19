package test;

import manager.*;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import task.Epic;
import task.SubTask;
import task.Task;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    private InMemoryTaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void testCreateTask() {
        Task task = new Task("Task 1", "Description", StatusTask.NEW, LocalDateTime.now(), Duration.ofHours(1));
        taskManager.createTask(task);
        assertEquals(1, taskManager.getAllTasks().size());
        assertTrue(taskManager.getAllTasks().contains(task));
    }

    @Test
    public void testCreateEpic() {
        Epic epic = new Epic("Epic 1", "Description", StatusTask.NEW);
        taskManager.createEpic(epic);
        assertEquals(1, taskManager.getAllEpic().size());
        assertTrue(taskManager.getAllEpic().contains(epic));
    }

    @Test
    public void testCreateSubTask() {
        Epic epic = new Epic("Epic 1", "Description", StatusTask.NEW);
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("SubTask 1", "Description", StatusTask.NEW, LocalDateTime.now(), Duration.ofHours(1), epic.getIdNumber());
        taskManager.createSubTask(subTask);
        assertEquals(1, taskManager.getAllSubTask().size());
        assertTrue(taskManager.getAllSubTask().contains(subTask));
    }

    @Test
    public void testGetTaskById() {
        Task task = new Task("Task 1", "Description", StatusTask.NEW, LocalDateTime.now(), Duration.ofHours(1));
        taskManager.createTask(task);
        Task fetchedTask = taskManager.getTaskById(task.getIdNumber());
        assertEquals(task, fetchedTask);
    }

    @Test
    public void testGetEpicById() {
        Epic epic = new Epic("Epic 1", "Description", StatusTask.NEW);
        taskManager.createEpic(epic);
        Epic fetchedEpic = taskManager.getEpicById(epic.getIdNumber());
        assertEquals(epic, fetchedEpic);
    }

    @Test
    public void testGetSubTaskById() {
        Epic epic = new Epic("Epic 1", "Description", StatusTask.NEW);
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("SubTask 1", "Description", StatusTask.NEW, LocalDateTime.now(), Duration.ofHours(1), epic.getIdNumber());
        taskManager.createSubTask(subTask);
        SubTask fetchedSubTask = taskManager.getSubTaskById(subTask.getIdNumber());
        assertEquals(subTask, fetchedSubTask);
    }

    @Test
    public void testUpdateTask() {
        Task task = new Task("Task 1", "Description", StatusTask.NEW, LocalDateTime.now(), Duration.ofHours(1));
        taskManager.createTask(task);
        task.setTaskName("Updated Task 1");
        taskManager.updateTask(task);
        Task updatedTask = taskManager.getTaskById(task.getIdNumber());
        assertEquals("Updated Task 1", updatedTask.getTaskName());
    }

    @Test
    public void testUpdateEpic() {
        Epic epic = new Epic("Epic 1", "Description", StatusTask.NEW);
        taskManager.createEpic(epic);
        epic.setTaskName("Updated Epic 1");
        taskManager.updateEpic(epic);
        Epic updatedEpic = taskManager.getEpicById(epic.getIdNumber());
        assertEquals("Updated Epic 1", updatedEpic.getTaskName());
    }

    @Test
    public void testUpdateSubTask() {
        Epic epic = new Epic("Epic 1", "Description", StatusTask.NEW);
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("SubTask 1", "Description", StatusTask.NEW, LocalDateTime.now(), Duration.ofHours(1), epic.getIdNumber());
        taskManager.createSubTask(subTask);
        subTask.setTaskName("Updated SubTask 1");
        taskManager.updateSubTask(subTask);
        SubTask updatedSubTask = taskManager.getSubTaskById(subTask.getIdNumber());
        assertEquals("Updated SubTask 1", updatedSubTask.getTaskName());
    }

    @Test
    public void testDeleteTask() {
        Task task = new Task("Task 1", "Description", StatusTask.NEW, LocalDateTime.now(), Duration.ofHours(1));
        taskManager.createTask(task);
        taskManager.deleteTask(task.getIdNumber());
        assertEquals(0, taskManager.getAllTasks().size());
    }

    @Test
    public void testDeleteEpic() {
        Epic epic = new Epic("Epic 1", "Description", StatusTask.NEW);
        taskManager.createEpic(epic);
        taskManager.deleteEpic(epic.getIdNumber());
        assertEquals(0, taskManager.getAllEpic().size());
        assertEquals(0, taskManager.getAllSubTask().size());
    }

    @Test
    public void testDeleteSubTask() {
        Epic epic = new Epic("Epic 1", "Description", StatusTask.NEW);
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("SubTask 1", "Description", StatusTask.NEW, LocalDateTime.now(), Duration.ofHours(1), epic.getIdNumber());
        taskManager.createSubTask(subTask);
        taskManager.deleteSubTasks(subTask.getIdNumber());
        assertEquals(0, taskManager.getAllSubTask().size());
    }

    @Test
    public void testGetAllEpicSubtasks() {
        Epic epic = new Epic("Epic 1", "Description", StatusTask.NEW);
        taskManager.createEpic(epic);
        SubTask subTask1 = new SubTask("SubTask 1", "Description", StatusTask.NEW, LocalDateTime.now(), Duration.ofHours(1), epic.getIdNumber());
        SubTask subTask2 = new SubTask("SubTask 2", "Description", StatusTask.NEW, LocalDateTime.now().plusHours(2), Duration.ofHours(1), epic.getIdNumber());
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        List<SubTask> subtasks = taskManager.getAllEpicSubtasks(epic.getIdNumber());
        assertEquals(2, subtasks.size());
        assertTrue(subtasks.contains(subTask1));
        assertTrue(subtasks.contains(subTask2));
    }

    @Test
    public void testGetPrioritizedTasks() {
        Task task1 = new Task("Task 1", "Description", StatusTask.NEW, LocalDateTime.now(), Duration.ofHours(1));
        Task task2 = new Task("Task 2", "Description", StatusTask.NEW, LocalDateTime.now().plusHours(2), Duration.ofHours(1));
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        TreeSet<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(2, prioritizedTasks.size());
        assertEquals(task1, prioritizedTasks.first());
        assertEquals(task2, prioritizedTasks.last());
    }
}