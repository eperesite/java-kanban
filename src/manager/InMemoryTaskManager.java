package manager;

import task.Epic;
import task.StatusTask;
import task.SubTask;
import task.Task;


import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, SubTask> subTasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected int idNumber = 0;

    private int generateId() {
        return idNumber++;
    }

    private Integer getId() {
        return idNumber;
    }

    @Override
    public List<Task> getHistory() { // История просмотра задач
        return historyManager.getHistory();
    }

    @Override
    public ArrayList<Task> getAllTasks() { // получение списка всех задач
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Task> getAllEpic() { // получение списка всех эпиков
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Task> getAllSubTask() { // получение списка всех подзадач
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void removeAllTasks() {
        for (Integer taskId : tasks.keySet()) {
            historyManager.remove(taskId);
        }
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        for (Integer epicId : epics.keySet()) {
            historyManager.remove(epicId);
        }
        epics.clear();
        for (Integer subtaskId : subTasks.keySet()) {
            historyManager.remove(subtaskId);
        }
        subTasks.clear();
    }

    @Override
    public void removeAllSubtasks() {
        for (Integer subtaskId : subTasks.keySet()) {
            historyManager.remove(subtaskId);
        }
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.setStatusTask(StatusTask.NEW);
            epic.clearSubtaskIdList();
        }
    }

    @Override
    public SubTask getSubTaskById(int idNumber) {
        if (subTasks.get(idNumber) != null) {
            historyManager.add(subTasks.get(idNumber));
        }
        return subTasks.get(idNumber);
    }

    @Override
    public Epic getEpicById(int idNumber) {
        if (epics.get(idNumber) != null) {
            historyManager.add(epics.get(idNumber));
        }
        return epics.get(idNumber);
    }

    @Override
    public Task getTaskById(int idNumber) {
        if (tasks.get(idNumber) != null) {
            historyManager.add(tasks.get(idNumber));
        }
        return tasks.get(idNumber);
    }

    @Override
    public List<SubTask> getAllEpicSubtasks(Integer epicId) {
        List<SubTask> result = new ArrayList<>();
        for (int subTaskId : epics.get(epicId).getSubTaskIds()) {
            result.add((subTasks.get(subTaskId)));
        }
        return result;
    }

    @Override
    public void createTask(Task task) {
        int id = generateId();
        task.setIdNumber(id);
        tasks.put(id, task);
    }

    @Override
    public void createEpic(Epic epic) {
        int id = generateId();
        epic.setIdNumber(id);
        epics.put(id, epic);
    }


    @Override
    public void createSubTask(SubTask newSubTask) {
        int id = generateId();
        newSubTask.setIdNumber(id);
        subTasks.put(newSubTask.getIdNumber(), newSubTask);
        int epicId = newSubTask.getEpicId();
        ArrayList<Integer> subTaskIdList = epics.get(epicId).getSubTaskIds();
        subTaskIdList.add(newSubTask.getIdNumber());
        updateEpicStatus(epicId);
    }


    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getIdNumber())) {
            tasks.put(task.getIdNumber(), task);
        }
    }

    @Override
    public void updateSubTask(SubTask newSubTask) {
        if (subTasks.containsKey(newSubTask.getIdNumber())) {
            subTasks.put(newSubTask.getIdNumber(), newSubTask);
            int epicId = subTasks.get(newSubTask.getIdNumber()).getEpicId();
            updateEpicStatus(epicId);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getIdNumber())) {
            Epic updatedEpic = epics.get(epic.getIdNumber());
            updatedEpic.setTaskName(epic.getTaskName());
            updatedEpic.setDescription(epic.getDescription());
            epics.put(updatedEpic.getIdNumber(), updatedEpic);
        }
    }

    protected void updateEpicStatus(int id) {
        int countNewTasks = 0;
        int countDoneTasks = 0;

        ArrayList<Integer> subTasksId = epics.get(id).getSubTaskIds();

        for (Integer subTaskId : subTasksId) {
            if (subTasks.get(subTaskId).getStatusTask().equals(StatusTask.NEW)) {
                countNewTasks++;
            } else if (subTasks.get(subTaskId).getStatusTask().equals(StatusTask.DONE)) {
                countDoneTasks++;
            }
        }

        if (subTasksId.size() == countDoneTasks) {
            epics.get(id).setStatusTask(StatusTask.DONE);
        } else if (subTasksId.size() == countNewTasks || subTasksId.isEmpty()) {
            epics.get(id).setStatusTask(StatusTask.NEW);
        } else {
            epics.get(id).setStatusTask(StatusTask.IN_PROGRESS);
        }

    }

    @Override
    public void deleteTask(int idNumber) {
        historyManager.remove(idNumber);
        tasks.remove(idNumber);
    }

    @Override
    public void deleteEpic(Integer idNumber) {
        Epic deletedEpic = epics.remove(idNumber);
        if (deletedEpic == null) {
            return;
        }
        historyManager.remove(idNumber);
        for (Integer subtaskId : deletedEpic.getSubTaskIds()) {
            subTasks.remove(subtaskId);
            historyManager.remove(subtaskId);
        }
    }

    @Override
    public void deleteSubTasks(Integer idNumber) {
        int epicId = subTasks.get(idNumber).getEpicId();
        epics.get(epicId).getSubTaskIds().remove((Integer) idNumber);
        historyManager.remove(idNumber);
        subTasks.remove(idNumber);
        updateEpicStatus(epicId);
    }
}