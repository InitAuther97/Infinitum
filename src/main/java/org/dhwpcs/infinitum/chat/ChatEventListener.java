package org.dhwpcs.infinitum.chat;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.dhwpcs.infinitum.chat.data.MsgHistory;
import org.dhwpcs.infinitum.chat.data.MsgManager;
import org.dhwpcs.infinitum.papershelled.Infinitum;

import java.io.IOException;

public class ChatEventListener implements Listener {
    final ChatRendererInf renderer;
    private final Infinitum plugin;
    public ChatEventListener(Infinitum plugin) {
        this.plugin = plugin;
        MsgManager manager;
        try {
            manager = new MsgManager(plugin.getDataFolder().toPath().resolve("msghistory"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MsgHistory history = new MsgHistory();
        history.setManager(manager);
        renderer = new ChatRendererInf(history);
    }

    @EventHandler
    public void onChatMessage(AsyncChatEvent event) {
        event.renderer(renderer);
        Bukkit.getScheduler().runTaskLater(plugin, renderer.afterTask(), 50);
    }
}
