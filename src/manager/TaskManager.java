package manager;

import task.Epic;
import task.SubTask;
import task.Task;

import java.util.List;
import java.util.TreeSet;

public interface TaskManager {1
    //получение списка всех задач
    List<Task> getAllTasks();

    //получение списка всех эпиков
    List<Epic> getAllEpic();

    //получение списка всех подзадач
    List<SubTask> getAllSubTask();

    // Получение истории
    List<Task> getHistory();

    // удаление всех задач
    void removeAllTasks();

    // удаление всех эпиков
    void removeAllEpics();

    // удаление всех подзадач
    void removeAllSubtasks();

    // получение подзадачи по Id
    SubTask getSubTaskById(int idNumber);

    // получение задачи по Id
    Epic getEpicById(int idNumber);

    // получение задачи по Id
    Task getTaskById(int idNumber);

    // создание задачи
    void createTask(Task task);

    // создание подзадачи
    void createEpic(Epic epic);

    // создание эпика
    void createSubTask(SubTask subTask);

    // обновление задачи
    void updateTask(Task task);

    // обновление подзадачи
    void updateSubTask(SubTask subTask);

    // обновление эпика
    void updateEpic(Epic epic);

    // удаление задачи
    void deleteTask(int idNumber);

    // удаление эпика
    void deleteEpic(Integer idNumber);

    // удаление подзадачи
    void deleteSubTasks(Integer idNumber);

    // получение подзадач эпика
    List<SubTask> getAllEpicSubtasks(Integer epicId);

    HistoryManager getHistoryManager();

    TreeSet<Task> getPrioritizedTasks();

    void updateEpicStatus(int id);

    boolean isCrossingTasks(Task task);


    int generateId();

    Integer getId();

}