package manager;

import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private int taskID = 0;
    private Map<Integer, Task> storageTask = new HashMap<>();
    private Map<Integer, SubTask> storageSubTask = new HashMap<>();
    private Map<Integer, Epic> storageEpic = new HashMap<>();
    private HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    private int generateTaskId() {
        return ++taskID;
    }

    @Override
    public Task createNewTask(Task task) {
        int id = generateTaskId();
        task.setTaskID(id);
        storageTask.put(id, task);
        return task;
    }

    @Override
    public void deleteAllTask() {
        storageTask.clear();
    }

    @Override
    public void updateTask(Task task) {
        storageTask.put(task.getTaskID(), task);
    }

    @Override


    public void deleteTaskByID(Integer ID) {

        storageTask.remove(ID);

    }

    @Override
    public List<Task> getAllTask() {
        return new ArrayList<>(storageTask.values());
    }

    @Override
    public Task getTaskByID(int ID) {
        Task task = storageTask.get(ID);
        if (task != null) {
            historyManager.add(task); 
        }
        return task;
    }


    @Override
    public Epic createNewEpic(Epic epic) {
        int ID = generateTaskId();
        epic.setTaskID(ID);
        storageEpic.put(ID, epic);
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic existingEpic = storageEpic.get(epic.getTaskID());
        if (existingEpic != null) {
            existingEpic.setNameTask(epic.getNameTask());
            existingEpic.setDescription(epic.getDescription());
        }
    }

    @Override
    public void deleteAllEpic() {
        storageEpic.clear();
        storageSubTask.clear();
    }

    @Override
    public void deleteEpicByID(Integer ID) {
        Epic epic = storageEpic.get(ID);
        if (epic != null) {
            for (int subTaskId : epic.getSubTaskIds()) {
                storageSubTask.remove(subTaskId);
            }
            storageEpic.remove(ID);
        }
    }

    @Override
    public List<Epic> getAllEpic() {
        return new ArrayList<>(storageEpic.values());
    }

    @Override
    public Epic getEpicByID(int ID) {
        Epic epic = storageEpic.get(ID);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public SubTask createNewSubTask(SubTask subTask) {
        Epic epic = storageEpic.get(subTask.getEpicId());
        if (epic == null) {
            return null;
        }
        int id = generateTaskId();
        subTask.setTaskID(id);
        epic.setSubTaskId(id);
        storageSubTask.put(id, subTask);
        updateStatus(epic);
        return subTask;
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (!storageSubTask.containsKey(subTask.getTaskID())) {
            return;
        }

        Epic currentEpic = storageEpic.get(subTask.getEpicId());
        Epic oldEpic = storageEpic.get(storageSubTask.get(subTask.getTaskID()).getEpicId());

        if (currentEpic == null || oldEpic == null || currentEpic.getTaskID() != oldEpic.getTaskID()) {
            return;
        }

        storageSubTask.put(subTask.getTaskID(), subTask);

        updateStatus(currentEpic);
    }

    @Override
    public void deleteAllSubTask() {
        storageSubTask.clear();
        for (Epic epic : storageEpic.values()) {
            epic.clearSubTaskIds();
            updateStatus(epic);
        }
    }

    @Override
    public void deleteSubTaskByID(Integer ID) {
        SubTask subTask = storageSubTask.get(ID);
        if (subTask != null) {
            Epic epic = storageEpic.get(subTask.getEpicId());
            if (epic != null) {
                epic.removeSubTaskId(ID);
                updateStatus(epic);
            }
            storageSubTask.remove(ID);
        }
    }

    @Override
    public List<SubTask> getAllSubTask() {
        return new ArrayList<>(storageSubTask.values());
    }


    @Override
    public SubTask getSubTaskByID(int ID) {
        SubTask subTask = storageSubTask.get(ID);
        if (subTask != null) {
            historyManager.add(subTask);
        }
        return subTask;
    }


    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }


    private void updateStatus(Epic epic) {
        if (epic.getSubTaskIds().isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }

        boolean isNew = true;
        boolean isDone = true;
        for (int subTaskId : epic.getSubTaskIds()) {
            TaskStatus status = storageSubTask.get(subTaskId).getStatus();
            if (status != TaskStatus.NEW) {
                isNew = false;
            }
            if (status != TaskStatus.DONE) {
                isDone = false;
            }
            if (status == TaskStatus.IN_PROGRESS) {
                epic.setStatus(TaskStatus.IN_PROGRESS);
                return;
            }
        }

        if (isNew) {
            epic.setStatus(TaskStatus.NEW);
        } else if (isDone) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

}
