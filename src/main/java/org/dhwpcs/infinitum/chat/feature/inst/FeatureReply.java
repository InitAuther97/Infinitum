package org.dhwpcs.infinitum.chat.feature.inst;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.dhwpcs.infinitum.I18n;
import org.dhwpcs.infinitum.chat.brigadier.ChatSource;
import org.dhwpcs.infinitum.chat.data.Message;
import org.dhwpcs.infinitum.chat.feature.ChatFeature;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Set;

public class FeatureReply implements ChatFeature {

    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendValue(ChronoField.YEAR, 4)
            .appendLiteral('/')
            .appendValue(ChronoField.MONTH_OF_YEAR, 2)
            .appendLiteral('/')
            .appendValue(ChronoField.DAY_OF_MONTH, 2)
            .appendLiteral(' ')
            .appendValue(ChronoField.HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
            .toFormatter();

    @Override
    public String name() {
        return "Reply";
    }

    @Override
    public String id() {
        return "reply";
    }

    @Override
    public List<String> aliases() {
        return List.of("re");
    }

    @Override
    public Set<ArgumentBuilder<ChatSource, ?>> arguments() {
        return Set.of(RequiredArgumentBuilder.argument("id", IntegerArgumentType.integer(0)));
    }

    @Override
    public Component compile(CommandContext<ChatSource> context) {
        int message = context.getArgument("id", Integer.class);
        Message msg = context.getSource().surfMsg(message);
        return I18n.format("chat.reply.format",
                        context.getSource().getBukkitSender(),
                        Bukkit.getOfflinePlayer(msg.sender()).getName(),
                        formatter.format(msg.timeAsTemporal()),
                        msg.message()).color(NamedTextColor.LIGHT_PURPLE);
    }

    @Override
    public boolean hasAfterTask() {
        return false;
    }
}
