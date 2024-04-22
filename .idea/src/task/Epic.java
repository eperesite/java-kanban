package task;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<Integer> subTaskIds = new ArrayList<>();

    public Epic(String nameTask, String description) {
        super(nameTask, description);
    }

    public ArrayList<Integer> getSubTaskIds() {
        return new ArrayList<>(subTaskIds);
    }

    public void setSubTaskId(int subTaskId) {
        subTaskIds.add(subTaskId);
    }

    public void removeSubTaskId(int subTaskId) {
        subTaskIds.remove(Integer.valueOf(subTaskId));
    }

    public void clearSubTaskIds() {
        subTaskIds.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic)) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTaskIds, epic.subTaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTaskIds);
    }

    @Override
    public String toString() {
        return "Epic{" +
                super.toString() +
                "subTaskIds=" + subTaskIds +
                '}';
    }
}
