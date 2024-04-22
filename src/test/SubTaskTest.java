package test;


import org.junit.jupiter.api.Test;
import task.SubTask;
import task.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

public class SubTaskTest {

    @Test
    public void testEquals() {
        SubTask subTask1 = new SubTask("SubTask 1", "Description 1", TaskStatus.NEW, 1);
        SubTask subTask2 = new SubTask("SubTask 1", "Description 1", TaskStatus.NEW, 1);
        assertEquals(subTask1, subTask2);
    }

    @Test
    public void testNotEquals() {
        SubTask subTask1 = new SubTask("SubTask 1", "Description 1", TaskStatus.NEW, 1);
        SubTask subTask2 = new SubTask("SubTask 2", "Description 2", TaskStatus.IN_PROGRESS, 2);
        assertNotEquals(subTask1, subTask2);
    }

    @Test
    public void testHashCode() {
        SubTask subTask1 = new SubTask("SubTask 1", "Description 1", TaskStatus.NEW, 1);
        SubTask subTask2 = new SubTask("SubTask 1", "Description 1", TaskStatus.NEW, 1);
        assertEquals(subTask1.hashCode(), subTask2.hashCode());
    }

    @Test
    public void testToString() {
        SubTask subTask = new SubTask("SubTask 1", "Description 1", TaskStatus.NEW, 1);
        String expectedString = "SubTask{Task{nameTask='SubTask 1', description='Description 1', status=NEW, taskID=0}epicId=1}";
        assertEquals(expectedString, subTask.toString());
    }
}
