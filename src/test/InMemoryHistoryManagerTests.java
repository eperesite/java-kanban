package test;

import manager.HistoryManager;
import manager.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;
import task.TaskStatus;


public class InMemoryHistoryManagerTests {

    public static HistoryManager historyManager;
    private static Task task1;

    @BeforeEach
    public void beforeEach() {
        historyManager = Managers.getDefaultHistory();
        task1 = new Task("Сходить в магазин", " ", TaskStatus.NEW);
    }

    @Test
    public void testGetDefaultHistory() {
        Assertions.assertNotNull(historyManager);
    }

    @Test
    public void testMaxElementsInHistoryShouldBe10() {
        for (int i = 0; i < 11; i++) {
            historyManager.add(task1);
        }

        Assertions.assertEquals(10, historyManager.getHistory().size());
    }
}
