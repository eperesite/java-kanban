package server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.regex.Pattern;
import manager.TaskManager;
import server.HttpTaskServer;

public class HistoryHandler extends AbstractHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        try (exchange) {
            switch (method) {
                case "GET": {
                    // ecли путь "/history"
                    if (Pattern.matches("^/history$", path)) {
                        String response = gson.toJson(taskManager.getHistory());
                        writeResponse(exchange, response);
                    } else {
                        sendNotFoundRequestResponseHeaders(exchange);
                    }

                    break;
                }
                default: {
                    sendNotFoundEndpointResponseHeaders(exchange, method);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } catch (Throwable exception) {
            exception.printStackTrace();
            sendInternalServerErrorResponseHeaders(exchange);
        }
    }
}