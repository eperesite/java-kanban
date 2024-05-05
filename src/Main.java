import manager.*;

import manager.Managers;
import manager.TaskManager;
import task.Epic;

import task.SubTask;

import task.Task;



public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Задача 1", "Описание 1");
        Task task2 = new Task("Задача 2", "Описание 2");

        Epic epic3 = new Epic("План 1", "Описание плана 1");
        Epic epic4 = new Epic("План 2", "Описание плана 2");

        SubTask subTask5 = new SubTask("Исполнение плана 1", "Описание 1", 2);
        SubTask subTask6 = new SubTask("Исполнение плана 2", "Описание 2", 2);
        SubTask subTask7 = new SubTask("Исполнение плана 3", "Описание 3", 2);

        // проверка работы истории просмотров задач
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

        manager.deleteTask(0);
        manager.deleteEpic(3);
        manager.deleteSubTasks(4);

        // проверить историю после удаления task/epic/subtask
        // в файле File.csv должны сохранится id в следующей последовательности: 1,2,5,6
        System.out.println("\nОбновленная история просмотров:");
        for (int i = 0; i < manager.getHistory().size(); i++) {
            System.out.println((i + 1) + ". " + manager.getHistory().get(i));
        }
    }
}