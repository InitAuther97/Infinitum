package org.dhwpcs.infinitum.http.daily;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.dhwpcs.infinitum.Global;
import org.dhwpcs.infinitum.dailictivity.DailictivityListener;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

public class DailyHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        DailictivityListener listener = Global.daily.getListener();
        Map<String, UUID> logins = listener.pop();
        byte[] data = Global.GSON.toJson(logins).getBytes(StandardCharsets.UTF_8);
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, data.length);
        OutputStream body = exchange.getResponseBody();
        body.write(data);
        exchange.close();
    }
}
