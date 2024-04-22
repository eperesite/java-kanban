import manager.InMemoryTaskManager;
import manager.TaskManager;
import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import static test.InMemoryHistoryManagerTests.historyManager;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new InMemoryTaskManager(historyManager);

        taskManager.createNewTask(new Task("Начать писать курсовую", "Осталось совсем немного до первого дедлайна",
                TaskStatus.IN_PROGRESS));

        taskManager.createNewTask(new Task("Сделать дз", "ПППП",
                TaskStatus.NEW));

        taskManager.createNewEpic(new Epic("Шопинг", "Надо купить лук для мероприятия"));
        taskManager.createNewSubTask(new SubTask("Купить платье", "дресскод белый",
                TaskStatus.DONE, 3));
        taskManager.createNewSubTask(new SubTask("Купить туфли", "не на каблуке, мероприятие долгое", TaskStatus.DONE, 3));

        taskManager.createNewEpic(new Epic("Студ союз",
                "ОЧень важно"));
        taskManager.createNewSubTask(new SubTask("Собрание 21.03", "18:00",
                TaskStatus.NEW, 6));
        taskManager.createNewSubTask(new SubTask("Написать спонсорам", "18:00",
                TaskStatus.DONE, 6));

        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubTask());

        System.out.println();
        taskManager.deleteTaskByID(2);
        System.out.println(taskManager.getAllTask());

        taskManager.deleteEpicByID(3);
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubTask());
    }
}
