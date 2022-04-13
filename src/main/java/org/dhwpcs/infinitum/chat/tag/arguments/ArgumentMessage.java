package org.dhwpcs.infinitum.chat.tag.arguments;

import org.dhwpcs.infinitum.chat.data.Message;
import org.dhwpcs.infinitum.chat.tag.ChatContext;

public class ArgumentMessage implements Argument<Message> {
    @Override
    public Class<Message> getValueClass() {
        return Message.class;
    }

    @Override
    public Message parse(Object input, ChatContext ctx) {
        return null;
    }
}
