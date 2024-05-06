package manager;

import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskType;

import java.io.*;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {
        File history = new File("fileCSV/File.csv");
        FileBackedTaskManager fileManager = loadFromFile(history);

        System.out.println("Список задач из TaskManager просмотров:");
        System.out.println(fileManager.getAllTasks() + "\n");
        System.out.println(fileManager.getAllEpic() + "\n");
        System.out.println(fileManager.getAllSubTask() + "\n");

        System.out.println("Список задач из HistoryManager просмотров:");
        for (Task task : fileManager.getHistory()) {
            System.out.println(task.toString() + "\n");
        }
    }

    private void save() {
        try (Writer writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,epic\n");

            for (Task task : tasks.values()) {
                writer.write(ConvertioCVS.toString(task) + "\n");
            }

            for (Epic epic : epics.values()) {
                writer.write(ConvertioCVS.toString(epic) + "\n");
            }

            for (SubTask subTask : subTasks.values()) {
                writer.write(ConvertioCVS.toString(subTask) + "\n");
            }

            writer.write("\n");
            writer.write(ConvertioCVS.historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи данных в файл.");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileManager = new FileBackedTaskManager(file);

        try (Reader reader = new FileReader(file); BufferedReader br = new BufferedReader(reader)) {
            int maxNumberId = 0;

            br.readLine();

            while (br.ready()) {
                String line = br.readLine();
                if (!line.isEmpty()) {
                    Task task = ConvertioCVS.fromString(line);
                    if (task.getType().equals(TaskType.EPIC)) {
                        fileManager.epics.put(task.getIdNumber(), (Epic) task);
                    } else if (task.getType().equals(TaskType.SUBTASK)) {
                        fileManager.subTasks.put(task.getIdNumber(), (SubTask) task);
                        Epic epic = fileManager.epics.get(((SubTask) task).getEpicId());
                        epic.getSubTaskIds().add(task.getIdNumber());
                    } else {
                        fileManager.tasks.put(task.getIdNumber(), task);
                    }
                    if (maxNumberId <= task.getIdNumber()) {
                        maxNumberId = task.getIdNumber();
                        fileManager.idNumber = maxNumberId;
                    }
                }

                if (line.isBlank()) {
                    line = br.readLine();
                    List<Integer> viewedTaskId = ConvertioCVS.historyFromString(line);

                    for (Integer id : viewedTaskId) {
                        if (fileManager.tasks.containsKey(id)) {
                            fileManager.historyManager.add(fileManager.tasks.get(id));
                        } else if (fileManager.epics.containsKey(id)) {
                            fileManager.historyManager.add(fileManager.epics.get(id));
                        } else if (fileManager.subTasks.containsKey(id)) {
                            fileManager.historyManager.add(fileManager.subTasks.get(id));
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new ManagerSaveException("Файл не найден.");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения данных из файла.");
        }
        return fileManager;
    }

    @Override
    public void createTask(Task newTask) {
        super.createTask(newTask);
        save();
    }

    @Override
    public void createEpic(Epic newEpic) {
        super.createEpic(newEpic);
        save();
    }

    @Override
    public void createSubTask(SubTask newSubTask) {
        super.createSubTask(newSubTask);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    protected void updateEpicStatus(int idNumber) {
        super.updateEpicStatus(idNumber);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
    }

    @Override
    public void deleteTask(int idNumber) {
        super.deleteTask(idNumber);
        save();
    }

    @Override
    public void deleteEpic(Integer idNumber) {
        super.deleteEpic(idNumber);
        save();
    }

    @Override
    public void deleteSubTasks(Integer idNumber) {
        super.deleteSubTasks(idNumber);
        save();
    }
}
