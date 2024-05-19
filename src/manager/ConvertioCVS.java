package manager;

import task.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConvertioCVS {1
    public static String toString(Task task) {
        String type;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        if (task.getType().equals(TaskType.SUBTASK)) {
            type = TaskType.SUBTASK.toString();
        } else if (task.getType().equals(TaskType.EPIC)) {
            type = TaskType.EPIC.toString();
        } else {
            type = TaskType.TASK.toString();
        }

        String line = task.getIdNumber() + "," + type + "," + task.getTaskName() + "," + task.getStatusTask() + "," + task.getDescription();

        if (task.getType().equals(TaskType.SUBTASK)) {
            line = line + "," + ((SubTask) task).getEpicId();
        }

        if (task.getStartTime() != null && task.getDuration() != null) {
            if (task.getType().equals(TaskType.SUBTASK)) {
                line = line + "," + task.getStartTime().format(formatter) + "," + task.getDuration().toMinutes() + "," + task.getEndTime().format(formatter);
            } else {
                line = line + ",null," + task.getStartTime().format(formatter) + "," + task.getDuration().toMinutes() + "," + task.getEndTime().format(formatter);
            }
        }
        return line;
    }

    public static Task fromString(String value) {
        String[] values = value.split(",");
        Task task = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        if (values[1].equals(TaskType.TASK.toString())) {
            if (values.length == 9) {

                task = new Task(values[2], values[4], LocalDateTime.parse(values[6], formatter), Duration.ofMinutes(Long.parseLong(values[7])));
            } else {
                task = new Task(values[2], values[4]);
            }
            task.setIdNumber(Integer.parseInt(values[0]));
            task.setStatusTask(StatusTask.valueOf(values[3]));
        } else if (values[1].equals((TaskType.EPIC.toString()))) {

            task = new Epic(values[2], values[4]);
            task.setIdNumber(Integer.parseInt(values[0]));
            task.setStatusTask(StatusTask.valueOf(values[3]));
        } else if (values[1].equals((TaskType.SUBTASK.toString()))) {
            if (values.length == 9) {

                task = new SubTask(values[2], values[4], Integer.parseInt(values[5]), LocalDateTime.parse(values[6], formatter), Duration.ofMinutes(Long.parseLong(values[7])));
            } else {
                task = new SubTask(values[2], values[4], Integer.parseInt(values[5]));
            }
            task.setIdNumber(Integer.parseInt(values[0]));
            task.setStatusTask(StatusTask.valueOf(values[3]));
        }
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