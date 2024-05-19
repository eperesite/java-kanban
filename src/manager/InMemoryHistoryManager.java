package manager;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {1

    private final Map<Integer, Node> history = new HashMap<>();
    private Node first;
    private Node last;

    private void linkLast(Task task) {
        final Node l = last;
        final Node newNode = new Node(l, task, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.setNext(newNode);
        }
    }

    private List<Task> getTasks() {
        List<Task> task = new ArrayList<>();
        Node node = first;

        while (node != null) {
            task.add(node.getObject());
            node = node.getNext();
        }
        return task;
    }

    public void removeNode(Node node) {
        if (node != null) {
            final Node next = node.getNext();
            final Node prev = node.getPrevious();
            if (prev == null) {
                first = next;
            } else {
                prev.setNext(next);
            }
            if (next == null) {
                last = prev;
            } else {
                next.setPrevious(prev);
            }
        }
    }

    @Override
    public void add(Task task) {
        if (history.isEmpty()) {
            linkLast(task);
            history.put(task.getIdNumber(), last);
        } else {
            if (history.containsKey(task.getIdNumber())) {
                remove(task.getIdNumber());
                linkLast(task);
                history.put(task.getIdNumber(), last);
            } else {
                linkLast(task);
                history.put(task.getIdNumber(), last);
            }
        }
    }

    @Override
    public void remove(int id) {
        removeNode(history.get(id));
        history.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}