package org.dhwpcs.infinitum.chat;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.dhwpcs.infinitum.Infinitum;

public class ChatEventListener implements Listener {

    private final ChatRendererInf renderer;
    private final Infinitum plugin;

    public ChatEventListener(Infinitum plugin, ChatGlobal global) {
        this.plugin = plugin;
        this.renderer = new ChatRendererInf(plugin, global);
    }

    @EventHandler
    public void onChatMessage(AsyncChatEvent event) {
        plugin.getLogger().info("Triggering paper async chat event!");
        event.renderer(renderer);
        Bukkit.getScheduler().runTaskLater(plugin, renderer.afterTask(), 50);
    }
}
