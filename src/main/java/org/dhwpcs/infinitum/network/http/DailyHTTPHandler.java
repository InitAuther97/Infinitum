package org.dhwpcs.infinitum.network.http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.dhwpcs.infinitum.Global;
import org.dhwpcs.infinitum.statistics.DailyEventHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class DailyHTTPHandler implements HttpHandler {

    private final DailyEventHandler handler;

    public DailyHTTPHandler(DailyEventHandler handler) {
        this.handler = handler;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, Long> logins = handler.getActivities();
        byte[] data = Global.GSON.toJson(logins).getBytes(StandardCharsets.UTF_8);
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, data.length);
        OutputStream body = exchange.getResponseBody();
        body.write(data);
        exchange.close();
    }
}