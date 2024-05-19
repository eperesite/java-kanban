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

class InMemoryTaskManagerTest {
    public TaskManager taskManager;
    public HistoryManager historyManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void shouldSetDurationAndStartTimeToNullForSubTasks() { // если по времени задачи пересекаются - обнулить время добавляемой задачи
        LocalDateTime now = LocalDateTime.now();
        Epic epic = new Epic("Epic", "Epic description");
        taskManager.createEpic(epic);
        SubTask subtask = new SubTask("Task", "Task description", 0, now, Duration.ofDays(5));
        taskManager.createSubTask(subtask);
        SubTask crossingSubTask = new SubTask("Task", "Task description", 0, now, Duration.ofDays(3));
        taskManager.createSubTask(crossingSubTask);
        assertNull(crossingSubTask.getDuration());
        assertNull(crossingSubTask.getStartTime());
    }

    @Test
    void createTask() {
        Task task = new Task("Task", "Task description");
        taskManager.createTask(task);
        final int taskId = task.getIdNumber();

        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        // проверить, что экземпляры Task равны друг другу, если равен их id
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTasks();

        // проверить запись в список и сравнить генерируемый id
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Количество задач в списке неверное.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void createEpic() {
        Epic epic = new Epic("Epic", "Epic description");
        taskManager.createEpic(epic);
        final int epicId = epic.getIdNumber();

        final Epic savedEpic = (Epic) taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Эпик не найден.");
        // проверить, что экземпляры Epic равны друг другу, если равен их id
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = taskManager.getAllEpic();

        // проверить запись в список и сравнить генерируемый id
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Количество эпиков в списке неверное.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    void createSubTask() {
        Epic epic = new Epic("Epic", "Epic description");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Subtask", "Subtask description", 0);
        taskManager.createSubTask(subTask);
        final int subTaskId = subTask.getIdNumber();

        final SubTask savedSubTask = (SubTask) taskManager.getSubTaskById(subTaskId);

        assertNotNull(savedSubTask, "Подзадача не найдена.");
        // проверить, что экземпляры SubTask равны друг другу, если равен их id
        assertEquals(subTask, savedSubTask, "Подзадачи не совпадают.");

        final List<SubTask> subTasks = taskManager.getAllSubTask();

        // проверить запись в список и сравнить генерируемый id
        assertNotNull(subTasks, "Подзадачи не возвращаются.");
        assertEquals(1, subTasks.size(), "Количество подзадач в списке неверное.");
        assertEquals(subTask, subTasks.get(0), "Подзадачи не совпадают.");
    }

    @Test
    void canNotCreateSubTaskToNonExistentEpic() {
        Epic epic = new Epic("Epic", "Epic description");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Test subtask", "Test subtask description", 0);
        taskManager.createSubTask(subTask);
        Epic newEpic = (Epic) taskManager.getEpicById(subTask.getEpicId());

        // проверить, что Epic не может добавить сам в себя в виде подзадачи
        assertNotNull(newEpic, "Подзадача не может быть записана в несуществующий эпик.");
    }

    @Test
    void canNotUpdateEpicByNonExistentId() {
        Epic epic = new Epic("Epic", "Epic description");
        taskManager.createEpic(epic);
        epic.setIdNumber(2);
        taskManager.updateEpic(epic);

        // проверить, что обновить несуществующий epic невозсожно
        assertNull(taskManager.getEpicById(2), "Обновлен несуществующий эпик.");
    }

    @Test
    void canNotUpdateSubTaskByNonExistentId() {
        Epic epic = new Epic("Epic", "Epic description");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Subtask", "Subtask description", 0);
        taskManager.createSubTask(subTask);
        taskManager.updateSubTask(subTask);

        // проверить, что обновить subTask несуществующего эпика невозможно
        assertNotNull(taskManager.getSubTaskById(subTask.getIdNumber()), "Обновлена несуществующего подзадача эпика.");
    }

    @Test
    void managersShouldNotReturnsNull() {
        // проверить, что утилитарный класс всегда возвращает проинициализированные
        // и готовые к работе экземпляры менеджеров
        assertNotNull(taskManager, "Объект класса не возвращаются.");
        assertNotNull(historyManager, "Объект класса не возвращается.");
    }

    @Test
    void removedSubTasksShouldNotContainsOldId() {
        Epic epic1 = new Epic("Epic", "Epic description");
        SubTask subTask2 = new SubTask("Subtask2", "Subtask description", 0);
        SubTask subTask3 = new SubTask("Subtask3", "Subtask description", 0);

        taskManager.createEpic(epic1);
        taskManager.createSubTask(subTask2);
        taskManager.deleteSubTasks(1);
        taskManager.createSubTask(subTask3);

        // удаляемые подзадачи не должны хранить старые id
        // внутри эпика не должны оставаться id неактуальных подзадач
        assertFalse(epic1.getSubTaskIds().contains(subTask2.getIdNumber()));
        assertFalse(subTask2.equals(subTask3));
    }

    @Test
    void dataOfTaskShouldNotBeChangedBySetters() {
        Task task = new Task("Task", "Task description");

        taskManager.createTask(task);

        int taskId = task.getIdNumber();
        task.setIdNumber(2);
        int newTaskId = task.getIdNumber();

        String taskDescription = "Task description";
        task.setDescription("Changed task description");
        String newTaskDescription = "Task description";

        taskManager.getTaskById(1);
        taskManager.getTaskById(2);

        assertEquals(2, task.getIdNumber(), "id задачи не изменился.");
        assertEquals("Changed task description", task.getDescription(), "Описание задачи не изменилось.");
        assertEquals(null, taskManager.getTaskById(2), "id задачи изменился на пользовательский.");
        assertEquals(0, taskManager.getHistory().size(), "В истории просмотра отображается 1 задачи.");
    }
}