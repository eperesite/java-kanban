package test;

import org.junit.jupiter.api.Test;
import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;


import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {

    @Test
    public void shouldReturnTaskManagerWhenManagersWorks() {
        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager);
    }

    @Test
    public void shouldReturnHistoryManagerWhenManagersWorks() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager);
    }
}
