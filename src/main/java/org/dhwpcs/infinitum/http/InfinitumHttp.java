package org.dhwpcs.infinitum.http;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.dhwpcs.infinitum.http.daily.DailyHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class InfinitumHttp {

    private final HttpServer server;
    private final Plugin plugin;

    public InfinitumHttp(int port, Plugin plg) throws IOException {
        plugin = plg;
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/infinitum/daily/", InfinitumHttp.runInMainThread(plugin, new DailyHandler()));
        server.setExecutor(null);
    }

    public void start() {
        server.start();
    }

    static HttpHandler runInMainThread(Plugin plugin, HttpHandler handler) {
        return it -> {
            IOException[] exs = new IOException[]{null};
            Bukkit.getScheduler().runTask(plugin, () -> {
                try {
                    handler.handle(it);
                } catch (IOException e) {
                    exs[0] = e;
                }
            });
            if(exs[0] != null) {
                throw exs[0];
            }
        };
    }
}
