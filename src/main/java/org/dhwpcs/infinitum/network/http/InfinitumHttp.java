package org.dhwpcs.infinitum.network.http;

import com.sun.net.httpserver.HttpServer;
import org.dhwpcs.infinitum.Infinitum;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class InfinitumHttp {

    private final HttpServer server;
    private final Infinitum plugin;

    public InfinitumHttp(int port, Infinitum plg) throws IOException {
        plugin = plg;
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/infinitum/daily/", new DailyHTTPHandler(plg.getStatistics().getDaily().getListener()));
        server.setExecutor(null);
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop((int) TimeUnit.SECONDS.toMillis(5));
    }
}
