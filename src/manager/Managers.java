package manager;

import java.io.File;

public class Managers {

    public static TaskManager getDefaultFileManager() {
        return new FileBackedTaskManager(new File("fileCSV/File.csv"));
    }

    public static TaskManager getDefault() { // возвращаем объект InMemoryTaskManager
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() { // возвращаем объект InMemoryHistoryManager

        return new InMemoryHistoryManager();

    }
}