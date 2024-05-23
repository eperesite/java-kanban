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
    public void createTaskByPostRequest() throws IOException, InterruptedException {
        taskManager.removeAllTasks();

        Task task = new Task("Task", "Task description",
                LocalDateTime.of(2024, 01, 01, 00, 00), Duration.ofMinutes(15));

        String taskToJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskToJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = taskManager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются.");
        assertEquals(1, tasksFromManager.size(), "Количество задач не соответствует.");
        assertEquals("Task", tasksFromManager.get(0).getTaskName(), "Наименование задачи не соответствует.");
    }

    @Test
    public void createEpicByPostRequest() throws IOException, InterruptedException {
        taskManager.removeAllTasks();
        taskManager.removeAllEpics();
        taskManager.removeAllSubtasks();

        Epic epic = new Epic("Epic", "Epic description");

        String epicToJson = gson.toJson(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicToJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Epic> epicsFromManager = taskManager.getAllEpic();

        assertNotNull(epicsFromManager, "Эпики не возвращаются.");
        assertEquals(1, epicsFromManager.size(), "Количество эпиков не соответствует.");
        assertEquals("Epic", epicsFromManager.get(0).getTaskName(), "Наименование эпика не соответствует.");
    }

    @Test
    public void createSubTaskByPostRequest() throws IOException, InterruptedException {
        taskManager.removeAllTasks();
        taskManager.removeAllEpics();
        taskManager.removeAllSubtasks();

        Epic epic = new Epic("Epic", "Epic description");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Subtask", "Subtask description", epic.getIdNumber(),
                LocalDateTime.of(2024, 01, 01, 00, 00), Duration.ofMinutes(15));

        String subTaskToJson = gson.toJson(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subTaskToJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<SubTask> subTasksFromManager = taskManager.getAllSubTask();

        assertNotNull(subTasksFromManager, "Подзадачи не возвращаются.");
        assertEquals(1, subTasksFromManager.size(), "Количество подзадач не соответствует.");
        assertEquals("Subtask", subTasksFromManager.get(0).getTaskName(), "Наименование подзадачи не соответствует.");
    }

    @Test
    public void getAllTasksByGetRequest() throws IOException, InterruptedException {
        Task task1 = taskManager.getTaskById(1);
        Task task2 = taskManager.getTaskById(2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        ArrayList<Task> tasksFromManager = gson.fromJson(response.body(), new TasksArrayListTypeToken().getType());

        assertEquals(task1.getIdNumber(), tasksFromManager.get(0).getIdNumber(), "id задачи не соответствует.");
        assertEquals(task2.getIdNumber(), tasksFromManager.get(1).getIdNumber(), "id задачи не соответствует.");
        assertEquals("Task1", tasksFromManager.get(0).getTaskName(), "Наименование задачи не соответствует.");
        assertEquals("Task2", tasksFromManager.get(1).getTaskName(), "Наименование задачи не соответствует.");
    }

    @Test
    public void getAllEpicsByGetRequest() throws IOException, InterruptedException {
        Epic epic3 = taskManager.getEpicById(3);
        Epic epic4 = taskManager.getEpicById(4);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        ArrayList<Epic> epicsFromManager = gson.fromJson(response.body(), new EpicsArrayListTypeToken().getType());

        assertEquals(epic3.getIdNumber(), epicsFromManager.get(0).getIdNumber(), "id эпика не соответствует.");
        assertEquals(epic4.getIdNumber(), epicsFromManager.get(1).getIdNumber(), "id эпика не соответствует.");
        assertEquals("Epic3", epicsFromManager.get(0).getTaskName(), "Наименование эпика не соответствует.");
        assertEquals("Epic4", epicsFromManager.get(1).getTaskName(), "Наименование эпика не соответствует.");

    }

    @Test
    public void getAllSubtasksByGetRequest() throws IOException, InterruptedException {
        SubTask subTask5 = taskManager.getSubTaskById(5);
        SubTask subTask6 = taskManager.getSubTaskById(6);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        ArrayList<SubTask> subTasksFromManager = gson.fromJson(response.body(),
                new SubTasksArrayListTypeToken().getType());

        assertEquals(subTask5.getIdNumber(), subTasksFromManager.get(0).getIdNumber(), "id подзадачи не соответствует.");
        assertEquals(subTask6.getIdNumber(), subTasksFromManager.get(1).getIdNumber(), "id подзадачи не соответствует.");
        assertEquals("Subtask5", subTasksFromManager.get(0).getTaskName(), "Наименование подзадачи не соответствует.");
        assertEquals("Subtask6", subTasksFromManager.get(1).getTaskName(), "Наименование подзадачи не соответствует.");
    }

    @Test
    public void getTaskByIdByGetRequest() throws IOException, InterruptedException {
        int taskId = 1;

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        final Task taskFromManager = gson.fromJson(response.body(), new TypeToken<Task>() {
        }.getType());

        assertEquals(taskId, taskFromManager.getIdNumber(), "id задачи не соответствует.");
        assertEquals("Task1", taskFromManager.getTaskName(), "Наименование задачи не соответствует.");
    }

    @Test
    public void getEpicByIdByGetRequest() throws IOException, InterruptedException {
        int epicId = 3;

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epicId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        final Epic epicFromManager = gson.fromJson(response.body(), new TypeToken<Epic>() {
        }.getType());

        assertEquals(epicId, epicFromManager.getIdNumber(), "id эпика не соответствует.");
        assertEquals("Epic3", epicFromManager.getTaskName(), "Наименование эпика не соответствует.");
    }

    @Test
    public void getSubtaskByIdByGetRequest() throws IOException, InterruptedException {
        int subTaskId = 5;

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subTaskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        final SubTask subTaskFromManager = gson.fromJson(response.body(), new TypeToken<SubTask>() {
        }.getType());

        assertEquals(subTaskId, subTaskFromManager.getIdNumber(), "id подзадачи не соответствует.");
        assertEquals("Subtask5", subTaskFromManager.getTaskName(), "Наименование подзадачи не соответствует.");
    }

    @Test
    public void getAllSubTaskByEpicIdByGetRequest() throws IOException, InterruptedException {
        SubTask subTask5 = taskManager.getSubTaskById(5);
        SubTask subTask6 = taskManager.getSubTaskById(6);

        int epicId = 3;

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epicId + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        ArrayList<SubTask> subTasksFromManager = gson.fromJson(response.body(),
                new SubTasksArrayListTypeToken().getType());

        assertEquals(subTask5.getIdNumber(), subTasksFromManager.get(0).getIdNumber(), "id подзадачи не соответствует.");
        assertEquals(subTask6.getIdNumber(), subTasksFromManager.get(1).getIdNumber(), "id подзадачи не соответствует.");
        assertEquals("Subtask5", subTasksFromManager.get(0).getTaskName(), "Наименование подзадачи не соответствует.");
        assertEquals("Subtask6", subTasksFromManager.get(1).getTaskName(), "Наименование подзадачи не соответствует.");
    }

    @Test
    public void updateTaskByPostRequest() throws IOException, InterruptedException {
        Task newTask = new Task("Task1", "Updated task",
                LocalDateTime.of(2024, 01, 01, 00, 00), Duration.ofMinutes(15));
        int taskId = 1;
        newTask.setIdNumber(taskId);

        String taskToJson = gson.toJson(newTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks?id=" + taskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskToJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = taskManager.getAllTasks();

        assertEquals("Updated task", tasksFromManager.get(0).getDescription(), "Описание задачи не соответствует.");
    }

    @Test
    public void updateEpicByPostRequest() throws IOException, InterruptedException {
        Epic newEpic = new Epic("Epic3", "Updated epic");
        int epicId = 3;
        newEpic.setIdNumber(epicId);

        String epicToJson = gson.toJson(newEpic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics?id=" + epicId);
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicToJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Epic> epicsFromManager = taskManager.getAllEpic();

        assertEquals("Updated epic", epicsFromManager.get(0).getDescription(), "Описание эпика не соответствует.");
    }

    @Test
    public void updateSubtaskByPostRequest() throws IOException, InterruptedException {
        SubTask newSubTask = new SubTask("Subtask5", "Updated subtask", 1,
                LocalDateTime.of(2024, 01, 02, 00, 00), Duration.ofMinutes(15));
        int subTaskId = 5;
        newSubTask.setIdNumber(subTaskId);

        String subTaskToJson = gson.toJson(newSubTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks?=id" + subTaskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subTaskToJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<SubTask> subTasksFromManager = taskManager.getAllSubTask();

        assertEquals("Updated subtask", subTasksFromManager.get(0).getDescription(),
                "Описание подзадачи не соответствует.");
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
    public void removeTaskByIdByDeleteRequest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks?id=" + 1);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(204, response.statusCode());

        ManagerTaskNotFoundException thrown = Assertions.assertThrows(ManagerTaskNotFoundException.class, () -> {
            taskManager.getTaskById(1);
        }, "Ожидалось получение исключения");

        assertEquals("Задача типа TASK не найдена в менеджере", thrown.getMessage());
    }

    @Test
    public void removeEpicByIdByDeleteRequest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics?=id" + 3);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(204, response.statusCode());

        ManagerTaskNotFoundException thrown = Assertions.assertThrows(ManagerTaskNotFoundException.class, () -> {
            taskManager.getEpicById(3);
        }, "Ожидалось получение исключения");

        assertEquals("Задача типа EPIC не найдена в менеджере", thrown.getMessage());
    }

    @Test
    public void removeSubtaskByIdByDeleteRequest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks?id=" + 5);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(204, response.statusCode());

        ManagerTaskNotFoundException thrown = Assertions.assertThrows(ManagerTaskNotFoundException.class, () -> {
            taskManager.getSubTaskById(5);
        }, "Ожидалось получение исключения");

        assertEquals("Задача типа SUBTASK не найдена в менеджере", thrown.getMessage());
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