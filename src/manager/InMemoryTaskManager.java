package manager;

import task.Epic;
import task.StatusTask;
import task.SubTask;
import task.Task;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected Map<Integer, SubTask> subTasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected int idNumber = 0;

    private final Comparator<Task> comparator = (task1, task2) -> {
        if (task1.getStartTime() == null) {
            return 1;
        } else if (task2.getStartTime() == null) {
            return -1;
        }
        return task1.getStartTime().compareTo(task2.getStartTime());
    };
    protected TreeSet<Task> sortedTasks = new TreeSet<>(comparator);

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return sortedTasks;
    }

    private boolean isCrossingTasks(Task task) {
        if (task.getStartTime() != null && task.getDuration() != null) {
            int taskId = task.getIdNumber();
            return !sortedTasks.stream().filter(t -> t.getIdNumber() != taskId && t.getStartTime() != null && t.getDuration() != null).allMatch(t -> (task.getEndTime().isBefore(t.getStartTime()) || task.getStartTime().isAfter(t.getEndTime())));
        }
        return true;
    }

    protected void updateEpicTime(Epic epic) {
        Duration epicDuration = Duration.ZERO;
        LocalDateTime earlyStartTime = LocalDateTime.MAX;
        LocalDateTime lateEndTime = LocalDateTime.MIN;

        if (!epic.getSubTaskIds().isEmpty()) {
            for (Integer id : epic.getSubTaskIds()) {
                SubTask subTask = subTasks.get(id);

                if (subTask.getStartTime() != null && subTask.getDuration() != null) {
                    if (earlyStartTime.isAfter(subTask.getStartTime())) {
                        earlyStartTime = subTask.getStartTime();
                    }

                    LocalDateTime subTaskEndTime = subTask.getStartTime().plus(subTask.getDuration());
                    if (lateEndTime.isBefore(subTaskEndTime)) {
                        lateEndTime = subTaskEndTime;
                    }

                    epicDuration = epicDuration.plus(subTask.getDuration());
                }
            }

            if (!earlyStartTime.equals(LocalDateTime.MAX) && !lateEndTime.equals(LocalDateTime.MIN)) {
                epic.setStartTime(earlyStartTime);
                epic.setEndTime(lateEndTime);
            } else {
                epic.setStartTime(null);
                epic.setEndTime(null);
            }
        } else {
            epic.setStartTime(null);
            epic.setEndTime(null);
            epicDuration = null;
        }

        epic.setDuration(epicDuration);
    }

    @Override
    public void createTask(Task task) {
        int id = generateId();
        task.setIdNumber(id);
        tasks.put(id, task);

        if (task.getStartTime() != null && task.getDuration() != null) {
            if (isCrossingTasks(task)) {
                System.out.println("Ошибка: задача пересекается с другими задачами и не может быть добавлена.");
            } else {
                sortedTasks.add(task);
            }
        }
    }

    @Override
    public void createEpic(Epic epic) {
        int id = generateId();
        epic.setIdNumber(id);
        epics.put(id, epic);
    }

    @Override
    public void createSubTask(SubTask newSubTask) {
        if (epics.containsKey(newSubTask.getEpicId())) {
            int id = generateId();
            newSubTask.setIdNumber(id);
            subTasks.put(newSubTask.getIdNumber(), newSubTask);
            epics.get(newSubTask.getEpicId()).getSubTaskIds().add(newSubTask.getIdNumber());

            if (newSubTask.getStartTime() != null && newSubTask.getDuration() != null) {
                if (isCrossingTasks(newSubTask)) {
                    System.out.println("Ошибка: подзадача пересекается с другими задачами и не может быть добавлена.");
                } else {
                    sortedTasks.add(newSubTask);
                }
            }
            updateEpicStatus(newSubTask.getEpicId());
            updateEpicTime(epics.get(newSubTask.getEpicId()));
        } else {
            System.out.println("Ошибка: указанный эпик не существует.");
        }
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTask() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public Task getTaskById(int idNumber) {
        if (tasks.get(idNumber) != null) {
            historyManager.add(tasks.get(idNumber));
        }
        return tasks.get(idNumber);
    }

    @Override
    public Epic getEpicById(int idNumber) {
        if (epics.get(idNumber) != null) {
            historyManager.add(epics.get(idNumber));
        }
        return epics.get(idNumber);
    }

    @Override
    public SubTask getSubTaskById(int idNumber) {
        if (subTasks.get(idNumber) != null) {
            historyManager.add(subTasks.get(idNumber));
        }
        return subTasks.get(idNumber);
    }

    @Override
    public List<SubTask> getAllEpicSubtasks(Integer epicId) {
        List<Integer> subTasksId = epics.get(epicId).getSubTaskIds();
        List<SubTask> subtasksByEpic = new ArrayList<>();
        subTasksId.stream().forEach(i -> subtasksByEpic.add(subTasks.get(i)));
        return subtasksByEpic;
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getIdNumber())) {
            Task oldTask = tasks.get(task.getIdNumber());
            if (!isCrossingTasks(task)) {
                sortedTasks.remove(oldTask);
                tasks.put(task.getIdNumber(), task);
                sortedTasks.add(task);
            } else {
                System.out.println("Ошибка: новое состояние задачи пересекается с другими задачами и не может быть обновлено.");
            }
        }
    }


    @Override
    public void updateSubTask(SubTask newSubTask) {
        if (subTasks.containsKey(newSubTask.getIdNumber())) {
            SubTask oldSubTask = subTasks.get(newSubTask.getIdNumber());
            if (!isCrossingTasks(newSubTask)) {
                subTasks.put(newSubTask.getIdNumber(), newSubTask);
                sortedTasks.remove(oldSubTask);
                sortedTasks.add(newSubTask);
                if (epics.containsKey(newSubTask.getEpicId())) {
                    updateEpicStatus(newSubTask.getEpicId());
                    updateEpicTime(epics.get(newSubTask.getEpicId()));
                }
            } else {
                System.out.println("Ошибка: новое состояние подзадачи пересекается с другими и не может быть обновлено.");
            }
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

    @Override
    public void updateEpicStatus(int id) {
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
    public void removeAllTasks() {
        tasks.values().stream().forEach(task -> {
            historyManager.remove(task.getIdNumber());
            sortedTasks.remove(task);
        });
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        epics.values().forEach(epic -> {
            epic.getSubTaskIds().forEach(id -> {
                historyManager.remove(id);
                sortedTasks.remove(subTasks.get(id));
                subTasks.remove(id);
            });
            historyManager.remove(epic.getIdNumber());
        });
        epics.clear();
    }

    @Override
    public void removeAllSubtasks() {
        epics.values().stream().forEach(epic -> {
            epic.getSubTaskIds().forEach(id -> {
                historyManager.remove(id);
                sortedTasks.remove(subTasks.get(id));
            });
            epic.getSubTaskIds().clear();
            updateEpicStatus(epic.getIdNumber());
        });
        subTasks.clear();

        epics.values().stream().forEach(epic -> updateEpicTime(epic));
    }

    @Override
    public void deleteTask(int idNumber) {
        sortedTasks.remove(tasks.get(idNumber));
        historyManager.remove(idNumber);
        tasks.remove(idNumber);
    }

    @Override
    public void deleteEpic(Integer idNumber) {
        ArrayList<Integer> subTasksId = epics.get(idNumber).getSubTaskIds();
        subTasksId.stream().filter(i -> subTasks.containsKey(i)).forEach(i -> {
            sortedTasks.remove(subTasks.get(i));
            historyManager.remove(i);
            subTasks.remove(i);
        });
        historyManager.remove(idNumber);
        epics.remove(idNumber);
    }

    @Override
    public void deleteSubTasks(Integer idNumber) {
        int epicId = subTasks.get(idNumber).getEpicId();
        epics.get(epicId).getSubTaskIds().remove((Integer) idNumber);
        sortedTasks.remove(subTasks.get(idNumber));
        historyManager.remove(idNumber);
        subTasks.remove(idNumber);
        updateEpicStatus(epicId);
        updateEpicTime(epics.get(epicId));
    }

    private int generateId() {
        return idNumber++;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

}