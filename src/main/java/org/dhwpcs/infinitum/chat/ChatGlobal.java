package org.dhwpcs.infinitum.chat;

import io.github.initauther97.nugget.local.PlayerLocal;
import org.dhwpcs.infinitum.chat.data.Message;
import org.dhwpcs.infinitum.chat.data.MsgHistory;
import org.dhwpcs.infinitum.chat.tag.inst.TagAt;
import org.dhwpcs.infinitum.chat.tag.inst.TagReply;
import org.dhwpcs.infinitum.chat.tag.parse.ChatParser;
import org.dhwpcs.infinitum.Infinitum;

public class ChatGlobal {
    private final MsgHistory hist;
    private final ChatParser parser = new ChatParser();
    private final PlayerLocal<Message> messageLocal;
    private final String whole_escape_mark = "!#";

    public ChatGlobal(Infinitum infinitum, MsgHistory hist) {
        this.hist = hist;
        this.messageLocal = infinitum.getLib().createPlayerLocal(Message.class);
        parser.addTag(new TagAt());
        parser.addTag(new TagReply());
    }

    public MsgHistory getHistory() {
        return hist;
    }

    public ChatParser getParser() {
        return parser;
    }

    public String getWholeEscape() {
        return whole_escape_mark;
    }

    public PlayerLocal<Message> getMessageLocal() {
        return messageLocal;
    }
}
