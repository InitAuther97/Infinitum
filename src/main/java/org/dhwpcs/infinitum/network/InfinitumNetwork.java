package org.dhwpcs.infinitum.network;

import org.bukkit.configuration.ConfigurationSection;
import org.dhwpcs.infinitum.network.http.InfinitumHttp;
import org.dhwpcs.infinitum.Infinitum;

import java.io.IOException;

public class InfinitumNetwork {
    private final Infinitum infinitum;
    private InfinitumHttp http;
    public InfinitumNetwork(Infinitum infinitum) {
        this.infinitum = infinitum;
    }

    public void initialize(ConfigurationSection http) throws IOException {
        if(!http.contains("port")) {
            http.set("port", 80);
        }
        this.http = new InfinitumHttp(http.getInt("port"), infinitum);
    }

    public void bootstrap() {
        http.start();
    }

    public void stop() {
        http.stop();
    }
}
