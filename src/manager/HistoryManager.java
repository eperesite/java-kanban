package manager;

import task.Task;

import java.util.List;

public interface HistoryManager {1
    void add(Task task);

    void remove(int id);

    List<Task> getHistory();
}