package manager;

import task.Epic;
import task.SubTask;
import task.Task;

import java.util.List;

public interface TaskManager {
    Task createNewTask(Task task);
    void deleteAllTask();
    void updateTask(Task task);
    void deleteTaskByID(Integer ID);
    List<Task> getAllTask();
    Task getTaskByID(int ID);

    Epic createNewEpic(Epic epic);
    void updateEpic(Epic epic);
    void deleteAllEpic();
    void deleteEpicByID(Integer ID);
    List<Epic> getAllEpic();
    Epic getEpicByID(int ID);

    SubTask createNewSubTask(SubTask subTask);
    void updateSubTask(SubTask subTask);
    void deleteAllSubTask();
    void deleteSubTaskByID(Integer ID);
    List<SubTask> getAllSubTask();
    SubTask getSubTaskByID(int ID);

    List<Task> getHistory();
}
