package manager;

import task.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConvertioCVS {
    public static String toString(Task task) {
        String type = task.getType().toString();
        String line = task.getIdNumber() + "," + type + "," + task.getTaskName() + "," + task.getStatusTask() + "," + task.getDescription();

        if (task.getType() == TaskType.SUBTASK) {
            line = line + "," + ((SubTask) task).getEpicId();
        }
        return line;
    }

    public static Task fromString(String value) {
        String[] values = value.split(",");
        Task task = null;

        if (values[1].equals(TaskType.TASK.toString())) {
            task = new Task(values[2], values[4]);
        } else if (values[1].equals((TaskType.EPIC.toString()))) {
            task = new Epic(values[2], values[4]);
        } else {
            task = new SubTask(values[2], values[4], Integer.parseInt(values[5]));
        }
        task.setIdNumber(Integer.parseInt(values[0]));
        task.setStatusTask(StatusTask.valueOf(values[3]));

        return task;
    }

    public static String historyToString(HistoryManager manager) {
        StringBuilder viewedTaskId = new StringBuilder();

        for (Task task : manager.getHistory()) {
            viewedTaskId.append(task.getIdNumber()).append(",");
        }
        return viewedTaskId.toString();
    }

    public static List<Integer> historyFromString(String value) {
        if (value != null) {
            String[] values = value.split(",");
            List<Integer> viewedTaskId = new ArrayList<>();

            for (String s : values) {
                viewedTaskId.add(Integer.parseInt(s));
            }
            return viewedTaskId;
        } else {
            return Collections.emptyList();
        }
    }
}