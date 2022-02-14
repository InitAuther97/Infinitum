package org.dhwpcs.infinitum.chat.feature;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kyori.adventure.text.Component;
import org.dhwpcs.infinitum.chat.brigadier.ChatSource;

import java.util.List;
import java.util.Set;

public interface ChatFeature {
    String name();
    String id();
    List<String> aliases();
    Set<ArgumentBuilder<ChatSource, ?>> arguments();
    Component compile(CommandContext<ChatSource> context) throws CommandSyntaxException;
    boolean hasAfterTask();
    default void afterTask(CommandContext<ChatSource> context) {}
}
