package org.dhwpcs.infinitum.chat;

import io.github.initauther97.nugget.file.type.FolderType;
import org.bukkit.Bukkit;
import org.dhwpcs.infinitum.chat.data.MsgHistory;
import org.dhwpcs.infinitum.chat.data.MsgManager;
import org.dhwpcs.infinitum.Infinitum;

import java.io.IOException;

public class InfinitumChat {
    private final ChatGlobal global;
    private final ChatEventListener listener;
    public InfinitumChat(Infinitum infinitum) {
        MsgManager manager;
        MsgHistory history;
        try {
            manager = new MsgManager(infinitum.getFileManager().visit(FolderType.IDENTITY, "msghistory"));
            history = new MsgHistory(manager);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        global = new ChatGlobal(infinitum, history);
        listener = new ChatEventListener(infinitum, global);
        Bukkit.getPluginManager().registerEvents(listener, infinitum);
    }

    public ChatEventListener getListener() {
        return listener;
    }

    public ChatGlobal getGlobal() {
        return global;
    }
}
