package org.dhwpcs.infinitum.chat.tag.arguments;

import org.dhwpcs.infinitum.chat.tag.ChatContext;

public interface Argument<T> {
    Class<T> getValueClass();
    T parse(Object input, ChatContext ctx);
    default int priority() {
        return 0;
    }
}
