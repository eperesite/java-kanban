package task;

import java.util.Objects;

public class SubTask extends Task {

    private int epicId;
    protected TaskType type;

    public SubTask(String taskName, String description, int epicId, StatusTask statusTask) {
        super(taskName, description);
        this.epicId = epicId;
        this.type = TaskType.SUBTASK;
        this.statusTask = statusTask;
    }

    public SubTask(String taskName, String description, int epicId) {
        super(taskName, description);
        this.epicId = epicId;
        this.type = TaskType.SUBTASK;
    }

    public int getEpicId() {
        return epicId;
    }

    public TaskType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return epicId == subTask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "описание = '" + getDescription() + '\'' +
                ", задача = '" + getTaskName() + '\'' +
                ", статус = " + getStatusTask() +
                ", id задачи = " + getIdNumber() +
                '}';
    }
}