package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected String description;
    protected String taskName;
    protected StatusTask statusTask;
    protected int idNumber;
    protected TaskType type;
    protected LocalDateTime startTime;
    protected Duration duration;

    public Task(String taskName, String description) {
        this(taskName, description, StatusTask.NEW);
    }

    public Task(String taskName, String description, LocalDateTime startTime, Duration duration) {
        this(taskName, description, StatusTask.NEW);
        this.startTime = startTime;
        this.duration = duration;
    }

    protected Task(String taskName, String description, StatusTask statusTask) {
        this.taskName = taskName;
        this.description = description;
        this.statusTask = statusTask;
        this.type = TaskType.TASK;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setStatusTask(StatusTask statusTask) {
        this.statusTask = statusTask;
    }

    public Integer getIdNumber() {
        return idNumber;
    }

    public StatusTask getStatusTask() {
        return statusTask;
    }

    public TaskType getType() {
        return type;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plusMinutes(duration.toMinutes());
        }
        return null;
    }

    @Override
    public String toString() {
        return "Task{" + "описание = '" + description + '\'' + ", задача = '" + taskName + '\'' + ", статус = " + statusTask + ", id задачи = " + idNumber + ", начало времени = '" + startTime + '\'' + ", продолжительность = '" + duration + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return idNumber == task.idNumber && Objects.equals(taskName, task.taskName) && Objects.equals(description, task.description) && statusTask == task.statusTask && Objects.equals(startTime, task.startTime) && Objects.equals(duration, task.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, taskName, statusTask, idNumber, startTime, duration);
    }
}
