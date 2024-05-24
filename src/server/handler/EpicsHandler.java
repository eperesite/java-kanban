package server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exeption.ManagerTaskNotFoundException;
import manager.TaskManager;
import server.HttpTaskServer;
import task.Epic;

import java.io.IOException;
import java.util.regex.Pattern;

public class EpicsHandler extends AbstractHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public EpicsHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        try {
            switch (method) {
                case "GET": {
                    if (Pattern.matches("^/epics$", path)) {
                        String response = gson.toJson(taskManager.getAllEpic());
                        writeResponse(exchange, response);
                        break;
                    }

                    if (Pattern.matches("^/epics/\\d+$", path)) {
                        String pathId = path.substring(7);
                        int id = parsePathId(pathId);
                        String response = gson.toJson(taskManager.getEpicById(id));
                        writeResponse(exchange, response);
                        break;
                    }

                    if (Pattern.matches("^/epics/\\d+/subtasks$", path)) {
                        String pathId = path.substring(7, (path.length() - 9));
                        int id = parsePathId(pathId);
                        String response = gson.toJson(taskManager.getAllEpicSubtasks(id));
                        writeResponse(exchange, response);
                        break;
                    }
                    break;
                }
                case "DELETE": {
                    if (Pattern.matches("^/epics$", path)) {
                        String query = exchange.getRequestURI().getQuery();

                        if (query != null) {
                            String pathId = query.substring(3);
                            int id = parsePathId(pathId);
                            taskManager.deleteEpic(id);
                            sendDeletedTaskContentResponseHeaders(exchange, id);
                        } else {
                            taskManager.removeAllEpics();
                            sendDeletedAllTasksContentResponseHeaders(exchange);
                        }
                    }
                    break;
                }
                case "POST": {
                    String request = readRequest(exchange);
                    if (request.isEmpty()) {
                        sendErrorRequestResponseHeaders(exchange);
                        break;
                    }
                    Epic epic = gson.fromJson(request, Epic.class);

                    if (Pattern.matches("^/epics$", path)) {
                        if (epic.getIdNumber() != null) {
                            taskManager.updateEpic(epic);
                            sendUpdatedTaskContentResponseHeaders(exchange, epic.getIdNumber());
                        } else {
                            taskManager.createEpic(epic);
                            sendCreatedTaskContentResponseHeaders(exchange, epic.getIdNumber());
                        }
                    }
                    break;
                }
                default: {
                    sendNotFoundEndpointResponseHeaders(exchange, method);
                }
            }
        } catch (NumberFormatException exception) {
            sendErrorRequestResponseHeaders(exchange);
        } catch (ManagerTaskNotFoundException exception) {
            sendNotFoundRequestResponseHeaders(exchange);
        } catch (Throwable exception) {
            exception.printStackTrace();
            sendInternalServerErrorResponseHeaders(exchange);
        } finally {
            exchange.close();
        }
    }
}
