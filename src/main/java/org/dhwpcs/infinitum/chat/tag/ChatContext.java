package org.dhwpcs.infinitum.chat.tag;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.CommandSender;
import org.dhwpcs.infinitum.chat.ChatGlobal;
import org.dhwpcs.infinitum.Infinitum;

import java.util.HashSet;
import java.util.Set;

public class ChatContext {
    private final Set<Runnable> afterTasks = new HashSet<>();
    private final CommandSender sender;
    private final String raw;
    private final Infinitum infinitum;
    private final ChatGlobal global;

    public ChatContext(CommandSender sender, TextComponent msg, Infinitum infinitum, ChatGlobal global) {
        this.sender = sender;
        this.raw = msg.content();
        this.infinitum = infinitum;
        this.global = global;
    }

    public void addAfterTask(Runnable runnable) {
        afterTasks.add(runnable);
    }

    public CommandSender getSender() {
        return sender;
    }

    public String getContent() {
        return raw;
    }

    public Infinitum getInfinitum() {
        return infinitum;
    }

    public ChatGlobal getGlobal() {
        return global;
    }
}
