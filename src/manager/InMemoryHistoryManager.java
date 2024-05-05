package manager;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private Node head;
    private Node tail;
    private final Map<Integer, Node> history = new HashMap<>();

    @Override
    public void add(Task task) {
        if (history.isEmpty()) {
            linkLast(task);
            history.put(task.getIdNumber(), tail);
        } else {
            if (history.containsKey(task.getIdNumber())) {
                remove(task.getIdNumber());
                linkLast(task);
                history.put(task.getIdNumber(), tail);
            } else {
                linkLast(task);
                history.put(task.getIdNumber(), tail);
            }
        }
    }

    private void linkLast(Task task) {
        final Node l = tail;
        final Node newNode = new Node(l, task, null);
        tail = newNode;
        if (l == null) {
            head = newNode;
        } else {
            l.setNext(newNode);
        }
    }

    @Override
    public void remove(int id) {
        removeNode(history.get(id));
        history.remove(id);
    }

    public void removeNode(Node node) {
        if (node != null) {
            final Node next = node.getNext();
            final Node prev = node.getPrevious();
            if (prev == null) {
                head = next;
            } else {
                prev.setNext(next);
            }
            if (next == null) {
                tail = prev;
            } else {
                next.setPrevious(prev);
            }
        }
    }

    @Override
    public List<Task> getHistory() { // История просмотра задач
        return getTasks();
    }

    private List<Task> getTasks() {
        List<Task> task = new ArrayList<>();
        Node node = head;

        while (node != null) {
            task.add(node.getObject());
            node = node.getNext();
        }
        return task;
    }
}