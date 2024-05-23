package exeption;

import task.TaskType;

public class ManagerTaskNotFoundException extends RuntimeException {1
    public ManagerTaskNotFoundException(String message) {
        super(message);
    }

    public ManagerTaskNotFoundException(final TaskType type) {
        this("Задача типа " + type.toString() + " не найдена в менеджере");
    }
}