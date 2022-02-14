package org.dhwpcs.infinitum.chat.feature.inst;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import net.minecraft.commands.arguments.GameProfileArgument;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import org.dhwpcs.infinitum.I18n;
import org.dhwpcs.infinitum.chat.brigadier.ChatSource;
import org.dhwpcs.infinitum.chat.brigadier.exception.ArgumentExceptionMessage;
import org.dhwpcs.infinitum.chat.feature.ChatFeature;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class FeatureAt implements ChatFeature {
    @Override
    public String name() {
        return "At";
    }

    @Override
    public String id() {
        return "at";
    }

    @Override
    public List<String> aliases() {
        return List.of("@");
    }

    @Override
    public Set<ArgumentBuilder<ChatSource, ?>> arguments() {
        return Set.of(RequiredArgumentBuilder.argument("player", new GameProfileArgument()));
    }

    @Override
    public Component compile(CommandContext<ChatSource> context) throws CommandSyntaxException {
        GameProfileArgument.Result result = context.getArgument("player", GameProfileArgument.Result.class);
        Collection<GameProfile> profs = result.getNames(context.getSource());
        if(profs.size() > 1) {
            throw new SimpleCommandExceptionType(ArgumentExceptionMessage.MULTIPLE_TARGET).create();
        }

        if(profs.size() < 1) {
            return Component.text("@[UNKNOWN TARGET]").color(NamedTextColor.RED);
        }

        GameProfile profile = profs.iterator().next();
        OfflinePlayer player = Bukkit.getOfflinePlayer(profile.getId());
        context.getSource().store(player);
        return I18n.format(player.isOnline() ? "chat.at.online" : "chat.at.offline",
                        context.getSource().getBukkitSender(),
                        player.getName());
    }

    @Override
    public boolean hasAfterTask() {
        return true;
    }

    @Override
    public void afterTask(CommandContext<ChatSource> context) {
        OfflinePlayer player = context.getSource().getCache(0);
        if(player.isOnline()) {
            Player entity = player.getPlayer();
            entity.playSound(Sound.sound(org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, Sound.Source.PLAYER, 1.0f, 0.0f));
        }
    }
}
