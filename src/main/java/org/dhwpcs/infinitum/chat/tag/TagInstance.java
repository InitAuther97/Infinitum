package org.dhwpcs.infinitum.chat.tag;

import org.dhwpcs.infinitum.chat.ChatContext;
import org.dhwpcs.infinitum.chat.adventure.Translatable;

public record TagInstance<T>(ChatTag<T> tag, T result, String raw) implements Parseable {

    public static final TagInstance<?> END_TAG = new TagInstance<>(null, null, null);

    public Translatable parse(ChatContext ctx) {
        return tag.parseComponent(result, ctx);
    }
}
