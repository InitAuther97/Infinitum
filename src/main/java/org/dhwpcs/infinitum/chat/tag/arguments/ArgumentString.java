package org.dhwpcs.infinitum.chat.tag.arguments;

import org.dhwpcs.infinitum.chat.tag.ChatContext;

import java.util.function.Function;

public class ArgumentString implements Argument<String> {

    private final Function<Object, String> defaultToStr;

    public ArgumentString() {
        defaultToStr = String::valueOf;
    }

    public static ArgumentString string() {
        return new ArgumentString();
    }

    @Override
    public Class<String> getValueClass() {
        return String.class;
    }

    @Override
    public String parse(Object input, ChatContext ctx) {
        return input instanceof String ? (String) input : defaultToStr.apply(input);
    }

    @Override
    public int priority() {
        return Integer.MIN_VALUE;
    }
}
