package test;

import org.junit.jupiter.api.Test;
import task.Task;
import task.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    public void testEquals() {
        Task task1 = new Task("Task 1", "Description 1", TaskStatus.NEW);
        Task task2 = new Task("Task 1", "Description 1", TaskStatus.NEW);
        assertEquals(task1, task2);
    }

    @Test
    public void testNotEquals() {
        Task task1 = new Task("Task 1", "Description 1", TaskStatus.NEW);
        Task task2 = new Task("Task 2", "Description 2", TaskStatus.IN_PROGRESS);
        assertNotEquals(task1, task2);
    }

    @Test
    public void testHashCode() {
        Task task1 = new Task("Task 1", "Description 1", TaskStatus.NEW);
        Task task2 = new Task("Task 1", "Description 1", TaskStatus.NEW);
        assertEquals(task1.hashCode(), task2.hashCode());
    }

    @Test
    public void testToString() {
        Task task = new Task("Task 1", "Description 1", TaskStatus.NEW);
        String expectedString = "Task{nameTask='Task 1', description='Description 1', status=NEW, taskID=0}";
        assertEquals(expectedString, task.toString());
    }
}
