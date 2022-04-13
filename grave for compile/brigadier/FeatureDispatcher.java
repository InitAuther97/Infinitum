package org.dhwpcs.infinitum.chat.brigadier;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kyori.adventure.text.Component;
import org.dhwpcs.infinitum.chat.tag.ChatTag;

public class FeatureDispatcher {
    public CommandDispatcher<ChatSource> dispatcher = new CommandDispatcher<>();
    public void register(ChatTag feature) {
        LiteralArgumentBuilder<ChatSource> builder = LiteralArgumentBuilder.literal(feature.id());
        feature.arguments().forEach(builder::then);
        builder.executes(ctx -> {
            ChatSource source = ctx.getSource();
            Component result = feature.compile(ctx);
            source.replace(result);
            source.afterTask(() -> feature.afterTask(ctx));
            return result == null ? 0 : 1;
        });
    }

    public int execute(String cmd, ChatSource source) throws CommandSyntaxException {
        return dispatcher.execute(cmd, source);
    }

    public static boolean move(ChatSource source) {
        if(source.result.length > source.pointer + 1) {
            source.pointer++;
            return true;
        } else return false;
    }

    public static void flip(ChatSource source) {
        source.pointer = 0;
    }
}
