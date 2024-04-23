package manager;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {
    private Map<Integer, Node> taskMap = new HashMap<>();
    private Node head;
    private Node tail;

    @Override
    public void add(Task task) {
        int taskId = task.getTaskID();
        if (taskMap.containsKey(taskId)) {
            Node nodeToRemove = taskMap.get(taskId);
            removeNode(nodeToRemove);
        }

        Node newNode = new Node(task);
        if (head == null) {
            head = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
        }
        tail = newNode;
        taskMap.put(taskId, newNode);
    }

    @Override
    public void remove(int id) {
        if (taskMap.containsKey(id)) {
            Node nodeToRemove = taskMap.get(id);
            removeNode(nodeToRemove);
        }
    }



    private void removeNode(Node nodeToRemove) {
        System.out.println("Removing task: " + nodeToRemove.task.getNameTask());
        taskMap.remove(nodeToRemove.task.getTaskID());
        if (nodeToRemove == head && nodeToRemove == tail) {

            head = null;
            tail = null;
        } else if (nodeToRemove == head) {

            head = nodeToRemove.next;
            head.prev = null;
        } else if (nodeToRemove == tail) {

            tail = nodeToRemove.prev;
            tail.next = null;
        } else {

            nodeToRemove.prev.next = nodeToRemove.next;
            nodeToRemove.next.prev = nodeToRemove.prev;
        }
    }


    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        Node current = head;
        while (current != null) {
            history.add(current.task);
            current = current.next;
        }
        return history;
    }

    public void removeTasksOfType(Class<?> type) {
        List<Node> nodesToRemove = new ArrayList<>();
        for (Node node : taskMap.values()) {
            if (type.isInstance(node.task)) {
                nodesToRemove.add(node);
            }
        }
        for (Node nodeToRemove : nodesToRemove) {
            removeNode(nodeToRemove);
        }
    }

    private static class Node {
        Task task;
        Node prev;
        Node next;

        Node(Task task) {
            this.task = task;
        }
    }
}
