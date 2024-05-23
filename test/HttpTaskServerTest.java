package test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import exeption.ManagerTaskNotFoundException;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import task.Epic;
import task.SubTask;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    TaskManager taskManager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(taskManager);
    private final Gson gson = HttpTaskServer.getGson();

    public HttpTaskServerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        Task task1 = new Task("Task1", "Task description", LocalDateTime.of(2024, 01, 01, 00, 00), Duration.ofMinutes(15));
        Task task2 = new Task("Task2", "Task description", LocalDateTime.of(2024, 01, 01, 01, 00), Duration.ofMinutes(15));
        Epic epic3 = new Epic("Epic3", "Epic description");
        Epic epic4 = new Epic("Epic4", "Epic description");
        SubTask subTask5 = new SubTask("Subtask5", "Subtask description", 3, LocalDateTime.of(2024, 01, 01, 02, 00),
                Duration.ofMinutes(15));
        SubTask subTask6 = new SubTask("Subtask6", "Subtask description", 3, LocalDateTime.of(2024, 01, 01, 03, 00),
                Duration.ofMinutes(15));

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic3);
        taskManager.createEpic(epic4);
        taskManager.createSubTask(subTask5);
        taskManager.createSubTask(subTask6);

        taskServer.start();
    }

    @Test
    public void removeAllTasksByDeleteRequest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(204, response.statusCode());

        assertTrue(taskManager.getAllTasks().isEmpty(), "Задачи не удалены.");
    }

    @Test
    public void removeAllEpicsByDeleteRequest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(204, response.statusCode());

        assertTrue(taskManager.getAllEpic().isEmpty(), "Эпики не удалены.");
    }

    @Test
    public void removeAllSubtasksByDeleteRequest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(204, response.statusCode());

        assertTrue(taskManager.getAllSubTask().isEmpty(), "Подзадачи не удалены.");
    }
    @Test
    public void getPrioritizedTasksByGetRequest() throws IOException, InterruptedException {
        TreeSet<Task> sortedTasks = taskManager.getPrioritizedTasks();
        String sortedTasksToJson = gson.toJson(sortedTasks);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        assertEquals(sortedTasksToJson, response.body(), "Список задач не соответствует.");
    }

    @Test
    public void getHistoryByGetRequest() throws IOException, InterruptedException {
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getEpicById(4);
        taskManager.getSubTaskById(5);
        taskManager.getSubTaskById(6);


        List<Task> history = taskManager.getHistory();
        String historyToJson = gson.toJson(history);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        assertEquals(historyToJson, response.body(), "Список истории не соответствует.");
    }

    @AfterEach
    public void shutDown() {
        taskManager.removeAllSubtasks();
        taskManager.removeAllEpics();
        taskManager.removeAllEpics();

        taskServer.stop();
    }

    class TasksArrayListTypeToken extends TypeToken<ArrayList<Task>> {
    }

    class EpicsArrayListTypeToken extends TypeToken<ArrayList<Epic>> {
    }

    class SubTasksArrayListTypeToken extends TypeToken<ArrayList<SubTask>> {
    }
}