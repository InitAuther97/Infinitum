package org.dhwpcs.infinitum.chat.tag.parse.argument;

import org.dhwpcs.infinitum.chat.ChatContext;
import org.dhwpcs.infinitum.chat.tag.arguments.Argument;

public record DefaultArgument<T>(Argument<T> delegate, T defaultValue) implements Argument<T> {
    @Override
    public Class<T> getValueClass() {
        return delegate.getValueClass();
    }

    @Override
    public T parse(Object input, ChatContext ctx) {
        if(input == null) {
            return defaultValue;
        }
        return delegate.parse(input, ctx);
    }
}
