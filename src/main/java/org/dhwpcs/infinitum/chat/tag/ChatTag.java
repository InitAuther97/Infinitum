package org.dhwpcs.infinitum.chat.tag;

import net.kyori.adventure.text.Component;
import org.dhwpcs.infinitum.chat.adventure.Translatable;
import org.dhwpcs.infinitum.chat.tag.parse.TagFailedException;
import org.dhwpcs.infinitum.chat.tag.parse.argument.ArgumentTable;
import org.dhwpcs.infinitum.chat.tag.parse.argument.TagContext;

import java.util.List;

public interface ChatTag<T> {
    String name();
    String id();
    List<String> aliases();
    ArgumentTable arguments() throws TagFailedException;
    Class<T> type();

    T parse(TagContext tag, ChatContext chat) throws TagFailedException;

    default Translatable parseComponent(T result, ChatContext ctx) {
        return Translatable.wrapNugget(ctx.getInfinitum().getI18n().get("chat.tag.info"), name());
    }
}
