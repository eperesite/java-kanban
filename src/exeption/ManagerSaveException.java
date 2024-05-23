package exeption;

public class ManagerSaveException extends RuntimeException {1
    public ManagerSaveException(String message) {
        super(message);
    }
}