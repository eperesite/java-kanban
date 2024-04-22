package test;

import org.junit.jupiter.api.Test;
import task.Epic;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {

    @Test
    void testGetSubTaskIds() {
        Epic epic = new Epic("Epic 1", "Description of Epic 1");
        epic.setSubTaskId(1);
        epic.setSubTaskId(2);

        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);

        assertEquals(expected, epic.getSubTaskIds());
    }

    @Test
    void testRemoveSubTaskId() {
        Epic epic = new Epic("Epic 2", "Description of Epic 2");
        epic.setSubTaskId(1);
        epic.setSubTaskId(2);

        epic.removeSubTaskId(1);

        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(2);

        assertEquals(expected, epic.getSubTaskIds());
    }

    @Test
    void testClearSubTaskIds() {
        Epic epic = new Epic("Epic 3", "Description of Epic 3");
        epic.setSubTaskId(1);
        epic.setSubTaskId(2);

        epic.clearSubTaskIds();

        assertTrue(epic.getSubTaskIds().isEmpty());
    }

    @Test
    void testEquals() {
        Epic epic1 = new Epic("Epic", "Description");
        epic1.setSubTaskId(1);
        epic1.setSubTaskId(2);

        Epic epic2 = new Epic("Epic", "Description");
        epic2.setSubTaskId(1);
        epic2.setSubTaskId(2);

        assertEquals(epic1, epic2);
    }

    @Test
    void testNotEquals() {
        Epic epic1 = new Epic("Epic", "Description");
        epic1.setSubTaskId(1);
        epic1.setSubTaskId(2);

        Epic epic2 = new Epic("Epic", "Description");
        epic2.setSubTaskId(1);

        assertNotEquals(epic1, epic2);
    }
}