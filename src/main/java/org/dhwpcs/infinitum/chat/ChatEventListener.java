package org.dhwpcs.infinitum.chat;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.dhwpcs.infinitum.Global;
import org.dhwpcs.infinitum.chat.brigadier.FeatureDispatcher;
import org.dhwpcs.infinitum.chat.data.MsgHistory;
import org.dhwpcs.infinitum.chat.data.MsgManager;
import org.dhwpcs.infinitum.chat.feature.inst.FeatureAt;
import org.dhwpcs.infinitum.chat.feature.inst.FeatureReply;
import org.dhwpcs.infinitum.papershelled.Infinitum;

import java.io.IOException;

public class ChatEventListener implements Listener {

    final ChatRendererInf renderer;
    private final Infinitum plugin;
    private final FeatureDispatcher dispatcher;

    public ChatEventListener(Infinitum plugin) {
        this.plugin = plugin;
        MsgManager manager;
        MsgHistory history;
        try {
            manager = new MsgManager(plugin.getDataFolder().toPath().resolve("msghistory"));
            history = new MsgHistory(manager);
            Global.setHistory(history);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dispatcher = new FeatureDispatcher();
        dispatcher.register(new FeatureAt());
        dispatcher.register(new FeatureReply());
        renderer = new ChatRendererInf(history, dispatcher);
    }

    @EventHandler
    public void onChatMessage(AsyncChatEvent event) {
        event.renderer(renderer);
        Bukkit.getScheduler().runTaskLater(plugin, renderer.afterTask(), 50);
    }
}
