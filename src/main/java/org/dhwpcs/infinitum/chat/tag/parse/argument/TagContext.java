package org.dhwpcs.infinitum.chat.tag.parse.argument;

import org.dhwpcs.infinitum.chat.tag.Parseable;
import org.dhwpcs.infinitum.chat.tag.parse.TagFailedException;

import java.util.Map;
import java.util.Set;

public class TagContext {
    private final Set<Object> attributes;
    private final Map<String, ParsedArgument<?>> arguments;
    private final Parseable content;
    public TagContext(Map<String, ParsedArgument<?>> arguments, Set<Object> attributes, Parseable content) {
        this.attributes = attributes;
        this.arguments = arguments;
        this.content = content;
    }

    public ParsedArgument<?> getArgument(String id) throws TagFailedException {
        ParsedArgument<?> value = arguments.get(id);
        if(value == null) {
            throw new TagFailedException(TagFailedException.Reason.CONTEXT_UNDEFINED_ARGUMENT,
                    String.format("Argument %s is not present.", id));
        }
        return value;
    }

    public<T> T getArgument(String id, Class<T> vt) throws TagFailedException {
        ParsedArgument<?> value = getArgument(id);
        if(!vt.isInstance(value.get())) {
            throw new TagFailedException(TagFailedException.Reason.CONTEXT_WRONG_ARGUMENT_TYPE,
                    String.format("Argument %s is not an instance of %s", id, vt.getName()));
        }
        return vt.cast(value.get());
    }
}
