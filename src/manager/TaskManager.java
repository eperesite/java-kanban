package manager;

import task.Epic;
import task.SubTask;
import task.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getAllTasks();

    List<Task> getAllEpic();

    List<Task> getAllSubTask();

    List<Task> getHistory();

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubtasks();

    Task getSubTaskById(int idNumber);

    Task getEpicById(int idNumber);

    Task getTaskById(int idNumber);

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubTask(SubTask subTask);

    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);

    void deleteTask(int idNumber);

    void deleteEpic(Integer idNumber);

    void deleteSubTasks(Integer idNumber);

    List<SubTask> getAllEpicSubtasks(Integer epicId);
}