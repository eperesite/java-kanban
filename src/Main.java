import manager.*;

import manager.Managers;
import manager.TaskManager;
import task.Epic;

import task.SubTask;

import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;


public class Main {
    public static void main(String[] args) {
        
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Задача 1", "Описание 1", LocalDateTime.of(2024, 03, 15, 10, 15), Duration.ofMinutes(360));
        Task task2 = new Task("Задача 2", "Описание 2", LocalDateTime.of(2024, 03, 20, 10, 15), Duration.ofMinutes(360));

        Epic epic3 = new Epic("План 1", "Описание плана 1");
        Epic epic4 = new Epic("План 2", "Описание плана 2");

        SubTask subTask5 = new SubTask("Исполнение плана 1", "Описание 1", 2, LocalDateTime.of(2024, 03, 17, 10, 00), Duration.ofMinutes(360));
        SubTask subTask6 = new SubTask("Исполнение плана 2", "Описание 2", 2, LocalDateTime.of(2024, 03, 17, 18, 00), Duration.ofMinutes(15));
        SubTask subTask7 = new SubTask("Исполнение плана 3", "Описание 3", 2, LocalDateTime.of(2024, 03, 18, 02, 00), Duration.ofMinutes(60));

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.createEpic(epic3);
        taskManager.createEpic(epic4);

        taskManager.createSubTask(subTask5);
        taskManager.createSubTask(subTask6);
        taskManager.createSubTask(subTask7);

        taskManager.getTaskById(0);
        taskManager.getTaskById(1);
        taskManager.getTaskById(0);
        taskManager.getTaskById(1);

        taskManager.getEpicById(2);
        taskManager.getEpicById(3);
        taskManager.getEpicById(2);
        taskManager.getEpicById(3);

        taskManager.getSubTaskById(4);
        taskManager.getSubTaskById(5);
        taskManager.getSubTaskById(6);
        taskManager.getSubTaskById(4);
        taskManager.getSubTaskById(5);
        taskManager.getSubTaskById(6);

        printAllTasks(taskManager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("\nИстория просмотров:");
        for (int i = 0; i < manager.getHistory().size(); i++) {
            System.out.println((i + 1) + ". " + manager.getHistory().get(i));
        }

        System.out.println(manager.getPrioritizedTasks().toString());

        manager.deleteTask(0);
        manager.deleteEpic(3);
        manager.deleteSubTasks(4);

        System.out.println("\nОбновленная история просмотров:");
        for (int i = 0; i < manager.getHistory().size(); i++) {
            System.out.println((i + 1) + ". " + manager.getHistory().get(i));
        }

        System.out.println(manager.getPrioritizedTasks().toString());
    }
}