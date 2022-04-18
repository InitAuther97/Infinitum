package org.dhwpcs.infinitum.chat.tag.inst;

import org.dhwpcs.infinitum.chat.adventure.Translatable;
import org.dhwpcs.infinitum.chat.data.Message;
import org.dhwpcs.infinitum.chat.ChatContext;
import org.dhwpcs.infinitum.chat.tag.ChatTag;
import org.dhwpcs.infinitum.chat.tag.arguments.ArgumentNumber;
import org.dhwpcs.infinitum.chat.tag.parse.TagFailedException;
import org.dhwpcs.infinitum.chat.tag.parse.argument.ArgumentTable;
import org.dhwpcs.infinitum.chat.tag.parse.argument.TagContext;

import java.util.List;

public class TagReply implements ChatTag<Message> {

    @Override
    public String name() {
        return "Reply";
    }

    @Override
    public String id() {
        return "reply";
    }

    @Override
    public List<String> aliases() {
        return List.of("re");
    }

    @Override
    public ArgumentTable arguments() throws TagFailedException {
        return ArgumentTable.table()
                .with("id", ArgumentNumber.integers());
    }

    @Override
    public Class<Message> type() {
        return Message.class;
    }

    @Override
    public Message parse(TagContext tag, ChatContext chat) throws TagFailedException {
        int num = ArgumentNumber.getNumber("id", Integer.class, tag);
        return chat.getGlobal().getHistory().getMessage(num);
    }

    @Override
    public Translatable parseComponent(Message result, ChatContext ctx) {
        return ChatTag.super.parseComponent(result, ctx);
    }
}
