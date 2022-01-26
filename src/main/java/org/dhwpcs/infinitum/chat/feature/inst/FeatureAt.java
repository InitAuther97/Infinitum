package org.dhwpcs.infinitum.chat.feature.inst;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.github.initauther97.arguments.ArgumentList;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.commands.arguments.GameProfileArgument;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.dhwpcs.infinitum.arguments.ArgumentPlayer;
import org.dhwpcs.infinitum.chat.brigadier.ChatSource;
import org.dhwpcs.infinitum.chat.brigadier.exception.ArgumentExceptionMessage;
import org.dhwpcs.infinitum.chat.brigadier.exception.ArgumentExceptionType;
import org.dhwpcs.infinitum.chat.feature.ChatFeature;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class FeatureAt implements ChatFeature {
    private static final ArgumentList ARGS = new ArgumentList(new ArgumentPlayer());
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

    }

    @Override
    public ArgumentList getArguments() {
        return ARGS;
    }

    @Override
    public Component compile(Object[] args) {
        OfflinePlayer player = (OfflinePlayer) args[0];
        return Component.text("@" + player.getName())
                .color(player.isOnline() ? NamedTextColor.BLUE : NamedTextColor.GRAY);
    }

    @Override
    public void applyExtra(Object[] args) {
        OfflinePlayer player = (OfflinePlayer) args[0];
        if(player.isOnline()) {
            Player entity = player.getPlayer();
            entity.playSound(Sound.sound(org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, Sound.Source.PLAYER, 1.0f, 0.0f));
        }
    }
}
