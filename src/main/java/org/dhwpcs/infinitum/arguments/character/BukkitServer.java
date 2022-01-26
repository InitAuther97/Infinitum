package org.dhwpcs.infinitum.arguments.character;

import io.github.initauther97.arguments.env.Characteristic;
import org.bukkit.Bukkit;
import org.bukkit.Server;

public class BukkitServer implements Characteristic<Server> {
    @Override
    public String identifier() {
        return "bukkit";
    }

    @Override
    public Server get() {
        return Bukkit.getServer();
    }
}
