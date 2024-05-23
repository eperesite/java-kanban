package task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<Integer> epicSubtasksId = new ArrayList<>();

    private LocalDateTime endTime;

    public Epic(String taskName, String description) {
        super(taskName, description);
        this.type = TaskType.EPIC;
    }

    public ArrayList<Integer> getSubTaskIds() {
        return epicSubtasksId;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void addEpicSubtaskId(Integer idNumber) {
        epicSubtasksId.add(idNumber);
    }

    public void removeEpicSubtaskId(int idNumber) {
        int index = epicSubtasksId.indexOf(idNumber);
        if (index != -1) {
            epicSubtasksId.remove(index);
        }
    }

    public void clearSubtaskIdList() {
        epicSubtasksId.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(epicSubtasksId, epic.epicSubtasksId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicSubtasksId);
    }

    @Override
    public String toString() {
        return "Epic{" + "задача='" + taskName + '\'' + ", описание='" + description + '\'' + ", id='" + idNumber + '\'' + ", статус='" + statusTask + '\'' + ", подзадача' ID='" + epicSubtasksId + '\'' + ", начало времени='" + startTime + '\'' + ", конец времени='" + endTime + '\'' + ", duration='" + duration + '}';
    }

}
