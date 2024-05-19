package test;

import manager.*;
import task.Epic;
import task.SubTask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager taskManager;
    private HistoryManager historyManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void shouldSetDurationAndStartTimeToNullForSubTasks() {
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

        assertNotNull(savedTask, "Task not found.");
        assertEquals(task, savedTask, "Tasks do not match.");

        final List<Task> tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "Tasks not returned.");
        assertEquals(1, tasks.size(), "Incorrect number of tasks in the list.");
        assertEquals(task, tasks.get(0), "Tasks do not match.");
    }

    @Test
    void createEpic() {
        Epic epic = new Epic("Epic", "Epic description");
        taskManager.createEpic(epic);
        final int epicId = epic.getIdNumber();

        final Epic savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Epic not found.");
        assertEquals(epic, savedEpic, "Epics do not match.");

        final List<Epic> epics = taskManager.getAllEpic();
        assertNotNull(epics, "Epics not returned.");
        assertEquals(1, epics.size(), "Incorrect number of epics in the list.");
        assertEquals(epic, epics.get(0), "Epics do not match.");
    }

    @Test
    void createSubTask() {
        Epic epic = new Epic("Epic", "Epic description");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Subtask", "Subtask description", 0);
        taskManager.createSubTask(subTask);
        final int subTaskId = subTask.getIdNumber();

        final SubTask savedSubTask = taskManager.getSubTaskById(subTaskId);

        assertNotNull(savedSubTask, "Subtask not found.");
        assertEquals(subTask, savedSubTask, "Subtasks do not match.");

        final List<SubTask> subTasks = taskManager.getAllSubTask();
        assertNotNull(subTasks, "Subtasks not returned.");
        assertEquals(1, subTasks.size(), "Incorrect number of subtasks in the list.");
        assertEquals(subTask, subTasks.get(0), "Subtasks do not match.");
    }
    
    @Test
    void canNotUpdateEpicByNonExistentId() {
        Epic epic = new Epic("Epic", "Epic description");
        epic.setIdNumber(999);
        taskManager.updateEpic(epic);
        assertNull(taskManager.getEpicById(999), "Non-existent epic should not be updated.");
    }

    @Test
    void canNotUpdateSubTaskByNonExistentId() {
        SubTask subTask = new SubTask("Subtask", "Subtask description", 999);
        subTask.setIdNumber(999);
        taskManager.updateSubTask(subTask);
        assertNull(taskManager.getSubTaskById(999), "Non-existent subtask should not be updated.");
    }

    @Test
    void managersShouldNotReturnsNull() {
        assertNotNull(taskManager, "TaskManager should not be null.");
        assertNotNull(historyManager, "HistoryManager should not be null.");
    }

    @Test
    void removedSubTasksShouldNotContainOldId() {
        Epic epic = new Epic("Epic", "Epic description");
        SubTask subTask = new SubTask("Subtask", "Subtask description", 0);
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);
        taskManager.deleteSubTasks(subTask.getIdNumber());
        assertFalse(epic.getSubTaskIds().contains(subTask.getIdNumber()), "Epic should not contain IDs of removed subtasks.");
    }
}
