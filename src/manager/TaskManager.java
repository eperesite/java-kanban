package manager;

import task.Epic;
import task.SubTask;
import task.Task;

import java.util.List;

public interface TaskManager {
    Task createNewTask(Task task);

    void deleteAllTask();

    void updateTask(Task task);

    void deleteTaskByID(Integer id);

    List<Task> getAllTask();

    Task getTaskByID(int id);

    Epic createNewEpic(Epic epic);

    void updateEpic(Epic epic);

    void deleteAllEpic();

    void deleteEpicByID(Integer id);

    List<Epic> getAllEpic();

    Epic getEpicByID(int id);

    SubTask createNewSubTask(SubTask subTask);

    void updateSubTask(SubTask subTask);

    void deleteAllSubTask();

    void deleteSubTaskByID(Integer id);

    List<SubTask> getAllSubTask();

    SubTask getSubTaskByID(int id);

    List<Task> getHistory();

    void removeTaskFromHistory(int taskID); // New method
}
