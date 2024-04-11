package test;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Task;
import task.TaskStatus;


public class InMemoryTaskManagerTests {

    private static TaskManager taskManager;
    private static Task task1;
    private static Epic epic1;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
        task1 = new Task("Сходить в магазин", " ", TaskStatus.NEW);
        epic1 = new Epic("Успеть сдать Проект", " ");
    }

    @Test
    public void testGetCheckChangesAfterUpdatedInfoInTask() {
        Task currentTask = new Task("Сделать дз", "kemf", TaskStatus.NEW);
        Task updatedTask = new Task("Обновленный таск", "kemf", TaskStatus.NEW);

        Task expected = currentTask;

        taskManager.createNewTask(currentTask);
        taskManager.createNewTask(updatedTask);

        taskManager.updateTask(updatedTask);

        Assertions.assertEquals(expected, taskManager.getHistory().get(0));
    }

    @Test
    public void testGetCheckAddTaskSubTaskEpicAndGetById() {
        taskManager.createNewTask(task1);
        taskManager.createNewEpic(epic1);

        Epic expectedEpic = taskManager.getEpicByID(epic1.getTaskID());
        Task expectedTask = taskManager.getTaskByID(task1.getTaskID());

        Assertions.assertEquals(epic1, expectedEpic);
        Assertions.assertEquals(task1, expectedTask);
    }

    @Test
    public void testGetEqualToIdsDontConflict() {
        Task firstTask = new Task("task1", "desc1", TaskStatus.NEW);
        Task secondTask = new Task("task2", "desc2", TaskStatus.NEW);

        taskManager.createNewTask(firstTask);
        taskManager.createNewTask(secondTask);

        Assertions.assertNotEquals(firstTask, secondTask);
    }

    @Test
    public void testGetShouldBeNotChangesAfterAddInManager() {
        Task currentTask = new Task("Таск", "таск", TaskStatus.NEW);
        Task updatedTask = new Task("Обновленный таск", "обновленный", TaskStatus.IN_PROGRESS);

        taskManager.createNewTask(currentTask);
        taskManager.createNewTask(updatedTask);

        updatedTask.setTaskID(currentTask.getTaskID());
        taskManager.updateTask(updatedTask);

        Assertions.assertEquals(currentTask, taskManager.getTaskByID(currentTask.getTaskID()));
    }

    @Test
    public void testGetTaskCheckGenerationId() {
        taskManager.createNewTask(task1);
        Task task2 = new Task("Сделать уборку", " ", TaskStatus.NEW);
        taskManager.createNewTask(task2);

        Assertions.assertEquals(task1.getTaskID() + 1, task2.getTaskID());
    }

    @Test
    public void testGetEqualTasksIfIdEqualTo() {
        taskManager.createNewTask(task1);
        Task task2 = new Task("Сделать уборку", " ", TaskStatus.NEW);
        taskManager.createNewTask(task2);

        task2.setTaskID(task1.getTaskID());

        Assertions.assertEquals(task1, task2);
    }

    @Test
    public void testGetTaskById() {
        taskManager.createNewTask(task1);
        Assertions.assertEquals(task1, taskManager.getTaskByID(task1.getTaskID()));
    }
}
