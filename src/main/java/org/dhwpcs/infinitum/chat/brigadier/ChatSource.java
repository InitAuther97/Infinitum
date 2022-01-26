package org.dhwpcs.infinitum.chat.brigadier;

import net.kyori.adventure.text.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.regex.MatchResult;

public class ChatSource extends CommandSourceStack {

    private final MatchResult result;
    private final org.bukkit.entity.Player bukkitPlayer;
    private final net.minecraft.server.level.ServerPlayer nmsPlayer;
    private Component replacement;
    private List<Runnable> afterTask;

    public ChatSource(CommandSourceStack source, MatchResult result, List<Runnable> afterTask) {
        super(source.getEntity(),
                source.getPosition(),
                source.getRotation(),
                source.getLevel(),
                source.getServer().getProfilePermissions(((Player)source.getEntity()).gameProfile),
                source.getTextName(),
                source.getDisplayName(),
                source.getServer(),
                source.getEntity());
        this.result = result;
        this.bukkitPlayer = (org.bukkit.entity.Player) source.getBukkitEntity();
        this.nmsPlayer = (net.minecraft.server.level.ServerPlayer) source.getEntity();
        this.afterTask = afterTask;
    }

    public void replace(Component cmp) {
        this.replacement = cmp;
    }

    public void replace(String str) {
        this.replacement = Component.text(str);
    }

    public Component getReplacement() {
        return replacement;
    }

    public void afterTask(Runnable t) {
        afterTask.add(t);
    }

    public List<Runnable> getTasks() {
        return afterTask;
    }
}
