package org.dhwpcs.infinitum.chat.tag.parse.argument;

import org.dhwpcs.infinitum.chat.ChatContext;
import org.dhwpcs.infinitum.chat.tag.arguments.Argument;
import org.dhwpcs.infinitum.chat.tag.parse.TagFailedException;

import java.util.HashMap;
import java.util.Map;

public class ArgumentTable {
    private final Map<String, Argument<?>> arguments;

    public static ArgumentTable table() {
        return new ArgumentTable();
    }

    public ArgumentTable() {
        arguments = new HashMap<>();
    }

    public ArgumentTable with(String id, Argument<?> argument) throws TagFailedException {
        if(arguments.containsKey(id)) {
            throw new TagFailedException(TagFailedException.Reason.ARGUMENT_TABLE_EXISTED,
                    String.format("There is already argument '%s' that exists", id));
        }
        arguments.put(id, argument);
        return this;
    }

    public<T> ArgumentTable with(String id, Argument<T> argument, T defaultValue) throws TagFailedException {
        if(arguments.containsKey(id)) {
            throw new TagFailedException(TagFailedException.Reason.ARGUMENT_TABLE_EXISTED,
                    String.format("There is already argument '%s' that exists", id));
        }
        arguments.put(id, new DefaultArgument<>(argument, defaultValue));
        return this;
    }

    private<T> ParsedArgument<T> fuckGenerics(Object object, Argument<T> argument) {
        return new ParsedArgument<>(argument, (T)object);
    }

    public Map<String, ParsedArgument<?>> parse(Map<String, Object> values, ChatContext ctx) {
        Map<String, ParsedArgument<?>> map = new HashMap<>(arguments.size());
        Argument<?> argument;
        Object value;
        for(String key : arguments.keySet()) {
            argument = arguments.get(key);
            value = argument.parse(values.getOrDefault(key, null), ctx);
            map.put(key, fuckGenerics(value, argument));
        }
        return map;
    }
}
