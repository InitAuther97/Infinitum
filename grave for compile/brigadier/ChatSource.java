package org.dhwpcs.infinitum.chat.brigadier;

import net.kyori.adventure.text.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.dhwpcs.infinitum.chat.data.Message;
import org.dhwpcs.infinitum.chat.data.MsgHistory;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.MatchResult;

public class ChatSource extends CommandSourceStack {

    final MatchResult[] result;
    private final org.bukkit.entity.Player bukkitPlayer;
    private final net.minecraft.server.level.ServerPlayer nmsPlayer;
    private final MsgHistory history;
    private Component[] replacement;
    private List<Runnable> afterTask;
    private List<Object> caches = new LinkedList<>();
    int pointer = 0;

    public ChatSource(CommandSourceStack source, MatchResult[] result, MsgHistory history, List<Runnable> afterTask) {
        super(source.getEntity(),
                source.getPosition(),
                source.getRotation(),
                source.getLevel(),
                source.getEntity() instanceof Player
                        ? source.getServer().getProfilePermissions(((Player)source.getEntity()).gameProfile)
                        : source.getServer().getOperatorUserPermissionLevel(),
                source.getTextName(),
                source.getDisplayName(),
                source.getServer(),
                source.getEntity());
        this.result = result;
        this.bukkitPlayer = (org.bukkit.entity.Player) source.getBukkitEntity();
        this.nmsPlayer = (net.minecraft.server.level.ServerPlayer) source.getEntity();
        this.history = history;
        this.replacement = new Component[result.length];
        this.afterTask = afterTask;
    }

    public void replace(Component cmp) {
        this.replacement[pointer] = cmp;
    }

    public void replace(String str) {
        this.replacement[pointer] = Component.text(str);
    }

    public void apply(Function<Component, Component> closure) {
        replacement[pointer] = closure.apply(replacement[pointer]);
    }

    public Component getReplacement() {
        return replacement[pointer];
    }

    void afterTask(Runnable t) {
        afterTask.add(t);
    }

    public List<Runnable> getTasks() {
        return afterTask;
    }

    public void store(Object cache) {
        caches.add(cache);
    }

    public<T> T getCache(int index) {
        return (T)caches.get(index);
    }

    public Message surfMsg(int id) {
        return history.getMessage(id);
    }

    public org.bukkit.entity.Player getBukkitPlayer() {
        return bukkitPlayer;
    }

    @Nullable
    public ServerPlayer getNMSPlayer() {
        return nmsPlayer;
    }
}
