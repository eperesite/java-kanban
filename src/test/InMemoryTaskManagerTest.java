package test;

import manager.*;
import task.Epic;
import task.SubTask;

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
    void canNotUpdateEpicByNonExistentId() {
        Epic epic = new Epic("Epic", "Epic description");
        epic.setIdNumber(999);
        taskManager.updateEpic(epic);
        assertNull(taskManager.getEpicById(999), "Non-existent epic should not be updated.");
    }

    @Test
    void managersShouldNotReturnsNull() {
        assertNotNull(taskManager, "TaskManager should not be null.");
        assertNotNull(historyManager, "HistoryManager should not be null.");
    }
}
