package org.dhwpcs.infinitum.chat.tag.parse.argument;

import org.dhwpcs.infinitum.chat.tag.arguments.Argument;

public class ParsedArgument<T> implements ArgumentType {
    private final T value;
    private final Argument<T> argument;

    public ParsedArgument(Argument<T> argument, T value) {
        this.value = value;
        this.argument = argument;
    }

    public Argument<T> getArgument() {
        return argument;
    }

    public T get() {
        return value;
    }
}
