package org.dhwpcs.infinitum.chat.brigadier;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.kyori.adventure.text.Component;
import org.dhwpcs.infinitum.chat.feature.ChatFeature;

public class FeatureDispatcher {
    public static CommandDispatcher<ChatSource> INSTANCE = new CommandDispatcher<>();
    public static void register(ChatFeature feature) {
        LiteralArgumentBuilder<ChatSource> builder = LiteralArgumentBuilder.literal(feature.id());
        feature.arguments().forEach(builder::then);
        builder.executes(ctx -> {
            ChatSource source = ctx.getSource();
            Component result = feature.compile(ctx);
            feature.applyExtra();
        });
    }
}
